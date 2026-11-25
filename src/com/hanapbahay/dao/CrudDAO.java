package com.hanapbahay.dao;

import java.util.ArrayList;

/**
 * 5.1.13 CrudDAO - INTERFACE (abstraction) for saving and retrieving records.
 *
 * It says WHAT every data class must offer (add, update, delete, get all),
 * not HOW the database does it. PropertyDAO, ClientDAO, ViewingDAO and DealDAO implement it.
 * The screens and services call these methods and never write SQL themselves.
 *
 * <T> is the kind of record, e.g. CrudDAO<Client> works with Client objects.
 * Each method returns true if it worked, false if the database refused or could not be reached.
 */
public interface CrudDAO<T> {

	boolean add(T item);

	boolean update(T item);

	boolean delete(int id);

	ArrayList<T> getAll();
}
