package com.hanapbahay.model;

/**
 * A completed property sale. Matches the "sales" table.
 * ENCAPSULATION (Module 4) - private fields with getters and setters.
 */
public class Sale {

	private int saleId;
	private int propertyId;
	private int clientId;
	private int agentId;
	private double salePrice;
	private String saleDate; // format: yyyy-mm-dd, e.g. "2026-11-20"

	public Sale(int saleId, int propertyId, int clientId, int agentId, double salePrice, String saleDate) {
		this.saleId = saleId;
		this.propertyId = propertyId;
		this.clientId = clientId;
		this.agentId = agentId;
		this.salePrice = salePrice;
		this.saleDate = saleDate;
	}

	// ---- Getters and setters ----
	public int getSaleId() {
		return saleId;
	}

	public void setSaleId(int saleId) {
		this.saleId = saleId;
	}

	public int getPropertyId() {
		return propertyId;
	}

	public void setPropertyId(int propertyId) {
		this.propertyId = propertyId;
	}

	public int getClientId() {
		return clientId;
	}

	public void setClientId(int clientId) {
		this.clientId = clientId;
	}

	public int getAgentId() {
		return agentId;
	}

	public void setAgentId(int agentId) {
		this.agentId = agentId;
	}

	public double getSalePrice() {
		return salePrice;
	}

	public void setSalePrice(double salePrice) {
		this.salePrice = salePrice;
	}

	public String getSaleDate() {
		return saleDate;
	}

	public void setSaleDate(String saleDate) {
		this.saleDate = saleDate;
	}
}
