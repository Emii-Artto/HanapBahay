package com.hanapbahay.dao.mysql;

import java.sql.Connection;
import java.sql.SQLException;


/**
 * Parent class of every MySQL DAO class. Holds the code they all share (INHERITANCE),
 * so each DAO does not repeat it.
 */
public abstract class MySqlBaseDao {

	// Closes a connection; a failure here is only printed because the work is already done
	protected void close(Connection conn) {
		if (conn == null) {
			return;
		}
		try {
			conn.close();
		} catch (SQLException e) {
			System.out.println("Cannot close the connection: " + e.getMessage());
		}
	}

	// Prints the database error so it shows in the Eclipse console (5.3.8)
	protected void printError(String action, SQLException e) {
		System.out.println(action + " failed: " + e.getMessage());
	}
}
