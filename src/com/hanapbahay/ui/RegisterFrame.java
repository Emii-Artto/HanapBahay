package com.hanapbahay.ui;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

import com.hanapbahay.AppContext;

/**
 * Sign-up screen for new clients. Owner: UI member.
 * Opened from the "Create one" link on LoginFrame. Same look as the login screen.
 */
public class RegisterFrame extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField txtUsername;
	private JTextField txtFullName;
	private JPasswordField txtPassword;
	private JPasswordField txtConfirm;

	public RegisterFrame() {
		setTitle("HanapBahay - Create account");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 760, 520);
		setResizable(false);
		setLocationRelativeTo(null);
		contentPane = new JPanel();
		contentPane.setBackground(Color.WHITE);
		contentPane.setBorder(new EmptyBorder(0, 0, 0, 0));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		// ================= LEFT: brand panel =================
		JPanel pnlBrand = new JPanel();
		pnlBrand.setBackground(Theme.SIDEBAR);
		pnlBrand.setBounds(0, 0, 320, 481);
		contentPane.add(pnlBrand);
		pnlBrand.setLayout(null);

		JPanel pnlAccent = new JPanel();
		pnlAccent.setBackground(Theme.ACCENT);
		pnlAccent.setBounds(35, 165, 48, 5);
		pnlBrand.add(pnlAccent);

		JLabel lblBrand = new JLabel("HanapBahay");
		lblBrand.setForeground(Color.WHITE);
		lblBrand.setFont(Theme.BRAND);
		lblBrand.setBounds(35, 180, 260, 44);
		pnlBrand.add(lblBrand);

		JLabel lblTagline = new JLabel("<html>Create a client account to browse<br>listings and send inquiries.</html>");
		lblTagline.setForeground(Theme.ACCENT_LIGHT);
		lblTagline.setFont(Theme.BODY);
		lblTagline.setBounds(35, 228, 260, 42);
		pnlBrand.add(lblTagline);

		// ================= RIGHT: form =================
		JLabel lblHeader = new JLabel("Create account");
		lblHeader.setForeground(Theme.TEXT);
		lblHeader.setFont(Theme.HERO);
		lblHeader.setBounds(380, 35, 320, 40);
		contentPane.add(lblHeader);

		JLabel lblUsername = new JLabel("Username");
		lblUsername.setForeground(Theme.TEXT);
		lblUsername.setFont(Theme.BODY_BOLD);
		lblUsername.setBounds(380, 90, 320, 20);
		contentPane.add(lblUsername);

		txtUsername = new JTextField();
		txtUsername.setFont(Theme.BODY);
		txtUsername.setBorder(Theme.FIELD_BORDER);
		txtUsername.setBounds(380, 112, 320, 34);
		contentPane.add(txtUsername);

		JLabel lblFullName = new JLabel("Full name");
		lblFullName.setForeground(Theme.TEXT);
		lblFullName.setFont(Theme.BODY_BOLD);
		lblFullName.setBounds(380, 154, 320, 20);
		contentPane.add(lblFullName);

		txtFullName = new JTextField();
		txtFullName.setFont(Theme.BODY);
		txtFullName.setBorder(Theme.FIELD_BORDER);
		txtFullName.setBounds(380, 176, 320, 34);
		contentPane.add(txtFullName);

		JLabel lblPassword = new JLabel("Password (at least 6 characters)");
		lblPassword.setForeground(Theme.TEXT);
		lblPassword.setFont(Theme.BODY_BOLD);
		lblPassword.setBounds(380, 218, 320, 20);
		contentPane.add(lblPassword);

		txtPassword = new JPasswordField();
		txtPassword.setFont(Theme.BODY);
		txtPassword.setBorder(Theme.FIELD_BORDER);
		txtPassword.setBounds(380, 240, 320, 34);
		contentPane.add(txtPassword);

		JLabel lblConfirm = new JLabel("Confirm password");
		lblConfirm.setForeground(Theme.TEXT);
		lblConfirm.setFont(Theme.BODY_BOLD);
		lblConfirm.setBounds(380, 282, 320, 20);
		contentPane.add(lblConfirm);

		txtConfirm = new JPasswordField();
		txtConfirm.setFont(Theme.BODY);
		txtConfirm.setBorder(Theme.FIELD_BORDER);
		txtConfirm.setBounds(380, 304, 320, 34);
		contentPane.add(txtConfirm);

		JButton btnRegister = new JButton("Create account");
		btnRegister.setForeground(Color.WHITE);
		btnRegister.setBackground(Theme.ACCENT);
		btnRegister.setFont(Theme.BODY_BOLD);
		btnRegister.setBorder(new LineBorder(Theme.ACCENT));
		btnRegister.setFocusPainted(false);
		btnRegister.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				doRegister();
			}
		});
		btnRegister.setBounds(380, 360, 320, 40);
		contentPane.add(btnRegister);
		getRootPane().setDefaultButton(btnRegister);

		JButton btnBack = new JButton("Already have an account? Log in");
		btnBack.setForeground(Theme.ACCENT);
		btnBack.setFont(Theme.SMALL);
		btnBack.setBorderPainted(false);
		btnBack.setContentAreaFilled(false);
		btnBack.setFocusPainted(false);
		btnBack.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				backToLogin();
			}
		});
		btnBack.setBounds(380, 410, 320, 25);
		contentPane.add(btnBack);
	}

	private void doRegister() {
		String password = new String(txtPassword.getPassword());
		String confirm = new String(txtConfirm.getPassword());

		// The backend checks the rules (blank fields, length, matching passwords, duplicates)
		boolean ok = AppContext.userService().register(txtUsername.getText(), password, confirm,
				txtFullName.getText());

		if (ok) {
			JOptionPane.showMessageDialog(this, AppContext.userService().getMessage(), "Account created",
					JOptionPane.INFORMATION_MESSAGE);
			backToLogin();
		} else {
			JOptionPane.showMessageDialog(this, AppContext.userService().getMessage(), "Cannot create account",
					JOptionPane.WARNING_MESSAGE);
		}
	}

	private void backToLogin() {
		LoginFrame login = new LoginFrame();
		login.setVisible(true);
		dispose();
	}
}
