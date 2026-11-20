package com.hanapbahay.dao.sample;

import java.util.ArrayList;

import com.hanapbahay.dao.UserDao;
import com.hanapbahay.model.Admin;
import com.hanapbahay.model.Agent;
import com.hanapbahay.model.User;

/**
 * Sample data kept in memory so the app can run before the MySQL database is ready.
 * The data resets every time the app starts.
 *
 * IMPLEMENTS the UserDao interface (Module 5): it must give a body to every method in UserDao.
 */
public class SampleUserDao implements UserDao {

	private ArrayList<User> users = new ArrayList<User>();
	private int nextId = 1;

	public SampleUserDao() {
		// POLYMORPHISM (Module 5): the list holds User, but each item is an Admin or an Agent
		add(new Admin(0, "admin", "admin123", "System Admin"));
		add(new Agent(0, "agent1", "agent123", "Maria Santos"));
		add(new Agent(0, "agent2", "agent123", "Paolo Reyes"));
	}

	@Override
	public User findByUsername(String username) {
		for (User user : users) {
			if (user.getUsername().equalsIgnoreCase(username)) {
				return user;
			}
		}
		return null;
	}

	@Override
	public User findById(int userId) {
		for (User user : users) {
			if (user.getUserId() == userId) {
				return user;
			}
		}
		return null;
	}

	@Override
	public ArrayList<User> findAll() {
		return users;
	}

	@Override
	public void add(User user) {
		user.setUserId(nextId);
		nextId++;
		users.add(user);
	}
}
