package service;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;

import dao.DealDao;
import dao.PropertyDao;
import model.Client;
import model.Deal;
import model.PaymentMethod;
import model.Property;

/**
 * 3.1.5 Deal and Payment Recording, with 5.3.6 Deal Validation.
 * Recording or updating a deal also updates the property status (Reserved or Sold).
 */
public class DealService extends BaseService {

	private DealDao dealDAO;
	private PropertyDao propertyDAO;

	public DealService(DealDao dealDAO, PropertyDao propertyDAO) {
		this.dealDAO = dealDAO;
		this.propertyDAO = propertyDAO;
	}

	public ArrayList<Deal> getDeals(int agentId) {
		if (agentId == 0) {
			return dealDAO.getAll();
		}
		return dealDAO.getByAgent(agentId);
	}

	public ArrayList<PaymentMethod> getPaymentMethods() {
		return dealDAO.getPaymentMethods();
	}

	/** Records a deal as PENDING (property becomes Reserved) or COMPLETED (property becomes Sold). */
	public boolean recordDeal(int agentId, Client client, Property property, PaymentMethod method, String priceText,
			String date, String status) {
		message = "";
		if (client == null || property == null || method == null) {
			message = "A deal needs a client, a property and a payment method.";
			return false;
		}
		double price = readNumber(priceText, "agreed price");
		if (price <= 0) {
			if (message.isEmpty()) {
				message = "The agreed price must be greater than zero.";
			}
			return false;
		}
		try {
			LocalDate.parse(date.trim());
		} catch (DateTimeParseException e) {
			message = "The deal date is not valid. Use a date like 2026-12-05.";
			return false;
		}
		if (!Deal.PENDING.equals(status) && !Deal.COMPLETED.equals(status)) {
			message = "A new deal must be Pending or Completed.";
			return false;
		}
		// 5.3.6: the same property cannot be offered or sold twice
		if (property.isSold() || dealDAO.hasActiveDeal(property.getPropertyId())) {
			message = "\"" + property.getTitle() + "\" already has an active deal or is already sold.";
			return false;
		}

		Deal deal = new Deal(0, property.getPropertyId(), client.getClientId(), agentId, method.getPaymentMethodId(),
				price, date.trim(), status);
		if (!dealDAO.add(deal)) {
			message = "Cannot save the deal. Please check the database connection.";
			return false;
		}
		propertyDAO.updateStatus(property.getPropertyId(), deal.getPropertyStatus());
		message = "Deal recorded. \"" + property.getTitle() + "\" is now "
				+ deal.getPropertyStatus().toLowerCase() + ".";
		return true;
	}

	/** Changes a deal's status and updates the property to match. */
	public boolean updateStatus(int agentId, int dealId, String newStatus) {
		Deal deal = dealDAO.findById(dealId);
		if (deal == null) {
			message = "That deal was not found.";
			return false;
		}
		if (agentId != 0 && deal.getAgentId() != agentId) {
			message = "You can only change your own deals.";
			return false;
		}
		if (newStatus.equals(deal.getStatus())) {
			message = "The deal is already " + newStatus.toLowerCase() + ".";
			return false;
		}
		if (Deal.COMPLETED.equals(deal.getStatus())) {
			message = "A completed deal cannot be changed. The property is already sold.";
			return false;
		}
		// re-opening a cancelled deal: the property must still be free
		if (Deal.CANCELLED.equals(deal.getStatus()) && dealDAO.hasActiveDeal(deal.getPropertyId())) {
			message = "This property already has another active deal.";
			return false;
		}
		if (!deal.setStatus(newStatus)) {
			message = "Unknown deal status.";
			return false;
		}
		if (!dealDAO.update(deal)) {
			message = "Cannot update the deal. Please check the database connection.";
			return false;
		}
		propertyDAO.updateStatus(deal.getPropertyId(), deal.getPropertyStatus());
		message = "Deal marked as " + newStatus.toLowerCase() + ". The property is now "
				+ deal.getPropertyStatus().toLowerCase() + ".";
		return true;
	}
}
