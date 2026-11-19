package com.hanapbahay.model;

import java.time.LocalDateTime;

/** Shared model: a client's inquiry about a property. Matches the "inquiries" table. */
public class Inquiry {

	public enum Status { NEW, RESPONDED, CLOSED }

	private int inquiryId;
	private int propertyId;
	private int clientId;
	private String message;
	private Status status = Status.NEW;
	private LocalDateTime createdAt;

	public Inquiry() {
	}

	public Inquiry(int inquiryId, int propertyId, int clientId, String message,
			Status status, LocalDateTime createdAt) {
		this.inquiryId = inquiryId;
		this.propertyId = propertyId;
		this.clientId = clientId;
		this.message = message;
		this.status = status;
		this.createdAt = createdAt;
	}

	public int getInquiryId() { return inquiryId; }
	public void setInquiryId(int inquiryId) { this.inquiryId = inquiryId; }

	public int getPropertyId() { return propertyId; }
	public void setPropertyId(int propertyId) { this.propertyId = propertyId; }

	public int getClientId() { return clientId; }
	public void setClientId(int clientId) { this.clientId = clientId; }

	public String getMessage() { return message; }
	public void setMessage(String message) { this.message = message; }

	public Status getStatus() { return status; }
	public void setStatus(Status status) { this.status = status; }

	public LocalDateTime getCreatedAt() { return createdAt; }
	public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
