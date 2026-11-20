package com.hanapbahay.model;

/**
 * INHERITANCE (Module 4) - an Admin IS-A User.
 * Admins manage the whole system: listings, agents and sales reports.
 */
public class Admin extends User {

	public Admin(int userId, String username, String password, String fullName) {
		super(userId, username, password, fullName); // calls the User constructor
	}

	@Override
	public String getRole() {
		return "ADMIN";
	}

	@Override
	public boolean canManageListings() {
		return true;
	}

	@Override
	public boolean canViewAllSales() {
		return true;
	}
}
