package dao;

import java.util.ArrayList;

import model.Deal;
import model.PaymentMethod;

/**
 * Property transactions and payment methods. Gets add, update, delete and getAll from
 * CrudDAO and adds the active-deal check. Implemented by mysql.MySqlDealDao.
 */
public interface DealDao extends CrudDAO<Deal> {

	ArrayList<Deal> getByAgent(int agentId);

	Deal findById(int dealId);

	boolean hasActiveDeal(int propertyId);

	int countByProperty(int propertyId);

	int countByClient(int clientId);

	ArrayList<PaymentMethod> getPaymentMethods();
}
