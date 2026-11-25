package com.hanapbahay.service;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.util.ArrayList;

import com.hanapbahay.dao.ClientDao;
import com.hanapbahay.dao.DealDao;
import com.hanapbahay.dao.PropertyDao;
import com.hanapbahay.dao.UserDao;
import com.hanapbahay.dao.ViewingDao;
import com.hanapbahay.model.Agent;
import com.hanapbahay.model.Client;
import com.hanapbahay.model.Deal;
import com.hanapbahay.model.PaymentMethod;
import com.hanapbahay.model.Property;
import com.hanapbahay.model.Viewing;

/**
 * 5.1.15 ReportGenerator - builds the summaries of 5.5 Reports / Output.
 * It gets the records through the DAO classes and turns them into readable Report objects.
 * An agentId of 0 means "all agents" (used by the Admin).
 */
public class ReportGenerator extends BaseService {

	// the report names shown in the Reports panel drop-down
	public static final String PROPERTY_LISTINGS = "Property Listings";
	public static final String STATUS_SUMMARY = "Property Status Summary";
	public static final String CLIENT_RECORDS = "Client Records";
	public static final String VIEWING_SCHEDULE = "Viewing Schedule";
	public static final String DEAL_RECORDS = "Deal Records";
	public static final String SALES_SUMMARY = "Sales Summary";
	public static final String PAYMENT_SUMMARY = "Payment Method Summary";
	public static final String AGENT_PERFORMANCE = "Agent Performance";

	public static final String[] AGENT_REPORTS = { PROPERTY_LISTINGS, STATUS_SUMMARY, CLIENT_RECORDS,
			VIEWING_SCHEDULE, DEAL_RECORDS, SALES_SUMMARY, PAYMENT_SUMMARY };
	public static final String[] ADMIN_REPORTS = { AGENT_PERFORMANCE, PROPERTY_LISTINGS, STATUS_SUMMARY,
			CLIENT_RECORDS, VIEWING_SCHEDULE, DEAL_RECORDS, SALES_SUMMARY, PAYMENT_SUMMARY };

	private PropertyDao propertyDAO;
	private ClientDao clientDAO;
	private ViewingDao viewingDAO;
	private DealDao dealDAO;
	private UserDao userDAO;

	public ReportGenerator(PropertyDao propertyDAO, ClientDao clientDAO, ViewingDao viewingDAO, DealDao dealDAO,
			UserDao userDAO) {
		this.propertyDAO = propertyDAO;
		this.clientDAO = clientDAO;
		this.viewingDAO = viewingDAO;
		this.dealDAO = dealDAO;
		this.userDAO = userDAO;
	}

	/** Builds the report with this name. */
	public Report generate(String reportName, int agentId) {
		if (STATUS_SUMMARY.equals(reportName)) {
			return statusSummary(agentId);
		}
		if (CLIENT_RECORDS.equals(reportName)) {
			return clientRecords(agentId);
		}
		if (VIEWING_SCHEDULE.equals(reportName)) {
			return viewingSchedule(agentId);
		}
		if (DEAL_RECORDS.equals(reportName)) {
			return dealRecords(agentId);
		}
		if (SALES_SUMMARY.equals(reportName)) {
			return salesSummary(agentId);
		}
		if (PAYMENT_SUMMARY.equals(reportName)) {
			return paymentMethodSummary(agentId);
		}
		if (AGENT_PERFORMANCE.equals(reportName)) {
			return agentPerformance();
		}
		return propertyListings(agentId);
	}

	// ---------------- 5.5.1 Property Listings ----------------
	public Report propertyListings(int agentId) {
		Report report = new Report(PROPERTY_LISTINGS,
				new String[] { "ID", "Title", "Type", "Subtype", "Location", "Price", "Lot Area (sqm)", "Status" });
		ArrayList<Property> properties = getProperties(agentId);
		for (Property p : properties) {
			report.addRow(new String[] { "P" + String.format("%03d", p.getPropertyId()), p.getTitle(),
					p.getPropertyType(), p.getSubtype(), p.getLocation(), peso(p.getPrice()),
					String.format("%,.0f", p.getLotArea()), titleCase(p.getStatus()) });
		}
		report.setSummary(properties.size() + " registered " + (properties.size() == 1 ? "property" : "properties"));
		return report;
	}

	// ---------------- 5.5.2 Property Status Summary ----------------
	public Report statusSummary(int agentId) {
		Report report = new Report(STATUS_SUMMARY, new String[] { "Status", "Number of Properties" });
		ArrayList<Property> properties = getProperties(agentId);
		String[] statuses = { Property.AVAILABLE, Property.RESERVED, Property.SOLD };
		for (String status : statuses) {
			report.addRow(new String[] { titleCase(status), String.valueOf(countStatus(properties, status)) });
		}
		report.setSummary("Total: " + properties.size() + " properties");
		return report;
	}

