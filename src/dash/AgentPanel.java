package dash;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.util.ArrayList;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;

import com.hanapbahay.AppContext;
import com.hanapbahay.model.Agent;
import com.hanapbahay.model.Client;
import com.hanapbahay.model.Property;
import com.hanapbahay.model.User;
import com.hanapbahay.model.Viewing;
import com.hanapbahay.service.BaseService;
import com.hanapbahay.service.ReportGenerator;

/**
 * The "Agent" section (first page after login): the agent's profile, the numbers for
 * their work, and their next viewings.
 */
public class AgentPanel extends JPanel implements DashboardSection {

	private static final long serialVersionUID = 1L;
	private Agent agent;
	private User user;
	private JLabel lblName;
	private JLabel lblRole;
	private JLabel lblUsername;
	private JLabel lblContact;
	private JLabel lblEmail;
	private StatCard cardListings;
	private StatCard cardClients;
	private StatCard cardViewings;
	private StatCard cardDeals;
	private AppTable tblViewings;
	private ReadOnlyTableModel viewingsModel;

	public AgentPanel() {
		setBackground(Theme.DARK);
		setBorder(new EmptyBorder(18, 24, 18, 24));
		setLayout(new BorderLayout(0, 14));

		JPanel pnlTop = new JPanel();
		pnlTop.setOpaque(false);
		add(pnlTop, BorderLayout.NORTH);
		pnlTop.setLayout(new BorderLayout(0, 14));

		// ---- profile card ----
		CardPanel pnlProfile = new CardPanel();
		pnlTop.add(pnlProfile, BorderLayout.NORTH);
		pnlProfile.setLayout(new BorderLayout(0, 0));

		JPanel pnlProfileText = new JPanel();
		pnlProfileText.setOpaque(false);
		pnlProfile.add(pnlProfileText, BorderLayout.CENTER);
		pnlProfileText.setLayout(new GridLayout(5, 1, 0, 2));

		lblName = new JLabel("Agent Name");
		lblName.setFont(Theme.TITLE);
		lblName.setForeground(Theme.WHITE);
		pnlProfileText.add(lblName);

		lblRole = new JLabel("Real Estate Agent");
		lblRole.setFont(Theme.BODY_BOLD);
		lblRole.setForeground(Theme.GOLD);
		pnlProfileText.add(lblRole);

		lblUsername = new JLabel("Username:");
		lblUsername.setFont(Theme.BODY);
		lblUsername.setForeground(Theme.MUTED);
		pnlProfileText.add(lblUsername);

		lblContact = new JLabel("Contact number:");
		lblContact.setFont(Theme.BODY);
		lblContact.setForeground(Theme.MUTED);
		pnlProfileText.add(lblContact);

		lblEmail = new JLabel("Email:");
		lblEmail.setFont(Theme.BODY);
		lblEmail.setForeground(Theme.MUTED);
		pnlProfileText.add(lblEmail);

		JPanel pnlProfileButtons = new JPanel();
		pnlProfileButtons.setOpaque(false);
		pnlProfile.add(pnlProfileButtons, BorderLayout.EAST);
		pnlProfileButtons.setLayout(new FlowLayout(FlowLayout.RIGHT, 0, 0));

		AppButton btnEditProfile = new AppButton("Edit Profile");
		btnEditProfile.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				editProfile();
			}
		});
		pnlProfileButtons.add(btnEditProfile);

		// ---- number cards ----
		JPanel pnlCards = new JPanel();
		pnlCards.setOpaque(false);
		pnlTop.add(pnlCards, BorderLayout.CENTER);
		pnlCards.setLayout(new GridLayout(1, 4, 14, 0));

		cardListings = new StatCard();
		cardListings.setIconType(NavIcon.PROPERTIES);
		cardListings.setTitle("My Listings");
		pnlCards.add(cardListings);

		cardClients = new StatCard();
		cardClients.setIconType(NavIcon.CLIENTS);
		cardClients.setTitle("My Clients");
		cardClients.setNote("registered buyers");
		pnlCards.add(cardClients);

		cardViewings = new StatCard();
		cardViewings.setIconType(NavIcon.VIEWING);
		cardViewings.setTitle("Upcoming Viewings");
		cardViewings.setNote("scheduled from today");
		pnlCards.add(cardViewings);

		cardDeals = new StatCard();
		cardDeals.setIconType(NavIcon.DEALS);
		cardDeals.setTitle("Completed Deals");
		pnlCards.add(cardDeals);

		// ---- next viewings ----
		JPanel pnlViewings = new JPanel();
		pnlViewings.setOpaque(false);
		add(pnlViewings, BorderLayout.CENTER);
		pnlViewings.setLayout(new BorderLayout(0, 8));

		JLabel lblNextViewings = new JLabel("Next Viewings");
		lblNextViewings.setFont(Theme.HEADING);
		lblNextViewings.setForeground(Theme.GOLD);
		pnlViewings.add(lblNextViewings, BorderLayout.NORTH);

		AppScrollPane scrollViewings = new AppScrollPane();
		pnlViewings.add(scrollViewings, BorderLayout.CENTER);

		tblViewings = new AppTable();
		viewingsModel = new ReadOnlyTableModel(new String[] { "Date", "Time", "Client", "Property" });
		tblViewings.setModel(viewingsModel);
		scrollViewings.setViewportView(tblViewings);
	}

	public void setUser(User user) {
		this.user = user;
	}

	@Override
	public void setAgent(Agent agent) {
		this.agent = agent;
	}

	@Override
	public String getPageTitle() {
		return "Agent";
	}

	@Override
	public void refresh() {
		lblName.setText(agent.getFullName());
		lblRole.setText(agent.getRoleLabel()); // Person's abstract method, Agent's version
		lblUsername.setText("Username: " + (user == null ? "-" : user.getUsername()));
		lblContact.setText("Contact number: " + agent.getContactNumber());
		lblEmail.setText("Email: " + (agent.getEmail() == null || agent.getEmail().isEmpty() ? "-" : agent.getEmail()));

		ReportGenerator reports = AppContext.reportGenerator();
		int id = agent.getAgentId();
		ArrayList<Property> properties = reports.getProperties(id);
		cardListings.setValue(String.valueOf(properties.size()));
		cardListings.setNote(reports.countStatus(properties, Property.AVAILABLE) + " available, "
				+ reports.countStatus(properties, Property.RESERVED) + " reserved, "
				+ reports.countStatus(properties, Property.SOLD) + " sold");
		ArrayList<Client> clients = reports.getClients(id);
		cardClients.setValue(String.valueOf(clients.size()));
		cardViewings.setValue(String.valueOf(reports.countUpcomingViewings(id)));
		cardDeals.setValue(String.valueOf(reports.countCompletedDeals(id)));
		cardDeals.setNote("worth " + BaseService.peso(reports.getTotalSales(id)));

		// scheduled viewings from today on, earliest first (the list comes newest first)
		viewingsModel.setRowCount(0);
		ArrayList<Viewing> viewings = AppContext.viewingService().getViewings(id);
		String today = LocalDate.now().toString();
		for (int i = viewings.size() - 1; i >= 0; i--) {
			Viewing v = viewings.get(i);
			if (Viewing.SCHEDULED.equals(v.getStatus()) && v.getViewingDate().compareTo(today) >= 0) {
				viewingsModel.addRow(new Object[] { v.getViewingDate(), v.getViewingTime(),
						ReportGenerator.clientName(clients, v.getClientId()),
						ReportGenerator.propertyTitle(properties, v.getPropertyId()) });
			}
		}
		if (viewingsModel.getRowCount() == 0) {
			viewingsModel.addRow(new Object[] { "No upcoming viewings", "", "", "" });
		}
	}

	private void editProfile() {
		ProfileDialog dialog = new ProfileDialog();
		dialog.setAgent(agent);
		dialog.setLocationRelativeTo(SwingUtilities.getWindowAncestor(this));
		dialog.setVisible(true);
		if (dialog.wasSaved()) {
			refresh();
		}
	}
}
