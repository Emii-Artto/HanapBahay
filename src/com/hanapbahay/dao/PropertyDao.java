package com.hanapbahay.dao;

import java.util.ArrayList;

import com.hanapbahay.model.Property;

public interface PropertyDao {

	Property findById(int propertyId); 

	ArrayList<Property> findAll();

	void add(Property property);

	void update(Property property);

	void delete(int propertyId);
}
