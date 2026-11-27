package service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;

import dao.ViewingDao;
import model.Client;
import model.Property;
import model.Viewing;

/**
 * 3.1.4 Viewing Appointment Scheduling, with 5.3.5 Viewing Conflict Validation.
 */
public class ViewingService extends BaseService {

	private ViewingDao viewingDAO;

	public ViewingService(ViewingDao viewingDAO) {
		this.viewingDAO = viewingDAO;
	}

	public ArrayList<Viewing> getViewings(int agentId) {
		if (agentId == 0) {
			return viewingDAO.getAll();
		}
		return viewingDAO.getByAgent(agentId);
	}

	/**
	 * Schedules a visit. date is yyyy-mm-dd and time is HH:mm (from the date and time fields).
	 * Blocks a second viewing of the same property at the same date and time.
	 */
	public boolean schedule(int agentId, Client client, Property property, String date, String time) {
		if (client == null || property == null || isBlank(date) || isBlank(time)) {
			message = "Please choose the client, the property, the date and the time.";
			return false;
		}
		LocalDate day;
		try {
			day = LocalDate.parse(date.trim());
			LocalTime.parse(time.trim());
		} catch (DateTimeParseException e) {
			message = "The date or time is not valid. Use a date like 2026-12-05 and a time like 14:30.";
			return false;
		}
		if (day.isBefore(LocalDate.now())) {
			message = "The date " + date + " has already passed. Please choose today or a later date.";
			return false;
		}
		if (property.isSold()) {
			message = "\"" + property.getTitle() + "\" is already sold.";
			return false;
		}
		if (viewingDAO.isSlotTaken(property.getPropertyId(), date.trim(), time.trim())) {
			message = "\"" + property.getTitle() + "\" already has a viewing on " + date + " at " + time
					+ ".\nPlease choose another date or time.";
			return false;
		}
		Viewing viewing = new Viewing(client.getClientId(), property.getPropertyId(), agentId, date.trim(), time.trim());
		if (!viewingDAO.add(viewing)) {
			message = "Cannot save the viewing. Please check the database connection.";
			return false;
		}
		message = "Viewing scheduled: " + client.getFullName() + " will visit \"" + property.getTitle() + "\" on "
				+ date + " at " + time + ".";
		return true;
	}

	/** Marks a viewing as Completed or Cancelled. */
	public boolean updateStatus(int agentId, int viewingId, String newStatus) {
		Viewing viewing = viewingDAO.findById(viewingId);
		if (viewing == null) {
			message = "That viewing was not found.";
			return false;
		}
		if (agentId != 0 && viewing.getAgentId() != agentId) {
			message = "You can only change your own viewings.";
			return false;
		}
		if (!Viewing.SCHEDULED.equals(viewing.getStatus())) {
			message = "This viewing is already " + viewing.getStatus().toLowerCase() + ".";
			return false;
		}
		if (!viewing.setStatus(newStatus)) {
			message = "Unknown viewing status.";
			return false;
		}
		if (!viewingDAO.update(viewing)) {
			message = "Cannot update the viewing. Please check the database connection.";
			return false;
		}
		message = "Viewing marked as " + newStatus.toLowerCase() + ".";
		return true;
	}
}
