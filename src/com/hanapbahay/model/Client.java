package com.hanapbahay.model;

/**
 * 5.1.3 Client - a buyer. Buyers do not have accounts; the agent registers them.
 * INHERITANCE: a Client IS-A Person and adds the budget and preferences.
 */
public class Client extends Person {

	public static final String ANY_TYPE = "Any";

	private int clientId;
	private double budget;
	private String preferredLocation;
	private String preferredType; // a property type, or "Any"
	private int agentId;

	public Client(int clientId, String firstName, String lastName, String contactNumber, String email, double budget,
			String preferredLocation, String preferredType, int agentId) {
		super(firstName, lastName, contactNumber, email);
		this.clientId = clientId;
		this.budget = budget;
		this.preferredLocation = preferredLocation;
		this.preferredType = preferredType;
		this.agentId = agentId;
	}

	@Override
	public String getRoleLabel() {
		return "Buyer";
	}

	/**
	 * Find Matches (3.1.3): does this property fit the client's budget, location and type?
	 * Only AVAILABLE properties can match.
	 */
	public boolean matches(Property property) {
		if (!property.isAvailable()) {
			return false;
		}
		if (property.getPrice() > budget) {
			return false;
		}
		if (preferredType != null && !preferredType.isEmpty() && !preferredType.equals(ANY_TYPE)
				&& !preferredType.equals(property.getPropertyType())) {
			return false;
		}
		if (preferredLocation != null && !preferredLocation.trim().isEmpty()) {
			String wanted = preferredLocation.trim().toLowerCase();
			if (!property.getLocation().toLowerCase().contains(wanted)) {
				return false;
			}
		}
		return true;
	}

	public int getClientId() {
		return clientId;
	}

	public void setClientId(int clientId) {
		this.clientId = clientId;
	}

	public double getBudget() {
		return budget;
	}

	// ENCAPSULATION: only a positive budget is accepted
	public void setBudget(double budget) {
		if (budget > 0) {
			this.budget = budget;
		}
	}

	public String getPreferredLocation() {
		return preferredLocation;
	}

	public void setPreferredLocation(String preferredLocation) {
		this.preferredLocation = preferredLocation;
	}

	public String getPreferredType() {
		return preferredType;
	}

	public void setPreferredType(String preferredType) {
		this.preferredType = preferredType;
	}

	public int getAgentId() {
		return agentId;
	}

	public void setAgentId(int agentId) {
		this.agentId = agentId;
	}
}
