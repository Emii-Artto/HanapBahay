package com.hanapbahay.dao;

import java.util.List;
import java.util.Optional;

import com.hanapbahay.model.User;

/** Contract between Backend and Database. Database member implements this with MySQL. */
public interface UserDao {

	Optional<User> findByUsername(String username);

	Optional<User> findById(int userId);

	List<User> findAll();

	/** Saves a new user and returns it with its generated userId. */
	User insert(User user);
}
