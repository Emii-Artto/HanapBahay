package com.hanapbahay.service;

import java.util.ArrayList;

import com.hanapbahay.dao.UserDao;
import com.hanapbahay.model.Client;
import com.hanapbahay.model.User;

/**
 * Backend: login and registration rules. Owner: Backend member.
 */
public class UserService extends BaseService {

	// STATIC FINAL constant (Module 4) - one value shared by the class, cannot be changed
	public static final int MIN_PASSWORD_LENGTH = 6;

	// The service talks to the INTERFACE, so it works with sample data or MySQL (Module 5)
	private UserDao userDao;

	public UserService(UserDao userDao) {
		this.userDao = userDao;
	}

	/** Returns the logged-in user, or null if login failed (the reason is in getMessage()). */
	public User login(String username, String password) {
		if (isBlank(username) || isBlank(password)) {
			message = "Please enter your username and password.";
			return null;
		}

		User user = userDao.findByUsername(username.trim());
		// TODO (Backend): compare hashed passwords instead of plain text
		if (user == null || !user.getPassword().equals(password)) {
			message = "Wrong username or password.";
			return null;
		}

		message = user.getWelcomeMessage(); // runs Admin's, Agent's or Client's getRole() (polymorphism)
		return user;
	}

	/** Creates a new Client account. Returns true if it worked. */
	public boolean register(String username, String password, String confirmPassword, String fullName) {
		if (isBlank(username) || isBlank(password) || isBlank(fullName)) {
			message = "All fields are required.";
			return false;
		}
		if (password.length() < MIN_PASSWORD_LENGTH) {
			message = "Password must be at least " + MIN_PASSWORD_LENGTH + " characters.";
			return false;
		}
		if (!password.equals(confirmPassword)) {
			message = "Passwords do not match.";
			return false;
		}
		if (userDao.findByUsername(username.trim()) != null) {
			message = "Username is already taken.";
			return false;
		}

		userDao.add(new Client(0, username.trim(), password, fullName.trim()));
		message = "Account created. You can now log in.";
		return true;
	}

	/** Finds one user by ID (used to show names in tables). Returns null if not found. */
	public User getUser(int userId) {
		return userDao.findById(userId);
	}

	/** All users with the CLIENT role (used when recording a sale). */
	public ArrayList<User> getClients() {
		ArrayList<User> result = new ArrayList<User>();
		for (User user : userDao.findAll()) {
			if (user.getRole().equals("CLIENT")) {
				result.add(user);
			}
		}
		return result;
	}
}
