package com.hanapbahay.model;

/**
 * INHERITANCE (Module 4) - an Agent IS-A User.
 * Agents post listings, open Client Mode for walk-in clients, answer inquiries and record sales.
 * Agents keep the inherited canViewAllSales() which returns false: they see only their own sales.
 */
public class Agent extends User {

	public Agent(int userId, String username, String password, String fullName) {
		super(userId, username, password, fullName);
	}

	@Override
	public String getRole() {
		return "AGENT";
	}

	@Override
	public String getPermissions() {
		return "Manage own listings, open Client Mode, and record own sales";
	}
}
