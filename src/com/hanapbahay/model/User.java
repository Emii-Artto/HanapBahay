package com.hanapbahay.model;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * 5.1.4 User - a login account (user ID, username, role and a PRIVATE password hash).
 *
 * ENCAPSULATION: there is no getter for the password hash. Other classes can only ask
 * checkPassword("typed password"), which answers true or false.
 */
public class User {

	public static final String ADMIN = "ADMIN";
	public static final String AGENT = "AGENT";

	private int userId;
	private String username;
	private String passwordHash; // SHA-256, never plain text
	private String role;

	public User(int userId, String username, String passwordHash, String role) {
		this.userId = userId;
		this.username = username;
		this.passwordHash = passwordHash;
		this.role = role;
	}

	// Verifies a typed password without exposing the stored hash
	public boolean checkPassword(String typedPassword) {
		if (typedPassword == null) {
			return false;
		}
		return passwordHash.equals(hash(typedPassword));
	}

	public boolean isAdmin() {
		return ADMIN.equals(role);
	}

	/** Turns a password into its SHA-256 hash (64 hex characters). */
	public static String hash(String password) {
		try {
			MessageDigest digest = MessageDigest.getInstance("SHA-256");
			byte[] bytes = digest.digest(password.getBytes(StandardCharsets.UTF_8));
			StringBuilder hex = new StringBuilder();
			for (int i = 0; i < bytes.length; i++) {
				hex.append(String.format("%02x", bytes[i]));
			}
			return hex.toString();
		} catch (NoSuchAlgorithmException e) {
			// every Java installation has SHA-256, so this should never happen
			throw new IllegalStateException("SHA-256 is not available", e);
		}
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

	public String getRole() {
		return role;
	}

	@Override
	public String toString() {
		return username;
	}
}
