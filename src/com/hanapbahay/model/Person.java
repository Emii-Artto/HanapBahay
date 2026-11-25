package com.hanapbahay.model;

/**
 * 5.1.1 Person - ABSTRACT parent class for the people in the system (Agent and Client).
 *
 * INHERITANCE: Agent and Client inherit the name, contact number and email from here,
 * so these fields and methods are written only once.
 * ABSTRACTION: you cannot create a plain Person; only an Agent or a Client.
 * ENCAPSULATION: the fields are private and changed only through setters.
 */
public abstract class Person {

	private String firstName;
	private String lastName;
	private String contactNumber;
	private String email;

	public Person(String firstName, String lastName, String contactNumber, String email) {
		this.firstName = firstName;
		this.lastName = lastName;
		this.contactNumber = contactNumber;
		this.email = email;
	}

	// ABSTRACT METHOD: each kind of person describes itself differently
	public abstract String getRoleLabel();

	public String getFullName() {
		return firstName + " " + lastName;
	}

	// shown in drop-down lists (JComboBox calls toString)
	@Override
	public String toString() {
		return getFullName();
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getContactNumber() {
		return contactNumber;
	}

	public void setContactNumber(String contactNumber) {
		this.contactNumber = contactNumber;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
}
