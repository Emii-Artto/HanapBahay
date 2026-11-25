package com.hanapbahay.dao;

import java.util.ArrayList;

import com.hanapbahay.model.Property;
import com.hanapbahay.model.PropertyPhoto;

/**
 * Property listings and their photos. Gets add, update, delete and getAll from CrudDAO
 * and adds its own search methods. Implemented by dao.mysql.MySqlPropertyDao.
 */
public interface PropertyDao extends CrudDAO<Property> {

	ArrayList<Property> getByAgent(int agentId);

	Property findById(int propertyId);

	// METHOD OVERLOADING (4.3): the same name, different parameters
	ArrayList<Property> search(String keyword);

	ArrayList<Property> search(double minPrice, double maxPrice);

	boolean updateStatus(int propertyId, String status);

	boolean addPhoto(PropertyPhoto photo);

	boolean deletePhotos(int propertyId);
}
