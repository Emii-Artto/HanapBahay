package com.hanapbahay.service;

import java.time.LocalDate;
import java.util.ArrayList;

import com.hanapbahay.dao.PropertyDao;
import com.hanapbahay.dao.SaleDao;
import com.hanapbahay.model.Property;
import com.hanapbahay.model.Sale;

/**
 * Backend: recording sales and sales monitoring numbers. Owner: Backend member.
 */
public class SaleService extends BaseService {

	private SaleDao saleDao;
	private PropertyDao propertyDao;

	public SaleService(SaleDao saleDao, PropertyDao propertyDao) {
		this.saleDao = saleDao;
		this.propertyDao = propertyDao;
	}

	/** Records a sale and marks the property as SOLD. The price comes from a text field. */
	public boolean recordSale(int propertyId, int clientId, String priceText) {
		Property property = propertyDao.findById(propertyId);
		if (property == null) {
			message = "Please choose a property.";
			return false;
		}
		if (property.isSold()) {
			message = "This property has already been sold.";
			return false;
		}
		if (clientId <= 0) {
			message = "Please choose a client.";
			return false;
		}
		double salePrice = toNumber(priceText);
		if (salePrice <= 0) {
			message = "Sale price must be a number greater than zero.";
			return false;
		}

		String today = LocalDate.now().toString(); // e.g. "2026-11-20"
		saleDao.add(new Sale(0, propertyId, clientId, property.getAgentId(), salePrice, today));

		property.setStatus(Property.SOLD);
		propertyDao.update(property);
		message = "Sale recorded. " + property.getTitle() + " is now SOLD.";
		return true;
	}

	public ArrayList<Sale> getAllSales() {
		return saleDao.findAll();
	}

	/** Only the sales of one agent (an agent's own sales report). */
	public ArrayList<Sale> getSalesByAgent(int agentId) {
		ArrayList<Sale> result = new ArrayList<Sale>();
		for (Sale sale : saleDao.findAll()) {
			if (sale.getAgentId() == agentId) {
				result.add(sale);
			}
		}
		return result;
	}

	public int getNumberOfSales() {
		return saleDao.findAll().size();
	}

	// ---- METHOD OVERLOADING (Module 5, compile-time polymorphism) ----

	/** Total amount of ALL sales (Admin's report) */
	public double getTotalSales() {
		double total = 0;
		for (Sale sale : saleDao.findAll()) {
			total = total + sale.getSalePrice();
		}
		return total;
	}

	/** Total amount of sales made by ONE agent (Agent's report) */
	public double getTotalSales(int agentId) {
		double total = 0;
		for (Sale sale : saleDao.findAll()) {
			if (sale.getAgentId() == agentId) {
				total = total + sale.getSalePrice();
			}
		}
		return total;
	}
}
