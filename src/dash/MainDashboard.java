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
import javax.swing.JPasswordField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import com.hanapbahay.model.Agent;
import com.hanapbahay.model.User;

import login.LoginFrame;

/**
 * 3.2.1 The agent's main window (UI member's design, WindowBuilder style).
 * Sidebar with six buttons: Agent, Properties, Clients, Viewing, Deals and Reports.
 * Selecting a button shows the matching panel in the content area (CardLayout).
 *
 * Client Browse Mode (3.1.6 / 3.2.4): the sidebar and the other panels are hidden, only
 * Available properties are shown read-only, and leaving needs the agent's password.
 */
public class MainDashboard extends JFrame {

	private static final long serialVersionUID = 1L;
	private static final String BROWSE = "Browse";

	private JPanel contentPane;
	private JPanel pnlSidebar;
	private JPanel pnlContent;
	private JLabel lblPageTitle;
	private JLabel lblAgentName;
	private JLabel lblAgentRole;
	private AppButton btnClientMode;
	private NavButton[] navButtons;
	private String[] sectionNames = { "Agent", "Properties", "Clients", "Viewing", "Deals", "Reports" };

	private AgentPanel agentPanel;
	private PropertiesPanel propertiesPanel;
	private ClientsPanel clientsPanel;
	private ViewingPanel viewingPanel;
	private DealsPanel dealsPanel;
	private ReportsPanel reportsPanel;
	private PropertiesPanel browsePanel;
	// POLYMORPHISM: six different panel classes kept in one array of the interface type
	private DashboardSection[] sections;

	private User user;
	private boolean browsing = false;
	private String currentSection = "Agent";

