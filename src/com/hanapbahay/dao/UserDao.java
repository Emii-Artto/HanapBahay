package com.hanapbahay.dao;

import java.util.ArrayList;

import com.hanapbahay.model.Agent;
import com.hanapbahay.model.User;

/**
 * Login accounts (users table) and agent profiles (agents table).
 * Implemented by dao.mysql.MySqlUserDao. The services only know this interface.
 */
public interface UserDao {

	User findByUsername(String username);

	/** Saves a new login account. Returns the new user_id, or 0 if it failed. */
	int addAccount(String username, String passwordHash, String role);

	boolean deleteAccount(int userId);

	Agent findAgentByUserId(int userId);

	ArrayList<Agent> getAllAgents();

	boolean addAgent(Agent agent);

	boolean updateAgent(Agent agent);
}
