package com.hanapbahay.service;

import java.time.LocalDateTime;
import java.util.List;

import com.hanapbahay.dao.InquiryDao;
import com.hanapbahay.dao.PropertyDao;
import com.hanapbahay.model.Inquiry;
import com.hanapbahay.model.Property;

/** Backend: client inquiries about listings. Owner: Backend member. */
public class InquiryService {

	private final InquiryDao inquiryDao;
	private final PropertyDao propertyDao;

	public InquiryService(InquiryDao inquiryDao, PropertyDao propertyDao) {
		this.inquiryDao = inquiryDao;
		this.propertyDao = propertyDao;
	}

	public Inquiry submitInquiry(int propertyId, int clientId, String message) {
		if (message == null || message.trim().isEmpty()) {
			throw new IllegalArgumentException("Please write a message.");
		}
		Property property = propertyDao.findById(propertyId)
				.orElseThrow(() -> new IllegalArgumentException("Property not found."));
		if (property.getStatus() == Property.Status.SOLD) {
			throw new IllegalArgumentException("This property has already been sold.");
		}
		return inquiryDao.insert(new Inquiry(0, propertyId, clientId, message.trim(),
				Inquiry.Status.NEW, LocalDateTime.now()));
	}

	public List<Inquiry> getAllInquiries() {
		return inquiryDao.findAll();
	}

	public List<Inquiry> getInquiriesForProperty(int propertyId) {
		return inquiryDao.findByProperty(propertyId);
	}

	public void markResponded(int inquiryId) {
		inquiryDao.updateStatus(inquiryId, Inquiry.Status.RESPONDED);
	}

	public void close(int inquiryId) {
		inquiryDao.updateStatus(inquiryId, Inquiry.Status.CLOSED);
	}
}
