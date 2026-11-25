package com.hanapbahay.service;

public class BaseService {

	protected String message = "";

	public String getMessage() {
		return message;
	}

	protected boolean isBlank(String text) {
		return text == null || text.trim().isEmpty();
	}
}
