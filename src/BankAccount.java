import java.text.DecimalFormat;

/**
 * Just like last time, the BankAccount class is primarily responsible
 * for depositing and withdrawing money. In the enhanced version, there
 * will be the added requirement of transferring funds between accounts.
 * 
 * Most of the functionality for this class should have already been
 * implemented last time. You can always reference my Github repository
 * for inspiration (https://github.com/rwilson-ucvts/java-sample-atm).
 */


//import java.util.Scanner;

public class BankAccount {
	private User user;
	private long accountNumber;
	private double balance = 0.00;
	private String status;
	private DecimalFormat df2;
	//private String df2;
	//private String df3;
	
	public BankAccount(String account) {
		this.user = new User(account.substring(48,63), account.substring(28,48), Integer.valueOf(account.substring(9, 13)), account.substring(63, 71), Long.valueOf(account.substring(71,81)), account.substring(81, 111), account.substring(111, 141), account.substring(141, 143), account.substring(143,148));
		this.accountNumber = Long.valueOf(account.substring(0,9));
		this.balance = Double.valueOf(account.substring(13, 28));
		this.status = account.substring(148, 149);
		this.df2 = new DecimalFormat();
		df2.setMaximumFractionDigits(2);
	}
	
	public User getUser() {
		return this.user;
	}
	
	public void Deposit(double amount) {
		if( amount <= 0 || !isValidDollarAmount(amount)) {
			System.out.println("Invalid deposit amount.");
		}
		else {
			this.balance += amount;
			System.out.printf("\nYou have successfully deposited $%.2f into your account.", amount);
		}
	}
	
	public void Withdraw(double amount) {
		//df3 = new DecimalFormat("#.##").format(amount);
		if( amount <= 0 || amount >= balance || !isValidDollarAmount(amount)) {
			System.out.println("Invalid withdrawal amount.");
		}
		else {
			this.balance -= amount;
			System.out.printf("\nYou have successfully withdrawn $%.2f from your account.", amount);
		}
	}
	
	public void Transfer(BankAccount transferAcct, double amount) {
		//String df3 = new DecimalFormat("###############.##").format(amount);
		if( amount <= 0 || amount >= balance || !isValidDollarAmount(amount)) {
			System.out.println("Invalid transfer amount.");
		}
		else {
			this.balance -= amount;
			transferAcct.balance +=amount;
			System.out.println("Transaction complete.");
		}
		
	}
	
	public double getBalance() {
		return this.balance;
	}
	
	public String getBalanceString() {
		String balanceString = df2.format(this.balance);
		return balanceString;
	}
	
	public long getAcctNumb() {
		return this.accountNumber;
	}
	
	public void viewBalance() {
		System.out.printf("Your current balance is: $%.2f.\n\n", balance);
	}
	
	public void viewAcctInfo() {
		System.out.println("Account Information: \n");
		System.out.println("First Name: " + user.getFirstName());
		System.out.println("Last Name: " + user.getLastName());
		System.out.println("Pin: " + user.getPin());
		System.out.println("Date of Birth: " + user.getDateOfBirth());
		System.out.println("Telephone Number: " + user.getPhoneNumber());
		System.out.println("Street Address: " + user.getAddress());
		System.out.println("City: " + user.getCity());
		System.out.println("State: " + user.getState());
		System.out.println("Postal Code: " + user.getPostalCode());
	}
	
	public void setStatus() {
		this.status = "N";
	}
	
	public String toString() {
		String bankAcctString = "";
		bankAcctString += Long.toString(this.accountNumber);
		bankAcctString += Integer.toString(this.user.getPin());
		bankAcctString += String.format("%1$-15s", this.getBalanceString());
		bankAcctString += String.format("%1$-20s", this.user.getLastName());
		bankAcctString += String.format("%1$-15s", this.user.getFirstName());
		bankAcctString += this.user.getDateOfBirth();
		bankAcctString += Long.toString(this.user.getPhoneNumber());
		bankAcctString += this.user.getAddress();
		bankAcctString += this.user.getCity();
		bankAcctString += this.user.getState();
		bankAcctString += this.user.getPostalCode();
		bankAcctString += this.status;
		//System.out.println(bankAcctString);
		return bankAcctString;
	}
	
	public boolean isValidDollarAmount(double amount) {
		String amountString = Double.toString(amount);
		for(int i = 0; i < amountString.length(); i++) {
			if(amountString.charAt(i) == '.') {
				int numberOver = amountString.length() - i;
				if(numberOver > 3) {
					return false;
				}
				return true;
			}
		}
		return true;
	}
}