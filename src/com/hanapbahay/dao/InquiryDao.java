package com.hanapbahay.dao;

import java.util.ArrayList;

import com.hanapbahay.model.Inquiry;

/** INTERFACE (Module 5) - what can be done with inquiries. */
public interface InquiryDao {

	Inquiry findById(int inquiryId); // returns null if not found

	ArrayList<Inquiry> findAll();

	void add(Inquiry inquiry);

	void update(Inquiry inquiry);
}
