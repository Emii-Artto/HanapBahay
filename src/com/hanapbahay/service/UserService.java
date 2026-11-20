package com.hanapbahay.service;

import com.hanapbahay.dao.UserDao;
import com.hanapbahay.model.User;

/**
 * Backend: login rules. Owner: Backend member.
 * Only Admins and Agents log in. Clients use Client Mode on the agent's device instead.
 */
public class UserService extends BaseService {

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

		message = user.getWelcomeMessage(); // runs Admin's or Agent's getRole() (polymorphism)
		return user;
	}
}
