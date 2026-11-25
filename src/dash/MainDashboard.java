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
	private AppButton[] navButtons;
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
		pnlSidebar.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 1, Theme.GOLD));
		contentPane.add(pnlSidebar, BorderLayout.WEST);
		pnlSidebar.setLayout(new BorderLayout(0, 0));

		JPanel pnlNav = new JPanel();
		pnlNav.setOpaque(false);
		pnlNav.setPreferredSize(new Dimension(240, 420));
		pnlSidebar.add(pnlNav, BorderLayout.NORTH);
		pnlNav.setLayout(null);

		JLabel lblLogo = new JLabel("⌂ HanapBahay");
		lblLogo.setFont(new Font("Arial", Font.BOLD, 24));
		lblLogo.setForeground(Theme.GOLD);
		lblLogo.setBounds(18, 24, 210, 32);
		pnlNav.add(lblLogo);

		JLabel lblSystem = new JLabel("REAL ESTATE SYSTEM");
		lblSystem.setFont(Theme.SMALL);
		lblSystem.setForeground(Theme.MUTED);
		lblSystem.setBounds(20, 58, 200, 16);
		pnlNav.add(lblSystem);

		AppButton btnAgent = new AppButton("   Agent", Theme.PANEL);
		btnAgent.setHorizontalAlignment(SwingConstants.LEFT);
		btnAgent.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				openSection("Agent");
			}
		});
		btnAgent.setBounds(14, 100, 212, 44);
		pnlNav.add(btnAgent);

		AppButton btnProperties = new AppButton("   Properties", Theme.PANEL);
		btnProperties.setHorizontalAlignment(SwingConstants.LEFT);
		btnProperties.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				openSection("Properties");
			}
		});
		btnProperties.setBounds(14, 150, 212, 44);
		pnlNav.add(btnProperties);

		AppButton btnClients = new AppButton("   Clients", Theme.PANEL);
		btnClients.setHorizontalAlignment(SwingConstants.LEFT);
		btnClients.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				openSection("Clients");
			}
		});
		btnClients.setBounds(14, 200, 212, 44);
		pnlNav.add(btnClients);

		AppButton btnViewing = new AppButton("   Viewing", Theme.PANEL);
		btnViewing.setHorizontalAlignment(SwingConstants.LEFT);
		btnViewing.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				openSection("Viewing");
			}
		});
		btnViewing.setBounds(14, 250, 212, 44);
		pnlNav.add(btnViewing);

		AppButton btnDeals = new AppButton("   Deals", Theme.PANEL);
		btnDeals.setHorizontalAlignment(SwingConstants.LEFT);
		btnDeals.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				openSection("Deals");
			}
		});
		btnDeals.setBounds(14, 300, 212, 44);
		pnlNav.add(btnDeals);

		AppButton btnReports = new AppButton("   Reports", Theme.PANEL);
		btnReports.setHorizontalAlignment(SwingConstants.LEFT);
		btnReports.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				openSection("Reports");
			}
		});
		btnReports.setBounds(14, 350, 212, 44);
		pnlNav.add(btnReports);

		navButtons = new AppButton[] { btnAgent, btnProperties, btnClients, btnViewing, btnDeals, btnReports };

		JPanel pnlProfile = new JPanel();
		pnlProfile.setOpaque(false);
		pnlProfile.setPreferredSize(new Dimension(240, 170));
		pnlSidebar.add(pnlProfile, BorderLayout.SOUTH);
		pnlProfile.setLayout(null);

		lblAgentName = new JLabel("● Agent Profile");
		lblAgentName.setFont(Theme.BODY_BOLD);
		lblAgentName.setForeground(Theme.GOLD);
		lblAgentName.setBounds(18, 10, 210, 20);
		pnlProfile.add(lblAgentName);

		lblAgentRole = new JLabel("Real Estate Agent");
		lblAgentRole.setFont(Theme.SMALL);
		lblAgentRole.setForeground(Theme.MUTED);
		lblAgentRole.setBounds(32, 32, 190, 16);
		pnlProfile.add(lblAgentRole);

		AppButton btnChangePassword = new AppButton("Change Password", Theme.GRAY);
		btnChangePassword.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				changePassword();
			}
		});
		btnChangePassword.setBounds(14, 60, 212, 36);
		pnlProfile.add(btnChangePassword);

		AppButton btnLogout = new AppButton("Log Out");
		btnLogout.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				logout();
			}
		});
		btnLogout.setBounds(14, 104, 212, 42);
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

		lblPageTitle = new JLabel("Agent");
		lblPageTitle.setFont(Theme.TITLE);
		lblPageTitle.setForeground(Theme.WHITE);
		pnlTopBar.add(lblPageTitle, BorderLayout.WEST);

		JPanel pnlTopRight = new JPanel();
		pnlTopRight.setOpaque(false);
		pnlTopBar.add(pnlTopRight, BorderLayout.EAST);
		pnlTopRight.setLayout(new FlowLayout(FlowLayout.RIGHT, 14, 0));

		JLabel lblSlogan = new JLabel("Build more dreams. Sell more homes.");
		lblSlogan.setFont(new Font("Arial", Font.ITALIC, 14));
		lblSlogan.setForeground(Theme.MUTED);
		pnlTopRight.add(lblSlogan);

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
		pnlTopRight.add(btnClientMode);

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
			navButtons[i].setBackground(active ? Theme.ACTIVE : Theme.PANEL);
			navButtons[i].setForeground(active ? Theme.GOLD_LIGHT : Theme.WHITE);
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
