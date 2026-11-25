package com.hanapbahay.service;

import com.hanapbahay.model.Inquiry;
import com.hanapbahay.model.Property;

public class DashboardSummary {

	private boolean allAgents; 

	private int totalListings;
	private int availableListings;
	private int reservedListings;
	private int soldListings;

	private int totalInquiries;
	private int newInquiries;

	private int totalSales;
	private double totalSalesAmount;

	DashboardSummary(boolean allAgents) {
		this.allAgents = allAgents;
	}


	void addListing(String status) {
		totalListings++;
		if (status.equals(Property.AVAILABLE)) {
			availableListings++;
		} else if (status.equals(Property.RESERVED)) {
			reservedListings++;
		} else if (status.equals(Property.SOLD)) {
			soldListings++;
		}
	}

	void addInquiry(String status) {
		totalInquiries++;
		if (status.equals(Inquiry.NEW)) {
			newInquiries++;
		}
	}

	void addSale(double salePrice) {
		totalSales++;
		totalSalesAmount += salePrice;
	}


	public boolean isAllAgents() {
		return allAgents;
	}

	public String getScopeLabel() {
		if (allAgents) {
			return "Showing data for all agents";
		} else {
			return "Showing your own listings, inquiries and sales";
		}
	}

	public int getTotalListings() {
		return totalListings;
	}

	public int getAvailableListings() {
		return availableListings;
	}

	public int getReservedListings() {
		return reservedListings;
	}

	public int getSoldListings() {
		return soldListings;
	}

	public int getTotalInquiries() {
		return totalInquiries;
	}

	public int getNewInquiries() {
		return newInquiries;
	}

	public int getTotalSales() {
		return totalSales;
	}

	public double getTotalSalesAmount() {
		return totalSalesAmount;
	}

	/** Total sales as Philippine pesos, e.g. "PHP 3,500,000.00". */
	public String getTotalSalesAmountText() {
		return String.format("PHP %,.2f", totalSalesAmount);
	}
}
