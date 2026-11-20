package com.hanapbahay.model;

/**
 * ABSTRACT CLASS (Module 5) - the general idea of a user who can log in.
 * We never create a plain "User"; we create an Admin or an Agent.
 * (Clients don't log in. An agent opens Client Mode and hands the device to the client.)
 *
 * ENCAPSULATION (Module 4) - all fields are private, accessed through getters and setters.
 */
public abstract class User {

	private int userId;
	private String username;
	private String password;
	private String fullName;

	// Constructor (Module 3) - uses "this" to tell fields apart from parameters
	public User(int userId, String username, String password, String fullName) {
		this.userId = userId;
		this.username = username;
		this.password = password;
		this.fullName = fullName;
	}

	// ABSTRACT METHODS - every subclass MUST give its own version
	public abstract String getRole();

	public abstract String getPermissions();

	// CONCRETE METHOD - subclasses inherit it. Only Admin overrides it to true:
	// an Admin sees ALL sales, an Agent sees only their own.
	public boolean canViewAllSales() {
		return false;
	}

	// CONCRETE METHOD shared by all users. Calls getRole(), which runs the subclass version.
	public String getWelcomeMessage() {
		return "Welcome, " + fullName + " (" + getRole() + ")";
	}

	// OVERRIDING (Module 5) Object's toString(): a JComboBox calls this to show the user's name
	@Override
	public String toString() {
		return fullName;
	}

	// ---- Getters and setters ----
	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}
}
