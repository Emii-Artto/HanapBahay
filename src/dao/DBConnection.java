package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
	public static final String URL = "jdbc:mysql://localhost:3306/hanapbahay";
	public static final String USER = "root";
	public static final String PASSWORD = "";

	public static Connection getConnection() {
		try {
			return DriverManager.getConnection(URL, USER, PASSWORD);
		} catch (SQLException e) {
			System.out.println("Cannot connect to the database: " + e.getMessage());
			return null;
		}
	}

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
