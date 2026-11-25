package com.hanapbahay.dao;

import java.util.ArrayList;

import com.hanapbahay.model.Client;

/**
 * Buyers. Gets add, update, delete and getAll from CrudDAO and adds a search by name or
 * contact number. Implemented by dao.mysql.MySqlClientDao.
 */
public interface ClientDao extends CrudDAO<Client> {

	ArrayList<Client> getByAgent(int agentId);

	ArrayList<Client> search(String keyword, int agentId);

	Client findById(int clientId);
}
