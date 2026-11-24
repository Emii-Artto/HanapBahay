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
			ps.setString(4, user.getRole());
			ps.executeUpdate();
			conn.close();
		} catch (SQLException e) {
			System.out.println("add user failed: " + e.getMessage());
		}
	}

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
