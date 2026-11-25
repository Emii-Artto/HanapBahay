package com.hanapbahay.ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import com.hanapbahay.AppContext;
import com.hanapbahay.model.Property;
import com.hanapbahay.model.User;
import com.hanapbahay.service.BaseService;
import com.hanapbahay.service.Report;
import com.hanapbahay.service.ReportGenerator;

/**
 * The Admin's window (an extra, not in the paper): an overview of ALL agents and the
 * reports for all agents. The admin does not manage listings, clients, viewings or deals.
 */
public class AdminDashboard extends BaseFrame {

	private static final long serialVersionUID = 1L;
	private static final String[] SECTIONS = { "Overview", "Reports" };

	private JLabel lblListings = new JLabel("0");
	private JLabel lblListingsNote = new JLabel(" ");
	private JLabel lblClients = new JLabel("0");
	private JLabel lblViewings = new JLabel("0");
	private JLabel lblSales = new JLabel("0");
	private JLabel lblSalesNote = new JLabel(" ");
	private DefaultTableModel agentModel;
	private ReportsPanel reportsPanel;

	public AdminDashboard(User user) {
		super(user, "HanapBahay - Admin", "ADMIN PANEL", SECTIONS, user.getUsername(), "System Administrator");
		addCard(createOverview(), "Overview");
		reportsPanel = new ReportsPanel(null); // null agent = all agents
		addCard(reportsPanel, "Reports");

		JButton btnRefresh = Theme.goldButton("Refresh");
		btnRefresh.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				loadOverview();
				reportsPanel.refresh();
			}
		});
		topRight.add(btnRefresh);
		openSection("Overview");
	}

	@Override
	protected void openSection(String section) {
		if ("Reports".equals(section)) {
			reportsPanel.refresh();
			showCard("Reports", "Reports - All Agents");
		} else {
			loadOverview();
			showCard("Overview", "Admin Overview");
		}
	}

	private JPanel createOverview() {
		JPanel page = new JPanel(new BorderLayout(0, 18));
		page.setBackground(Theme.DARK);
		page.setBorder(new EmptyBorder(18, 24, 18, 24));

		JPanel cards = new JPanel(new GridLayout(1, 4, 14, 0));
		cards.setOpaque(false);
		cards.setPreferredSize(new Dimension(600, 100));
		cards.add(Theme.statCard("⌂", "Total Listings", lblListings, lblListingsNote));
		cards.add(Theme.statCard("●", "Registered Clients", lblClients, new JLabel("all agents")));
		cards.add(Theme.statCard("■", "Upcoming Viewings", lblViewings, new JLabel("scheduled from today")));
		cards.add(Theme.statCard("₱", "Completed Deals", lblSales, lblSalesNote));
		page.add(cards, BorderLayout.NORTH);

		JPanel tablePanel = new JPanel(new BorderLayout());
		tablePanel.setOpaque(false);
		tablePanel.add(Theme.sectionTitle(ReportGenerator.AGENT_PERFORMANCE), BorderLayout.NORTH);
		agentModel = new DefaultTableModel() {
			private static final long serialVersionUID = 1L;

			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};
		tablePanel.add(Theme.styleTable(new JTable(agentModel)), BorderLayout.CENTER);
		page.add(tablePanel, BorderLayout.CENTER);
		return page;
	}

	private void loadOverview() {
		ReportGenerator reports = AppContext.reportGenerator();
		ArrayList<Property> properties = reports.getProperties(0); // 0 = all agents
		lblListings.setText(String.valueOf(properties.size()));
		lblListingsNote.setText(reports.countStatus(properties, Property.AVAILABLE) + " available, "
				+ reports.countStatus(properties, Property.RESERVED) + " reserved, "
				+ reports.countStatus(properties, Property.SOLD) + " sold");
		lblClients.setText(String.valueOf(reports.getClients(0).size()));
		lblViewings.setText(String.valueOf(reports.countUpcomingViewings(0)));
		lblSales.setText(String.valueOf(reports.countCompletedDeals(0)));
		lblSalesNote.setText("worth " + BaseService.peso(reports.getTotalSales(0)));

		Report performance = reports.agentPerformance();
		agentModel.setColumnIdentifiers(performance.getColumns());
		agentModel.setRowCount(0);
		for (String[] row : performance.getRows()) {
			agentModel.addRow(row);
		}
	}
}
