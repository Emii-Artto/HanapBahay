package com.hanapbahay.model;

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
