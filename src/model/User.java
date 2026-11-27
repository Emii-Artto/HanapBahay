package model;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * 5.1.4 User - a login account: user ID, username, role, active status and a PRIVATE password hash.
 *
 * Roles, from the highest authority down:
 *   OWNER - created once at installation; manages Admins and Agents; cannot be deactivated
 *   ADMIN - manages Agents and sees all reports; cannot create or remove Admins
 *   AGENT - manages their own properties, clients, viewings and deals
 *
 * ENCAPSULATION: there is no getter for the password hash. Other classes can only ask
 * checkPassword("typed password"), which answers true or false.
 */
public class User {

	public static final String OWNER = "OWNER";
	public static final String ADMIN = "ADMIN";
	public static final String AGENT = "AGENT";

	private int userId;
	private String username;
	private String passwordHash; // SHA-256, never plain text
	private String role;
	private boolean active;
	private boolean mustChangePassword; // true = still using a temporary password
	private int createdBy; // user_id of the account that created this one (0 = created at installation)

	public User(int userId, String username, String passwordHash, String role, boolean active,
			boolean mustChangePassword, int createdBy) {
		this.userId = userId;
		this.username = username;
		this.passwordHash = passwordHash;
		this.role = role;
		this.active = active;
		this.mustChangePassword = mustChangePassword;
		this.createdBy = createdBy;
	}

	// Verifies a typed password without exposing the stored hash
	public boolean checkPassword(String typedPassword) {
		if (typedPassword == null) {
			return false;
		}
		return passwordHash.equals(hash(typedPassword));
	}

	// ENCAPSULATION: the new password is hashed inside this class; the hash never leaves it
	public void setNewPassword(String newPassword) {
		this.passwordHash = hash(newPassword);
		this.mustChangePassword = false;
	}

	// ---- what this account is allowed to do (role-based access, 3.3.2) ----

	public boolean isOwner() {
		return OWNER.equals(role);
	}

	public boolean isAdmin() {
		return ADMIN.equals(role);
	}

	public boolean isAgent() {
		return AGENT.equals(role);
	}

	/** Owner and Admins use the Admin dashboard and can add, reset and deactivate Agents. */
	public boolean canManageAgents() {
		return isOwner() || isAdmin();
	}

	/** Only the Owner can add, deactivate or reactivate Admins. */
	public boolean canManageAdmins() {
		return isOwner();
	}

	/** The role in normal words, e.g. "Owner". */
	public String getRoleLabel() {
		return role.substring(0, 1) + role.substring(1).toLowerCase();
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

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public boolean mustChangePassword() {
		return mustChangePassword;
	}

	public int getCreatedBy() {
		return createdBy;
	}

	@Override
	public String toString() {
		return username;
	}
}
