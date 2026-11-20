package com.hanapbahay.dao.mysql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.hanapbahay.dao.DBConnection;
import com.hanapbahay.dao.UserDao;
import com.hanapbahay.model.Admin;
import com.hanapbahay.model.Agent;
import com.hanapbahay.model.User;

/**
 * Reads and saves users in the MySQL "users" table (XAMPP). Owner: Database member.
 *
 * IMPLEMENTS the UserDao interface (Module 5): it gives a body to every method in UserDao,
 * just like SampleUserDao, so UserService works with either one without any change.
 *
 * How each method works:
 *   1. Get a connection with DBConnection.getConnection().
 *   2. Prepare the SQL. Each "?" is filled in with setString()/setInt(). This also stops
 *      SQL injection, because typed text is never pasted directly into the SQL.
 *   3. Run it and read the result rows with ResultSet.
 *   4. Close the connection.
 * EXCEPTION HANDLING (required by the project matrix): every database call is inside try/catch,
 * so a database problem prints a message instead of crashing the program.
 */
public class MySqlUserDao implements UserDao {

	@Override
	public User findByUsername(String username) {
		User user = null;
		Connection conn = DBConnection.getConnection();
		if (conn == null) {
			return null; // database is not reachable
		}
		try {
			PreparedStatement ps = conn.prepareStatement("SELECT * FROM users WHERE username = ?");
			ps.setString(1, username);
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				user = toUser(rs);
			}
			conn.close();
		} catch (SQLException e) {
			System.out.println("findByUsername failed: " + e.getMessage());
		}
		return user;
	}

	@Override
	public User findById(int userId) {
		User user = null;
		Connection conn = DBConnection.getConnection();
		if (conn == null) {
			return null;
		}
		try {
			PreparedStatement ps = conn.prepareStatement("SELECT * FROM users WHERE user_id = ?");
			ps.setInt(1, userId);
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				user = toUser(rs);
			}
			conn.close();
		} catch (SQLException e) {
			System.out.println("findById failed: " + e.getMessage());
		}
		return user;
	}

	@Override
	public ArrayList<User> findAll() {
		ArrayList<User> users = new ArrayList<User>();
		Connection conn = DBConnection.getConnection();
		if (conn == null) {
			return users; // empty list
		}
		try {
			PreparedStatement ps = conn.prepareStatement("SELECT * FROM users ORDER BY user_id");
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				users.add(toUser(rs));
			}
			conn.close();
		} catch (SQLException e) {
			System.out.println("findAll failed: " + e.getMessage());
		}
		return users;
	}

	@Override
	public void add(User user) {
		Connection conn = DBConnection.getConnection();
		if (conn == null) {
			return;
		}
		try {
			PreparedStatement ps = conn.prepareStatement(
					"INSERT INTO users (username, password, full_name, role) VALUES (?, ?, ?, ?)");
			ps.setString(1, user.getUsername());
			ps.setString(2, user.getPassword());
			ps.setString(3, user.getFullName());
			ps.setString(4, user.getRole()); // POLYMORPHISM: "ADMIN" or "AGENT" from the subclass
			ps.executeUpdate();
			conn.close();
		} catch (SQLException e) {
			System.out.println("add user failed: " + e.getMessage());
		}
	}

	/**
	 * Turns one row of the "users" table into a Java object.
	 * The "role" column decides which subclass to create (Admin or Agent),
	 * but the method returns the parent type User (POLYMORPHISM, Module 5).
	 */
	private User toUser(ResultSet rs) throws SQLException {
		int id = rs.getInt("user_id");
		String username = rs.getString("username");
		String password = rs.getString("password");
		String fullName = rs.getString("full_name");
		String role = rs.getString("role");

		if (role.equals("ADMIN")) {
			return new Admin(id, username, password, fullName);
		} else {
			return new Agent(id, username, password, fullName);
		}
	}
}
