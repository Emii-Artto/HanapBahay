package com.hanapbahay;

import com.hanapbahay.dao.ClientDAO;
import com.hanapbahay.dao.DealDAO;
import com.hanapbahay.dao.PropertyDAO;
import com.hanapbahay.dao.UserDAO;
import com.hanapbahay.dao.ViewingDAO;
import com.hanapbahay.service.ClientService;
import com.hanapbahay.service.DealService;
import com.hanapbahay.service.PropertyService;
import com.hanapbahay.service.ReportGenerator;
import com.hanapbahay.service.UserService;
import com.hanapbahay.service.ViewingService;

/**
 * The one place where the layers are connected.
 *
 * STATIC fields: only one copy of each DAO and service exists, shared by every screen.
 * STATIC methods: screens call them by class name, e.g. AppContext.propertyService().
 *
 *   GUI (ui) --> services (service) --> DAO classes (dao) --> MySQL
 */
public class AppContext {

	// ---- data layer (DAO classes, 5.1.13) ----
	private static UserDAO userDAO = new UserDAO();
	private static PropertyDAO propertyDAO = new PropertyDAO();
	private static ClientDAO clientDAO = new ClientDAO();
	private static ViewingDAO viewingDAO = new ViewingDAO();
	private static DealDAO dealDAO = new DealDAO();

	// ---- business layer (rules and validation) ----
	private static UserService userService = new UserService(userDAO);
	private static PropertyService propertyService = new PropertyService(propertyDAO, viewingDAO, dealDAO);
	private static ClientService clientService = new ClientService(clientDAO, propertyDAO, viewingDAO, dealDAO);
	private static ViewingService viewingService = new ViewingService(viewingDAO);
	private static DealService dealService = new DealService(dealDAO, propertyDAO);
	private static ReportGenerator reportGenerator = new ReportGenerator(propertyDAO, clientDAO, viewingDAO, dealDAO,
			userDAO);

	public static UserService userService() {
		return userService;
	}

	public static PropertyService propertyService() {
		return propertyService;
	}

	public static ClientService clientService() {
		return clientService;
	}

	public static ViewingService viewingService() {
		return viewingService;
	}

	public static DealService dealService() {
		return dealService;
	}

	public static ReportGenerator reportGenerator() {
		return reportGenerator;
	}
}
