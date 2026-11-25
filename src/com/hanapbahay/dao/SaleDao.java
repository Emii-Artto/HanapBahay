package com.hanapbahay.dao;

import java.util.ArrayList;

import com.hanapbahay.model.Sale;

public interface SaleDao {

	ArrayList<Sale> findAll();

	void add(Sale sale);
}
