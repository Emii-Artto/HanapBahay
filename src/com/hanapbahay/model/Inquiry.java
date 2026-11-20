package com.hanapbahay.model;

/**
 * A client's question about a property. Matches the "inquiries" table.
 * ENCAPSULATION (Module 4) - private fields with getters and setters.
 */
public class Inquiry {

	public static final String NEW = "NEW";
	public static final String RESPONDED = "RESPONDED";
	public static final String CLOSED = "CLOSED";

	private int inquiryId;
	private int propertyId;
	private int clientId;
	private String message;
	private String status;

	public Inquiry(int inquiryId, int propertyId, int clientId, String message, String status) {
		this.inquiryId = inquiryId;
		this.propertyId = propertyId;
		this.clientId = clientId;
		this.message = message;
		this.status = status;
	}

	// Shorter constructor for a NEW inquiry (constructor chaining with this(...))
	public Inquiry(int propertyId, int clientId, String message) {
		this(0, propertyId, clientId, message, NEW);
	}

	// ---- Getters and setters ----
	public int getInquiryId() {
		return inquiryId;
	}

	public void setInquiryId(int inquiryId) {
		this.inquiryId = inquiryId;
	}

	public int getPropertyId() {
		return propertyId;
	}

	public void setPropertyId(int propertyId) {
		this.propertyId = propertyId;
	}

	public int getClientId() {
		return clientId;
	}

	public void setClientId(int clientId) {
		this.clientId = clientId;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
}
