package com.hanapbahay.model;

import java.time.LocalDate;

public class Inquiry {

	public static final String NEW = "NEW";
	public static final String RESPONDED = "RESPONDED";
	public static final String CLOSED = "CLOSED";

	private int inquiryId;
	private int propertyId;    
	private String clientName;
	private String clientContact;
	private String message;    
	private int agentId;      
	private String status;
	private String inquiryDate; 

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

	public Inquiry(int propertyId, String clientName, String clientContact, String message, int agentId) {
		this(0, propertyId, clientName, clientContact, message, agentId, NEW, LocalDate.now().toString());
	}

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
