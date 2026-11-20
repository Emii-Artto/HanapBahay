package com.hanapbahay.model;

/**
 * INHERITANCE (Module 4) - an Agent IS-A User.
 * Agents post property listings, answer inquiries and record sales.
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
	public boolean canManageListings() {
		return true;
	}
}
