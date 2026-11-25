package dash;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import com.hanapbahay.model.User;

import login.LoginFrame;

/**
 * The Admin's window (an extra, not in the paper): an overview of ALL agents and the
 * reports for all agents. The admin does not manage listings, clients, viewings or deals.
 */
public class AdminDashboard extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JPanel pnlContent;
	private JLabel lblPageTitle;
	private JLabel lblAdminName;
	private AppButton btnOverview;
	private AppButton btnReports;
	private AdminOverviewPanel overviewPanel;
	private ReportsPanel reportsPanel;

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
		pnlSidebar.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 1, Theme.GOLD));
		contentPane.add(pnlSidebar, BorderLayout.WEST);
		pnlSidebar.setLayout(new BorderLayout(0, 0));

		JPanel pnlNav = new JPanel();
		pnlNav.setOpaque(false);
		pnlNav.setPreferredSize(new Dimension(240, 220));
		pnlSidebar.add(pnlNav, BorderLayout.NORTH);
		pnlNav.setLayout(null);

		JLabel lblLogo = new JLabel("⌂ HanapBahay");
		lblLogo.setFont(new Font("Arial", Font.BOLD, 24));
		lblLogo.setForeground(Theme.GOLD);
		lblLogo.setBounds(18, 24, 210, 32);
		pnlNav.add(lblLogo);

		JLabel lblPanel = new JLabel("ADMIN PANEL");
		lblPanel.setFont(Theme.SMALL);
		lblPanel.setForeground(Theme.MUTED);
		lblPanel.setBounds(20, 58, 200, 16);
		pnlNav.add(lblPanel);

		btnOverview = new AppButton("   Overview", Theme.PANEL);
		btnOverview.setHorizontalAlignment(SwingConstants.LEFT);
		btnOverview.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				openSection(true);
			}
		});
		btnOverview.setBounds(14, 100, 212, 44);
		pnlNav.add(btnOverview);

		btnReports = new AppButton("   Reports", Theme.PANEL);
		btnReports.setHorizontalAlignment(SwingConstants.LEFT);
		btnReports.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				openSection(false);
			}
		});
		btnReports.setBounds(14, 150, 212, 44);
		pnlNav.add(btnReports);

		JPanel pnlProfile = new JPanel();
		pnlProfile.setOpaque(false);
		pnlProfile.setPreferredSize(new Dimension(240, 130));
		pnlSidebar.add(pnlProfile, BorderLayout.SOUTH);
		pnlProfile.setLayout(null);

		lblAdminName = new JLabel("● admin");
		lblAdminName.setFont(Theme.BODY_BOLD);
		lblAdminName.setForeground(Theme.GOLD);
		lblAdminName.setBounds(18, 10, 210, 20);
		pnlProfile.add(lblAdminName);

		JLabel lblAdminRole = new JLabel("System Administrator");
		lblAdminRole.setFont(Theme.SMALL);
		lblAdminRole.setForeground(Theme.MUTED);
		lblAdminRole.setBounds(32, 32, 190, 16);
		pnlProfile.add(lblAdminRole);

		AppButton btnLogout = new AppButton("Log Out");
		btnLogout.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				logout();
			}
		});
		btnLogout.setBounds(14, 64, 212, 42);
		pnlProfile.add(btnLogout);

		// ================= MAIN AREA =================
		JPanel pnlMain = new JPanel();
		pnlMain.setBackground(Theme.DARK);
		contentPane.add(pnlMain, BorderLayout.CENTER);
		pnlMain.setLayout(new BorderLayout(0, 0));

		JPanel pnlTopBar = new JPanel();
		pnlTopBar.setBackground(Theme.DARK);
		pnlTopBar.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Theme.LINE),
				new EmptyBorder(12, 24, 12, 20)));
		pnlMain.add(pnlTopBar, BorderLayout.NORTH);
		pnlTopBar.setLayout(new BorderLayout(0, 0));

		lblPageTitle = new JLabel("Admin Overview");
		lblPageTitle.setFont(Theme.TITLE);
		lblPageTitle.setForeground(Theme.WHITE);
		pnlTopBar.add(lblPageTitle, BorderLayout.WEST);

		JPanel pnlTopRight = new JPanel();
		pnlTopRight.setOpaque(false);
		pnlTopBar.add(pnlTopRight, BorderLayout.EAST);
		pnlTopRight.setLayout(new FlowLayout(FlowLayout.RIGHT, 8, 0));

		AppButton btnRefresh = new AppButton("Refresh");
		btnRefresh.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				overviewPanel.refresh();
				reportsPanel.refresh();
			}
		});
		pnlTopRight.add(btnRefresh);

		pnlContent = new JPanel();
		pnlContent.setBackground(Theme.DARK);
		pnlMain.add(pnlContent, BorderLayout.CENTER);
		pnlContent.setLayout(new CardLayout(0, 0));

		overviewPanel = new AdminOverviewPanel();
		pnlContent.add(overviewPanel, "Overview");

		reportsPanel = new ReportsPanel();
		pnlContent.add(reportsPanel, "Reports");
	}

	/** Called by LoginFrame after the admin logs in. */
	public void startSession(User user) {
		lblAdminName.setText("● " + user.getUsername());
		reportsPanel.setAgent(null); // null = all agents
		openSection(true);
	}

	private void openSection(boolean overview) {
		btnOverview.setBackground(overview ? Theme.ACTIVE : Theme.PANEL);
		btnOverview.setForeground(overview ? Theme.GOLD_LIGHT : Theme.WHITE);
		btnReports.setBackground(overview ? Theme.PANEL : Theme.ACTIVE);
		btnReports.setForeground(overview ? Theme.WHITE : Theme.GOLD_LIGHT);
		DashboardSection section = overview ? overviewPanel : reportsPanel;
		section.refresh();
		lblPageTitle.setText(section.getPageTitle());
		((CardLayout) pnlContent.getLayout()).show(pnlContent, overview ? "Overview" : "Reports");
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
