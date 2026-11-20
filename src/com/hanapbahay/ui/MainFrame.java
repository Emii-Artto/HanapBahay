package com.hanapbahay.ui;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;

import com.hanapbahay.model.User;

/**
 * The main window after login. Owner: UI member.
 *
 * Left: dark sidebar menu. Top: page title and role badge. Middle (pnlContent): the current page.
 * Clicking a menu button puts a different page (DashboardPanel, ListingsPanel, InquiriesPanel,
 * SalesPanel) inside pnlContent.
 */
public class MainFrame extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JLabel lblUserName;
	private JLabel lblUserRole;
	private JButton btnNavDashboard;
	private JButton btnNavListings;
	private JButton btnNavInquiries;
	private JButton btnNavSales;
	private JLabel lblPageTitle;
	private JLabel lblPageSubtitle;
	private JLabel lblRoleBadge;
	private JPanel pnlContent;

	private User currentUser;

	public MainFrame() {
		setTitle("HanapBahay - Real Estate Listing, Inquiry, and Sales Monitoring");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 1180, 700);
		setResizable(false);
		contentPane = new JPanel();
		contentPane.setBackground(Theme.PAGE_BG);
		setContentPane(contentPane);
		contentPane.setLayout(null);

		// ================= SIDEBAR =================
		JPanel pnlSidebar = new JPanel();
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

		btnNavDashboard = new JButton("Dashboard");
		btnNavDashboard.setHorizontalAlignment(SwingConstants.LEFT);
		btnNavDashboard.setFont(Theme.BODY);
		btnNavDashboard.setForeground(Theme.SIDEBAR_TEXT);
		btnNavDashboard.setBackground(Theme.SIDEBAR);
		btnNavDashboard.setBorder(new EmptyBorder(0, 18, 0, 0));
		btnNavDashboard.setFocusPainted(false);
		btnNavDashboard.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				openDashboard();
			}
		});
		btnNavDashboard.setBounds(10, 205, 210, 40);
		pnlSidebar.add(btnNavDashboard);

		btnNavListings = new JButton("Property listings");
		btnNavListings.setHorizontalAlignment(SwingConstants.LEFT);
		btnNavListings.setFont(Theme.BODY);
		btnNavListings.setForeground(Theme.SIDEBAR_TEXT);
		btnNavListings.setBackground(Theme.SIDEBAR);
		btnNavListings.setBorder(new EmptyBorder(0, 18, 0, 0));
		btnNavListings.setFocusPainted(false);
		btnNavListings.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				openListings();
			}
		});
		btnNavListings.setBounds(10, 250, 210, 40);
		pnlSidebar.add(btnNavListings);

		btnNavInquiries = new JButton("Inquiries");
		btnNavInquiries.setHorizontalAlignment(SwingConstants.LEFT);
		btnNavInquiries.setFont(Theme.BODY);
		btnNavInquiries.setForeground(Theme.SIDEBAR_TEXT);
		btnNavInquiries.setBackground(Theme.SIDEBAR);
		btnNavInquiries.setBorder(new EmptyBorder(0, 18, 0, 0));
		btnNavInquiries.setFocusPainted(false);
		btnNavInquiries.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				openInquiries();
			}
		});
		btnNavInquiries.setBounds(10, 295, 210, 40);
		pnlSidebar.add(btnNavInquiries);

		btnNavSales = new JButton("Sales and reports");
		btnNavSales.setHorizontalAlignment(SwingConstants.LEFT);
		btnNavSales.setFont(Theme.BODY);
		btnNavSales.setForeground(Theme.SIDEBAR_TEXT);
		btnNavSales.setBackground(Theme.SIDEBAR);
		btnNavSales.setBorder(new EmptyBorder(0, 18, 0, 0));
		btnNavSales.setFocusPainted(false);
		btnNavSales.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				openSales();
			}
		});
		btnNavSales.setBounds(10, 340, 210, 40);
		pnlSidebar.add(btnNavSales);

		JButton btnLogout = new JButton("Log out");
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

		// ================= HEADER =================
		JPanel pnlHeader = new JPanel();
		pnlHeader.setBackground(Color.WHITE);
		pnlHeader.setBorder(new MatteBorder(0, 0, 1, 0, Theme.BORDER)); // line at the bottom only
		pnlHeader.setBounds(230, 0, 934, 80);
		contentPane.add(pnlHeader);
		pnlHeader.setLayout(null);

		lblPageTitle = new JLabel("Dashboard");
		lblPageTitle.setForeground(Theme.TEXT);
		lblPageTitle.setFont(Theme.TITLE);
		lblPageTitle.setBounds(25, 12, 600, 32);
		pnlHeader.add(lblPageTitle);

		lblPageSubtitle = new JLabel("Subtitle");
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

		// ================= PAGE AREA =================
		pnlContent = new JPanel();
		pnlContent.setBackground(Theme.PAGE_BG);
		pnlContent.setBounds(230, 80, 934, 581);
		contentPane.add(pnlContent);
		pnlContent.setLayout(null);
	}

	/** Called after a successful login. Kept out of the constructor so WindowBuilder can open this file. */
	public void showFor(User user) {
		currentUser = user;
		lblUserName.setText(user.getFullName());
		lblUserRole.setText(user.getRole());
		lblRoleBadge.setText(user.getRole());

		// POLYMORPHISM: canManageListings() runs the Admin, Agent or Client version
		if (user.canManageListings()) {
			btnNavListings.setText("Property listings");
			btnNavInquiries.setText("Inquiries");
			btnNavSales.setVisible(true);
		} else {
			btnNavListings.setText("Browse listings");
			btnNavInquiries.setText("My inquiries");
			btnNavSales.setVisible(false);
		}

		openDashboard();
		setLocationRelativeTo(null);
		setVisible(true);
	}

	// ---- Menu actions: each one creates a fresh page, so the data is always up to date ----

	private void openDashboard() {
		DashboardPanel page = new DashboardPanel();
		page.loadFor(currentUser);
		showPage(page, "Dashboard", "Overview of listings, inquiries and sales");
		highlight(btnNavDashboard);
	}

	private void openListings() {
		ListingsPanel page = new ListingsPanel();
		page.loadFor(currentUser);
		if (currentUser.canManageListings()) {
			showPage(page, "Property listings", "Search, add, update and delete property listings");
		} else {
			showPage(page, "Browse listings", "Find a property and send an inquiry to its agent");
		}
		highlight(btnNavListings);
	}

	private void openInquiries() {
		InquiriesPanel page = new InquiriesPanel();
		page.loadFor(currentUser);
		if (currentUser.canManageListings()) {
			showPage(page, "Inquiries", "Questions from clients about your listings");
		} else {
			showPage(page, "My inquiries", "Questions you sent to agents and their status");
		}
		highlight(btnNavInquiries);
	}

	private void openSales() {
		SalesPanel page = new SalesPanel();
		page.loadFor(currentUser);
		showPage(page, "Sales and reports", "Record sales and monitor sales totals");
		highlight(btnNavSales);
	}

	/** Puts a page inside the middle area and updates the header. */
	private void showPage(JPanel page, String title, String subtitle) {
		lblPageTitle.setText(title);
		lblPageSubtitle.setText(subtitle);
		pnlContent.removeAll();
		page.setBounds(0, 0, pnlContent.getWidth(), pnlContent.getHeight());
		pnlContent.add(page);
		pnlContent.revalidate();
		pnlContent.repaint();
	}

	/** Colors the clicked menu button teal and the others back to normal. */
	private void highlight(JButton active) {
		resetNav(btnNavDashboard);
		resetNav(btnNavListings);
		resetNav(btnNavInquiries);
		resetNav(btnNavSales);
		active.setBackground(Theme.ACCENT);
		active.setForeground(Color.WHITE);
		active.setFont(Theme.BODY_BOLD);
	}

	private void resetNav(JButton button) {
		button.setBackground(Theme.SIDEBAR);
		button.setForeground(Theme.SIDEBAR_TEXT);
		button.setFont(Theme.BODY);
	}

	private void doLogout() {
		int answer = JOptionPane.showConfirmDialog(this, "Do you want to log out?", "Log out",
				JOptionPane.YES_NO_OPTION);
		if (answer == JOptionPane.YES_OPTION) {
			LoginFrame login = new LoginFrame();
			login.setVisible(true);
			dispose();
		}
	}
}
