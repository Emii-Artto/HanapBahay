package dash;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import model.User;

import login.LoginFrame;

/**
 * The window of the Owner and the Admins (3.2.1): Overview of all agents, Manage Users
 * (3.1.9) and the reports for all agents. They do not manage listings, clients, viewings
 * or deals; that is the agents' work.
 */
public class AdminDashboard extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JPanel pnlContent;
	private JLabel lblPageTitle;
	private JLabel lblAdminName;
	private JLabel lblAdminRole;
	private NavButton btnOverview;
	private NavButton btnUsers;
	private NavButton btnReports;
	private AdminOverviewPanel overviewPanel;
	private ManageUsersPanel usersPanel;
	private ReportsPanel reportsPanel;
	private User user;

	public AdminDashboard() {
		setTitle("HanapBahay - Admin");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 1360, 860);
		setMinimumSize(new Dimension(1100, 680));
		setLocationRelativeTo(null);
		contentPane = new JPanel();
		contentPane.setBackground(Theme.DARK);
		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(0, 0));

		// ================= SIDEBAR =================
		JPanel pnlSidebar = new JPanel();
		pnlSidebar.setBackground(Theme.PANEL);
		pnlSidebar.setPreferredSize(new Dimension(240, 10));
		pnlSidebar.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 1, Theme.LINE));
		contentPane.add(pnlSidebar, BorderLayout.WEST);
		pnlSidebar.setLayout(new BorderLayout(0, 0));

		JPanel pnlNav = new JPanel();
		pnlNav.setOpaque(false);
		pnlNav.setPreferredSize(new Dimension(240, 270));
		pnlSidebar.add(pnlNav, BorderLayout.NORTH);
		pnlNav.setLayout(null);

		JLabel lblLogo = new JLabel("HanapBahay");
		lblLogo.setIcon(new NavIcon(NavIcon.PROPERTIES, 26, Theme.GOLD));
		lblLogo.setIconTextGap(10);
		lblLogo.setFont(new Font("Segoe UI", Font.BOLD, 21));
		lblLogo.setForeground(Theme.GOLD);
		lblLogo.setBounds(18, 22, 210, 34);
		pnlNav.add(lblLogo);

		JLabel lblPanel = new JLabel("ADMIN PANEL");
		lblPanel.setFont(Theme.SMALL);
		lblPanel.setForeground(Theme.MUTED);
		lblPanel.setBounds(54, 54, 170, 16);
		pnlNav.add(lblPanel);

		JLabel lblMenu = new JLabel("MENU");
		lblMenu.setFont(Theme.SMALL_BOLD);
		lblMenu.setForeground(Theme.MUTED);
		lblMenu.setBounds(20, 92, 200, 16);
		pnlNav.add(lblMenu);

		btnOverview = new NavButton("Overview", NavIcon.OVERVIEW);
		btnOverview.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				openSection("Overview");
			}
		});
		btnOverview.setBounds(12, 114, 216, 40);
		pnlNav.add(btnOverview);

		btnUsers = new NavButton("Manage Users", NavIcon.USERS);
		btnUsers.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				openSection("Users");
			}
		});
		btnUsers.setBounds(12, 160, 216, 40);
		pnlNav.add(btnUsers);

		btnReports = new NavButton("Reports", NavIcon.REPORTS);
		btnReports.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				openSection("Reports");
			}
		});
		btnReports.setBounds(12, 206, 216, 40);
		pnlNav.add(btnReports);

		JPanel pnlProfile = new JPanel();
		pnlProfile.setOpaque(false);
		pnlProfile.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, Theme.LINE));
		pnlProfile.setPreferredSize(new Dimension(240, 172));
		pnlSidebar.add(pnlProfile, BorderLayout.SOUTH);
		pnlProfile.setLayout(null);

		lblAdminName = new JLabel("● admin");
		lblAdminName.setFont(Theme.BODY_BOLD);
		lblAdminName.setForeground(Theme.GOLD);
		lblAdminName.setBounds(18, 16, 210, 20);
		pnlProfile.add(lblAdminName);

		lblAdminRole = new JLabel("Administrator");
		lblAdminRole.setFont(Theme.SMALL);
		lblAdminRole.setForeground(Theme.MUTED);
		lblAdminRole.setBounds(32, 37, 190, 16);
		pnlProfile.add(lblAdminRole);

		AppButton btnChangePassword = new AppButton("Change Password", Theme.PANEL);
		btnChangePassword.setIcon(new NavIcon(NavIcon.KEY));
		btnChangePassword.setHorizontalAlignment(SwingConstants.LEFT);
		btnChangePassword.setForeground(Theme.MUTED);
		btnChangePassword.setOutline(Theme.CARD_BORDER);
		btnChangePassword.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				changePassword();
			}
		});
		btnChangePassword.setBounds(12, 66, 216, 38);
		pnlProfile.add(btnChangePassword);

		AppButton btnLogout = new AppButton("Log Out", Theme.PANEL);
		btnLogout.setIcon(new NavIcon(NavIcon.LOGOUT));
		btnLogout.setHorizontalAlignment(SwingConstants.LEFT);
		btnLogout.setForeground(Theme.RED);
		btnLogout.setOutline(Theme.CARD_BORDER);
		btnLogout.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				logout();
			}
		});
		btnLogout.setBounds(12, 112, 216, 38);
		pnlProfile.add(btnLogout);

		// ================= MAIN AREA =================
		JPanel pnlMain = new JPanel();
		pnlMain.setBackground(Theme.DARK);
		contentPane.add(pnlMain, BorderLayout.CENTER);
		pnlMain.setLayout(new BorderLayout(0, 0));

		JPanel pnlTopBar = new JPanel();
		pnlTopBar.setBackground(Theme.DARK);
		pnlTopBar.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Theme.LINE),
				new EmptyBorder(14, 28, 14, 24)));
		pnlMain.add(pnlTopBar, BorderLayout.NORTH);
		pnlTopBar.setLayout(new BorderLayout(0, 0));

		JPanel pnlTitle = new JPanel();
		pnlTitle.setOpaque(false);
		pnlTopBar.add(pnlTitle, BorderLayout.WEST);
		pnlTitle.setLayout(new GridLayout(2, 1, 0, 0));

		lblPageTitle = new JLabel("Admin Overview");
		lblPageTitle.setFont(Theme.TITLE);
		lblPageTitle.setForeground(Theme.WHITE);
		pnlTitle.add(lblPageTitle);

		JLabel lblSubtitle = new JLabel("All agents, accounts and reports in one place.");
		lblSubtitle.setFont(Theme.SMALL);
		lblSubtitle.setForeground(Theme.MUTED);
		pnlTitle.add(lblSubtitle);

		JPanel pnlTopRight = new JPanel();
		pnlTopRight.setOpaque(false);
		pnlTopBar.add(pnlTopRight, BorderLayout.EAST);
		pnlTopRight.setLayout(new GridBagLayout()); // keeps the button centered vertically

		AppButton btnRefresh = new AppButton("Refresh");
		btnRefresh.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				overviewPanel.refresh();
				usersPanel.refresh();
				reportsPanel.refresh();
			}
		});
		pnlTopRight.add(btnRefresh, new GridBagConstraints());

		pnlContent = new JPanel();
		pnlContent.setBackground(Theme.DARK);
		pnlMain.add(pnlContent, BorderLayout.CENTER);
		pnlContent.setLayout(new CardLayout(0, 0));

		overviewPanel = new AdminOverviewPanel();
		pnlContent.add(overviewPanel, "Overview");

		usersPanel = new ManageUsersPanel();
		pnlContent.add(usersPanel, "Users");

		reportsPanel = new ReportsPanel();
		pnlContent.add(reportsPanel, "Reports");
	}

	/** Called by LoginFrame after the Owner or an Admin logs in. */
	public void startSession(User user) {
		this.user = user;
		setTitle("HanapBahay - " + user.getRoleLabel());
		lblAdminName.setText("● " + user.getUsername());
		lblAdminRole.setText(user.isOwner() ? "Owner (highest authority)" : "Administrator");
		usersPanel.setActor(user);
		reportsPanel.setAgent(null); // null = all agents
		openSection("Overview");
	}

	private void openSection(String name) {
		highlight(btnOverview, "Overview".equals(name));
		highlight(btnUsers, "Users".equals(name));
		highlight(btnReports, "Reports".equals(name));
		DashboardSection section = overviewPanel;
		if ("Users".equals(name)) {
			section = usersPanel;
		} else if ("Reports".equals(name)) {
			section = reportsPanel;
		}
		section.refresh(); // POLYMORPHISM: each panel's own refresh()
		lblPageTitle.setText(section.getPageTitle());
		((CardLayout) pnlContent.getLayout()).show(pnlContent, name);
	}

	private void highlight(NavButton button, boolean active) {
		button.setActive(active); // gold text and gold bar on the page being shown
	}

	private void changePassword() {
		ChangePasswordDialog dialog = new ChangePasswordDialog();
		dialog.setUser(user, false);
		dialog.setLocationRelativeTo(this);
		dialog.setVisible(true);
	}

	private void logout() {
		int answer = JOptionPane.showConfirmDialog(this, "Do you want to log out?", "Log Out",
				JOptionPane.YES_NO_OPTION);
		if (answer == JOptionPane.YES_OPTION) {
			new LoginFrame().setVisible(true);
			dispose();
		}
	}
}
