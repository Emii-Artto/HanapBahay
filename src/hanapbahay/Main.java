package hanapbahay;

import javax.swing.JOptionPane;

import dao.DBConnection;

import login.LoginFrame;

/**
 * Start the app from here: right-click this file > Run As > Java Application.
 * Before running, start MySQL in the XAMPP Control Panel.
 */
public class Main {

	public static void main(String[] args) {
		// 3.3.4 Reliability: check the database first and explain what to do if it is not running
		if (!DBConnection.isAvailable()) {
			JOptionPane.showMessageDialog(null,
					"Cannot connect to the HanapBahay database.\n\n"
							+ "1. Open the XAMPP Control Panel and click Start next to MySQL.\n"
							+ "2. Make sure the \"hanapbahay\" database exists (import db/schema.sql in phpMyAdmin).\n"
							+ "3. Run the app again.",
					"Database not available", JOptionPane.ERROR_MESSAGE);
			return;
		}

		LoginFrame login = new LoginFrame();
		login.setVisible(true);
	}
}
