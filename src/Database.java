/**
 * This class will serve as the intermediary between our ATM program and
 * the database of BankAccounts. It'll be responsible for fetching accounts
 * when users try to login, as well as updating those accounts after any
 * changes are made.
 */

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
//import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
//import java.io.InputStreamReader;
import java.util.Arrays;

public class Database {
	
	private String path;
	private String[] accounts;
	
	public Database(String path) throws FileNotFoundException, IOException {
		this.path = path;
		this.accounts = getAllAccounts();
	}
	
	
	public String[] getAllAccounts() throws FileNotFoundException, IOException {
		int count = 0;
		String[] accounts = new String[10];
		
		FileReader altered = null;
		InputStreamReader original = null;
		try {
			altered = new FileReader(System.getProperty("user.dir") + File.separator + path);			
		} catch (FileNotFoundException e) {
			original = new InputStreamReader(getClass().getResourceAsStream(path));
		}
		
		try (BufferedReader br = new BufferedReader(original != null ? original : altered)) {
			String line;
			
			while ((line = br.readLine()) != null) {
				if (count >= accounts.length) {
					accounts = Arrays.copyOf(accounts, accounts.length + 10);
				}
				accounts[count++] = line;
			}
		}
		
		return Arrays.copyOf(accounts, count);
	}
	
	public String getAccount(long accountNumber) {
		for (String account : accounts) {
			if (account.startsWith(String.valueOf(accountNumber)) && account.endsWith("Y")) {
				return account;
			}
		}
		
		return null;
	}
	
	public void updateAccount(BankAccount account, BankAccount destination) throws IOException {
		boolean newAccount = true;
		
		for (int i = 0; i < accounts.length; i++) {			
			if (accounts[i].startsWith(String.valueOf(account.getAcctNumb()))) {
				accounts[i] = account.toString();
				newAccount = false;
			}
			
			if (destination != null) {
				if (accounts[i].startsWith(String.valueOf(destination.getAcctNumb()))) {
					accounts[i] = destination.toString();
				}
			}
		}
		
		if (newAccount) {
			accounts = Arrays.copyOf(accounts, accounts.length + 1);
			accounts[accounts.length - 1] = account.toString();
		}
		
		try (BufferedWriter bw = new BufferedWriter(new FileWriter(System.getProperty("user.dir") + File.separator + path))) {
			for (String acct : accounts) {
				bw.write(acct);
				bw.newLine();
			}
		}
	}
	
	public long getMaxAccountNumber() {
		long max = -1L;
		
		for (String account : accounts) {
			long accountNumber = Long.parseLong(account.substring(0, 9));
			
			if (accountNumber > max) {
				max = accountNumber;
			}
		}
		
		return max;
	}
}