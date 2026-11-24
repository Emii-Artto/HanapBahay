package com.hanapbahay.service;

import com.hanapbahay.dao.UserDao;
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
}
