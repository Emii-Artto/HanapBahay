package com.hanapbahay.dao;

import java.util.ArrayList;

import com.hanapbahay.model.Viewing;

/**
 * Scheduled property visits. Gets add, update, delete and getAll from CrudDAO and adds
 * the double-booking check and the counts for delete protection.
 * Implemented by dao.mysql.MySqlViewingDao.
 */
public interface ViewingDao extends CrudDAO<Viewing> {

	ArrayList<Viewing> getByAgent(int agentId);

	Viewing findById(int viewingId);

	boolean isSlotTaken(int propertyId, String date, String time);

	int countByProperty(int propertyId);

	int countByClient(int clientId);
}
