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
import com.hanapbahay.model.User;

/**
 * Login screen. Owner: UI member.
 * Open with WindowBuilder: right-click > Open With > WindowBuilder Editor, then the Design tab.
 *
 * Test accounts (sample data): admin / admin123, agent1 / agent123, agent2 / agent123
 * Only Admins and Agents log in. Clients use Client Mode on the agent's device.
 */
public class LoginFrame extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField txtUsername;
	private JPasswordField txtPassword;

	public LoginFrame() {
		setTitle("HanapBahay - Login");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 760, 460);
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
		pnlBrand.setBounds(0, 0, 320, 421);
		contentPane.add(pnlBrand);
		pnlBrand.setLayout(null);

		JPanel pnlAccent = new JPanel();
		pnlAccent.setBackground(Theme.ACCENT);
		pnlAccent.setBounds(35, 135, 48, 5);
		pnlBrand.add(pnlAccent);

		JLabel lblTitle = new JLabel("HanapBahay");
		lblTitle.setForeground(Color.WHITE);
		lblTitle.setFont(Theme.BRAND);
		lblTitle.setBounds(35, 150, 260, 44);
		pnlBrand.add(lblTitle);

		JLabel lblTagline = new JLabel("<html>Real estate listing, inquiry,<br>and sales monitoring system</html>");
		lblTagline.setForeground(Theme.ACCENT_LIGHT);
		lblTagline.setFont(Theme.BODY);
		lblTagline.setBounds(35, 198, 260, 42);
		pnlBrand.add(lblTagline);

		JLabel lblFooter = new JLabel("Find, inquire, and close property deals in one place.");
		lblFooter.setForeground(new Color(110, 135, 128));
		lblFooter.setFont(Theme.SMALL);
		lblFooter.setBounds(35, 370, 280, 20);
		pnlBrand.add(lblFooter);

		// ================= RIGHT: sign-in form =================
		JLabel lblSignIn = new JLabel("Sign in");
		lblSignIn.setForeground(Theme.TEXT);
		lblSignIn.setFont(Theme.HERO);
		lblSignIn.setBounds(380, 70, 320, 40);
		contentPane.add(lblSignIn);

		JLabel lblWelcome = new JLabel("Welcome back. Log in to continue.");
		lblWelcome.setForeground(Theme.TEXT_MUTED);
		lblWelcome.setFont(Theme.BODY);
		lblWelcome.setBounds(380, 110, 320, 20);
		contentPane.add(lblWelcome);

		JLabel lblUsername = new JLabel("Username");
		lblUsername.setForeground(Theme.TEXT);
		lblUsername.setFont(Theme.BODY_BOLD);
		lblUsername.setBounds(380, 150, 320, 20);
		contentPane.add(lblUsername);

		txtUsername = new JTextField();
		txtUsername.setFont(Theme.BODY);
		txtUsername.setBorder(Theme.FIELD_BORDER);
		txtUsername.setBounds(380, 172, 320, 36);
		contentPane.add(txtUsername);

		JLabel lblPassword = new JLabel("Password");
		lblPassword.setForeground(Theme.TEXT);
		lblPassword.setFont(Theme.BODY_BOLD);
		lblPassword.setBounds(380, 220, 320, 20);
		contentPane.add(lblPassword);

		txtPassword = new JPasswordField();
		txtPassword.setFont(Theme.BODY);
		txtPassword.setBorder(Theme.FIELD_BORDER);
		txtPassword.setBounds(380, 242, 320, 36);
		contentPane.add(txtPassword);

		JButton btnLogin = new JButton("Log in");
		btnLogin.setForeground(Color.WHITE);
		btnLogin.setBackground(Theme.ACCENT);
		btnLogin.setFont(Theme.BODY_BOLD);
		btnLogin.setBorder(new LineBorder(Theme.ACCENT));
		btnLogin.setFocusPainted(false);
		btnLogin.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				doLogin();
			}
		});
		btnLogin.setBounds(380, 300, 320, 40);
		contentPane.add(btnLogin);
		getRootPane().setDefaultButton(btnLogin); // pressing Enter clicks Log in
	}

	private void doLogin() {
		String username = txtUsername.getText();
		String password = new String(txtPassword.getPassword());

		User user = AppContext.userService().login(username, password); // calls the backend

		if (user != null) {
			MainFrame main = new MainFrame();
			main.showFor(user);
			dispose(); // close the login window
		} else {
			JOptionPane.showMessageDialog(this, AppContext.userService().getMessage(), "Cannot log in",
					JOptionPane.WARNING_MESSAGE);
		}
	}
}
