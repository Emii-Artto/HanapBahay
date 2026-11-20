package com.hanapbahay.dao.sample;

import java.util.ArrayList;

import com.hanapbahay.dao.InquiryDao;
import com.hanapbahay.model.Inquiry;

/** Sample inquiries kept in memory until MySQL is ready. Implements InquiryDao (Module 5). */
public class SampleInquiryDao implements InquiryDao {

	private ArrayList<Inquiry> inquiries = new ArrayList<Inquiry>();
	private int nextId = 1;

	@Override
	public Inquiry findById(int inquiryId) {
		for (Inquiry inquiry : inquiries) {
			if (inquiry.getInquiryId() == inquiryId) {
				return inquiry;
			}
		}
		return null;
	}

	@Override
	public ArrayList<Inquiry> findAll() {
		return inquiries;
	}

	@Override
	public void add(Inquiry inquiry) {
		inquiry.setInquiryId(nextId);
		nextId++;
		inquiries.add(inquiry);
	}

	@Override
	public void update(Inquiry inquiry) {
		// Nothing to do in memory. The MySQL version will run an UPDATE statement here.
	}
}
