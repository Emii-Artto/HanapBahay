package com.hanapbahay.ui;

import java.awt.BorderLayout;
import java.awt.Component;

import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import com.hanapbahay.model.Agent;

/**
 * Parent class of the six panels in the main content area (Agent, Properties, Clients,
 * Viewing, Deals and Reports).
 *
 * INHERITANCE: BasePanel IS-A Swing JPanel, and each panel IS-A BasePanel.
 * ABSTRACTION + POLYMORPHISM: every panel must have refresh(); MainDashboard calls
 *   refresh() on whichever panel is opened, and Java runs that panel's own version.
 */
public abstract class BasePanel extends JPanel {

	private static final long serialVersionUID = 1L;

	protected Agent agent; // the logged-in agent (whose records this panel shows)

	public BasePanel(Agent agent) {
		this.agent = agent;
		setLayout(new BorderLayout(0, 14));
		setBackground(Theme.DARK);
		setBorder(new EmptyBorder(18, 24, 18, 24));
	}

	/** Reloads the records from the database and shows them again. */
	public abstract void refresh();

	/** The title shown in the top bar when this panel is open. */
	public abstract String getTitle();

	protected int agentId() {
		return agent == null ? 0 : agent.getAgentId();
	}

	// ---- messages in plain language (3.2.3) ----
	protected void showInfo(String text) {
		JOptionPane.showMessageDialog(this, text, getTitle(), JOptionPane.INFORMATION_MESSAGE);
	}

	protected void showWarning(String text) {
		JOptionPane.showMessageDialog(this, text, getTitle(), JOptionPane.WARNING_MESSAGE);
	}

	protected boolean confirm(String text) {
		int answer = JOptionPane.showConfirmDialog(this, text, getTitle(), JOptionPane.YES_NO_OPTION,
				JOptionPane.WARNING_MESSAGE);
		return answer == JOptionPane.YES_OPTION;
	}

	/** Shows a form in a dialog. Returns true if the user clicked OK. */
	protected boolean showForm(Component form, String dialogTitle) {
		int answer = JOptionPane.showConfirmDialog(this, form, dialogTitle, JOptionPane.OK_CANCEL_OPTION,
				JOptionPane.PLAIN_MESSAGE);
		return answer == JOptionPane.OK_OPTION;
	}
}
