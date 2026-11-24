package com.hanapbahay.model;

//since wala acount mga client
public class Sale {

	private int saleId;
	private int propertyId;
	private String buyerName;
	private String buyerContact;
	private int agentId;
	private double salePrice;
	private String saleDate; // format: yyyy-mm-dd, e.g. "2026-11-20"

	public Sale(int saleId, int propertyId, String buyerName, String buyerContact, int agentId,
			double salePrice, String saleDate) {
		this.saleId = saleId;
		this.propertyId = propertyId;
		this.buyerName = buyerName;
		this.buyerContact = buyerContact;
		this.agentId = agentId;
		this.salePrice = salePrice;
		this.saleDate = saleDate;
	}

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

	public String getBuyerName() {
		return buyerName;
	}

	public void setBuyerName(String buyerName) {
		this.buyerName = buyerName;
	}

	public String getBuyerContact() {
		return buyerContact;
	}

	public void setBuyerContact(String buyerContact) {
		this.buyerContact = buyerContact;
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