	public int countStatus(ArrayList<Property> properties, String status) {
		int count = 0;
		for (Property p : properties) {
			if (status.equals(p.getStatus())) {
				count++;
			}
		}
		return count;
	}

	// ---------------- 5.5.3 Client Records ----------------
	public Report clientRecords(int agentId) {
		Report report = new Report(CLIENT_RECORDS, new String[] { "Name", "Contact Number", "Email", "Budget",
				"Preferred Location", "Preferred Type" });
		ArrayList<Client> clients = getClients(agentId);
		for (Client c : clients) {
			report.addRow(new String[] { c.getFullName(), c.getContactNumber(), c.getEmail(), peso(c.getBudget()),
					c.getPreferredLocation(), c.getPreferredType() });
		}
		report.setSummary("Total: " + clients.size() + " registered " + (clients.size() == 1 ? "client" : "clients"));
		return report;
	}

	// ---------------- 5.5.4 Viewing Schedule ----------------
	public Report viewingSchedule(int agentId) {
		Report report = new Report(VIEWING_SCHEDULE,
				new String[] { "Date", "Time", "Client", "Property", "Status", "Upcoming / Past" });
		ArrayList<Viewing> viewings = getViewings(agentId);
		ArrayList<Client> clients = getClients(agentId);
		ArrayList<Property> properties = getProperties(agentId);
		String today = LocalDate.now().toString();
		int upcoming = 0;
		for (Viewing v : viewings) {
			boolean isUpcoming = v.getViewingDate().compareTo(today) >= 0 && Viewing.SCHEDULED.equals(v.getStatus());
			if (isUpcoming) {
				upcoming++;
			}
			report.addRow(new String[] { v.getViewingDate(), v.getViewingTime(), clientName(clients, v.getClientId()),
					propertyTitle(properties, v.getPropertyId()), titleCase(v.getStatus()),
					isUpcoming ? "Upcoming" : "Past" });
		}
		report.setSummary(viewings.size() + " viewings, " + upcoming + " upcoming");
		return report;
	}

	// ---------------- 5.5.5 Deal Records ----------------
	public Report dealRecords(int agentId) {
		Report report = new Report(DEAL_RECORDS,
				new String[] { "Date", "Client", "Property", "Payment Method", "Agreed Price", "Status" });
		ArrayList<Deal> deals = getDeals(agentId);
		ArrayList<Client> clients = getClients(agentId);
		ArrayList<Property> properties = getProperties(agentId);
		ArrayList<PaymentMethod> methods = dealDAO.getPaymentMethods();
		for (Deal d : deals) {
			report.addRow(new String[] { d.getDealDate(), clientName(clients, d.getClientId()),
					propertyTitle(properties, d.getPropertyId()), methodName(methods, d.getPaymentMethodId()),
					peso(d.getAgreedPrice()), titleCase(d.getStatus()) });
		}
		report.setSummary(deals.size() + " recorded " + (deals.size() == 1 ? "deal" : "deals"));
		return report;
	}

	// ---------------- 5.5.6 Sales Summary ----------------
	public Report salesSummary(int agentId) {
		Report report = new Report(SALES_SUMMARY, new String[] { "Deal Status", "Number of Deals", "Total Value" });
		ArrayList<Deal> deals = getDeals(agentId);
		String[] statuses = { Deal.COMPLETED, Deal.PENDING, Deal.CANCELLED };
		for (String status : statuses) {
			int count = 0;
			double total = 0;
			for (Deal d : deals) {
				if (status.equals(d.getStatus())) {
					count++;
					total += d.getAgreedPrice();
				}
			}
			report.addRow(new String[] { titleCase(status), String.valueOf(count), peso(total) });
		}
		report.setSummary(countCompletedDeals(agentId) + " completed deals worth " + peso(getTotalSales(agentId)));
		return report;
	}

	// ---------------- 5.5.7 Payment Method Summary ----------------
	public Report paymentMethodSummary(int agentId) {
		Report report = new Report(PAYMENT_SUMMARY,
				new String[] { "Payment Method", "Number of Deals", "Total Value" });
		ArrayList<Deal> deals = getDeals(agentId);
		for (PaymentMethod method : dealDAO.getPaymentMethods()) {
			int count = 0;
			double total = 0;
			for (Deal d : deals) {
				if (d.getPaymentMethodId() == method.getPaymentMethodId() && d.isActive()) {
					count++;
					total += d.getAgreedPrice();
				}
			}
			report.addRow(new String[] { method.getMethodName(), String.valueOf(count), peso(total) });
		}
		report.setSummary("Pending and completed deals only (cancelled deals are not counted)");
		return report;
	}

