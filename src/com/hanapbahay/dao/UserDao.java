package com.hanapbahay.dao;

import java.util.ArrayList;

import com.hanapbahay.model.User;

public interface UserDao {

	User findByUsername(String username);

	User findById(int userId);

	ArrayList<User> findAll();

	void add(User user);
}
