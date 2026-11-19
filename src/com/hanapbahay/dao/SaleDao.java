package com.hanapbahay.dao;

import java.util.List;

import com.hanapbahay.model.Sale;

/** Contract between Backend and Database. Database member implements this with MySQL. */
public interface SaleDao {

	List<Sale> findAll();

	List<Sale> findByAgent(int agentId);

	/** Saves a new sale and returns it with its generated saleId. */
	Sale insert(Sale sale);
}
