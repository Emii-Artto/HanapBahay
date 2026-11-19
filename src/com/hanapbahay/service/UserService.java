package com.hanapbahay.service;

import java.util.Optional;

import com.hanapbahay.dao.UserDao;
import com.hanapbahay.model.User;

/**
 * Backend: login and account rules. Owner: Backend member.
 * UI calls these methods; they throw IllegalArgumentException with a message
 * the UI can show directly (e.g. in a JOptionPane).
 */
public class UserService {

	private final UserDao userDao;

	public UserService(UserDao userDao) {
		this.userDao = userDao;
	}

	/** Returns the user if the username and password match, otherwise empty. */
	public Optional<User> login(String username, String password) {
		if (isBlank(username) || isBlank(password)) {
			throw new IllegalArgumentException("Please enter your username and password.");
		}
		// TODO (Backend): compare hashed passwords instead of plain text.
		return userDao.findByUsername(username.trim())
				.filter(u -> u.getPassword().equals(password));
	}

	public User register(String username, String password, String fullName, User.Role role) {
		if (isBlank(username) || isBlank(password) || isBlank(fullName)) {
			throw new IllegalArgumentException("All fields are required.");
		}
		if (password.length() < 6) {
			throw new IllegalArgumentException("Password must be at least 6 characters.");
		}
		if (userDao.findByUsername(username.trim()).isPresent()) {
			throw new IllegalArgumentException("Username is already taken.");
		}
		return userDao.insert(new User(0, username.trim(), password, fullName.trim(), role));
	}

	private static boolean isBlank(String s) {
		return s == null || s.trim().isEmpty();
	}
}
