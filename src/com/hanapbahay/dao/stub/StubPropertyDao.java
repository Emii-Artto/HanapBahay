package com.hanapbahay.dao.stub;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.hanapbahay.dao.PropertyDao;
import com.hanapbahay.model.Property;
import com.hanapbahay.model.Property.Status;

/** Fake in-memory data so the app runs before MySQL is ready. Data resets on every run. */
public class StubPropertyDao implements PropertyDao {

	private final List<Property> properties = new ArrayList<>();
	private int nextId = 1;

	public StubPropertyDao() {
		insert(new Property(0, "2BR Townhouse", "Quezon City", "House",
				new BigDecimal("3500000"), Status.AVAILABLE, 2));
		insert(new Property(0, "Studio Condo Unit", "Makati City", "Condo",
				new BigDecimal("2800000"), Status.AVAILABLE, 2));
	}

	@Override
	public Optional<Property> findById(int propertyId) {
		return properties.stream().filter(p -> p.getPropertyId() == propertyId).findFirst();
	}

	@Override
	public List<Property> findAll() {
		return new ArrayList<>(properties);
	}

	@Override
	public List<Property> findByStatus(Status status) {
		return properties.stream().filter(p -> p.getStatus() == status).collect(Collectors.toList());
	}

	@Override
	public Property insert(Property property) {
		property.setPropertyId(nextId++);
		properties.add(property);
		return property;
	}

	@Override
	public void update(Property property) {
		for (int i = 0; i < properties.size(); i++) {
			if (properties.get(i).getPropertyId() == property.getPropertyId()) {
				properties.set(i, property);
				return;
			}
		}
	}

	@Override
	public void delete(int propertyId) {
		properties.removeIf(p -> p.getPropertyId() == propertyId);
	}
}
