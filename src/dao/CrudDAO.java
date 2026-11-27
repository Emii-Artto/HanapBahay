package dao;

import java.util.ArrayList;

/**
 * 5.1.13 CrudDAO - INTERFACE (abstraction) for saving and retrieving records.
 *
 * It says WHAT every data class must offer (add, update, delete, get all), not HOW the
 * database does it. PropertyDao, ClientDao, ViewingDao and DealDao extend it, and the
 * MySQL classes in the mysql package implement them. Screens and services never write SQL.
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
