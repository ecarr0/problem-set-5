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
			System.out.println(account.toString());
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
	
/*	
	public BankAccount checkExistingAccount(int accountNumber) {
		try(BufferedReader br = new BufferedReader(new FileReader(System.getProperty("user.dir") + File.separator + path))) {
			String line;
			
			while((line = br.readLine()) != null) {
				if(accountNumber == Integer.valueOf(line.substring(0, 9))) {
					System.out.println(Integer.valueOf(line.substring(0, 9)));
					System.out.println(line.substring(148, 149));
					if((line.substring(148, 149)).equals("Y")) {
						BankAccount account = new BankAccount(User user, accountNumber, user.getBalance);
					}
				}
			}
		} 
		catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}
	
	public int setPin(int accountNumber) {
		try(BufferedReader br = new BufferedReader(new FileReader(System.getProperty("user.dir") + File.separator + path))) {
			String line;
			
			while((line = br.readLine()) != null) {
				if(accountNumber == Integer.valueOf(line.substring(0, 9))) {
					return Integer.valueOf(line.substring(9, 13));
				}
			}
		} 	
		catch (FileNotFoundException e) {
			e.printStackTrace();
		} 
		catch (IOException e) {
			e.printStackTrace();
		}
		return 0;
	}
	
	public double setBalance(int accountNumber) {
		try(BufferedReader br = new BufferedReader(new FileReader(System.getProperty("user.dir") + File.separator + path))) {
			String line;
			
			while((line = br.readLine()) != null) {
				if(accountNumber == Integer.valueOf(line.substring(0, 9))) {
					return Double.valueOf(line.substring(13, 28));
				}
			}
			return 0;
		} 
		catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return 0;
	}
	
	public String setLastName(int accountNumber) {
		try(BufferedReader br = new BufferedReader(new FileReader(System.getProperty("user.dir") + File.separator + path))) {
			String line;
			
			while((line = br.readLine()) != null) {
				if(accountNumber == Integer.valueOf(line.substring(0, 9))) {
					return line.substring(28, 48);
				}
			}
			return null;
		} 
		catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	public String setFirstName(int accountNumber) {
		try(BufferedReader br = new BufferedReader(new FileReader(System.getProperty("user.dir") + File.separator + path))) {
			String line;
			
			while((line = br.readLine()) != null) {
				if(accountNumber == Integer.valueOf(line.substring(0, 9))) {
					return line.substring(48, 63);
				}
			}
			return null;
		} 
		catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	public String setBirthDate(int accountNumber) {
		try(BufferedReader br = new BufferedReader(new FileReader(System.getProperty("user.dir") + File.separator + path))) {
			String line;
			
			while((line = br.readLine()) != null) {
				if(accountNumber == Integer.valueOf(line.substring(0, 9))) {
					return line.substring(63, 71);
				}
			}
			return null;
		} 
		catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	public long setPhoneNumber(int accountNumber) {
		try(BufferedReader br = new BufferedReader(new FileReader(System.getProperty("user.dir") + File.separator + path))) {
			String line;
			
			while((line = br.readLine()) != null) {
				if(accountNumber == Integer.valueOf(line.substring(0, 9))) {
					return Long.valueOf(line.substring(71, 81));
				}
			}
			return 0;
		} 
		catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return 0;
	}
	
	public String setAddress(int accountNumber) {
		try(BufferedReader br = new BufferedReader(new FileReader(System.getProperty("user.dir") + File.separator + path))) {
			String line;
			
			while((line = br.readLine()) != null) {
				if(accountNumber == Integer.valueOf(line.substring(0, 9))) {
					return line.substring(81, 111);
				}
			}
			return null;
		} 
		catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	public String setCity(int accountNumber) {
		try(BufferedReader br = new BufferedReader(new FileReader(System.getProperty("user.dir") + File.separator + path))) {
			String line;
			
			while((line = br.readLine()) != null) {
				if(accountNumber == Integer.valueOf(line.substring(0, 9))) {
					return line.substring(111, 141);
				}
			}
			return null;
		} 
		catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	public String setState(int accountNumber) {
		try(BufferedReader br = new BufferedReader(new FileReader(System.getProperty("user.dir") + File.separator + path))) {
			String line;
			
			while((line = br.readLine()) != null) {
				if(accountNumber == Integer.valueOf(line.substring(0, 9))) {
					return line.substring(141, 143);
				}
			}
			return null;
		} 
		catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	public String setPostalCode(int accountNumber) {
		try(BufferedReader br = new BufferedReader(new FileReader(System.getProperty("user.dir") + File.separator + path))) {
			String line;
			
			while((line = br.readLine()) != null) {
				if(accountNumber == Integer.valueOf(line.substring(0, 9))) {
					return line.substring(143, 148);
				}
			}
			return null;
		} 
		catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	public String setStatus(int accountNumber) {
//		for (String account : accounts) {
//			if (accountNumber == Integer.valueOf(account.substring(0, 9))) {
//				return account.substring(0, 9);
//			}
//		}
//		
//		return null;
		
		
		try(BufferedReader br = new BufferedReader(new FileReader(System.getProperty("user.dir") + File.separator + path))) {
			String line;
			
			while((line = br.readLine()) != null) {
				if(accountNumber == Integer.valueOf(line.substring(0, 9))) {
					return line.substring(148, 149);
				}
			}
			return null;
		} 
		catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	//writing to the file
	
	public void updateAccount(BankAccount account, BankAccount edited) throws FileNotFoundException, IOException {
		int count = 0;
		try(BufferedReader br = new BufferedReader(new FileReader("C:\\Users\\ekar\\Desktop\\APCSA\\pset5\\accounts-db.txt"))) {
			while((line = br.readLine()) != null) {
				if(count >= accounts.length) {
					accounts = Arrays.copyOf(accounts, accounts.length + 10);
				}
				accounts[count++] = line;
			}
		}
		boolean newAccount = true;
		
		for(int i = 0; i < accounts.length; i++) {
			System.out.println(account.getAcctNumb());
			if(accounts[i].startsWith(String.valueOf(account.getAcctNumb()))) { //account itself might be null URGENT FIX
				accounts[i] = account.toString();
				newAccount = false;
			}
			
			if(edited != null) {
				if(accounts[i].startsWith(String.valueOf(edited.getAcctNumb()))) {
					accounts[i] = account.toString();
				}
			}
			
		}
		
		if(newAccount) {
			accounts = Arrays.copyOf(accounts, accounts.length + 1);
			accounts[accounts.length - 1] = account.toString();
		}
		
		try(BufferedWriter bw = new BufferedWriter(new FileWriter(System.getProperty("user.dir") + File.separator + path))) {
			for(String acct : accounts) {
				bw.write(acct);
				bw.newLine();
			}
			
		}
		
	}
	
	*/
}