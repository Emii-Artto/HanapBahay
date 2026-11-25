package com.hanapbahay;

import com.hanapbahay.dao.ClientDao;
import com.hanapbahay.dao.DealDao;
import com.hanapbahay.dao.PropertyDao;
import com.hanapbahay.dao.UserDao;
import com.hanapbahay.dao.ViewingDao;
import com.hanapbahay.dao.mysql.MySqlClientDao;
import com.hanapbahay.dao.mysql.MySqlDealDao;
import com.hanapbahay.dao.mysql.MySqlPropertyDao;
import com.hanapbahay.dao.mysql.MySqlUserDao;
import com.hanapbahay.dao.mysql.MySqlViewingDao;
import com.hanapbahay.service.ClientService;
import com.hanapbahay.service.DealService;
import com.hanapbahay.service.PropertyService;
import com.hanapbahay.service.ReportGenerator;
import com.hanapbahay.service.UserService;
import com.hanapbahay.service.ViewingService;

/**
 * The one place where the layers are connected:
 *   WindowBuilder GUI (login, dash) --> services --> DAO interfaces --> MySQL classes --> MySQL
 *
 * STATIC fields: one shared copy of each DAO and service for every screen.
 * STATIC methods: screens call them by class name, e.g. AppContext.propertyService().
 * The variable types are the INTERFACES (PropertyDao ...), the objects are the MySQL classes.
 */
public class AppContext {

	// ---- data layer (Database member) ----
	private static UserDao userDao = new MySqlUserDao();
	private static PropertyDao propertyDao = new MySqlPropertyDao();
	private static ClientDao clientDao = new MySqlClientDao();
	private static ViewingDao viewingDao = new MySqlViewingDao();
	private static DealDao dealDao = new MySqlDealDao();

	// ---- business layer (Backend member) ----
	private static UserService userService = new UserService(userDao);
	private static PropertyService propertyService = new PropertyService(propertyDao, viewingDao, dealDao);
	private static ClientService clientService = new ClientService(clientDao, propertyDao, viewingDao, dealDao);
	private static ViewingService viewingService = new ViewingService(viewingDao);
	private static DealService dealService = new DealService(dealDao, propertyDao);
	private static ReportGenerator reportGenerator = new ReportGenerator(propertyDao, clientDao, viewingDao, dealDao,
			userDao);

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
