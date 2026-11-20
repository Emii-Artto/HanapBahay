package com.hanapbahay.dao;

import java.util.ArrayList;

import com.hanapbahay.model.User;

/**
 * INTERFACE (Module 5, full abstraction) - says WHAT can be done with users, not HOW.
 * SampleUserDao does it with sample data in memory; the Database member's MySQL class
 * will do it with real tables. The backend works with either one.
 */
public interface UserDao {

	User findByUsername(String username); // returns null if not found

	User findById(int userId); // returns null if not found

	ArrayList<User> findAll();

	void add(User user);
}
