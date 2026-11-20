package com.hanapbahay.model;

import java.time.LocalDate;

/**
 * A unit a client selected in Client Mode ("I'm interested"). Matches the "inquiries" table.
 * The agent types the client's name when starting Client Mode, so the client has no account.
 *
 * ENCAPSULATION (Module 4) - private fields with getters and setters.
 */
public class Inquiry {

	// STATIC FINAL constants (Modules 3 and 4) - the allowed status values
	public static final String NEW = "NEW";
	public static final String RESPONDED = "RESPONDED";
	public static final String CLOSED = "CLOSED";

	private int inquiryId;
	private int propertyId;     // the unit the client selected
	private String clientName;
	private String clientContact;
	private String message;     // optional note, e.g. "Can I visit on Saturday?"
	private int agentId;        // the agent who opened Client Mode
	private String status;
	private String inquiryDate; // format: yyyy-mm-dd, e.g. "2026-11-20"

	// Constructor with all fields (used when reading an inquiry that already exists)
	public Inquiry(int inquiryId, int propertyId, String clientName, String clientContact, String message,
			int agentId, String status, String inquiryDate) {
		this.inquiryId = inquiryId;
		this.propertyId = propertyId;
		this.clientName = clientName;
		this.clientContact = clientContact;
		this.message = message;
		this.agentId = agentId;
		this.status = status;
		this.inquiryDate = inquiryDate;
	}

	// Shorter constructor for a NEW selection made today.
	// Uses this(...) to call the constructor above (constructor chaining, Module 3).
	public Inquiry(int propertyId, String clientName, String clientContact, String message, int agentId) {
		this(0, propertyId, clientName, clientContact, message, agentId, NEW, LocalDate.now().toString());
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

	public String getClientName() {
		return clientName;
	}

	public void setClientName(String clientName) {
		this.clientName = clientName;
	}

	public String getClientContact() {
		return clientContact;
	}

	public void setClientContact(String clientContact) {
		this.clientContact = clientContact;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public int getAgentId() {
		return agentId;
	}

	public void setAgentId(int agentId) {
		this.agentId = agentId;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getInquiryDate() {
		return inquiryDate;
	}

	public void setInquiryDate(String inquiryDate) {
		this.inquiryDate = inquiryDate;
	}
}