	public MainDashboard() {
		setTitle("HanapBahay - Agent");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 1360, 860);
		setMinimumSize(new Dimension(1150, 700));
		setLocationRelativeTo(null);
		contentPane = new JPanel();
		contentPane.setBackground(Theme.DARK);
		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(0, 0));

		// ================= SIDEBAR =================
		pnlSidebar = new JPanel();
		pnlSidebar.setBackground(Theme.PANEL);
		pnlSidebar.setPreferredSize(new Dimension(240, 10));
		pnlSidebar.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 1, Theme.LINE));
		contentPane.add(pnlSidebar, BorderLayout.WEST);
		pnlSidebar.setLayout(new BorderLayout(0, 0));

		JPanel pnlNav = new JPanel();
		pnlNav.setOpaque(false);
		pnlNav.setPreferredSize(new Dimension(240, 410));
		pnlSidebar.add(pnlNav, BorderLayout.NORTH);
		pnlNav.setLayout(null);

		JLabel lblLogo = new JLabel("HanapBahay");
		lblLogo.setIcon(new NavIcon(NavIcon.PROPERTIES, 26, Theme.GOLD));
		lblLogo.setIconTextGap(10);
		lblLogo.setFont(new Font("Segoe UI", Font.BOLD, 21));
		lblLogo.setForeground(Theme.GOLD);
		lblLogo.setBounds(18, 22, 210, 34);
		pnlNav.add(lblLogo);

		JLabel lblSystem = new JLabel("REAL ESTATE SYSTEM");
		lblSystem.setFont(Theme.SMALL);
		lblSystem.setForeground(Theme.MUTED);
		lblSystem.setBounds(54, 54, 170, 16);
		pnlNav.add(lblSystem);

		JLabel lblMenu = new JLabel("MENU");
		lblMenu.setFont(Theme.SMALL_BOLD);
		lblMenu.setForeground(Theme.MUTED);
		lblMenu.setBounds(20, 92, 200, 16);
		pnlNav.add(lblMenu);

		NavButton btnAgent = new NavButton("Agent", NavIcon.AGENT);
		btnAgent.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				openSection("Agent");
			}
		});
		btnAgent.setBounds(12, 114, 216, 40);
		pnlNav.add(btnAgent);

		NavButton btnProperties = new NavButton("Properties", NavIcon.PROPERTIES);
		btnProperties.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				openSection("Properties");
			}
		});
		btnProperties.setBounds(12, 160, 216, 40);
		pnlNav.add(btnProperties);

		NavButton btnClients = new NavButton("Clients", NavIcon.CLIENTS);
		btnClients.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				openSection("Clients");
			}
		});
		btnClients.setBounds(12, 206, 216, 40);
		pnlNav.add(btnClients);

		NavButton btnViewing = new NavButton("Viewing", NavIcon.VIEWING);
		btnViewing.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				openSection("Viewing");
			}
		});
		btnViewing.setBounds(12, 252, 216, 40);
		pnlNav.add(btnViewing);

		NavButton btnDeals = new NavButton("Deals", NavIcon.DEALS);
		btnDeals.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				openSection("Deals");
			}
		});
		btnDeals.setBounds(12, 298, 216, 40);
		pnlNav.add(btnDeals);

		NavButton btnReports = new NavButton("Reports", NavIcon.REPORTS);
		btnReports.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				openSection("Reports");
			}
		});
		btnReports.setBounds(12, 344, 216, 40);
		pnlNav.add(btnReports);

		navButtons = new NavButton[] { btnAgent, btnProperties, btnClients, btnViewing, btnDeals, btnReports };

		JPanel pnlProfile = new JPanel();
		pnlProfile.setOpaque(false);
		pnlProfile.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, Theme.LINE));
		pnlProfile.setPreferredSize(new Dimension(240, 172));
		pnlSidebar.add(pnlProfile, BorderLayout.SOUTH);
		pnlProfile.setLayout(null);

		lblAgentName = new JLabel("● Agent Profile");
		lblAgentName.setFont(Theme.BODY_BOLD);
		lblAgentName.setForeground(Theme.GOLD);
		lblAgentName.setBounds(18, 16, 210, 20);
		pnlProfile.add(lblAgentName);

		lblAgentRole = new JLabel("Real Estate Agent");
		lblAgentRole.setFont(Theme.SMALL);
		lblAgentRole.setForeground(Theme.MUTED);
		lblAgentRole.setBounds(32, 37, 190, 16);
		pnlProfile.add(lblAgentRole);

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

		lblPageTitle = new JLabel("Agent");
		lblPageTitle.setFont(Theme.TITLE);
		lblPageTitle.setForeground(Theme.WHITE);
		pnlTitle.add(lblPageTitle);

		JLabel lblSlogan = new JLabel("Build more dreams. Sell more homes.");
		lblSlogan.setFont(Theme.SMALL);
		lblSlogan.setForeground(Theme.MUTED);
		pnlTitle.add(lblSlogan);

		JPanel pnlTopRight = new JPanel();
		pnlTopRight.setOpaque(false);
		pnlTopBar.add(pnlTopRight, BorderLayout.EAST);
		pnlTopRight.setLayout(new GridBagLayout()); // keeps the button centered vertically

		btnClientMode = new AppButton("● Start Client Browse Mode");
		btnClientMode.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (browsing) {
					exitClientMode();
				} else {
					startClientMode();
				}
			}
		});
		pnlTopRight.add(btnClientMode, new GridBagConstraints());

		pnlContent = new JPanel();
		pnlContent.setBackground(Theme.DARK);
		pnlMain.add(pnlContent, BorderLayout.CENTER);
		pnlContent.setLayout(new CardLayout(0, 0));

		agentPanel = new AgentPanel();
		pnlContent.add(agentPanel, "Agent");

		propertiesPanel = new PropertiesPanel();
		pnlContent.add(propertiesPanel, "Properties");

		clientsPanel = new ClientsPanel();
		pnlContent.add(clientsPanel, "Clients");

		viewingPanel = new ViewingPanel();
		pnlContent.add(viewingPanel, "Viewing");

		dealsPanel = new DealsPanel();
		pnlContent.add(dealsPanel, "Deals");

		reportsPanel = new ReportsPanel();
		pnlContent.add(reportsPanel, "Reports");

		browsePanel = new PropertiesPanel();
		browsePanel.setBrowseMode(true);
		pnlContent.add(browsePanel, BROWSE);

		sections = new DashboardSection[] { agentPanel, propertiesPanel, clientsPanel, viewingPanel, dealsPanel,
				reportsPanel };
	}

	/** Called by LoginFrame after a successful login. */
	public void startSession(User user, Agent agent) {
		this.user = user;
		setTitle("HanapBahay - " + agent.getFullName());
		lblAgentName.setText("● " + agent.getFullName());
		lblAgentRole.setText(agent.getRoleLabel());
		agentPanel.setUser(user);
		for (int i = 0; i < sections.length; i++) {
			sections[i].setAgent(agent); // the same call works for every kind of panel
		}
		browsePanel.setAgent(agent);
		openSection("Agent");
	}

	/** Shows one section and reloads its data. */
	private void openSection(String name) {
		for (int i = 0; i < sectionNames.length; i++) {
			boolean active = sectionNames[i].equals(name);
			navButtons[i].setActive(active); // gold text and gold bar on the page being shown
			if (active) {
				sections[i].refresh(); // POLYMORPHISM: runs that panel's own refresh()
				lblPageTitle.setText(sections[i].getPageTitle());
				((CardLayout) pnlContent.getLayout()).show(pnlContent, name);
				currentSection = name;
			}
		}
	}

	// ================= Client Browse Mode =================

	private void startClientMode() {
		int answer = JOptionPane.showConfirmDialog(this,
				"Start Client Browse Mode?\n\nThe buyer will only see Available properties and their photos.\n"
						+ "Clients, viewings, deals and reports are hidden.\n"
						+ "You will need your password to leave this mode.",
				"Client Browse Mode", JOptionPane.OK_CANCEL_OPTION, JOptionPane.INFORMATION_MESSAGE);
		if (answer != JOptionPane.OK_OPTION) {
			return;
		}
		browsing = true;
		pnlSidebar.setVisible(false);
		browsePanel.refresh();
		lblPageTitle.setText(browsePanel.getPageTitle());
		((CardLayout) pnlContent.getLayout()).show(pnlContent, BROWSE);
		btnClientMode.setText("Exit Client Mode");
	}

	/** Leaving the mode requires the agent to type the password again (3.2.4). */
	private void exitClientMode() {
		JPasswordField txtPassword = new JPasswordField();
		int answer = JOptionPane.showConfirmDialog(this,
				new Object[] { "Only the agent can leave Client Browse Mode.\nAgent password:", txtPassword },
				"Exit Client Mode", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
		if (answer != JOptionPane.OK_OPTION) {
			return;
		}
		// ENCAPSULATION: User only answers true or false; the password hash is never shown
		if (!user.checkPassword(new String(txtPassword.getPassword()))) {
			JOptionPane.showMessageDialog(this, "Wrong password. Client Browse Mode stays on.", "Exit Client Mode",
					JOptionPane.WARNING_MESSAGE);
			return;
		}
		browsing = false;
		pnlSidebar.setVisible(true);
		btnClientMode.setText("● Start Client Browse Mode");
		openSection(currentSection);
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
