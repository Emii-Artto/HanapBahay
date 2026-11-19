package com.hanapbahay.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import com.hanapbahay.dao.PropertyDao;
import com.hanapbahay.dao.SaleDao;
import com.hanapbahay.model.Property;
import com.hanapbahay.model.Sale;

/** Backend: recording sales and sales monitoring numbers. Owner: Backend member. */
public class SaleService {

	private final SaleDao saleDao;
	private final PropertyDao propertyDao;

	public SaleService(SaleDao saleDao, PropertyDao propertyDao) {
		this.saleDao = saleDao;
		this.propertyDao = propertyDao;
	}

	/** Records a sale and marks the property as SOLD. */
	public Sale recordSale(int propertyId, int clientId, BigDecimal salePrice) {
		Property property = propertyDao.findById(propertyId)
				.orElseThrow(() -> new IllegalArgumentException("Property not found."));
		if (property.getStatus() == Property.Status.SOLD) {
			throw new IllegalArgumentException("This property has already been sold.");
		}
		if (salePrice == null || salePrice.signum() <= 0) {
			throw new IllegalArgumentException("Sale price must be greater than zero.");
		}
		Sale sale = saleDao.insert(new Sale(0, propertyId, clientId, property.getAgentId(),
				salePrice, LocalDate.now()));
		property.setStatus(Property.Status.SOLD);
		propertyDao.update(property);
		return sale;
	}

	public List<Sale> getAllSales() {
		return saleDao.findAll();
	}

	public List<Sale> getSalesByAgent(int agentId) {
		return saleDao.findByAgent(agentId);
	}

	public BigDecimal getTotalSalesAmount() {
		return saleDao.findAll().stream()
				.map(Sale::getSalePrice)
				.reduce(BigDecimal.ZERO, BigDecimal::add);
	}

	public int getNumberOfSales() {
		return saleDao.findAll().size();
	}
}
