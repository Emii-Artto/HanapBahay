package com.hanapbahay;

import java.awt.EventQueue;

import com.hanapbahay.ui.LoginFrame;

/**
 * Start the app from here: right-click this file > Run As > Java Application.
 */
public class Main {

	public static void main(String[] args) {
		EventQueue.invokeLater(() -> new LoginFrame().setVisible(true));
	}
}
