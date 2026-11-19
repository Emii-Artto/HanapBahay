package com.hanapbahay.dao;

import java.util.List;
import java.util.Optional;

import com.hanapbahay.model.Inquiry;

/** Contract between Backend and Database. Database member implements this with MySQL. */
public interface InquiryDao {

	Optional<Inquiry> findById(int inquiryId);

	List<Inquiry> findAll();

	List<Inquiry> findByProperty(int propertyId);

	/** Saves a new inquiry and returns it with its generated inquiryId. */
	Inquiry insert(Inquiry inquiry);

	void updateStatus(int inquiryId, Inquiry.Status status);
}
