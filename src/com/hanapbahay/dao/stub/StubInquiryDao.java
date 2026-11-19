package com.hanapbahay.dao.stub;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.hanapbahay.dao.InquiryDao;
import com.hanapbahay.model.Inquiry;

/** Fake in-memory data so the app runs before MySQL is ready. Data resets on every run. */
public class StubInquiryDao implements InquiryDao {

	private final List<Inquiry> inquiries = new ArrayList<>();
	private int nextId = 1;

	@Override
	public Optional<Inquiry> findById(int inquiryId) {
		return inquiries.stream().filter(i -> i.getInquiryId() == inquiryId).findFirst();
	}

	@Override
	public List<Inquiry> findAll() {
		return new ArrayList<>(inquiries);
	}

	@Override
	public List<Inquiry> findByProperty(int propertyId) {
		return inquiries.stream().filter(i -> i.getPropertyId() == propertyId).collect(Collectors.toList());
	}

	@Override
	public Inquiry insert(Inquiry inquiry) {
		inquiry.setInquiryId(nextId++);
		inquiries.add(inquiry);
		return inquiry;
	}

	@Override
	public void updateStatus(int inquiryId, Inquiry.Status status) {
		findById(inquiryId).ifPresent(i -> i.setStatus(status));
	}
}
