package com.hanapbahay.dao;

import java.util.ArrayList;

import com.hanapbahay.model.Property;

/** INTERFACE (Module 5) - what can be done with property listings. */
public interface PropertyDao {

	Property findById(int propertyId); // returns null if not found

	ArrayList<Property> findAll();

	void add(Property property);

	void update(Property property);

	void delete(int propertyId);
}
