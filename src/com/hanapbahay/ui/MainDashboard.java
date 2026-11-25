package com.hanapbahay.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;

import com.hanapbahay.model.Agent;
import com.hanapbahay.model.User;

/**
 * 3.2.1 The agent's main window after login.
 * Sidebar with six buttons: Agent, Properties, Clients, Viewing, Deals and Reports.
 * Selecting a button opens the matching panel in the main content area.
 *
 * It also switches in and out of Client Browse Mode (3.1.6 / 3.2.4): the sidebar and all other
 * panels are hidden, only Available properties are shown read-only, and leaving needs the agent's password.
 */
public class MainDashboard extends BaseFrame {

	private static final long serialVersionUID = 1L;
	private static final String[] SECTIONS = { "Agent", "Properties", "Clients", "Viewing", "Deals", "Reports" };
	private static final String BROWSE = "Client Browse Mode";

	private Agent agent;
	private BasePanel[] panels; // POLYMORPHISM: six different panel classes in one array
	private JButton btnClientMode;
	private PropertiesPanel browsePanel;
	private boolean browsing = false;
	private String lastSection = "Agent";

	public MainDashboard(User user, Agent agent) {
		super(user, "HanapBahay - " + agent.getFullName(), "REAL ESTATE SYSTEM", SECTIONS, agent.getFullName(),
				agent.getRoleLabel());
		this.agent = agent;

		panels = new BasePanel[] { new AgentPanel(user, agent), new PropertiesPanel(agent, false),
				new ClientsPanel(agent), new ViewingPanel(agent), new DealsPanel(agent), new ReportsPanel(agent) };
		for (int i = 0; i < SECTIONS.length; i++) {
			addCard(panels[i], SECTIONS[i]);
		}

		btnClientMode = Theme.goldButton("● Start Client Browse Mode");
		btnClientMode.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (browsing) {
					exitClientMode();
				} else {
					startClientMode();
				}
			}
		});
		topRight.add(btnClientMode);

		openSection("Agent");
	}

	@Override
	protected void openSection(String section) {
		for (int i = 0; i < SECTIONS.length; i++) {
			if (SECTIONS[i].equals(section)) {
				panels[i].refresh(); // each panel reloads its own data (its own version of refresh)
				showCard(section, panels[i].getTitle());
				lastSection = section;
			}
		}
	}

	// ======================= Client Browse Mode =======================

	private void startClientMode() {
		int answer = JOptionPane.showConfirmDialog(this,
				"Start Client Browse Mode?\n\nThe buyer will only see Available properties and their photos.\n"
						+ "Clients, viewings, deals and reports are hidden.\nYou will need your password to leave this mode.",
				BROWSE, JOptionPane.OK_CANCEL_OPTION, JOptionPane.INFORMATION_MESSAGE);
		if (answer != JOptionPane.OK_OPTION) {
			return;
		}
		// a fresh read-only Properties panel (no Add, Edit or Delete)
		if (browsePanel != null) {
			content.remove(browsePanel);
		}
		browsePanel = new PropertiesPanel(agent, true);
		addCard(browsePanel, BROWSE);
		sidebar.setVisible(false);
		showCard(BROWSE, "Find Your Perfect Property");
		btnClientMode.setText("Exit Client Mode");
		browsing = true;
	}

	/** Leaving the mode requires the agent to enter the password again (3.2.4). */
	private void exitClientMode() {
		JPasswordField passwordField = new JPasswordField();
		JPanel form = Theme.form(new Object[] { "Agent password", passwordField });
		int answer = JOptionPane.showConfirmDialog(this, new Object[] {
				new JLabel("Only the agent can leave Client Browse Mode."), form }, "Exit Client Mode",
				JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
		if (answer != JOptionPane.OK_OPTION) {
			return;
		}
		// ENCAPSULATION: User only answers true/false; the password hash is never shown
		if (!user.checkPassword(new String(passwordField.getPassword()))) {
			JOptionPane.showMessageDialog(this, "Wrong password. Client Browse Mode stays on.", "Exit Client Mode",
					JOptionPane.WARNING_MESSAGE);
			return;
		}
		browsing = false;
		btnClientMode.setText("● Start Client Browse Mode");
		sidebar.setVisible(true);
		openSection(lastSection);
	}
}
