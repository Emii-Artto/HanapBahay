package com.hanapbahay.dao.stub;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.hanapbahay.dao.SaleDao;
import com.hanapbahay.model.Sale;

/** Fake in-memory data so the app runs before MySQL is ready. Data resets on every run. */
public class StubSaleDao implements SaleDao {

	private final List<Sale> sales = new ArrayList<>();
	private int nextId = 1;

	@Override
	public List<Sale> findAll() {
		return new ArrayList<>(sales);
	}

	@Override
	public List<Sale> findByAgent(int agentId) {
		return sales.stream().filter(s -> s.getAgentId() == agentId).collect(Collectors.toList());
	}

	@Override
	public Sale insert(Sale sale) {
		sale.setSaleId(nextId++);
		sales.add(sale);
		return sale;
	}
}
