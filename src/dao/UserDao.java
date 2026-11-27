package dao;

import java.util.ArrayList;

import model.Agent;
import model.User;

/**
 * Login accounts (users table) and agent profiles (agents table).
 * Implemented by mysql.MySqlUserDao. The services only know this interface.
 */
public interface UserDao {

	User findByUsername(String username);

	User findById(int userId);

	ArrayList<User> getAllUsers();

	/** Saves a new login account with a temporary password. Returns the new user_id, or 0 if it failed. */
	int addAccount(String username, String passwordHash, String role, int createdBy);

	boolean deleteAccount(int userId);

	boolean setActive(int userId, boolean active);

	/** mustChange = true for a temporary password that must be changed at the next login. */
	boolean updatePassword(int userId, String passwordHash, boolean mustChange);

	Agent findAgentByUserId(int userId);

	ArrayList<Agent> getAllAgents();

	boolean addAgent(Agent agent);

	boolean updateAgent(Agent agent);
}
