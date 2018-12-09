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
	
	public void run() throws FileNotFoundException, IOException, InterruptedException {
		boolean done = false;
		while(!done) {
			boolean valid = false;
			int userInput = 0;
			String userIn = "";
			while(!valid) {
				System.out.println("\nPlease enter one of the options listed below:\n");
				System.out.println("[1] Open Account");
				System.out.println("[2] Log in");
				System.out.println("[3] Quit");
				userIn = in.nextLine();
				valid = checkUserInput(userIn, 1);
			}
			
			userInput = Integer.valueOf(userIn);
			
			if(userInput != 1 && userInput != 2 && userInput != 3) {
				System.out.println("\nInvalid response. Try again.\n");
			}
			else if(userInput == 1) {
				register();
			}
			else if(userInput == 2){
				signIn();
			}
			else {
				done = true;
			}
		}
		System.out.println("Thank you for using this ATM. Have a nice day! :)");
		returnAnimation(10);
		in.close();
		return;
	}
	
	public void signIn() throws FileNotFoundException, IOException, InterruptedException {
		System.out.println("\nPlease enter your account information to sign in.");
		System.out.println("Enter * to return to the previous menu.\n");
		boolean validSignIn = false;
		while(!validSignIn) {
			boolean validAcctNum = false;
			String accountNumString = "";
			while(!validAcctNum) {
				System.out.printf("Account Number: ");
				accountNumString = in.nextLine();
				if(accountNumString.equals("*")) {
					validAcctNum = true;
					returnAnimation(1);
					return;
				}
				else {
					validAcctNum = checkUserInput(accountNumString, 3);
				}
			}
			
			long accountNumber = Long.valueOf(accountNumString);
			
			boolean validPin = false;
			String pinString = "";
			while (!validPin) {
				System.out.printf("Pin Number: ");
				pinString = in.nextLine();
				if(pinString.equals("*")) {
					returnAnimation(1);
					validPin = true;
					return;
				}
				validPin = checkUserInput(pinString, 1);
			}
			
			int pin = Integer.valueOf(pinString);
			returnAnimation(8);
			if(db.getAccount(accountNumber) != null && (Long.toString(accountNumber).length() == 9)) {
				account = new BankAccount(db.getAccount(accountNumber));
				if(pin == account.getUser().getPin()) {
					System.out.println("Login successful.\n");
					validSignIn = true;
					BankAccountFunctions();
				}
				else {
					System.out.println("\nYou have entered an invalid account number or pin number. Please try again.\n");
					validSignIn = false;
				}
			}
			else {
				System.out.println("\nYou have entered an invalid account number or pin number. Please try again.\n");
				validSignIn = false;
			}
		}
	}
	
	/**
	 * @throws IOException
	 * @throws InterruptedException 
	 */
	public void register() throws IOException, InterruptedException {
		returnAnimation(9);
		System.out.println("\nWelcome, new customer!");
		System.out.println("Please enter * at any time to cancel your registration and return to the previous menu.\n");
		String input = "";
		String newAcct = "";
		long numActs = db.getMaxAccountNumber() + 1;
		newAcct += Long.toString(numActs);
		System.out.println("Your account number is: " + newAcct);
		
		boolean validPin = false;
		String pinString = "";
		while(!validPin) {
			System.out.printf("Please enter a 4-digit pin: ");
			pinString = in.nextLine();
			
			if(pinString.equals("*")) {
				validPin = true;
				returnAnimation(2);
				newAcct = null;
				returnAnimation(1);
				return;
			}
			else {
				validPin = checkUserInput(pinString, 1);
				
				if(validPin && pinString.length() != 4) {
					System.out.println("Pin must be a 4 digit number. Please try again.");
					validPin = false;
				}
			}
		}
		
		newAcct += pinString;
		//balance
		newAcct += String.format("%1$-15s", "0.00");
		
		System.out.println("Please enter the following personal information to register for a new account.\n");
		//in.nextLine();
		String nextInput = "";
		System.out.printf("Last Name: ");
		nextInput = in.nextLine();
		if(nextInput.equals("*")) {
			returnAnimation(2);
			newAcct = null;
			returnAnimation(1);
			return;
		}
		else {
			newAcct += String.format("%1$-20s", nextInput);
		}
		System.out.printf("First Name: ");
		nextInput = in.nextLine();
		if(nextInput.equals("*")) {
			returnAnimation(2);
			newAcct = null;
			returnAnimation(1);
			return;
		}
		newAcct += String.format("%1$-15s", nextInput);
		
		boolean validDOB = false;
		String dobString = "";
		while(!validDOB) {
			System.out.printf("Date of Birth (YYYYMMDD): ");
			dobString = in.nextLine();
			if(dobString.equals("*")) {
				validDOB = true;
				returnAnimation(2);
				newAcct = null;
				returnAnimation(1);
				return;
			}
			
			validDOB = checkUserInput(dobString, 3);
			if(dobString.length() != 8) {
				System.out.println("Invalid date of birth. Please enter the date of birth in the YYYYMMDD format.");
				validDOB = false;
			}
			
		}
		
		newAcct += dobString;
		
		boolean validPhone = false;
		String phoneString = "";
		while(!validPhone) {
			System.out.println("Phone Number: ");
			phoneString = in.nextLine();
			
			if(phoneString.equals("*")) {
				validPhone = true;
				returnAnimation(2);
				newAcct = null;
				returnAnimation(1);
				return;
			}
			validPhone = checkUserInput(phoneString, 3);
			
			if(phoneString.length() != 10) {
				System.out.println("Invalid phone number. Please enter a 10-digit phone number.");
				validPhone = false;
			}
		}
		
		newAcct += phoneString;
		boolean wrong = true;
		while (wrong) {
			System.out.println("Street Address: ");
			input = in.nextLine();
			
			if(input.equals("*")) {
				wrong = false;
				returnAnimation(2);
				newAcct = null;
				returnAnimation(1);
				return;
			}
			else {
				if(input.length() > 30) {
					System.out.println("Street address is too long. Please abbreviate it to 30 characters.");
				}
				else if (input.length() < 30) {
					input = String.format("%1$-30s", input);
					wrong = false;
				}
				else {
					wrong = false;
				}
			}
		}
		
		newAcct += input;
		
		wrong = true;
		while (wrong) {
			System.out.println("City: ");
			input = in.nextLine();
			if(input.equals("*")) {
				wrong = false;
				returnAnimation(2);
				newAcct = null;
				returnAnimation(1);
				return;
			}
			else {
				if(input.length() > 30) {
					System.out.println("City is too long. Please abbreviate it to 30 characters.");
				}
				else if (input.length() < 30) {
					input = String.format("%1$-30s", input);
					wrong = false;
				}
				else {
					wrong = false;
				}
			}
		}
		newAcct += input;
		wrong = true;
		while (wrong) {
			System.out.println("State: ");
			input = in.nextLine();
			
			if(input.equals("*")) {
				wrong = false;
				returnAnimation(2);
				newAcct = null;
				returnAnimation(1);
				return;
			}
			else {
				if(input.length() != 2) {
					System.out.println("Invalid state. Please abbreviate to 2 letters.");
				}
				else {
					wrong = false;
				}	
			}
			
		}
		newAcct += input;
		boolean validPostalCode = false;
		String postalCodeString = "";
		while (!validPostalCode) {
			System.out.println("Postal Code: ");
			postalCodeString = in.nextLine();
			
			if(postalCodeString.equals("*")) {
				validPostalCode = true;
				returnAnimation(2);
				newAcct = null;
				returnAnimation(1);
				return;
			}
			
			validPostalCode = checkUserInput(postalCodeString, 3);
			
			if(postalCodeString.length() != 5) {
				System.out.println("Invalid postal code. Please enter a 5-digit postal code.");
				validPostalCode = false;
			}
		}
		
		newAcct += postalCodeString;
		newAcct += "Y";
		//System.out.println(newAcct);
		BankAccount newAccount = new BankAccount(newAcct);
		//System.out.println(newAccount.toString());
		db.updateAccount(newAccount, null);
		System.out.println("Please sign in with your new credentials.");
	}
	
	public void BankAccountFunctions() throws FileNotFoundException, IOException, InterruptedException {
		String firstName = "";
		String lastName = "";
		for(int i = 0; account.getUser().getFirstName().charAt(i) != ' '; i++) {
			firstName += account.getUser().getFirstName().charAt(i);
		}
		for(int h = 0; account.getUser().getLastName().charAt(h) != ' '; h++) {
			lastName += account.getUser().getLastName().charAt(h);
		}
		System.out.printf("Welcome, %s %s!", firstName, lastName);
		boolean done = false;
		while(!done) {
			System.out.println("\nPlease select one of the options below.\n");
			System.out.println("[1] Deposit");
			System.out.println("[2] Withdraw");
			System.out.println("[3] Transfer funds");
			System.out.println("[4] View balance");
			System.out.println("[5] View personal information.");
			System.out.println("[6] Update personal information");
			System.out.println("[7] Close account");
			System.out.println("[8] Log out");
			
			int userInput;
			userInput = in.nextInt();
			
			while(userInput > 8 || userInput < 1) {
				System.out.println("Invalid response. Try again.");
				BankAccountFunctions();
			}
			
			if(userInput == 1) {
				String amountString = "";
				boolean valid = false;
				while(!valid) {
					System.out.println("Please enter the amount you wish to deposit.");
					System.out.println("Enter * to return to the previous menu.");
					System.out.println("Deposit Amount: ");
					in.nextLine();
					amountString = in.nextLine();
					if(amountString.equals("*")) {
						valid = true;
						returnAnimation(1);
					}
					else {
						valid = checkUserInput(amountString, 2);
						if(valid) {
							double amount = Double.valueOf(amountString);
							account.Deposit(amount);
							db.updateAccount(account, null);
						}
					}
				}
				
			}
			
			else if(userInput == 2) {
				String amountString = "";
				boolean valid = false;
				while(!valid) {
					System.out.println("Please enter the amount you wish to withdraw.");
					System.out.println("Enter * to return to the previous menu.");
					System.out.println("Withdrawal Amount: ");
					in.nextLine();
					amountString = in.nextLine();
					if(amountString.equals("*")) {
						valid = true;
						returnAnimation(1);
					}
					else {
						valid = checkUserInput(amountString, 2);
						if(valid) {
							double amount = Double.valueOf(amountString);
							account.Withdraw(amount);
							db.updateAccount(account, null);
						}
					}
					
				}
				
			}
			
			else if(userInput == 3) {
				in.nextLine();
				String transferNumString = "";
				boolean valid = false;
				while(!valid) {
					System.out.println("Please enter the bank account number of the bank account you wish to transfer funds to.");
					System.out.println("Enter * to return to the previous menu.");
					System.out.println("Bank Account No.: ");
					transferNumString = in.nextLine();
					if(transferNumString.equals("*")) {
						valid = true;
						returnAnimation(1);	
					}
					else {
						valid = checkUserInput(transferNumString, 3);
						if(valid) {
							long transferNum = Long.valueOf(transferNumString);
							if(db.getAccount(transferNum) != null) {
								transferAcct = new BankAccount(db.getAccount(transferNum));
								valid = false;
								String amountString = "";
								while(!valid) {
									System.out.println("Please enter the amount you wish to transfer.");
									System.out.println("Transfer Amount: ");
									amountString = in.nextLine();
									
									if(amountString.equals("*")) {
										valid = true;
										returnAnimation(1);
										transferAcct = null;
										amountString = null;
										valid = true;
										return;
									}
									
									valid = checkUserInput(amountString, 2);
								}
								double amount = Double.valueOf(amountString);
								account.Transfer(transferAcct, amount);
								db.updateAccount(account, transferAcct);
							}
							else {
								System.out.println("Invalid account number.");
								//BankAccountFunctions();
							}
						}
					}
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
				if(account.getStatus().equals("N")) {
					closeSession();
					done = true;
				}
			}
			
			else {
				returnAnimation(4);
				closeSession();
				done = true;
			}
		}
	}
	
	public void updateAcctInfo(BankAccount account, int choice) throws IOException, InterruptedException {
		in.nextLine();
		if(choice == 0) {
			//in.nextLine();
			boolean valid = false;
			while(!valid) {
				System.out.println("Are you sure you want to close your account? Press Y/N");//Database.setStatus(accountNumber);
				//in.nextLine();
				String input = in.nextLine();
				input = input.toLowerCase();
				if(input.equals("y")) {
					account.setStatus();
					returnAnimation(5);
					System.out.println("Your account has been deleted.");
					returnAnimation(11);
					valid = true;
				}
				else if(input.equals("n")){
					System.out.println("Action cancelled.");
					returnAnimation(1);
					valid = true;
				}
				else {
					System.out.println("Invalid response.");
				}
			}
			
		}
		else {
			boolean validChoice = false;
			String choiceString = "";
			while(!validChoice) {
				System.out.println("Please select one of the following numbers to update your account information: ");
				System.out.println("[1] Pin");
				System.out.println("[2] Telephone Number");
				System.out.println("[3] Street Address");
				System.out.println("[4] City");
				System.out.println("[5] State");
				System.out.println("[6] Postal Code");
				System.out.println("Enter * at any time to return to the previous menu.\n");
				
				choiceString = in.nextLine();
				
				if(choiceString == "*") {
					validChoice = true;
					returnAnimation(1);
					return;
				}
				else {
					validChoice = checkUserInput(choiceString, 1);
				}
			}
						
			choice = Integer.valueOf(choiceString);
			
			if(choice == 1) {
				boolean validExistingPin = false;
				boolean validNewPin = false;
				String newPinString = "";
				String checkPinString = "";
				int newPin = account.getUser().getPin();
				while(!validExistingPin) {
					System.out.println("Enter your existing pin number: ");
					checkPinString = in.nextLine();
					if(checkPinString.equals("*")) {
						returnAnimation(6);
						returnAnimation(1);
						return;
					}
					validExistingPin = checkUserInput(checkPinString, 1);
				}
				int checkPin = Integer.valueOf(checkPinString);
				if(checkPin == account.getUser().getPin()) {
					System.out.println("Pin verified.");
					while(!validNewPin) {
						System.out.println("Please enter your new pin number: ");
						newPinString = in.nextLine();
						if(newPinString.equals("*")) {
							returnAnimation(6);
							returnAnimation(1);
							return;
						}
						validNewPin = checkUserInput(newPinString, 1);
						if(newPinString.length()!= 4) {
							System.out.println("Invalid new pin. Please enter a 4-digit pin.");
							validNewPin = false;
						}
						if(validNewPin) {
							newPin = Integer.valueOf(newPinString);
						}
						if(newPin == account.getUser().getPin()) {
							System.out.println("New pin is the same as the old pin. Please enter a new pin or enter * to cancel.");
							validNewPin = false;
						}
					}
					account.getUser().setPin(newPin);
					returnAnimation(7);
				}
				else {
					System.out.println("Invalid pin number.");
				}
				
			}
			else if(choice == 2) {
				boolean valid = false;
				String newPhoneNumString = "";
				long newPhoneNumber = 0;
				while(!valid) {
					System.out.printf("Enter your new phone number: ");
					newPhoneNumString = in.nextLine();
					if(newPhoneNumString.equals("*")) {
						returnAnimation(6);
						returnAnimation(1);
						return;
					}
					valid = checkUserInput(newPhoneNumString, 3);
					if(newPhoneNumString.length() != 10) {
						System.out.println("Invalid phone number. Please enter a 10-digit phone number.");
						valid = false;
					}
					if(valid) {
						newPhoneNumber = Long.valueOf(newPhoneNumString);
					}
					if(newPhoneNumber == account.getUser().getPhoneNumber()) {
						System.out.println("New phone number is the same as the old phone number. Please enter a new phone number or enter * to cancel.");
						valid = false;
					}
				}
				account.getUser().setPhoneNumber(newPhoneNumber);
				returnAnimation(7);
			}
			else if(choice == 3) {
				boolean valid = false;
				String newAddress = "";
				while(!valid) {
					System.out.printf("Enter your new street address: ");
					newAddress = in.nextLine();
					if(newAddress.equals("*")) {
						returnAnimation(6);
						returnAnimation(1);
						return;
					}
					valid = true;
					if(newAddress.length() > 30) {
						System.out.println("New address is too long. Please abbreviate it to 30 characters.");
						valid = false;
					}
					if(valid) {
						if(newAddress == account.getUser().getAddress()) {
							System.out.println("New address is the same as the old address. Please enter a new address or enter * to cancel.");
							valid = false;
						}
					}
				}
				
				account.getUser().setAddress(newAddress);
				returnAnimation(7);
			}
			else if(choice == 4) {
				boolean valid = false;
				String newCity = "";
				while(!valid) {
					System.out.printf("Enter your new city: ");
					newCity = in.nextLine();
					if(newCity.equals("*")) {
						returnAnimation(6);
						returnAnimation(1);
						return;
					}
					valid = true;
					if(newCity.length() > 30) {
						System.out.println("New city is too long. Please abbreviate it to 30 characters.");
						valid = false;
					}
					if(valid) {
						if(newCity == account.getUser().getCity()) {
							System.out.println("New city is the same as the old city. Please enter a new city or enter * to cancel.");
							valid = false;
						}
					}
				}
				
				account.getUser().setCity(newCity);
				returnAnimation(7);
			}
			else if(choice == 5) {
				boolean valid = false; 
				String newState = "";
				while(!valid) {
					System.out.printf("Enter your new state: ");
					newState = in.nextLine();
					if(newState.equals("*")) {
						returnAnimation(6);
						returnAnimation(1);
						return;
					}
					valid = true;
					if(newState.length() != 2) {
						System.out.println("Invalid state. Please enter the state abbreviated to 2 letters.");
						valid = false;
					}
				}
				account.getUser().setState(newState);
				returnAnimation(7);
			}
			else if(choice == 6) {
				boolean valid = false;
				String newPostalCodeString = "";
				while (!valid) { 
					System.out.printf("Enter your new postal code: ");
					newPostalCodeString = in.nextLine();
					if(newPostalCodeString.equals("*")) {
						returnAnimation(6);
						returnAnimation(1);
						return;
					}
					valid = checkUserInput(newPostalCodeString,1);
					
					if(valid) {
						if(newPostalCodeString.length() != 5) {
							System.out.println("Invalid postal code. Please enter a 5-digit postal code.");
							valid = false;
						}
						if(newPostalCodeString.equals(account.getUser().getPostalCode())) {
							System.out.println("New postal code is the same as the old postal code. Please enter a new postal code or enter * to cancel.");
							valid = false;
						}
					}
				}
				account.getUser().setPostalCode(newPostalCodeString);
				returnAnimation(7);
			}
			else {
				System.out.println("Invalid Input");
				updateAcctInfo(account, 1);
			}
		}
		
	}
	
	public void closeSession() throws IOException, InterruptedException {
		db.updateAccount(account, transferAcct);
	}
	
	public boolean checkUserInput(String input, int type) {
		// 1 = integer, 2 = double, 3 = long
		if(type == 1) {
			int integerInput;
			try{
				integerInput = Integer.parseInt(input);
		    }
		    catch(NumberFormatException e){
		    	System.out.println("Response must be numerical. Try again.\n");
		    	return false;
		    }
			return true;
		}
		
		else if(type == 2){
			double doubleInput;
			try {
				doubleInput = Double.parseDouble(input);
			}
			catch (NumberFormatException e){
				System.out.println("Response must be numerical. Try again.\n");
				return false;
			}
			return true;
		}
		else {
			Long longInput;
			try {
				longInput = Long.parseLong(input);
			}
			catch (NumberFormatException e) {
				System.out.println("Response must be numerical. Try again.\n");
				return false;
			}
			return true;
		}
	}
	
	public void returnAnimation(int choice) throws InterruptedException {
		//1 = normal return, 2 = cancel registration, 3 = cancel transfer, 4 = log off
		//5 = closing account, 6 = cancel update, 7 = updating, 8 = sign in
		if(choice == 1) {
			System.out.printf("Returning to previous menu");
			Thread.sleep(1000);
			System.out.printf(".");
			Thread.sleep(1000);
			System.out.printf(".");
			Thread.sleep(1000);
			System.out.printf(".\n");
			Thread.sleep(1000);

		}
		else if(choice == 2) {
			System.out.printf("Cancelling registration");
			Thread.sleep(1000);
			System.out.printf(".");
			Thread.sleep(1000);
			System.out.printf(".");
			Thread.sleep(1000);
			System.out.printf(".\n");
			Thread.sleep(1000);

		}
		else if(choice == 3){
			System.out.printf("Cancelling transferral");
			Thread.sleep(1000);
			System.out.printf(".");
			Thread.sleep(1000);
			System.out.printf(".");
			Thread.sleep(1000);
			System.out.printf(".\n");
			Thread.sleep(1000);

		}
		else if(choice == 4){
			System.out.printf("Logging off");
			Thread.sleep(1000);
			System.out.printf(".");
			Thread.sleep(1000);
			System.out.printf(".");
			Thread.sleep(1000);
			System.out.printf(".\n");
			Thread.sleep(1000);
			in.nextLine();
		}
		else if(choice == 5){
			System.out.printf("Closing account");
			Thread.sleep(1000);
			System.out.printf(".");
			Thread.sleep(1000);
			System.out.printf(".");
			Thread.sleep(1000);
			System.out.printf(".\n");
			Thread.sleep(1000);

		}
		else if(choice == 6) {
			System.out.printf("Cancelling account update");
			Thread.sleep(1000);
			System.out.printf(".");
			Thread.sleep(1000);
			System.out.printf(".");
			Thread.sleep(1000);
			System.out.printf(".\n");
			Thread.sleep(1000);

		}
		else if(choice == 7) {
			System.out.printf("Updating account information");
			Thread.sleep(1000);
			System.out.printf(".");
			Thread.sleep(1000);
			System.out.printf(".");
			Thread.sleep(1000);
			System.out.printf(".\n");
			Thread.sleep(1000);
		}
		else if(choice == 8) {
			System.out.printf("Logging in");
			Thread.sleep(1000);
			System.out.printf(".");
			Thread.sleep(1000);
			System.out.printf(".");
			Thread.sleep(1000);
			System.out.printf(".\n");
			Thread.sleep(1000);
		}
		else if(choice == 9) {
			System.out.printf("Entering registration interface");
			Thread.sleep(1000);
			System.out.printf(".");
			Thread.sleep(1000);
			System.out.printf(".");
			Thread.sleep(1000);
			System.out.printf(".\n");
			Thread.sleep(1000);
		}
		else if(choice == 10) {
			System.out.printf("Powering off");
			Thread.sleep(1000);
			System.out.printf(".");
			Thread.sleep(1000);
			System.out.printf(".");
			Thread.sleep(1000);
			System.out.printf(".\n");
			Thread.sleep(1000);
		}
		else if(choice == 11) {
			System.out.printf("Returning to start menu");
			Thread.sleep(1000);
			System.out.printf(".");
			Thread.sleep(1000);
			System.out.printf(".");
			Thread.sleep(1000);
			System.out.printf(".\n");
			Thread.sleep(1000);
		}
	}
	
}