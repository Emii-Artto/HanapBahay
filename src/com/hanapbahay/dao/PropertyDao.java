package com.hanapbahay.dao;

import java.util.List;
import java.util.Optional;

import com.hanapbahay.model.Property;

/** Contract between Backend and Database. Database member implements this with MySQL. */
public interface PropertyDao {

	Optional<Property> findById(int propertyId);

	List<Property> findAll();

	List<Property> findByStatus(Property.Status status);

	/** Saves a new listing and returns it with its generated propertyId. */
	Property insert(Property property);

	void update(Property property);

	void delete(int propertyId);
}
