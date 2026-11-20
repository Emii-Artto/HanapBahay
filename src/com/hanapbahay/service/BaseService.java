package com.hanapbahay.service;

/**
 * Parent class of all services. Owner: Backend member.
 *
 * INHERITANCE (Module 4) - every service "extends BaseService", so they reuse this code
 * instead of copying it. Week 1: UserService. Next: PropertyService, InquiryService, SaleService.
 *
 * How the UI uses it:
 *   User user = userService.login(...);
 *   JOptionPane.showMessageDialog(this, userService.getMessage());
 */
public class BaseService {

	// PROTECTED (Module 3) - subclasses can set it directly; other classes must use getMessage()
	protected String message = "";

	// The result of the last action, e.g. "Wrong username or password."
	public String getMessage() {
		return message;
	}

	// Shared helper: true if the text is null or only spaces
	protected boolean isBlank(String text) {
		return text == null || text.trim().isEmpty();
	}
}
