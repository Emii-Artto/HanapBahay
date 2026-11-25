package com.hanapbahay.ui;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import com.hanapbahay.model.User;

/**
 * Parent class of the two main windows (MainDashboard for agents, AdminDashboard for the admin).
 * It builds what they share: the sidebar with section buttons, the top bar, the content area
 * (CardLayout shows one section at a time) and Log Out.
 *
 * INHERITANCE: BaseFrame IS-A Swing JFrame; MainDashboard and AdminDashboard are BaseFrames.
 * ABSTRACTION: each window must say what happens when a section button is clicked (openSection).
 */
public abstract class BaseFrame extends JFrame {

	private static final long serialVersionUID = 1L;

	protected User user;
	protected JPanel sidebar;
	protected JPanel topRight; // subclasses put their own buttons here
	protected JLabel lblPageTitle;
	protected JPanel content;
	private CardLayout cardLayout = new CardLayout();
	private ArrayList<JButton> navButtons = new ArrayList<JButton>();
	private String activeSection = "";

	public BaseFrame(User user, String windowTitle, String panelLabel, String[] sections, String profileName,
			String profileRole) {
		this.user = user;
		setTitle(windowTitle);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(1360, 860);
		setMinimumSize(new Dimension(1100, 680));
		setLocationRelativeTo(null);

		JPanel root = new JPanel(new BorderLayout());
		root.setBackground(Theme.DARK);
		setContentPane(root);

		sidebar = createSidebar(panelLabel, sections, profileName, profileRole);
		root.add(sidebar, BorderLayout.WEST);

		JPanel main = new JPanel(new BorderLayout());
		main.setBackground(Theme.DARK);
		main.add(createTopBar(), BorderLayout.NORTH);
		content = new JPanel(cardLayout);
		content.setBackground(Theme.DARK);
		main.add(content, BorderLayout.CENTER);
		root.add(main, BorderLayout.CENTER);
	}

	/** What happens when a sidebar button is clicked. Each window decides. */
	protected abstract void openSection(String section);

	private JPanel createSidebar(String panelLabel, String[] sections, String profileName, String profileRole) {
		JPanel bar = new JPanel(new BorderLayout());
		bar.setPreferredSize(new Dimension(240, 0));
		bar.setBackground(Theme.PANEL);
		bar.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 1, Theme.GOLD));

		JPanel upper = new JPanel();
		upper.setOpaque(false);
		upper.setLayout(new BoxLayout(upper, BoxLayout.Y_AXIS));
		upper.setBorder(new EmptyBorder(26, 18, 10, 18));
		JLabel logo = new JLabel("⌂ HanapBahay");
		logo.setFont(new java.awt.Font("Arial", java.awt.Font.BOLD, 24));
		logo.setForeground(Theme.GOLD);
		logo.setAlignmentX(Component.LEFT_ALIGNMENT);
		JLabel sub = new JLabel(panelLabel);
		sub.setFont(Theme.SMALL);
		sub.setForeground(Theme.MUTED);
		sub.setBorder(new EmptyBorder(6, 2, 28, 0));
		sub.setAlignmentX(Component.LEFT_ALIGNMENT);
		upper.add(logo);
		upper.add(sub);

		for (int i = 0; i < sections.length; i++) {
			final String section = sections[i];
			final JButton button = new JButton("   " + section);
			button.setFont(Theme.BODY_BOLD);
			button.setForeground(Theme.WHITE);
			button.setBackground(Theme.PANEL);
			button.setOpaque(true);
			button.setBorderPainted(false);
			button.setFocusPainted(false);
			button.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
			button.setBorder(new EmptyBorder(10, 12, 10, 12));
			// hover color, but the open section keeps its gold highlight
			button.addMouseListener(new java.awt.event.MouseAdapter() {
				@Override
				public void mouseEntered(java.awt.event.MouseEvent e) {
					if (!section.equals(activeSection)) {
						button.setBackground(new Color(45, 48, 58));
					}
				}

				@Override
				public void mouseExited(java.awt.event.MouseEvent e) {
					if (!section.equals(activeSection)) {
						button.setBackground(Theme.PANEL);
					}
				}
			});
			button.setName(section);
			button.setHorizontalAlignment(SwingConstants.LEFT);
			button.setAlignmentX(Component.LEFT_ALIGNMENT);
			button.setMaximumSize(new Dimension(Integer.MAX_VALUE, 46));
			button.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					openSection(section);
				}
			});
			navButtons.add(button);
			upper.add(button);
			upper.add(Box.createVerticalStrut(6));
		}
		bar.add(upper, BorderLayout.NORTH);

		JPanel lower = new JPanel();
		lower.setOpaque(false);
		lower.setLayout(new BoxLayout(lower, BoxLayout.Y_AXIS));
		lower.setBorder(new EmptyBorder(10, 18, 22, 18));
		JLabel name = new JLabel("● " + profileName);
		name.setFont(Theme.BODY_BOLD);
		name.setForeground(Theme.GOLD);
		JLabel role = new JLabel(profileRole);
		role.setFont(Theme.SMALL);
		role.setForeground(Theme.MUTED);
		role.setBorder(new EmptyBorder(4, 14, 14, 0));
		JButton btnLogout = Theme.goldButton("Log Out");
		btnLogout.setAlignmentX(Component.LEFT_ALIGNMENT);
		btnLogout.setMaximumSize(new Dimension(Integer.MAX_VALUE, 42));
		btnLogout.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				logout();
			}
		});
		lower.add(name);
		lower.add(role);
		lower.add(btnLogout);
		bar.add(lower, BorderLayout.SOUTH);
		return bar;
	}

	private JPanel createTopBar() {
		JPanel bar = new JPanel(new BorderLayout());
		bar.setBackground(Theme.DARK);
		bar.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Theme.LINE),
				new EmptyBorder(14, 24, 14, 20)));
		lblPageTitle = new JLabel(" ");
		lblPageTitle.setFont(Theme.TITLE);
		lblPageTitle.setForeground(Theme.WHITE);
		bar.add(lblPageTitle, BorderLayout.WEST);
		topRight = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
		topRight.setOpaque(false);
		bar.add(topRight, BorderLayout.EAST);
		return bar;
	}

	protected void addCard(Component panel, String name) {
		content.add(panel, name);
	}

	/** Shows one card in the content area and marks its sidebar button. */
	protected void showCard(String name, String title) {
		cardLayout.show(content, name);
		lblPageTitle.setText(title);
		activeSection = name;
		for (JButton button : navButtons) {
			boolean active = name.equals(button.getName());
			Color background = active ? new Color(66, 60, 46) : Theme.PANEL;
			button.setBackground(background);
			button.setForeground(active ? Theme.GOLD_LIGHT : Theme.WHITE);
		}
	}

	protected void logout() {
		int answer = JOptionPane.showConfirmDialog(this, "Do you want to log out?", "Log Out",
				JOptionPane.YES_NO_OPTION);
		if (answer == JOptionPane.YES_OPTION) {
			new LoginFrame().setVisible(true);
			dispose();
		}
	}
}
