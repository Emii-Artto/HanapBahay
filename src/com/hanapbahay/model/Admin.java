package com.hanapbahay.model;

public class Admin extends User {

	public Admin(int userId, String username, String password, String fullName) {
		super(userId, username, password, fullName); // calls the User constructor
	}

	@Override
	public String getRole() {
		return "ADMIN";
	}

	@Override
	public String getPermissions() {
		return "Manage agent accounts, all listings, and all sales reports";
	}

	@Override
	public boolean canViewAllSales() {
		return true;
	}
}
