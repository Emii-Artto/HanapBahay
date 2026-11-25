package com.hanapbahay.service;

import java.util.ArrayList;

import com.hanapbahay.dao.UserDao;
import com.hanapbahay.model.Agent;
import com.hanapbahay.model.User;

/**
 * Login, creating agent accounts, and agent profiles (5.3.3 Login Validation).
 */
public class UserService extends BaseService {

	private UserDao userDAO;

	public UserService(UserDao userDAO) {
		this.userDAO = userDAO;
	}

	/** Returns the User if the username and password are correct, otherwise null (see getMessage()). */
	public User login(String username, String password) {
		if (isBlank(username) || isBlank(password)) {
			message = "Please enter your username and password.";
			return null;
		}
		User user = userDAO.findByUsername(username.trim());
		// checkPassword() compares hashes; the stored hash is never read by this class
		if (user == null || !user.checkPassword(password)) {
			message = "Wrong username or password.";
			return null;
		}
		message = "Welcome, " + user.getUsername() + "!";
		return user;
	}

	/** The agent profile of a login account, or null for an admin account. */
	public Agent getAgentProfile(User user) {
		if (user == null || user.isAdmin()) {
			return null;
		}
		Agent agent = userDAO.findAgentByUserId(user.getUserId());
		if (agent == null) {
			message = "This account has no agent profile.";
		}
		return agent;
	}

	public ArrayList<Agent> getAllAgents() {
		return userDAO.getAllAgents();
	}

	/** Creates a new AGENT account with its profile (the "Create Account" form). */
	public boolean registerAgent(String firstName, String lastName, String contactNumber, String email,
			String username, String password, String confirmPassword) {
		if (isBlank(firstName) || isBlank(lastName) || isBlank(contactNumber) || isBlank(username)
				|| isBlank(password) || isBlank(confirmPassword)) {
			message = "Please complete all required fields (everything except email).";
			return false;
		}
		if (!isValidEmail(email)) {
			message = "The email \"" + email.trim() + "\" is not valid. Example: name@email.com";
			return false;
		}
		if (password.length() < 6) {
			message = "The password must have at least 6 characters.";
			return false;
		}
		if (!password.equals(confirmPassword)) {
			message = "The passwords do not match.";
			return false;
		}
		if (userDAO.findByUsername(username.trim()) != null) {
			message = "The username \"" + username.trim() + "\" is already taken.";
			return false;
		}

		// 1. the login account (only the hash of the password is saved)
		int userId = userDAO.addAccount(username.trim(), User.hash(password), User.AGENT);
		if (userId == 0) {
			message = "Cannot save the account. Please check the database connection.";
			return false;
		}
		// 2. the agent profile linked to it
		Agent agent = new Agent(0, userId, firstName.trim(), lastName.trim(), contactNumber.trim(), clean(email));
		if (!userDAO.addAgent(agent)) {
			userDAO.deleteAccount(userId); // undo step 1 so no half-made account is left
			message = "Cannot save the agent profile. Please check the database connection.";
			return false;
		}
		message = "Account created successfully!\nYou can now log in as " + username.trim() + ".";
		return true;
	}

	/** AgentPanel: edit the logged-in agent's own profile. */
	public boolean updateProfile(Agent agent, String firstName, String lastName, String contactNumber, String email) {
		if (isBlank(firstName) || isBlank(lastName) || isBlank(contactNumber)) {
			message = "First name, last name and contact number are required.";
			return false;
		}
		if (!isValidEmail(email)) {
			message = "The email \"" + email.trim() + "\" is not valid. Example: name@email.com";
			return false;
		}
		agent.setFirstName(firstName.trim());
		agent.setLastName(lastName.trim());
		agent.setContactNumber(contactNumber.trim());
		agent.setEmail(clean(email));
		if (!userDAO.updateAgent(agent)) {
			message = "Cannot save your profile. Please check the database connection.";
			return false;
		}
		message = "Profile updated.";
		return true;
	}

	// email is optional; if typed, it must look like name@domain
	private boolean isValidEmail(String email) {
		if (isBlank(email)) {
			return true;
		}
		String e = email.trim();
		int at = e.indexOf('@');
		return at > 0 && e.indexOf('.', at) > at + 1 && !e.endsWith(".") && !e.contains(" ");
	}

	private String clean(String text) {
		if (text == null) {
			return "";
		}
		return text.trim();
	}
}
