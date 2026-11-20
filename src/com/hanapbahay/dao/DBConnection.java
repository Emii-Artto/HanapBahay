package com.hanapbahay.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Opens a connection to the MySQL database. Owner: Database member.
 *
 * Change USER and PASSWORD to match the MySQL on your computer
 * (XAMPP default: user "root", empty password).
 */
public class DBConnection {

	// STATIC FINAL constants (Modules 3 and 4) - same for the whole program, cannot be changed
	public static final String URL = "jdbc:mysql://localhost:3306/hanapbahay";
	public static final String USER = "root";
	public static final String PASSWORD = "";

	// STATIC METHOD (Module 3) - call it with DBConnection.getConnection(), no object needed.
	// Returns null if the database cannot be reached.
	public static Connection getConnection() {
		try {
			return DriverManager.getConnection(URL, USER, PASSWORD);
		} catch (SQLException e) {
			System.out.println("Cannot connect to the database: " + e.getMessage());
			return null;
		}
	}

	// Returns true if MySQL is running and the "hanapbahay" database exists.
	// Main calls this at startup so the user gets a clear message instead of failed logins.
	public static boolean isAvailable() {
		Connection conn = getConnection();
		if (conn == null) {
			return false;
		}
		try {
			conn.close();
		} catch (SQLException e) {
			System.out.println("Cannot close the connection: " + e.getMessage());
		}
		return true;
	}
}
