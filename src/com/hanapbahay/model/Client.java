package com.hanapbahay.model;

/**
 * INHERITANCE (Module 4) - a Client IS-A User.
 * Clients browse listings and send inquiries. They cannot manage listings,
 * so this class keeps the inherited canManageListings() which returns false.
 */
public class Client extends User {

	public Client(int userId, String username, String password, String fullName) {
		super(userId, username, password, fullName);
	}

	@Override
	public String getRole() {
		return "CLIENT";
	}
}
