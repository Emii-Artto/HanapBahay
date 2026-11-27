package dao;

import java.util.ArrayList;

import model.Viewing;

/**
 * Scheduled property visits. Gets add, update, delete and getAll from CrudDAO and adds
 * the double-booking check and the counts for delete protection.
 * Implemented by mysql.MySqlViewingDao.
 */
public interface ViewingDao extends CrudDAO<Viewing> {

	ArrayList<Viewing> getByAgent(int agentId);

	Viewing findById(int viewingId);

	boolean isSlotTaken(int propertyId, String date, String time);

	int countByProperty(int propertyId);

	int countByClient(int clientId);
}
