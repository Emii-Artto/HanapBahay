package com.hanapbahay.service;

/**
 * Parent class of all services. Owner: Backend member.
 *
 * INHERITANCE (Module 4) - UserService, PropertyService, InquiryService and SaleService
 * all "extends BaseService", so they reuse this code instead of copying it four times.
 *
 * How the UI uses it:
 *   boolean ok = propertyService.addListing(...);
 *   JOptionPane.showMessageDialog(this, propertyService.getMessage());
 */
public class BaseService {

	// PROTECTED (Module 3) - subclasses can set it directly; other classes must use getMessage()
	protected String message = "";

	// The result of the last action, e.g. "Listing added." or "Price must be greater than zero."
	public String getMessage() {
		return message;
	}

	// Shared helper: true if the text is null or only spaces
	protected boolean isBlank(String text) {
		return text == null || text.trim().isEmpty();
	}

	/**
	 * Shared helper: turns text typed in a form (e.g. "3500000" or "3,500,000") into a number.
	 *
	 * EXCEPTION HANDLING (required by the project matrix): if the text is not a number
	 * (e.g. "abc"), Double.parseDouble() throws a NumberFormatException. We catch it and
	 * return -1, so the service can show a message instead of the program crashing.
	 */
	protected double toNumber(String text) {
		if (isBlank(text)) {
			return -1;
		}
		try {
			return Double.parseDouble(text.trim().replace(",", ""));
		} catch (NumberFormatException e) {
			return -1;
		}
	}
}
