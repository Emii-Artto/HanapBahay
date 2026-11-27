package model;

/**
 * 5.1.10 Viewing - a scheduled property visit.
 * Status can be Scheduled, Completed or Cancelled.
 */
public class Viewing {

	public static final String SCHEDULED = "SCHEDULED";
	public static final String COMPLETED = "COMPLETED";
	public static final String CANCELLED = "CANCELLED";

	private int viewingId;
	private int clientId;
	private int propertyId;
	private int agentId;
	private String viewingDate; // yyyy-mm-dd, e.g. 2026-12-05
	private String viewingTime; // HH:mm, e.g. 14:30
	private String status;

	public Viewing(int viewingId, int clientId, int propertyId, int agentId, String viewingDate, String viewingTime,
			String status) {
		this.viewingId = viewingId;
		this.clientId = clientId;
		this.propertyId = propertyId;
		this.agentId = agentId;
		this.viewingDate = viewingDate;
		this.viewingTime = viewingTime;
		this.status = status;
	}

	// A new viewing always starts as SCHEDULED (constructor chaining)
	public Viewing(int clientId, int propertyId, int agentId, String viewingDate, String viewingTime) {
		this(0, clientId, propertyId, agentId, viewingDate, viewingTime, SCHEDULED);
	}

	// Same property, same date and same time? (used for the double-booking check)
	public boolean isSameSlot(int otherPropertyId, String otherDate, String otherTime) {
		return propertyId == otherPropertyId && viewingDate.equals(otherDate) && viewingTime.equals(otherTime);
	}

	public boolean isActive() {
		return !CANCELLED.equals(status);
	}

	public int getViewingId() {
		return viewingId;
	}

	public void setViewingId(int viewingId) {
		this.viewingId = viewingId;
	}

	public int getClientId() {
		return clientId;
	}

	public int getPropertyId() {
		return propertyId;
	}

	public int getAgentId() {
		return agentId;
	}

	public String getViewingDate() {
		return viewingDate;
	}

	public String getViewingTime() {
		return viewingTime;
	}

	public String getStatus() {
		return status;
	}

	// ENCAPSULATION: only the three allowed statuses
	public boolean setStatus(String status) {
		if (!SCHEDULED.equals(status) && !COMPLETED.equals(status) && !CANCELLED.equals(status)) {
			return false;
		}
		this.status = status;
		return true;
	}
}
