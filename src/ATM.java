/**
 * Just like last time, the ATM class is responsible for managing all
 * of the user interaction. This means login procedures, displaying the
 * menu, and responding to menu selections. In the enhanced version, the
 * ATM class will have the added responsibility of interfacing with the
 * Database class to write and read information to and from the database.
 * 
 * Most of the functionality for this class should have already been
 * implemented last time. You can always reference my Github repository
 * for inspiration (https://github.com/rwilson-ucvts/java-sample-atm).
 */

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;


public class ATM {
	private Database db;
	private BankAccount account;
	private BankAccount transferAcct;

	
	public Scanner in;
	
	public ATM() throws FileNotFoundException, IOException {
		db = new Database("accounts-db.txt");
		in = new Scanner(System.in);
	}
	
	public void run() throws FileNotFoundException, IOException {
		System.out.println("Welcome to your ATM!\n");
		System.out.println("Please enter one of the options listed below:\n\n");
		System.out.println("[1] Open Account");
		System.out.println("[2] Log in");
		System.out.println("[3] Quit");
		int userInput = in.nextInt();
		while(userInput != 1 && userInput != 2 && userInput != 3) {
			System.out.println("\nInvalid response. Try again.\n\n");
			System.out.println("Please enter one of the options listed below:\n\n");
			System.out.println("[1] Open Account");
			System.out.println("[2] Log in");
			System.out.println("[3] Quit");
			userInput = in.nextInt();
		}
		if(userInput == 1) {
			register();
		}
		else if(userInput == 2){
			signIn();
		}
		else {
			System.out.println("Thank you for using this ATM. Have a nice day! :)");
			in.close();
			return;
		}
	}
	
	public void signIn() throws FileNotFoundException, IOException {
		System.out.println("Please enter your account information to sign in.\n");
		System.out.printf("Account Number: ");
		long accountNumber = in.nextLong();
		System.out.printf("Pin Number: ");
		int pin = in.nextInt();
		if(db.getAccount(accountNumber) != null && (Long.toString(accountNumber).length() == 9)) {
			account = new BankAccount(db.getAccount(accountNumber));
			if(pin == account.getUser().getPin()) {
				System.out.println("Sign in successful.\n\n");
				BankAccountFunctions();
			}
			else {
				System.out.println("You have entered an invalid account number or pin number. Please try again.");
				signIn();
			}
		}
		else {
			System.out.println("You have entered an invalid account number or pin number. Please try again.");
			signIn();
		}
	}
	
	/**
	 * @throws IOException
	 */
	public void register() throws IOException {
		String input = "";
		String newAcct = "";
		long numActs = db.getMaxAccountNumber() + 1;
		newAcct += Long.toString(numActs);
		System.out.println("Your account number is : " + newAcct);
		boolean wrong = true;
		while(wrong) {
			System.out.println("Please enter a 4-digit pin.");
			int pinNum = in.nextInt();
			input = Integer.toString(pinNum);
			if(input.length() != 4) {
				System.out.println("Invalid pin number. Try again.");
			}
			else {
				wrong = false;
			}
		}
		newAcct += input;
		newAcct += String.format("%1$-15s", "0.00");
		System.out.println("Please enter the following information to register for a new account.\n");
		in.nextLine();
		System.out.println("Last Name: ");
		newAcct += String.format("%1$-20s", in.nextLine());
		System.out.println("First Name: ");
		newAcct += String.format("%1$-15s", in.nextLine());
		wrong = true;
		while (wrong) {
			System.out.println("Date of Birth (YYYYMMDD): ");
			input = in.nextLine();
			if(input.length() != 8) {
				System.out.println("Invalid date of birth. Try again.");
			}
			else {
				wrong = false;
			}
		}
		newAcct += input;
		wrong = true;
		while (wrong) {
			System.out.println("Phone Number: ");
			input = in.nextLine();
			if(input.length() != 10) {
				System.out.println("Invalid phone number. Try again.");
			}
			else {
				wrong = false;
			}
		}
		newAcct += input;
		wrong = true;
		while (wrong) {
			System.out.println("Street Address: ");
			input = in.nextLine();
			if(input.length() > 30) {
				System.out.println("Street address is too long. Please abbreviate. Try again.");
			}
			else if (input.length() < 30) {
				input = String.format("%1$-30s", input);
				wrong = false;
			}
			else {
				wrong = false;
			}
		}
		newAcct += input;
		wrong = true;
		while (wrong) {
			System.out.println("City: ");
			input = in.nextLine();
			if(input.length() > 30) {
				System.out.println("City is too long. Please abbreviate. Try again.");
			}
			else if (input.length() < 30) {
				input = String.format("%1$-30s", input);
				wrong = false;
			}
			else {
				wrong = false;
			}
		}
		newAcct += input;
		wrong = true;
		while (wrong) {
			System.out.println("State: ");
			input = in.nextLine();
			if(input.length() != 2) {
				System.out.println("Invalid state. Please abbreviate to 2 letters. Try again.");
			}
			else {
				wrong = false;
			}
		}
		newAcct += input;
		wrong = true;
		while (wrong) {
			System.out.println("Postal Code: ");
			input = in.nextLine();
			if(input.length() != 5) {
				System.out.println("Invalid postal code. Try again.");
			}
			else {
				wrong = false;
			}
		}
		newAcct += input;
		newAcct += "Y";
		System.out.println(newAcct);
		BankAccount newAccount = new BankAccount(newAcct);
		System.out.println(newAccount.toString());
		db.updateAccount(newAccount, null);
		System.out.println("Please sign in with your new credentials.");
		run();
	}
	
