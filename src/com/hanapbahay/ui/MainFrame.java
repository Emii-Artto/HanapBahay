package com.hanapbahay.ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.MatteBorder;

import com.hanapbahay.model.User;

import login.LogIn;

public class MainFrame extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JPanel pnlSidebar;
	private JPanel pnlHeader;
	private JButton btnLogout;
	private JLabel lblUserName;
	private JLabel lblUserRole;
	private JLabel lblRoleBadge;
	private JLabel lblWelcome;
	private JLabel lblUsernameValue;
	private JLabel lblRoleValue;
	private JLabel lblSalesValue;
	private JLabel lblPermissionsValue;

	public MainFrame() {
		setTitle("HanapBahay - Real Estate Listing, Inquiry, and Sales Monitoring");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 1180, 700);
		setMinimumSize(new Dimension(1000, 650)); // can be maximized, but not made too small
		addComponentListener(new ComponentAdapter() {
			@Override
			public void componentResized(ComponentEvent e) {
				fitToWindow(); // runs every time the window is resized or maximized
			}
		});
		contentPane = new JPanel();
		contentPane.setBackground(Theme.PAGE_BG);
		setContentPane(contentPane);
		contentPane.setLayout(null);

		// ================= SIDEBAR =================
		pnlSidebar = new JPanel();
		pnlSidebar.setBackground(Theme.SIDEBAR);
		pnlSidebar.setBounds(0, 0, 230, 661);
		contentPane.add(pnlSidebar);
		pnlSidebar.setLayout(null);

		JLabel lblBrand = new JLabel("HanapBahay");
		lblBrand.setForeground(Color.WHITE);
		lblBrand.setFont(Theme.TITLE);
		lblBrand.setBounds(22, 25, 200, 32);
		pnlSidebar.add(lblBrand);

		JLabel lblBrandSub = new JLabel("REAL ESTATE MONITORING");
		lblBrandSub.setForeground(Theme.ACCENT_LIGHT);
		lblBrandSub.setFont(Theme.SMALL_BOLD);
		lblBrandSub.setBounds(22, 57, 200, 18);
		pnlSidebar.add(lblBrandSub);

		JPanel pnlLine = new JPanel();
		pnlLine.setBackground(new Color(45, 70, 65));
		pnlLine.setBounds(22, 95, 186, 1);
		pnlSidebar.add(pnlLine);

		lblUserName = new JLabel("User name");
		lblUserName.setForeground(Color.WHITE);
		lblUserName.setFont(Theme.BODY_BOLD);
		lblUserName.setBounds(22, 112, 200, 22);
		pnlSidebar.add(lblUserName);

		lblUserRole = new JLabel("ROLE");
		lblUserRole.setForeground(Theme.ACCENT_LIGHT);
		lblUserRole.setFont(Theme.SMALL_BOLD);
		lblUserRole.setBounds(22, 134, 200, 18);
		pnlSidebar.add(lblUserRole);

		JLabel lblMenu = new JLabel("MAIN MENU");
		lblMenu.setForeground(new Color(110, 135, 128));
		lblMenu.setFont(Theme.SMALL_BOLD);
		lblMenu.setBounds(22, 180, 200, 18);
		pnlSidebar.add(lblMenu);

		JButton btnNavDashboard = new JButton("Dashboard");
		btnNavDashboard.setHorizontalAlignment(SwingConstants.LEFT);
		btnNavDashboard.setFont(Theme.BODY_BOLD);
		btnNavDashboard.setForeground(Color.WHITE);
		btnNavDashboard.setBackground(Theme.ACCENT);
		btnNavDashboard.setBorder(new EmptyBorder(0, 18, 0, 0));
		btnNavDashboard.setFocusPainted(false);
		btnNavDashboard.setBounds(10, 205, 210, 40);
		pnlSidebar.add(btnNavDashboard);

		btnLogout = new JButton("Log out");
		btnLogout.setFont(Theme.BODY_BOLD);
		btnLogout.setForeground(Color.WHITE);
		btnLogout.setBackground(new Color(45, 70, 65));
		btnLogout.setBorder(new EmptyBorder(0, 0, 0, 0));
		btnLogout.setFocusPainted(false);
		btnLogout.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				doLogout();
			}
		});
		btnLogout.setBounds(10, 605, 210, 40);
		pnlSidebar.add(btnLogout);

		// HEADER 
		pnlHeader = new JPanel();
		pnlHeader.setBackground(Color.WHITE);
		pnlHeader.setBorder(new MatteBorder(0, 0, 1, 0, Theme.BORDER)); // line at the bottom only
		pnlHeader.setBounds(230, 0, 934, 80);
		contentPane.add(pnlHeader);
		pnlHeader.setLayout(null);

		JLabel lblPageTitle = new JLabel("Dashboard");
		lblPageTitle.setForeground(Theme.TEXT);
		lblPageTitle.setFont(Theme.TITLE);
		lblPageTitle.setBounds(25, 12, 600, 32);
		pnlHeader.add(lblPageTitle);

		JLabel lblPageSubtitle = new JLabel("Your account and what your role can do");
		lblPageSubtitle.setForeground(Theme.TEXT_MUTED);
		lblPageSubtitle.setFont(Theme.SMALL);
		lblPageSubtitle.setBounds(25, 45, 600, 20);
		pnlHeader.add(lblPageSubtitle);

		lblRoleBadge = new JLabel("ROLE");
		lblRoleBadge.setOpaque(true);
		lblRoleBadge.setHorizontalAlignment(SwingConstants.CENTER);
		lblRoleBadge.setForeground(Color.WHITE);
		lblRoleBadge.setBackground(Theme.ACCENT);
		lblRoleBadge.setFont(Theme.SMALL_BOLD);
		lblRoleBadge.setBounds(809, 23, 100, 34);
		pnlHeader.add(lblRoleBadge);

		// ================= PAGE AREA: account card =================
		JPanel pnlAccount = new JPanel();
		pnlAccount.setBackground(Color.WHITE);
		pnlAccount.setBorder(new LineBorder(Theme.BORDER));
		pnlAccount.setBounds(255, 100, 520, 250);
		contentPane.add(pnlAccount);
		pnlAccount.setLayout(null);

		JPanel pnlBar = new JPanel();
		pnlBar.setBackground(Theme.ACCENT);
		pnlBar.setBounds(0, 0, 4, 250);
		pnlAccount.add(pnlBar);

		lblWelcome = new JLabel("Welcome!");
		lblWelcome.setForeground(Theme.TEXT);
		lblWelcome.setFont(Theme.HEADING);
		lblWelcome.setBounds(25, 20, 480, 28);
		pnlAccount.add(lblWelcome);

		JLabel lblUsername = new JLabel("Username");
		lblUsername.setForeground(Theme.TEXT_MUTED);
		lblUsername.setFont(Theme.BODY);
		lblUsername.setBounds(25, 70, 200, 24);
		pnlAccount.add(lblUsername);

		lblUsernameValue = new JLabel("-");
		lblUsernameValue.setForeground(Theme.TEXT);
		lblUsernameValue.setFont(Theme.BODY_BOLD);
		lblUsernameValue.setBounds(240, 70, 260, 24);
		pnlAccount.add(lblUsernameValue);

		JLabel lblRole = new JLabel("Role");
		lblRole.setForeground(Theme.TEXT_MUTED);
		lblRole.setFont(Theme.BODY);
		lblRole.setBounds(25, 110, 200, 24);
		pnlAccount.add(lblRole);

		lblRoleValue = new JLabel("-");
		lblRoleValue.setForeground(Theme.TEXT);
		lblRoleValue.setFont(Theme.BODY_BOLD);
		lblRoleValue.setBounds(240, 110, 260, 24);
		pnlAccount.add(lblRoleValue);

		JLabel lblSales = new JLabel("Can view all sales");
		lblSales.setForeground(Theme.TEXT_MUTED);
		lblSales.setFont(Theme.BODY);
		lblSales.setBounds(25, 150, 200, 24);
		pnlAccount.add(lblSales);

		lblSalesValue = new JLabel("-");
		lblSalesValue.setForeground(Theme.TEXT);
		lblSalesValue.setFont(Theme.BODY_BOLD);
		lblSalesValue.setBounds(240, 150, 260, 24);
		pnlAccount.add(lblSalesValue);

		JLabel lblPermissions = new JLabel("Permissions");
		lblPermissions.setForeground(Theme.TEXT_MUTED);
		lblPermissions.setFont(Theme.BODY);
		lblPermissions.setBounds(25, 190, 200, 24);
		pnlAccount.add(lblPermissions);

		lblPermissionsValue = new JLabel("-");
		lblPermissionsValue.setVerticalAlignment(SwingConstants.TOP);
		lblPermissionsValue.setForeground(Theme.TEXT);
		lblPermissionsValue.setFont(Theme.BODY_BOLD);
		lblPermissionsValue.setBounds(240, 193, 260, 40);
		pnlAccount.add(lblPermissionsValue);
	}

	public void showFor(User user) {
		lblUserName.setText(user.getFullName());
		lblUserRole.setText(user.getRole());
		lblRoleBadge.setText(user.getRole());

		lblWelcome.setText(user.getWelcomeMessage());
		lblUsernameValue.setText(user.getUsername());
		lblRoleValue.setText(user.getRole());
		lblSalesValue.setText(yesOrNo(user.canViewAllSales()));
		lblPermissionsValue.setText("<html>" + user.getPermissions() + "</html>"); // <html> lets long text wrap

		setLocationRelativeTo(null);
		setVisible(true);
	}

//stretch
	private void fitToWindow() {
		int width = contentPane.getWidth();
		int height = contentPane.getHeight();

		pnlSidebar.setBounds(0, 0, 230, height);           // full height
		btnLogout.setBounds(10, height - 56, 210, 40);      // stays at the bottom
		pnlHeader.setBounds(230, 0, width - 230, 80);       // full width
		lblRoleBadge.setBounds(width - 355, 23, 100, 34);   // stays at the right edge
	}

	private String yesOrNo(boolean value) {
		if (value) {
			return "Yes";
		} else {
			return "No";
		}
	}

	private void doLogout() {
		int answer = JOptionPane.showConfirmDialog(this, "Do you want to log out?", "Log out",
				JOptionPane.YES_NO_OPTION);
		if (answer == JOptionPane.YES_OPTION) {
			LogIn login = new LogIn(); // back to the new login screen
			login.setVisible(true);
			dispose();
		}
	}
}
