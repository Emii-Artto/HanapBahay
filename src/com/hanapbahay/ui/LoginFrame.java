package com.hanapbahay.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import com.hanapbahay.AppContext;
import com.hanapbahay.model.Agent;
import com.hanapbahay.model.User;
import com.hanapbahay.service.UserService;

/**
 * 3.2.1 Authentication: the login screen (design by the UI member, connected to the backend).
 * The username and password are checked by UserService; invalid input shows an error message
 * and the user stays on this screen (5.3.3).
 */
public class LoginFrame extends JFrame {

	private static final long serialVersionUID = 1L;
	private static final String ADMIN = "Admin";
	private static final String AGENT = "Agent";

	private JTextField txtUsername;
	private JPasswordField txtPassword;
	private JComboBox<String> cboAccountType;

	public LoginFrame() {
		setTitle("HanapBahay - Login");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(1100, 700);
		setMinimumSize(new Dimension(900, 620));
		setLocationRelativeTo(null);

		JPanel mainPanel = new JPanel(new GridLayout(1, 2));
		mainPanel.setBackground(Theme.DARK);
		mainPanel.add(createBrandPanel());
		mainPanel.add(createLoginSide());
		setContentPane(mainPanel);
	}

	// ======================= left: brand panel =======================
	private JPanel createBrandPanel() {
		JPanel left = new BrandPanel();
		left.setLayout(new BorderLayout());
		left.setBorder(new EmptyBorder(35, 42, 40, 35));

		JLabel brand = new JLabel("⌂  HANAPBAHAY");
		brand.setFont(new Font("SansSerif", Font.BOLD, 20));
		brand.setForeground(Theme.GOLD);
		left.add(brand, BorderLayout.NORTH);

		JPanel caption = new JPanel();
		caption.setOpaque(false);
		caption.setLayout(new BoxLayout(caption, BoxLayout.Y_AXIS));
		JLabel small = new JLabel("FIND YOUR");
		small.setFont(new Font("SansSerif", Font.BOLD, 16));
		small.setForeground(Theme.GOLD);
		JLabel big = new JLabel("Perfect Property.");
		big.setFont(new Font("Serif", Font.BOLD, 38));
		big.setForeground(Theme.WHITE);
		JLabel description = new JLabel("<html>Manage property listings, buyers, viewings<br>"
				+ "and deals in one convenient platform.</html>");
		description.setFont(new Font("SansSerif", Font.PLAIN, 14));
		description.setForeground(Theme.WHITE);
		caption.add(small);
		caption.add(Box.createVerticalStrut(10));
		caption.add(big);
		caption.add(Box.createVerticalStrut(14));
		caption.add(description);
		left.add(caption, BorderLayout.SOUTH);
		return left;
	}

	/** Dark gradient with simple gold houses (used when there is no property photo). */
	private static class BrandPanel extends JPanel {
		private static final long serialVersionUID = 1L;

		@Override
		protected void paintComponent(Graphics g) {
			super.paintComponent(g);
			Graphics2D g2 = (Graphics2D) g.create();
			g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			int w = getWidth();
			int h = getHeight();
			g2.setPaint(new GradientPaint(0, 0, new Color(40, 36, 30), w, h, new Color(18, 20, 26)));
			g2.fillRect(0, 0, w, h);
			g2.setColor(new Color(205, 169, 96, 60));
			g2.fillOval(w / 2 - 40, h / 3 - 120, 360, 360);
			drawHouse(g2, w / 2 - 20, h / 2 - 20, 150);
			drawHouse(g2, w / 2 + 110, h / 2 + 20, 110);
			g2.dispose();
		}

