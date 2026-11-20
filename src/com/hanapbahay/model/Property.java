package com.hanapbahay.model;

/**
 * A real estate listing. Matches the "properties" table.
 * ENCAPSULATION (Module 4) - private fields, public getters and setters, with validation in setPrice().
 */
public class Property {

	// STATIC FINAL constants (Modules 3 and 4) - the allowed status values, shared by all objects
	public static final String AVAILABLE = "AVAILABLE";
	public static final String RESERVED = "RESERVED";
	public static final String SOLD = "SOLD";

	private int propertyId;
	private String title;
	private String location;
	private String type;
	private double price;
	private String status;
	private int agentId;

	// Constructor with all fields (used when reading a listing that already exists)
	public Property(int propertyId, String title, String location, String type,
			double price, String status, int agentId) {
		this.propertyId = propertyId;
		this.title = title;
		this.location = location;
		this.type = type;
		this.price = price;
		this.status = status;
		this.agentId = agentId;
	}

	// Shorter constructor for a NEW listing: no ID yet, always starts AVAILABLE.
	// Uses this(...) to call the constructor above (constructor chaining, Module 3).
	public Property(String title, String location, String type, double price, int agentId) {
		this(0, title, location, type, price, AVAILABLE, agentId);
	}

	public boolean isSold() {
		return status.equals(SOLD);
	}

	// OVERRIDING (Module 5) Object's toString(): a JComboBox calls this to show the listing
	@Override
	public String toString() {
		return title + " (" + location + ")";
	}

	// ---- Getters and setters ----
	public int getPropertyId() {
		return propertyId;
	}

	public void setPropertyId(int propertyId) {
		this.propertyId = propertyId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		if (price > 0) { // validation, like setAge() in the Module 4 slides
			this.price = price;
		} else {
			System.out.println("Invalid price value");
		}
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public int getAgentId() {
		return agentId;
	}

	public void setAgentId(int agentId) {
		this.agentId = agentId;
	}
}
