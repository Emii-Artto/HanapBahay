package com.hanapbahay.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import com.hanapbahay.model.Agent;
import com.hanapbahay.model.User;

/**
 * UserDAO - handles login accounts (users table) and agent profiles (agents table).
 * Every query uses a PreparedStatement with ? so typed text can never change the SQL.
 */
public class UserDAO extends BaseDAO {

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
				return new User(rs.getInt("user_id"), rs.getString("username"), rs.getString("password_hash"),
						rs.getString("role"));
			}
		} catch (SQLException e) {
			printError("Find user", e);
		} finally {
			close(conn);
		}
		return null;
	}

	/** Saves a new login account. Returns the new user_id, or 0 if it failed. */
	public int addAccount(String username, String passwordHash, String role) {
		Connection conn = DBConnection.getConnection();
		if (conn == null) {
			return 0;
		}
		try {
			PreparedStatement ps = conn.prepareStatement(
					"INSERT INTO users (username, password_hash, role) VALUES (?, ?, ?)",
					Statement.RETURN_GENERATED_KEYS);
			ps.setString(1, username);
			ps.setString(2, passwordHash);
			ps.setString(3, role);
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
