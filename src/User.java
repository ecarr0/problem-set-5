/**
 * Just like last time, the User class is responsible for retrieving
 * (i.e., getting), and updating (i.e., setting) user information.
 * This time, though, you'll need to add the ability to update user
 * information and display that information in a formatted manner.
 * 
 * Most of the functionality for this class should have already been
 * implemented last time. You can always reference my Github repository
 * for inspiration (https://github.com/rwilson-ucvts/java-sample-atm).
 */

import java.util.Scanner;

public class User {
	private String firstName;
	private String lastName;
	private int pin;
	private String dateOfBirth;
	private long phoneNumber;
	private String address;
	private String city;
	private String state;
	private String postalCode;
	public Scanner in = new Scanner(System.in);
	
	public User(String firstName, String lastName, int pin, String dateOfBirth, long phoneNumber, String address, String city, String state, String postalCode) {
		this.firstName = firstName;
		this.lastName = lastName;
		this.pin = pin;
		this.dateOfBirth = dateOfBirth;
		this.phoneNumber = phoneNumber;
		this.address = address;
		this.city = city;
		this.state = state;
		this.postalCode = postalCode;
	}
	
	public String getFirstName() {
		return this.firstName;
	}
	
	public String getLastName() {
		return this.lastName;
	}
	
	public int getPin() {
		return this.pin;
	}
	
	public void setPin(int newPin) {
		this.pin = newPin;
	}
	
	public String getDateOfBirth() {
		return this.dateOfBirth;
	}
	
	public long getPhoneNumber() {
		return this.phoneNumber;
	}
	
	public void setPhoneNumber(long newPhoneNumber) {
		this.phoneNumber = newPhoneNumber;
	}
	
	public String getAddress() {
		return this.address;
	}
	
	public void setAddress(String newAddress) {
		this.address = newAddress;
	}
	
	public String getCity() {
		return this.city;
	}
	
	public void setCity(String newCity) {
		this.city = newCity;
	}
	
	public String getState() {
		return this.state;
	}
	
	public void setState(String newState) {
		this.state = newState;
	}
	
	public String getPostalCode() {
		return this.postalCode;
	}
	
	public void setPostalCode(String newPostalCode) {
		this.postalCode = newPostalCode;
	}
	
}