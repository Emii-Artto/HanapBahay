package com.hanapbahay.dao.sample;

import java.util.ArrayList;

import com.hanapbahay.dao.PropertyDao;
import com.hanapbahay.model.Property;

/** Sample listings kept in memory until MySQL is ready. Implements PropertyDao (Module 5). */
public class SamplePropertyDao implements PropertyDao {

	private ArrayList<Property> properties = new ArrayList<Property>();
	private int nextId = 1;

	public SamplePropertyDao() {
		add(new Property("2BR Townhouse", "Quezon City", "House", 3500000, 2));
		add(new Property("Studio Condo Unit", "Makati City", "Condo", 2800000, 2));
	}

	@Override
	public Property findById(int propertyId) {
		for (Property property : properties) {
			if (property.getPropertyId() == propertyId) {
				return property;
			}
		}
		return null;
	}

	@Override
	public ArrayList<Property> findAll() {
		return properties;
	}

	@Override
	public void add(Property property) {
		property.setPropertyId(nextId);
		nextId++;
		properties.add(property);
	}

	@Override
	public void update(Property property) {
		// Nothing to do: the object in the list is the same object that was changed.
		// The MySQL version will run an UPDATE statement here.
	}

	@Override
	public void delete(int propertyId) {
		Property property = findById(propertyId);
		if (property != null) {
			properties.remove(property);
		}
	}
}
