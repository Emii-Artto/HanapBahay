package service;

import java.util.ArrayList;

import dao.ClientDao;
import dao.DealDao;
import dao.PropertyDao;
import dao.ViewingDao;
import model.Client;
import model.Property;

/**
 * 3.1.3 Client Management: register and manage buyers, search them, and Find Matches.
 */
public class ClientService extends BaseService {

	private ClientDao clientDAO;
	private PropertyDao propertyDAO;
	private ViewingDao viewingDAO;
	private DealDao dealDAO;

	public ClientService(ClientDao clientDAO, PropertyDao propertyDAO, ViewingDao viewingDAO, DealDao dealDAO) {
		this.clientDAO = clientDAO;
		this.propertyDAO = propertyDAO;
		this.viewingDAO = viewingDAO;
		this.dealDAO = dealDAO;
	}

	public ArrayList<Client> getClients(int agentId) {
		if (agentId == 0) {
			return clientDAO.getAll();
		}
		return clientDAO.getByAgent(agentId);
	}

	/** 5.4.5 Client Search by name or contact number. */
	public ArrayList<Client> search(int agentId, String keyword) {
		if (isBlank(keyword)) {
			return getClients(agentId);
		}
		return clientDAO.search(keyword.trim(), agentId);
	}

	public Client findById(int clientId) {
		return clientDAO.findById(clientId);
	}

	public boolean addClient(int agentId, String firstName, String lastName, String contactNumber, String email,
			String budgetText, String preferredLocation, String preferredType) {
		Client client = buildClient(0, agentId, firstName, lastName, contactNumber, email, budgetText,
				preferredLocation, preferredType);
		if (client == null) {
			return false;
		}
		if (!clientDAO.add(client)) {
			message = "Cannot save the client. Please check the database connection.";
			return false;
		}
		message = "Client " + client.getFullName() + " registered.";
		return true;
	}

	public boolean updateClient(int agentId, int clientId, String firstName, String lastName, String contactNumber,
			String email, String budgetText, String preferredLocation, String preferredType) {
		Client old = findOwnClient(agentId, clientId);
		if (old == null) {
			return false;
		}
		Client client = buildClient(clientId, old.getAgentId(), firstName, lastName, contactNumber, email, budgetText,
				preferredLocation, preferredType);
		if (client == null) {
			return false;
		}
		if (!clientDAO.update(client)) {
			message = "Cannot update the client. Please check the database connection.";
			return false;
		}
		message = "Client " + client.getFullName() + " updated.";
		return true;
	}

	/** A client who already has viewings or deals cannot be deleted (keeps the history). */
	public boolean deleteClient(int agentId, int clientId) {
		Client client = findOwnClient(agentId, clientId);
		if (client == null) {
			return false;
		}
		int viewings = viewingDAO.countByClient(clientId);
		int deals = dealDAO.countByClient(clientId);
		if (viewings > 0 || deals > 0) {
			message = "Cannot delete " + client.getFullName() + " because this client has " + viewings
					+ " viewing(s) and " + deals + " deal(s).";
			return false;
		}
		if (!clientDAO.delete(clientId)) {
			message = "Cannot delete the client. Please check the database connection.";
			return false;
		}
		message = "Client " + client.getFullName() + " was deleted.";
		return true;
	}

	/**
	 * 5.4.6 Find Matches: this agent's AVAILABLE properties that fit the client's
	 * budget, preferred location and preferred type (Client.matches() decides).
	 */
	public ArrayList<Property> findMatches(Client client) {
		ArrayList<Property> matches = new ArrayList<Property>();
		for (Property property : propertyDAO.getByAgent(client.getAgentId())) {
			if (client.matches(property)) {
				matches.add(property);
			}
		}
		if (matches.isEmpty()) {
			message = "No available property fits " + client.getFullName() + "'s budget ("
					+ peso(client.getBudget()) + ") and preferences yet.";
		} else {
			message = matches.size() + " " + (matches.size() == 1 ? "property fits " : "properties fit ")
					+ client.getFullName() + ".";
		}
		return matches;
	}

	private Client findOwnClient(int agentId, int clientId) {
		Client client = clientDAO.findById(clientId);
		if (client == null) {
			message = "That client was not found. It may have been deleted.";
			return null;
		}
		if (agentId != 0 && client.getAgentId() != agentId) {
			message = "You can only change your own clients.";
			return null;
		}
		return client;
	}

	// 5.3.1 required fields and 5.3.2 budget validation
	private Client buildClient(int clientId, int agentId, String firstName, String lastName, String contactNumber,
			String email, String budgetText, String preferredLocation, String preferredType) {
		message = "";
		if (isBlank(firstName) || isBlank(lastName)) {
			message = "Please enter the client's first and last name.";
			return null;
		}
		String name = firstName.trim() + " " + lastName.trim();
		if (isBlank(contactNumber)) {
			message = "Please enter " + name + "'s contact number.";
			return null;
		}
		if (countDigits(contactNumber) < 7) {
			message = "The contact number of " + name + " looks too short. Example: 0917 123 4567";
			return null;
		}
		if (!isBlank(email) && (email.indexOf('@') < 1 || email.trim().contains(" "))) {
			message = "The email of " + name + " is not valid. Example: name@email.com";
			return null;
		}
		double budget = readNumber(budgetText, "budget");
		if (budget <= 0) {
			if (message.isEmpty()) {
				message = name + "'s budget must be greater than zero.";
			}
			return null;
		}
		String type = isBlank(preferredType) ? Client.ANY_TYPE : preferredType;
		return new Client(clientId, firstName.trim(), lastName.trim(), contactNumber.trim(),
				email == null ? "" : email.trim(), budget, preferredLocation == null ? "" : preferredLocation.trim(),
				type, agentId);
	}

	private int countDigits(String text) {
		int digits = 0;
		for (int i = 0; i < text.length(); i++) {
			if (Character.isDigit(text.charAt(i))) {
				digits++;
			}
		}
		return digits;
	}
}
