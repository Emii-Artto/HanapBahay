package com.hanapbahay.model;

public abstract class User {

	private int userId;
	private String username;
	private String password;
	private String fullName;

	public User(int userId, String username, String password, String fullName) {
		this.userId = userId;
		this.username = username;
		this.password = password;
		this.fullName = fullName;
	}

	public abstract String getRole();

	public abstract String getPermissions();

	public boolean canViewAllSales() {
		return false;
	}

	public String getWelcomeMessage() {
		return "Welcome, " + fullName + " (" + getRole() + ")";
	}
	
	
	@Override
	public String toString() {
		return fullName;
	}

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
