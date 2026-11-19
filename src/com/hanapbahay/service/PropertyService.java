package com.hanapbahay.service;

import java.math.BigDecimal;
import java.util.List;

import com.hanapbahay.dao.PropertyDao;
import com.hanapbahay.model.Property;

/** Backend: property listing rules. Owner: Backend member. */
public class PropertyService {

	private final PropertyDao propertyDao;

	public PropertyService(PropertyDao propertyDao) {
		this.propertyDao = propertyDao;
	}

	public List<Property> getAllListings() {
		return propertyDao.findAll();
	}

	public List<Property> getAvailableListings() {
		return propertyDao.findByStatus(Property.Status.AVAILABLE);
	}

	public Property addListing(String title, String location, String type, BigDecimal price, int agentId) {
		if (isBlank(title) || isBlank(location) || isBlank(type)) {
			throw new IllegalArgumentException("Title, location and type are required.");
		}
		if (price == null || price.signum() <= 0) {
			throw new IllegalArgumentException("Price must be greater than zero.");
		}
		return propertyDao.insert(new Property(0, title.trim(), location.trim(), type.trim(),
				price, Property.Status.AVAILABLE, agentId));
	}

	public void updateListing(Property property) {
		if (property.getStatus() == Property.Status.SOLD) {
			throw new IllegalArgumentException("Sold properties can no longer be edited.");
		}
		propertyDao.update(property);
	}

	public void removeListing(int propertyId) {
		propertyDao.delete(propertyId);
	}

	private static boolean isBlank(String s) {
		return s == null || s.trim().isEmpty();
	}
}
