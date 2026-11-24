package com.hanapbahay.dao;

import java.util.ArrayList;

import com.hanapbahay.model.Inquiry;

public interface InquiryDao {

	Inquiry findById(int inquiryId); 

	ArrayList<Inquiry> findAll();

	void add(Inquiry inquiry);

	void update(Inquiry inquiry);
}
