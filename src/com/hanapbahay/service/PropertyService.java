package com.hanapbahay.service;

import java.util.ArrayList;

import com.hanapbahay.dao.PropertyDao;
import com.hanapbahay.model.Property;
import com.hanapbahay.model.User;

/**
 * Business rules for property listings. Owner: Backend member.
 *
 * The UI calls these methods; only PropertyDao talks to MySQL.
 * Methods return true/false (or a list). If something fails, getMessage() says why.
 */
public class PropertyService extends BaseService {

	private PropertyDao propertyDao;

	public PropertyService(PropertyDao propertyDao) {
		this.propertyDao = propertyDao;
	}

	// The listings this user may manage: an Agent gets only their own, an Admin gets all
	public ArrayList<Property> getListings(User user) {
		ArrayList<Property> listings = new ArrayList<Property>();
		if (user == null) {
			message = "No user is logged in.";
			return listings;
		}
		for (Property property : propertyDao.findAll()) {
			if (user.canViewAllSales() || property.getAgentId() == user.getUserId()) {
				listings.add(property);
			}
		}
		message = listings.size() + " listings";
		return listings;
	}

	// Adds a new listing for this agent. priceText is what was typed, e.g. "3,500,000"
	public boolean addProperty(User agent, String title, String location, String type, String priceText,
			String status) {
		if (agent == null) {
			message = "No user is logged in.";
			return false;
		}
		double price = checkFields(title, location, type, priceText, status);
		if (price <= 0) {
			return false; // message was set by checkFields
		}

		Property property = new Property(0, title.trim(), location.trim(), type, price,
				status.toUpperCase(), agent.getUserId());
		propertyDao.add(property);

		// the DAO puts the new AUTO_INCREMENT id in the object; still 0 means it was not saved
		if (property.getPropertyId() == 0) {
			message = "Cannot save the property. Please check the database.";
			return false;
		}
		message = "Property added successfully!\nProperty ID: P" + String.format("%03d", property.getPropertyId());
		return true;
	}

	// Changes an existing listing. Agents can only change their own.
	public boolean updateProperty(User agent, int propertyId, String title, String location, String type,
			String priceText, String status) {
		Property property = findOwnProperty(agent, propertyId);
		if (property == null) {
			return false;
		}
		double price = checkFields(title, location, type, priceText, status);
		if (price <= 0) {
			return false;
		}

		property.setTitle(title.trim());
		property.setLocation(location.trim());
		property.setType(type);
		property.setPrice(price);
		property.setStatus(status.toUpperCase());
		propertyDao.update(property);

		message = "Property updated successfully.";
		return true;
	}

	// Deletes a listing. Agents can only delete their own.
	public boolean deleteProperty(User agent, int propertyId) {
		Property property = findOwnProperty(agent, propertyId);
		if (property == null) {
			return false;
		}
		propertyDao.delete(propertyId);

		// MySQL refuses the delete if a client inquiry or a sale uses this property (foreign key)
		if (propertyDao.findById(propertyId) != null) {
			message = "Cannot delete this property because it already has client inquiries or a sale.\n"
					+ "Change its status to Sold or Reserved instead.";
			return false;
		}
		message = "Property record P" + String.format("%03d", propertyId) + " was deleted.";
		return true;
	}

	// Returns the listing if it exists and belongs to this agent (Admins may use any)
	private Property findOwnProperty(User agent, int propertyId) {
		if (agent == null) {
			message = "No user is logged in.";
			return null;
		}
		Property property = propertyDao.findById(propertyId);
		if (property == null) {
			message = "Property not found. It may have been deleted.";
			return null;
		}
		if (!agent.canViewAllSales() && property.getAgentId() != agent.getUserId()) {
			message = "You can only change your own listings.";
			return null;
		}
		return property;
	}

	// Checks the form. Returns the price if everything is valid, or 0 if not (with a message).
	private double checkFields(String title, String location, String type, String priceText, String status) {
		if (isBlank(title) || isBlank(location) || isBlank(type) || isBlank(priceText) || isBlank(status)) {
			message = "Complete all fields.";
			return 0;
		}

		double price;
		try {
			price = Double.parseDouble(priceText.replace(",", "").replace("PHP", "").trim());
		} catch (NumberFormatException e) {
			message = "Please enter a valid numeric price.";
			return 0;
		}
		if (price <= 0) {
			message = "Enter a price greater than zero.";
			return 0;
		}

		String upper = status.toUpperCase();
		if (!upper.equals(Property.AVAILABLE) && !upper.equals(Property.RESERVED) && !upper.equals(Property.SOLD)) {
			message = "Status must be Available, Reserved or Sold.";
			return 0;
		}
		return price;
	}
}
