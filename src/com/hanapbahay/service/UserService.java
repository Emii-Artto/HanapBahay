package com.hanapbahay.service;

import java.util.ArrayList;

import com.hanapbahay.dao.UserDao;
import com.hanapbahay.model.Admin;
import com.hanapbahay.model.Agent;
import com.hanapbahay.model.User;

public class UserService extends BaseService {

	private UserDao userDao;

	public UserService(UserDao userDao) {
		this.userDao = userDao;
	}

	public User login(String username, String password) {
		if (isBlank(username) || isBlank(password)) {
			message = "Please enter your username and password.";
			return null;
		}

		User user = userDao.findByUsername(username.trim());
		if (user == null || !user.getPassword().equals(password)) {
			message = "Wrong username or password.";
			return null;
		}

		message = user.getWelcomeMessage();
		return user;
	}

	// Returns every Agent account (used by the Admin dashboard's agent table)
	public ArrayList<User> getAgents() {
		ArrayList<User> agents = new ArrayList<User>();
		for (User user : userDao.findAll()) {
			if (user.getRole().equals("AGENT")) {
				agents.add(user);
			}
		}
		message = agents.size() + " agents";
		return agents;
	}

	// Creates a new account in the database. Used by the "Create Account" form on the login screen.
	// Returns true if saved; if not, getMessage() tells why.
	public boolean register(String fullName, String username, String password, String confirm, String role) {
		if (isBlank(fullName) || isBlank(username) || isBlank(password) || isBlank(confirm)) {
			message = "Please complete all fields.";
			return false;
		}
		if (password.length() < 6) {
			message = "Password must contain at least 6 characters.";
			return false;
		}
		if (!password.equals(confirm)) {
			message = "Passwords do not match.";
			return false;
		}
		if (userDao.findByUsername(username.trim()) != null) {
			message = "Username already exists.";
			return false;
		}

		// POLYMORPHISM: the variable is a User, but the object is an Admin or an Agent
		User user;
		if (role.equalsIgnoreCase("Admin")) {
			user = new Admin(0, username.trim(), password, fullName.trim());
		} else {
			user = new Agent(0, username.trim(), password, fullName.trim());
		}
		userDao.add(user);

		// check that it was really saved (the DAO returns nothing if the database failed)
		if (userDao.findByUsername(username.trim()) == null) {
			message = "Cannot save the account. Please check the database.";
			return false;
		}
		message = "Account created successfully!\nYou can now log in.";
		return true;
	}
}
