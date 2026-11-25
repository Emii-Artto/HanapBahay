package dash;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.util.ArrayList;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import com.hanapbahay.AppContext;
import com.hanapbahay.model.Agent;
import com.hanapbahay.model.Property;
import com.hanapbahay.service.BaseService;
import com.hanapbahay.service.Report;
import com.hanapbahay.service.ReportGenerator;

/** The Admin's overview: totals for all agents and the Agent Performance table. */
public class AdminOverviewPanel extends JPanel implements DashboardSection {

	private static final long serialVersionUID = 1L;
	private StatCard cardListings;
	private StatCard cardClients;
	private StatCard cardViewings;
	private StatCard cardDeals;
	private AppTable table;

	public AdminOverviewPanel() {
		setBackground(Theme.DARK);
		setBorder(new EmptyBorder(18, 24, 18, 24));
		setLayout(new BorderLayout(0, 16));

		JPanel pnlCards = new JPanel();
		pnlCards.setOpaque(false);
		add(pnlCards, BorderLayout.NORTH);
		pnlCards.setLayout(new GridLayout(1, 4, 14, 0));

		cardListings = new StatCard();
		cardListings.setIcon("⌂");
		cardListings.setTitle("Total Listings");
		pnlCards.add(cardListings);

		cardClients = new StatCard();
		cardClients.setIcon("●");
		cardClients.setTitle("Registered Clients");
		cardClients.setNote("all agents");
		pnlCards.add(cardClients);

		cardViewings = new StatCard();
		cardViewings.setIcon("■");
		cardViewings.setTitle("Upcoming Viewings");
		cardViewings.setNote("scheduled from today");
		pnlCards.add(cardViewings);

		cardDeals = new StatCard();
		cardDeals.setIcon("₱");
		cardDeals.setTitle("Completed Deals");
		pnlCards.add(cardDeals);

		JPanel pnlTable = new JPanel();
		pnlTable.setOpaque(false);
		add(pnlTable, BorderLayout.CENTER);
		pnlTable.setLayout(new BorderLayout(0, 8));

		JLabel lblPerformance = new JLabel("Agent Performance");
		lblPerformance.setFont(Theme.HEADING);
		lblPerformance.setForeground(Theme.GOLD);
		pnlTable.add(lblPerformance, BorderLayout.NORTH);

		AppScrollPane scrollTable = new AppScrollPane();
		pnlTable.add(scrollTable, BorderLayout.CENTER);

		table = new AppTable();
		table.setModel(new ReadOnlyTableModel(new String[] { "Agent", "Contact", "Listings", "Clients", "Viewings",
				"Completed Deals", "Total Sales" }));
		scrollTable.setViewportView(table);
	}

	@Override
	public void setAgent(Agent agent) {
		// the admin sees all agents, so nothing to remember
	}

	@Override
	public String getPageTitle() {
		return "Admin Overview";
	}

	@Override
	public void refresh() {
		ReportGenerator reports = AppContext.reportGenerator();
		ArrayList<Property> properties = reports.getProperties(0); // 0 = all agents
		cardListings.setValue(String.valueOf(properties.size()));
		cardListings.setNote(reports.countStatus(properties, Property.AVAILABLE) + " available, "
				+ reports.countStatus(properties, Property.RESERVED) + " reserved, "
				+ reports.countStatus(properties, Property.SOLD) + " sold");
		cardClients.setValue(String.valueOf(reports.getClients(0).size()));
		cardViewings.setValue(String.valueOf(reports.countUpcomingViewings(0)));
		cardDeals.setValue(String.valueOf(reports.countCompletedDeals(0)));
		cardDeals.setNote("worth " + BaseService.peso(reports.getTotalSales(0)));

		Report performance = reports.agentPerformance();
		ReadOnlyTableModel model = new ReadOnlyTableModel(performance.getColumns());
		for (String[] row : performance.getRows()) {
			model.addRow(row);
		}
		table.setModel(model);
	}
}
