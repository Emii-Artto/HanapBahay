package com.hanapbahay.dao.mysql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import com.hanapbahay.model.Agent;
import com.hanapbahay.model.User;

import com.hanapbahay.dao.DBConnection;
import com.hanapbahay.dao.UserDao;

/**
 * UserDAO - handles login accounts (users table) and agent profiles (agents table).
 * Every query uses a PreparedStatement with ? so typed text can never change the SQL.
 */
public class MySqlUserDao extends MySqlBaseDao implements UserDao {

	@Override
	public User findByUsername(String username) {
		Connection conn = DBConnection.getConnection();
		if (conn == null) {
			return null;
		}
		try {
			PreparedStatement ps = conn.prepareStatement("SELECT * FROM users WHERE username = ?");
			ps.setString(1, username);
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				return toUser(rs);
			}
		} catch (SQLException e) {
			printError("Find user", e);
		} finally {
			close(conn);
		}
		return null;
	}

	@Override
	public User findById(int userId) {
		Connection conn = DBConnection.getConnection();
		if (conn == null) {
			return null;
		}
		try {
			PreparedStatement ps = conn.prepareStatement("SELECT * FROM users WHERE user_id = ?");
			ps.setInt(1, userId);
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				return toUser(rs);
			}
		} catch (SQLException e) {
			printError("Find user", e);
		} finally {
			close(conn);
		}
		return null;
	}

	/** Every account, the Owner first, then Admins, then Agents. */
	@Override
	public ArrayList<User> getAllUsers() {
		ArrayList<User> users = new ArrayList<User>();
		Connection conn = DBConnection.getConnection();
		if (conn == null) {
			return users;
		}
		try {
			PreparedStatement ps = conn.prepareStatement("SELECT * FROM users ORDER BY role, username");
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				users.add(toUser(rs));
			}
		} catch (SQLException e) {
			printError("Get users", e);
		} finally {
			close(conn);
		}
		return users;
	}

	private User toUser(ResultSet rs) throws SQLException {
		return new User(rs.getInt("user_id"), rs.getString("username"), rs.getString("password_hash"),
				rs.getString("role"), rs.getBoolean("is_active"), rs.getBoolean("must_change_password"),
				rs.getInt("created_by"));
	}

	@Override
	public boolean setActive(int userId, boolean active) {
		Connection conn = DBConnection.getConnection();
		if (conn == null) {
			return false;
		}
		try {
			PreparedStatement ps = conn.prepareStatement("UPDATE users SET is_active = ? WHERE user_id = ?");
			ps.setBoolean(1, active);
			ps.setInt(2, userId);
			return ps.executeUpdate() > 0;
		} catch (SQLException e) {
			printError("Change account status", e);
			return false;
		} finally {
			close(conn);
		}
	}

	@Override
	public boolean updatePassword(int userId, String passwordHash, boolean mustChange) {
		Connection conn = DBConnection.getConnection();
		if (conn == null) {
			return false;
		}
		try {
			PreparedStatement ps = conn.prepareStatement(
					"UPDATE users SET password_hash = ?, must_change_password = ? WHERE user_id = ?");
			ps.setString(1, passwordHash);
			ps.setBoolean(2, mustChange);
			ps.setInt(3, userId);
			return ps.executeUpdate() > 0;
		} catch (SQLException e) {
			printError("Reset password", e);
			return false;
		} finally {
			close(conn);
		}
	}

	/** Saves a new login account. Returns the new user_id, or 0 if it failed. */
	@Override
	public int addAccount(String username, String passwordHash, String role, int createdBy) {
		Connection conn = DBConnection.getConnection();
		if (conn == null) {
			return 0;
		}
		try {
			PreparedStatement ps = conn.prepareStatement(
					"INSERT INTO users (username, password_hash, role, must_change_password, created_by) "
							+ "VALUES (?, ?, ?, 1, ?)", // 1 = the temporary password must be changed at first login
					Statement.RETURN_GENERATED_KEYS);
			ps.setString(1, username);
			ps.setString(2, passwordHash);
			ps.setString(3, role);
			ps.setInt(4, createdBy);
			ps.executeUpdate();
			ResultSet keys = ps.getGeneratedKeys();
			if (keys.next()) {
				return keys.getInt(1);
			}
		} catch (SQLException e) {
			printError("Add account", e);
		} finally {
			close(conn);
		}
		return 0;
	}

	@Override
	public boolean deleteAccount(int userId) {
		Connection conn = DBConnection.getConnection();
		if (conn == null) {
			return false;
		}
		try {
			PreparedStatement ps = conn.prepareStatement("DELETE FROM users WHERE user_id = ?");
			ps.setInt(1, userId);
			return ps.executeUpdate() > 0;
		} catch (SQLException e) {
			printError("Delete account", e);
			return false;
		} finally {
			close(conn);
		}
	}

	// ---------------- agent profiles ----------------

	@Override
	public Agent findAgentByUserId(int userId) {
		Connection conn = DBConnection.getConnection();
		if (conn == null) {
			return null;
		}
		try {
			PreparedStatement ps = conn.prepareStatement("SELECT * FROM agents WHERE user_id = ?");
			ps.setInt(1, userId);
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				return toAgent(rs);
			}
		} catch (SQLException e) {
			printError("Find agent", e);
		} finally {
			close(conn);
		}
		return null;
	}

	@Override
	public ArrayList<Agent> getAllAgents() {
		ArrayList<Agent> agents = new ArrayList<Agent>();
		Connection conn = DBConnection.getConnection();
		if (conn == null) {
			return agents;
		}
		try {
			PreparedStatement ps = conn.prepareStatement("SELECT * FROM agents ORDER BY last_name, first_name");
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				agents.add(toAgent(rs));
			}
		} catch (SQLException e) {
			printError("Get agents", e);
		} finally {
			close(conn);
		}
		return agents;
	}

	@Override
	public boolean addAgent(Agent agent) {
		Connection conn = DBConnection.getConnection();
		if (conn == null) {
			return false;
		}
		try {
			PreparedStatement ps = conn.prepareStatement(
					"INSERT INTO agents (user_id, first_name, last_name, contact_number, email) VALUES (?, ?, ?, ?, ?)",
					Statement.RETURN_GENERATED_KEYS);
			ps.setInt(1, agent.getUserId());
			ps.setString(2, agent.getFirstName());
			ps.setString(3, agent.getLastName());
			ps.setString(4, agent.getContactNumber());
			ps.setString(5, agent.getEmail());
			ps.executeUpdate();
			ResultSet keys = ps.getGeneratedKeys();
			if (keys.next()) {
				agent.setAgentId(keys.getInt(1));
			}
			return true;
		} catch (SQLException e) {
			printError("Add agent", e);
			return false;
		} finally {
			close(conn);
		}
	}

	@Override
	public boolean updateAgent(Agent agent) {
		Connection conn = DBConnection.getConnection();
		if (conn == null) {
			return false;
		}
		try {
			PreparedStatement ps = conn.prepareStatement(
					"UPDATE agents SET first_name = ?, last_name = ?, contact_number = ?, email = ? WHERE agent_id = ?");
			ps.setString(1, agent.getFirstName());
			ps.setString(2, agent.getLastName());
			ps.setString(3, agent.getContactNumber());
			ps.setString(4, agent.getEmail());
			ps.setInt(5, agent.getAgentId());
			return ps.executeUpdate() > 0;
		} catch (SQLException e) {
			printError("Update agent", e);
			return false;
		} finally {
			close(conn);
		}
	}

	private Agent toAgent(ResultSet rs) throws SQLException {
		return new Agent(rs.getInt("agent_id"), rs.getInt("user_id"), rs.getString("first_name"),
				rs.getString("last_name"), rs.getString("contact_number"), rs.getString("email"));
	}
}
