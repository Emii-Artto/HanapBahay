package com.hanapbahay.model;

import java.math.BigDecimal;

/** Shared model: a real estate listing. Matches the "properties" table. */
public class Property {

	public enum Status { AVAILABLE, RESERVED, SOLD }

	private int propertyId;
	private String title;
	private String location;
	private String type;
	private BigDecimal price;
	private Status status = Status.AVAILABLE;
	private int agentId;

	public Property() {
	}

	public Property(int propertyId, String title, String location, String type,
			BigDecimal price, Status status, int agentId) {
		this.propertyId = propertyId;
		this.title = title;
		this.location = location;
		this.type = type;
		this.price = price;
		this.status = status;
		this.agentId = agentId;
	}

	public int getPropertyId() { return propertyId; }
	public void setPropertyId(int propertyId) { this.propertyId = propertyId; }

	public String getTitle() { return title; }
	public void setTitle(String title) { this.title = title; }

	public String getLocation() { return location; }
	public void setLocation(String location) { this.location = location; }

	public String getType() { return type; }
	public void setType(String type) { this.type = type; }

	public BigDecimal getPrice() { return price; }
	public void setPrice(BigDecimal price) { this.price = price; }

	public Status getStatus() { return status; }
	public void setStatus(Status status) { this.status = status; }

	public int getAgentId() { return agentId; }
	public void setAgentId(int agentId) { this.agentId = agentId; }

	@Override
	public String toString() {
		return title + " - " + location;
	}
}
