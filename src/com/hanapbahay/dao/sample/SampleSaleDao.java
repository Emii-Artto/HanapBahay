package com.hanapbahay.dao.sample;

import java.util.ArrayList;

import com.hanapbahay.dao.SaleDao;
import com.hanapbahay.model.Sale;

/** Sample sales kept in memory until MySQL is ready. Implements SaleDao (Module 5). */
public class SampleSaleDao implements SaleDao {

	private ArrayList<Sale> sales = new ArrayList<Sale>();
	private int nextId = 1;

	@Override
	public ArrayList<Sale> findAll() {
		return sales;
	}

	@Override
	public void add(Sale sale) {
		sale.setSaleId(nextId);
		nextId++;
		sales.add(sale);
	}
}
