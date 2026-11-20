package com.hanapbahay.service;

import java.util.ArrayList;

import com.hanapbahay.dao.PropertyDao;
import com.hanapbahay.model.Property;

/**
 * Backend: property listing rules. Owner: Backend member.
 */
public class PropertyService extends BaseService {

	private PropertyDao propertyDao;

	public PropertyService(PropertyDao propertyDao) {
		this.propertyDao = propertyDao;
	}

	public ArrayList<Property> getAllListings() {
		return propertyDao.findAll();
	}

	/** Finds one listing by ID. Returns null if not found. */
	public Property getListing(int propertyId) {
		return propertyDao.findById(propertyId);
	}

	/** Filter: only listings with this status (AVAILABLE, RESERVED or SOLD). */
	public ArrayList<Property> getListingsByStatus(String status) {
		ArrayList<Property> result = new ArrayList<Property>();
		for (Property property : propertyDao.findAll()) {
			if (property.getStatus().equals(status)) {
				result.add(property);
			}
		}
		return result;
	}

	public ArrayList<Property> getAvailableListings() {
		return getListingsByStatus(Property.AVAILABLE);
	}

	/** Listings that can still be sold (AVAILABLE or RESERVED). */
	public ArrayList<Property> getUnsoldListings() {
		ArrayList<Property> result = new ArrayList<Property>();
		for (Property property : propertyDao.findAll()) {
			if (!property.isSold()) {
				result.add(property);
			}
		}
		return result;
	}

	// ---- METHOD OVERLOADING (Module 5, compile-time polymorphism) ----
	// Same name "searchListings", different parameters. Java picks the right one.

	/** Search by a word in the title or location, e.g. searchListings("condo") or searchListings("Quezon") */
	public ArrayList<Property> searchListings(String keyword) {
		String word = keyword.trim().toLowerCase();
		ArrayList<Property> result = new ArrayList<Property>();
		for (Property property : propertyDao.findAll()) {
			if (property.getTitle().toLowerCase().contains(word)
					|| property.getLocation().toLowerCase().contains(word)) {
				result.add(property);
			}
		}
		return result;
	}

	/**
	 * Search by price range, e.g. searchListings("2000000", "3000000").
	 * Returns null if the prices typed are not valid (the reason is in getMessage()).
	 */
	public ArrayList<Property> searchListings(String minPriceText, String maxPriceText) {
		double minPrice = toNumber(minPriceText);
		double maxPrice = toNumber(maxPriceText);
		if (minPrice < 0 || maxPrice < 0) {
			message = "Enter a valid minimum and maximum price.";
			return null;
		}
		if (minPrice > maxPrice) {
			message = "The minimum price can't be higher than the maximum price.";
			return null;
		}

		ArrayList<Property> result = new ArrayList<Property>();
		for (Property property : propertyDao.findAll()) {
			if (property.getPrice() >= minPrice && property.getPrice() <= maxPrice) {
				result.add(property);
			}
		}
		return result;
	}

	/** Adds a new listing. The price comes from a text field. Returns true if it worked. */
	public boolean addListing(String title, String location, String type, String priceText, int agentId) {
		if (isBlank(title) || isBlank(location) || isBlank(type)) {
			message = "Title, location and type are required.";
			return false;
		}
		double price = toNumber(priceText);
		if (price <= 0) {
			message = "Price must be a number greater than zero.";
			return false;
		}

		propertyDao.add(new Property(title.trim(), location.trim(), type.trim(), price, agentId));
		message = "Listing added.";
		return true;
	}

	/** Saves changes to a listing. Sold listings can no longer be edited. */
	public boolean updateListing(int propertyId, String title, String location, String type,
			String priceText, String status) {
		Property property = propertyDao.findById(propertyId);
		if (property == null) {
			message = "Property not found.";
			return false;
		}
		if (property.isSold()) {
			message = "Sold properties can no longer be edited.";
			return false;
		}
		if (isBlank(title) || isBlank(location) || isBlank(type)) {
			message = "Title, location and type are required.";
			return false;
		}
		double price = toNumber(priceText);
		if (price <= 0) {
			message = "Price must be a number greater than zero.";
			return false;
		}

		property.setTitle(title.trim());
		property.setLocation(location.trim());
		property.setType(type.trim());
		property.setPrice(price);
		property.setStatus(status);
		propertyDao.update(property);
		message = "Listing updated.";
		return true;
	}

	/** Removes a listing. Sold listings are kept for the sales records. */
	public boolean removeListing(int propertyId) {
		Property property = propertyDao.findById(propertyId);
		if (property == null) {
			message = "Property not found.";
			return false;
		}
		if (property.isSold()) {
			message = "Sold properties cannot be removed.";
			return false;
		}
		propertyDao.delete(propertyId);
		message = "Listing removed.";
		return true;
	}
}
