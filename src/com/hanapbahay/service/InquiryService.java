package com.hanapbahay.service;

import java.util.ArrayList;

import com.hanapbahay.dao.InquiryDao;
import com.hanapbahay.dao.PropertyDao;
import com.hanapbahay.model.Inquiry;
import com.hanapbahay.model.Property;

/**
 * Backend: client inquiries about listings. Owner: Backend member.
 */
public class InquiryService extends BaseService {

	private InquiryDao inquiryDao;
	private PropertyDao propertyDao;

	public InquiryService(InquiryDao inquiryDao, PropertyDao propertyDao) {
		this.inquiryDao = inquiryDao;
		this.propertyDao = propertyDao;
	}

	/** A client asks about a property. Returns true if it worked. */
	public boolean submitInquiry(int propertyId, int clientId, String text) {
		if (isBlank(text)) {
			message = "Please write a message.";
			return false;
		}
		Property property = propertyDao.findById(propertyId);
		if (property == null) {
			message = "Property not found.";
			return false;
		}
		if (property.isSold()) {
			message = "This property has already been sold.";
			return false;
		}

		inquiryDao.add(new Inquiry(propertyId, clientId, text.trim()));
		message = "Inquiry sent. An agent will contact you.";
		return true;
	}

	public ArrayList<Inquiry> getAllInquiries() {
		return inquiryDao.findAll();
	}

	public ArrayList<Inquiry> getInquiriesForProperty(int propertyId) {
		ArrayList<Inquiry> result = new ArrayList<Inquiry>();
		for (Inquiry inquiry : inquiryDao.findAll()) {
			if (inquiry.getPropertyId() == propertyId) {
				result.add(inquiry);
			}
		}
		return result;
	}

	/** Filter: only inquiries with this status (NEW, RESPONDED or CLOSED). */
	public ArrayList<Inquiry> getInquiriesByStatus(String status) {
		ArrayList<Inquiry> result = new ArrayList<Inquiry>();
		for (Inquiry inquiry : inquiryDao.findAll()) {
			if (inquiry.getStatus().equals(status)) {
				result.add(inquiry);
			}
		}
		return result;
	}

	/** Only the inquiries sent by one client (the client's "My inquiries" screen). */
	public ArrayList<Inquiry> getInquiriesByClient(int clientId) {
		ArrayList<Inquiry> result = new ArrayList<Inquiry>();
		for (Inquiry inquiry : inquiryDao.findAll()) {
			if (inquiry.getClientId() == clientId) {
				result.add(inquiry);
			}
		}
		return result;
	}

	/** OVERLOADING (Module 5): one client's inquiries with a given status, e.g. RESPONDED. */
	public ArrayList<Inquiry> getInquiriesByClient(int clientId, String status) {
		ArrayList<Inquiry> result = new ArrayList<Inquiry>();
		for (Inquiry inquiry : getInquiriesByClient(clientId)) {
			if (inquiry.getStatus().equals(status)) {
				result.add(inquiry);
			}
		}
		return result;
	}

	public boolean markResponded(int inquiryId) {
		return changeStatus(inquiryId, Inquiry.RESPONDED);
	}

	public boolean closeInquiry(int inquiryId) {
		return changeStatus(inquiryId, Inquiry.CLOSED);
	}

	// PRIVATE helper (Module 3) - only this class can call it
	private boolean changeStatus(int inquiryId, String newStatus) {
		Inquiry inquiry = inquiryDao.findById(inquiryId);
		if (inquiry == null) {
			message = "Inquiry not found.";
			return false;
		}
		if (inquiry.getStatus().equals(Inquiry.CLOSED)) {
			message = "This inquiry is already closed.";
			return false;
		}
		inquiry.setStatus(newStatus);
		inquiryDao.update(inquiry);
		message = "Inquiry marked as " + newStatus + ".";
		return true;
	}
}