		private void drawHouse(Graphics2D g2, int x, int y, int size) {
			g2.setStroke(new java.awt.BasicStroke(4));
			g2.setColor(Theme.GOLD);
			g2.drawLine(x - size / 2 - 10, y, x, y - size / 2);
			g2.drawLine(x, y - size / 2, x + size / 2 + 10, y);
			g2.setColor(new Color(15, 16, 20));
			g2.fillRect(x - size / 2, y, size, (int) (size * 0.6));
			g2.setColor(Theme.GOLD);
			int window = size / 5;
			g2.fillRect(x - size / 3, y + size / 8, window, (int) (window * 1.4));
			g2.fillRect(x + size / 3 - window, y + size / 8, window, (int) (window * 1.4));
		}
	}

	// ======================= right: login card =======================
	private JPanel createLoginSide() {
		JPanel right = new JPanel(new GridBagLayout());
		right.setBackground(Theme.DARK);

		JPanel card = new JPanel();
		card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
		card.setBackground(Theme.PANEL);
		card.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(new Color(65, 62, 54)),
				new EmptyBorder(35, 40, 28, 40)));

		JLabel title = new JLabel("Welcome Back!");
		title.setFont(new Font("Serif", Font.BOLD, 31));
		title.setForeground(Theme.WHITE);
		title.setAlignmentX(Component.LEFT_ALIGNMENT);
		JLabel subtitle = Theme.mutedLabel("Log in to access your account.");
		subtitle.setAlignmentX(Component.LEFT_ALIGNMENT);
		card.add(title);
		card.add(Box.createVerticalStrut(6));
		card.add(subtitle);
		card.add(Box.createVerticalStrut(28));

		card.add(row(Theme.caption("Account type")));
		cboAccountType = new JComboBox<String>(new String[] { AGENT, ADMIN });
		Theme.styleCombo(cboAccountType);
		card.add(row(cboAccountType));
		card.add(Box.createVerticalStrut(14));

		card.add(row(Theme.caption("Username")));
		txtUsername = Theme.field();
		card.add(row(txtUsername));
		card.add(Box.createVerticalStrut(14));

		card.add(row(Theme.caption("Password")));
		txtPassword = new JPasswordField();
		Theme.styleField(txtPassword);
		card.add(row(txtPassword));

		final JCheckBox chkShow = new JCheckBox("Show password");
		chkShow.setFont(Theme.SMALL);
		chkShow.setForeground(Theme.MUTED);
		chkShow.setBackground(Theme.PANEL);
		chkShow.setFocusPainted(false);
		chkShow.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				txtPassword.setEchoChar(chkShow.isSelected() ? (char) 0 : '•');
			}
		});
		card.add(row(chkShow));
		card.add(Box.createVerticalStrut(18));

		JButton btnLogin = Theme.goldButton("SIGN IN");
		btnLogin.setFont(new Font("SansSerif", Font.BOLD, 14));
		btnLogin.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				doLogin();
			}
		});
		card.add(row(btnLogin));
		getRootPane().setDefaultButton(btnLogin); // pressing Enter clicks SIGN IN
		card.add(Box.createVerticalStrut(16));

		JPanel createRow = new JPanel(new FlowLayout(FlowLayout.CENTER, 6, 0));
		createRow.setOpaque(false);
		createRow.add(Theme.mutedLabel("New agent?"));
		JButton btnCreate = new JButton("CREATE ACCOUNT");
		btnCreate.setFont(Theme.SMALL_BOLD);
		btnCreate.setForeground(Theme.GOLD);
		btnCreate.setBackground(Theme.PANEL);
		btnCreate.setBorder(null);
		btnCreate.setFocusPainted(false);
		btnCreate.setCursor(new Cursor(Cursor.HAND_CURSOR));
		btnCreate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				showCreateAccount();
			}
		});
		createRow.add(btnCreate);
		card.add(row(createRow));
		card.add(Box.createVerticalStrut(14));
		JLabel authorized = new JLabel("AUTHORIZED USERS ONLY");
		authorized.setFont(new Font("SansSerif", Font.BOLD, 10));
		authorized.setForeground(Theme.MUTED);
		JPanel authorizedRow = new JPanel(new FlowLayout(FlowLayout.CENTER));
		authorizedRow.setOpaque(false);
		authorizedRow.add(authorized);
		card.add(row(authorizedRow));

		right.add(card);
		return right;
	}

	// every row in the card has the same width (350) so the fields line up
	private JPanel row(Component component) {
		JPanel row = new JPanel(new BorderLayout());
		row.setOpaque(false);
		row.setAlignmentX(Component.LEFT_ALIGNMENT);
		int height = Math.max(component.getPreferredSize().height, 22);
		if (component instanceof JTextField || component instanceof JComboBox || component instanceof JButton) {
			height = 42;
		}
		row.setPreferredSize(new Dimension(350, height));
		row.setMaximumSize(new Dimension(350, height));
		row.add(component, BorderLayout.CENTER);
		return row;
	}

	// ======================= login =======================
	private void doLogin() {
		UserService service = AppContext.userService();
		User user = service.login(txtUsername.getText(), new String(txtPassword.getPassword()));
		if (user == null) {
			JOptionPane.showMessageDialog(this, service.getMessage(), "Cannot log in", JOptionPane.WARNING_MESSAGE);
			return;
		}
		String chosenType = (String) cboAccountType.getSelectedItem();
		boolean wantsAdmin = ADMIN.equals(chosenType);
		if (user.isAdmin() != wantsAdmin) {
			JOptionPane.showMessageDialog(this,
					"This is " + (user.isAdmin() ? "an Admin" : "an Agent") + " account.\nPlease choose \""
							+ (user.isAdmin() ? ADMIN : AGENT) + "\" as the account type.",
					"Wrong account type", JOptionPane.WARNING_MESSAGE);
			return;
		}

		if (user.isAdmin()) {
			new AdminDashboard(user).setVisible(true);
		} else {
			Agent agent = service.getAgentProfile(user);
			if (agent == null) {
				JOptionPane.showMessageDialog(this, service.getMessage(), "Cannot log in",
						JOptionPane.WARNING_MESSAGE);
				return;
			}
			new MainDashboard(user, agent).setVisible(true);
		}
		dispose(); // close the login window
	}

	/** Create Account: makes a new AGENT account with its profile. */
	private void showCreateAccount() {
		JTextField txtFirst = Theme.field();
		JTextField txtLast = Theme.field();
		JTextField txtContact = Theme.field();
		JTextField txtEmail = Theme.field();
		JTextField txtNewUsername = Theme.field();
		JPasswordField txtNewPassword = new JPasswordField();
		JPasswordField txtConfirm = new JPasswordField();
		Theme.styleField(txtNewPassword);
		Theme.styleField(txtConfirm);
		JPanel form = Theme.form(new Object[] { "First name *", txtFirst, "Last name *", txtLast, "Contact number *",
				txtContact, "Email", txtEmail, "Username *", txtNewUsername, "Password * (6+ characters)",
				txtNewPassword, "Confirm password *", txtConfirm });

		UserService service = AppContext.userService();
		while (JOptionPane.showConfirmDialog(this, form, "Create Agent Account", JOptionPane.OK_CANCEL_OPTION,
				JOptionPane.PLAIN_MESSAGE) == JOptionPane.OK_OPTION) {
			if (service.registerAgent(txtFirst.getText(), txtLast.getText(), txtContact.getText(), txtEmail.getText(),
					txtNewUsername.getText(), new String(txtNewPassword.getPassword()),
					new String(txtConfirm.getPassword()))) {
				JOptionPane.showMessageDialog(this, service.getMessage(), "Account created",
						JOptionPane.INFORMATION_MESSAGE);
				cboAccountType.setSelectedItem(AGENT);
				txtUsername.setText(txtNewUsername.getText().trim());
				txtPassword.setText("");
				return;
			}
			JOptionPane.showMessageDialog(this, service.getMessage(), "Cannot create account",
					JOptionPane.WARNING_MESSAGE);
		}
	}
}