	// ---------------- Admin: Agent Performance ----------------
	public Report agentPerformance() {
		Report report = new Report(AGENT_PERFORMANCE, new String[] { "Agent", "Contact", "Listings", "Clients",
				"Viewings", "Completed Deals", "Total Sales" });
		ArrayList<Agent> agents = userDAO.getAllAgents();
		for (Agent a : agents) {
			int id = a.getAgentId();
			report.addRow(new String[] { a.getFullName(), a.getContactNumber(),
					String.valueOf(propertyDAO.getByAgent(id).size()), String.valueOf(clientDAO.getByAgent(id).size()),
					String.valueOf(viewingDAO.getByAgent(id).size()), String.valueOf(countCompletedDeals(id)),
					peso(getTotalSales(id)) });
		}
		report.setSummary(agents.size() + " agents");
		return report;
	}

	// ---------------- numbers for the dashboard cards ----------------

	public int countUpcomingViewings(int agentId) {
		String today = LocalDate.now().toString();
		int count = 0;
		for (Viewing v : getViewings(agentId)) {
			if (Viewing.SCHEDULED.equals(v.getStatus()) && v.getViewingDate().compareTo(today) >= 0) {
				count++;
			}
		}
		return count;
	}

	public int countCompletedDeals(int agentId) {
		int count = 0;
		for (Deal d : getDeals(agentId)) {
			if (d.isCompleted()) {
				count++;
			}
		}
		return count;
	}

	public double getTotalSales(int agentId) {
		double total = 0;
		for (Deal d : getDeals(agentId)) {
			if (d.isCompleted()) {
				total += d.getAgreedPrice();
			}
		}
		return total;
	}

	public ArrayList<Property> getProperties(int agentId) {
		return agentId == 0 ? propertyDAO.getAll() : propertyDAO.getByAgent(agentId);
	}

	public ArrayList<Client> getClients(int agentId) {
		return agentId == 0 ? clientDAO.getAll() : clientDAO.getByAgent(agentId);
	}

	private ArrayList<Viewing> getViewings(int agentId) {
		return agentId == 0 ? viewingDAO.getAll() : viewingDAO.getByAgent(agentId);
	}

	private ArrayList<Deal> getDeals(int agentId) {
		return agentId == 0 ? dealDAO.getAll() : dealDAO.getByAgent(agentId);
	}

	// ---------------- CSV export (5.5) ----------------

	/** Saves the report as a CSV file that opens in Excel. EXCEPTION HANDLING for file errors. */
	public boolean exportCsv(Report report, File file) {
		PrintWriter out = null;
		try {
			out = new PrintWriter(new FileWriter(file));
			out.println(csvLine(new String[] { "HanapBahay - " + report.getTitle() }));
			out.println(csvLine(new String[] { "Generated on " + LocalDate.now() }));
			out.println();
			out.println(csvLine(report.getColumns()));
			for (String[] row : report.getRows()) {
				out.println(csvLine(row));
			}
			out.println();
			out.println(csvLine(new String[] { report.getSummary() }));
			message = "Report saved to " + file.getAbsolutePath();
			return true;
		} catch (IOException e) {
			message = "Cannot save the file: " + e.getMessage();
			return false;
		} finally {
			if (out != null) {
				out.close();
			}
		}
	}

	// puts quotes around each value so commas inside a value (like PHP 3,500,000.00) stay together
	private String csvLine(String[] values) {
		StringBuilder line = new StringBuilder();
		for (int i = 0; i < values.length; i++) {
			if (i > 0) {
				line.append(',');
			}
			String value = values[i] == null ? "" : values[i];
			line.append('"').append(value.replace("\"", "\"\"")).append('"');
		}
		return line.toString();
	}

	// ---------------- small helpers to show names instead of ID numbers ----------------

	public static String titleCase(String upper) {
		if (upper == null || upper.isEmpty()) {
			return "";
		}
		return upper.substring(0, 1) + upper.substring(1).toLowerCase();
	}

	public static String clientName(ArrayList<Client> clients, int clientId) {
		for (Client c : clients) {
			if (c.getClientId() == clientId) {
				return c.getFullName();
			}
		}
		return "Client #" + clientId;
	}

	public static String propertyTitle(ArrayList<Property> properties, int propertyId) {
		for (Property p : properties) {
			if (p.getPropertyId() == propertyId) {
				return p.getTitle();
			}
		}
		return "Property #" + propertyId;
	}

	public static String methodName(ArrayList<PaymentMethod> methods, int methodId) {
		for (PaymentMethod m : methods) {
			if (m.getPaymentMethodId() == methodId) {
				return m.getMethodName();
			}
		}
		return "Method #" + methodId;
	}
}
