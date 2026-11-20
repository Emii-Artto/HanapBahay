package com.hanapbahay.dao;

import java.util.ArrayList;

import com.hanapbahay.model.Sale;

/** INTERFACE (Module 5) - what can be done with sales records. */
public interface SaleDao {

	ArrayList<Sale> findAll();

	void add(Sale sale);
}
