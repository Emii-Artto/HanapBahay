package com.hanapbahay;

import javax.swing.JOptionPane;

import com.hanapbahay.dao.DBConnection;
import com.hanapbahay.ui.LoginFrame;

public class Main {

	public static void main(String[] args) {
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
