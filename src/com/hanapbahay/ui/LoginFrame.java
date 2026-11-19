package com.hanapbahay.ui;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Optional;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import com.hanapbahay.AppContext;
import com.hanapbahay.model.User;

/**
 * Login screen. Owner: UI member (open with WindowBuilder: right-click > Open With > WindowBuilder Editor).
 * Test accounts while using stub data: admin / admin123, agent1 / agent123, client1 / client123
 */
public class LoginFrame extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField txtUsername;
	private JPasswordField txtPassword;

	public LoginFrame() {
		setTitle("HanapBahay - Login");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 400, 300);
		setLocationRelativeTo(null);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		JLabel lblTitle = new JLabel("HanapBahay");
		lblTitle.setHorizontalAlignment(SwingConstants.CENTER);
		lblTitle.setFont(new Font("Tahoma", Font.BOLD, 20));
		lblTitle.setBounds(10, 20, 366, 30);
		contentPane.add(lblTitle);

		JLabel lblUsername = new JLabel("Username");
		lblUsername.setBounds(60, 80, 80, 20);
		contentPane.add(lblUsername);

		txtUsername = new JTextField();
		txtUsername.setBounds(140, 80, 180, 24);
		contentPane.add(txtUsername);

		JLabel lblPassword = new JLabel("Password");
		lblPassword.setBounds(60, 120, 80, 20);
		contentPane.add(lblPassword);

		txtPassword = new JPasswordField();
		txtPassword.setBounds(140, 120, 180, 24);
		contentPane.add(txtPassword);

		JButton btnLogin = new JButton("Login");
		btnLogin.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				doLogin();
			}
		});
		btnLogin.setBounds(140, 170, 100, 30);
		contentPane.add(btnLogin);
		getRootPane().setDefaultButton(btnLogin);
	}

	private void doLogin() {
		try {
			Optional<User> user = AppContext.userService()
					.login(txtUsername.getText(), new String(txtPassword.getPassword()));
			if (user.isPresent()) {
				MainFrame main = new MainFrame();
				main.showFor(user.get());
				dispose();
			} else {
				JOptionPane.showMessageDialog(this, "Wrong username or password.",
						"Login failed", JOptionPane.ERROR_MESSAGE);
			}
		} catch (IllegalArgumentException ex) {
			JOptionPane.showMessageDialog(this, ex.getMessage(), "Login", JOptionPane.WARNING_MESSAGE);
		}
	}
}
