package com.hanapbahay.dao.stub;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.hanapbahay.dao.UserDao;
import com.hanapbahay.model.User;
import com.hanapbahay.model.User.Role;

/** Fake in-memory data so the app runs before MySQL is ready. Data resets on every run. */
public class StubUserDao implements UserDao {

	private final List<User> users = new ArrayList<>();
	private int nextId = 1;

	public StubUserDao() {
		insert(new User(0, "admin", "admin123", "System Admin", Role.ADMIN));
		insert(new User(0, "agent1", "agent123", "Maria Santos", Role.AGENT));
		insert(new User(0, "client1", "client123", "Juan Dela Cruz", Role.CLIENT));
	}

	@Override
	public Optional<User> findByUsername(String username) {
		return users.stream().filter(u -> u.getUsername().equalsIgnoreCase(username)).findFirst();
	}

	@Override
	public Optional<User> findById(int userId) {
		return users.stream().filter(u -> u.getUserId() == userId).findFirst();
	}

	@Override
	public List<User> findAll() {
		return new ArrayList<>(users);
	}

	@Override
	public User insert(User user) {
		user.setUserId(nextId++);
		users.add(user);
		return user;
	}
}