	public void BankAccountFunctions() throws FileNotFoundException, IOException {
		boolean done = false;
		while(!done) {
			System.out.println("Please select one of the options below.\n");
			System.out.println("[1] Deposit");
			System.out.println("[2] Withdraw");
			System.out.println("[3] Transfer funds");
			System.out.println("[4] View Balance");
			System.out.println("[5] View personal information.");
			System.out.println("[6] Update personal information");
			System.out.println("[7] Close Account");
			System.out.println("[8] Log off");
			
			int userInput;
			userInput = in.nextInt();
			
			while(userInput > 8 || userInput < 1) {
				System.out.println("Invalid response. Try again.");
				BankAccountFunctions();
			}
			
			if(userInput == 1) {
				System.out.println("Please enter the amount you wish to deposit.");
				System.out.println("Deposit Amount: ");
				double amount = in.nextDouble();
				account.Deposit(amount);
				db.updateAccount(account, null);
			}
			
			else if(userInput == 2) {
				System.out.println("Please enter the amount you wish to withdraw.");
				System.out.println("Withdrawal Amount: ");
				double amount = in.nextDouble();
				account.Withdraw(amount);
				db.updateAccount(account, null);
			}
			
			else if(userInput == 3) {
				System.out.println("Please enter the bank account number of the bank account you wish to transfer funds to.");
				System.out.println("Bank Account No.: ");
				long transferNum = in.nextLong();
				if(db.getAccount(transferNum) != null) {
					transferAcct = new BankAccount(db.getAccount(transferNum));
					System.out.println("Please enter the amount you wish to transfer.");
					System.out.println("Transfer Amount: ");
					double amount = in.nextDouble();
					account.Transfer(transferAcct, amount);
					db.updateAccount(account, transferAcct);
				}
				else {
					System.out.println("Invalid account number.");
					BankAccountFunctions();
				}
			}
			
			else if(userInput == 4) {
				account.viewBalance();
			}
			
			else if(userInput == 5) {
				account.viewAcctInfo();
			}
			
			else if(userInput == 6) {
				updateAcctInfo(account, 1);
			}
			
			else if(userInput == 7) {
				updateAcctInfo(account, 0);		
				closeSession();
				done = true;
			}
			
			else {
				System.out.println("You have been logged out.\n\n");
				closeSession();
				done = true;
			}
		}
	}
	
	public void updateAcctInfo(BankAccount account, int choice) throws IOException {
		
		if(choice == 0) {
			System.out.println("Are you sure you want to close your account? Press Y/N");//Database.setStatus(accountNumber);
			in.nextLine();
			String input = in.nextLine();
			input = input.toLowerCase();
			if(input.equals("y")) {
				account.setStatus();
			}
			System.out.println("Your account has been deleted."); 
		}
		else {
			System.out.println("Please select one of the following numbers to update your account information: ");
			System.out.println("[1] Pin");
			System.out.println("[2] Telephone Number");
			System.out.println("[3] Street Address");
			System.out.println("[4] City");
			System.out.println("[5] State");
			System.out.println("[6] Postal Code");
						
			choice = in.nextInt();
			
			if(choice == 1) {
				account.getUser().setPin();
			}
			else if(choice == 2) {
				System.out.printf("Enter your new phone number: ");
				long newPhoneNumber = in.nextLong();
				account.getUser().setPhoneNumber(newPhoneNumber);
			}
			else if(choice == 3) {
				System.out.printf("Enter your new street address: ");
				String newAddress = in.nextLine();
				account.getUser().setAddress(newAddress);
			}
			else if(choice == 4) {
				System.out.printf("Enter your new city: ");
				String newCity = in.nextLine();
				account.getUser().setCity(newCity);
			}
			else if(choice == 5) {
				System.out.printf("Enter your new state: ");
				String newState = in.nextLine();
				account.getUser().setState(newState);
			}
			else if(choice == 6) {
				boolean wrong = true;
				String newPostalCodeString = "";
				while (wrong) { 
					System.out.printf("Enter your new postal code: ");
					newPostalCodeString = in.nextLine();
					if(newPostalCodeString.length() != 5) {
						System.out.println("Invaid postal code. Please try again.");
					}
					else {
						wrong = false;
					}
				}
				account.getUser().setPostalCode(newPostalCodeString);
			}
			else {
				System.out.println("Invaid Input");
			}
		}
		
	}
	
	public void closeSession() throws IOException {
		db.updateAccount(account, transferAcct);
		run();
	}
}