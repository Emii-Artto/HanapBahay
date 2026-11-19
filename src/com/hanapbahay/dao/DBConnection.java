package com.hanapbahay.dao;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * Opens MySQL connections using config/db.properties (each person's own local settings).
 * Owner: Database member.
 *
 * Usage inside a MySQL DAO:
 *   try (Connection conn = DBConnection.getConnection();
 *        PreparedStatement ps = conn.prepareStatement("SELECT ...")) { ... }
 */
public final class DBConnection {

	private static final String CONFIG_FILE = "config/db.properties";

	private DBConnection() {
	}

	public static Connection getConnection() throws SQLException {
		Properties props = loadConfig();
		return DriverManager.getConnection(
				props.getProperty("db.url"),
				props.getProperty("db.user"),
				props.getProperty("db.password", ""));
	}

	private static Properties loadConfig() throws SQLException {
		Properties props = new Properties();
		try (InputStream in = new FileInputStream(CONFIG_FILE)) {
			props.load(in);
		} catch (IOException e) {
			throw new SQLException("Missing " + CONFIG_FILE
					+ ". Copy config/db.properties.example to config/db.properties and fill it in.", e);
		}
		return props;
	}
}
