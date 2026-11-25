package com.hanapbahay.ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.util.ArrayList;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

import com.hanapbahay.AppContext;
import com.hanapbahay.model.Agent;
import com.hanapbahay.model.Client;
import com.hanapbahay.model.Property;
import com.hanapbahay.model.User;
import com.hanapbahay.model.Viewing;
import com.hanapbahay.service.BaseService;
import com.hanapbahay.service.ReportGenerator;
import com.hanapbahay.service.UserService;

/**
 * The "Agent" section: the agent's profile, the numbers for their work,
 * and their next viewings. It is the first page after login.
 */
public class AgentPanel extends BasePanel {

	private static final long serialVersionUID = 1L;

	private User user;
	private JLabel lblName;
	private JLabel lblRole;
	private JLabel lblUsername;
	private JLabel lblContact;
	private JLabel lblEmail;
	private JLabel lblListings = new JLabel("0");
	private JLabel lblListingsNote = new JLabel(" ");
	private JLabel lblClients = new JLabel("0");
	private JLabel lblViewings = new JLabel("0");
	private JLabel lblSales = new JLabel("0");
	private JLabel lblSalesNote = new JLabel(" ");
	private DefaultTableModel upcomingModel;

	public AgentPanel(User user, Agent agent) {
		super(agent);
		this.user = user;

		JPanel top = new JPanel(new BorderLayout(0, 16));
		top.setOpaque(false);
		top.add(createProfileCard(), BorderLayout.NORTH);
		top.add(createStatCards(), BorderLayout.CENTER);
		add(top, BorderLayout.NORTH);
		add(createUpcomingTable(), BorderLayout.CENTER);
		refresh();
	}

	@Override
	public String getTitle() {
		return "Agent";
	}

	private JPanel createProfileCard() {
		JPanel card = Theme.card();
		card.setLayout(new BorderLayout(16, 0));

		JPanel text = new JPanel();
		text.setOpaque(false);
		text.setLayout(new BoxLayout(text, BoxLayout.Y_AXIS));
		lblName = new JLabel(" ");
		lblName.setFont(Theme.TITLE);
		lblName.setForeground(Theme.WHITE);
		lblRole = new JLabel(" ");
		lblRole.setFont(Theme.BODY_BOLD);
		lblRole.setForeground(Theme.GOLD);
		lblUsername = Theme.mutedLabel(" ");
		lblContact = Theme.mutedLabel(" ");
		lblEmail = Theme.mutedLabel(" ");
		text.add(lblName);
		text.add(lblRole);
		text.add(new JLabel(" "));
		text.add(lblUsername);
		text.add(lblContact);
		text.add(lblEmail);
		card.add(text, BorderLayout.CENTER);

		JButton btnEdit = Theme.goldButton("Edit Profile");
		btnEdit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				editProfile();
			}
		});
		JPanel buttonHolder = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		buttonHolder.setOpaque(false);
		buttonHolder.add(btnEdit);
		card.add(buttonHolder, BorderLayout.EAST);
		return card;
	}

	private JPanel createStatCards() {
		JPanel cards = new JPanel(new GridLayout(1, 4, 14, 0));
		cards.setOpaque(false);
		cards.setPreferredSize(new Dimension(600, 100));
		cards.add(Theme.statCard("⌂", "My Listings", lblListings, lblListingsNote));
		cards.add(Theme.statCard("●", "My Clients", lblClients, new JLabel("registered buyers")));
		cards.add(Theme.statCard("■", "Upcoming Viewings", lblViewings, new JLabel("scheduled from today")));
		cards.add(Theme.statCard("₱", "Completed Deals", lblSales, lblSalesNote));
		return cards;
	}

	private JPanel createUpcomingTable() {
		JPanel panel = new JPanel(new BorderLayout());
		panel.setOpaque(false);
		panel.add(Theme.sectionTitle("Next Viewings"), BorderLayout.NORTH);
		upcomingModel = new DefaultTableModel(new String[] { "Date", "Time", "Client", "Property" }, 0) {
			private static final long serialVersionUID = 1L;

			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};
		JScrollPane scroll = Theme.styleTable(new JTable(upcomingModel));
		panel.add(scroll, BorderLayout.CENTER);
		return panel;
	}

	@Override
	public void refresh() {
		lblName.setText(agent.getFullName());
		lblRole.setText(agent.getRoleLabel()); // Person's abstract method, Agent's version
		lblUsername.setText("Username: " + user.getUsername());
		lblContact.setText("Contact number: " + agent.getContactNumber());
		lblEmail.setText("Email: " + (agent.getEmail() == null || agent.getEmail().isEmpty() ? "-" : agent.getEmail()));

		ReportGenerator reports = AppContext.reportGenerator();
		int id = agent.getAgentId();
		ArrayList<Property> properties = reports.getProperties(id);
		lblListings.setText(String.valueOf(properties.size()));
		lblListingsNote.setText(reports.countStatus(properties, Property.AVAILABLE) + " available, "
				+ reports.countStatus(properties, Property.RESERVED) + " reserved, "
				+ reports.countStatus(properties, Property.SOLD) + " sold");
		ArrayList<Client> clients = reports.getClients(id);
		lblClients.setText(String.valueOf(clients.size()));
		lblViewings.setText(String.valueOf(reports.countUpcomingViewings(id)));
		lblSales.setText(String.valueOf(reports.countCompletedDeals(id)));
		lblSalesNote.setText("worth " + BaseService.peso(reports.getTotalSales(id)));

		// the scheduled viewings from today on, earliest first
		upcomingModel.setRowCount(0);
		ArrayList<Viewing> viewings = AppContext.viewingService().getViewings(id);
		String today = LocalDate.now().toString();
		for (int i = viewings.size() - 1; i >= 0; i--) { // the list is newest first, so read it backwards
			Viewing v = viewings.get(i);
			if (Viewing.SCHEDULED.equals(v.getStatus()) && v.getViewingDate().compareTo(today) >= 0) {
				upcomingModel.addRow(new Object[] { v.getViewingDate(), v.getViewingTime(),
						ReportGenerator.clientName(clients, v.getClientId()),
						ReportGenerator.propertyTitle(properties, v.getPropertyId()) });
			}
		}
		if (upcomingModel.getRowCount() == 0) {
			upcomingModel.addRow(new Object[] { "No upcoming viewings", "", "", "" });
		}
	}

	private void editProfile() {
		JTextField txtFirst = Theme.field();
		JTextField txtLast = Theme.field();
		JTextField txtContact = Theme.field();
		JTextField txtEmail = Theme.field();
		txtFirst.setText(agent.getFirstName());
		txtLast.setText(agent.getLastName());
		txtContact.setText(agent.getContactNumber());
		txtEmail.setText(agent.getEmail());
		JPanel form = Theme.form(new Object[] { "First name *", txtFirst, "Last name *", txtLast, "Contact number *",
				txtContact, "Email", txtEmail });

		UserService service = AppContext.userService();
		while (showForm(form, "Edit Profile")) {
			if (service.updateProfile(agent, txtFirst.getText(), txtLast.getText(), txtContact.getText(),
					txtEmail.getText())) {
				refresh();
				showInfo(service.getMessage());
				return;
			}
			showWarning(service.getMessage());
		}
	}
}
