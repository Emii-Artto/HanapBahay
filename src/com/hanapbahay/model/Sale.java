package com.hanapbahay.model;

import java.math.BigDecimal;
import java.time.LocalDate;

/** Shared model: a completed property sale. Matches the "sales" table. */
public class Sale {

	private int saleId;
	private int propertyId;
	private int clientId;
	private int agentId;
	private BigDecimal salePrice;
	private LocalDate saleDate;

	public Sale() {
	}

	public Sale(int saleId, int propertyId, int clientId, int agentId,
			BigDecimal salePrice, LocalDate saleDate) {
		this.saleId = saleId;
		this.propertyId = propertyId;
		this.clientId = clientId;
		this.agentId = agentId;
		this.salePrice = salePrice;
		this.saleDate = saleDate;
	}

	public int getSaleId() { return saleId; }
	public void setSaleId(int saleId) { this.saleId = saleId; }

	public int getPropertyId() { return propertyId; }
	public void setPropertyId(int propertyId) { this.propertyId = propertyId; }

	public int getClientId() { return clientId; }
	public void setClientId(int clientId) { this.clientId = clientId; }

	public int getAgentId() { return agentId; }
	public void setAgentId(int agentId) { this.agentId = agentId; }

	public BigDecimal getSalePrice() { return salePrice; }
	public void setSalePrice(BigDecimal salePrice) { this.salePrice = salePrice; }

	public LocalDate getSaleDate() { return saleDate; }
	public void setSaleDate(LocalDate saleDate) { this.saleDate = saleDate; }
}
