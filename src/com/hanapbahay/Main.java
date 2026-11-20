package com.hanapbahay;

import javax.swing.JOptionPane;

import com.hanapbahay.dao.DBConnection;
import com.hanapbahay.ui.LoginFrame;

/**
 * Start the app from here: right-click this file > Run As > Java Application.
 * Before running, start MySQL in the XAMPP Control Panel.
 */
public class Main {

	public static void main(String[] args) {
		// Check the database first, so the user gets a clear message instead of failed logins
		if (!DBConnection.isAvailable()) {
			JOptionPane.showMessageDialog(null,
					"Cannot connect to the HanapBahay database.\n\n"
							+ "1. Open the XAMPP Control Panel and click Start next to MySQL.\n"
							+ "2. Make sure the \"hanapbahay\" database exists (import db/schema.sql in phpMyAdmin).\n"
							+ "3. Run the app again.",
					"Database not available", JOptionPane.ERROR_MESSAGE);
			return; // stop here; the app can't work without the database
		}

		LoginFrame login = new LoginFrame(); // create an object of the LoginFrame class
		login.setVisible(true);              // show the window
	}
}
