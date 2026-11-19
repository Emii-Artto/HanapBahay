package com.hanapbahay;

import com.hanapbahay.dao.InquiryDao;
import com.hanapbahay.dao.PropertyDao;
import com.hanapbahay.dao.SaleDao;
import com.hanapbahay.dao.UserDao;
import com.hanapbahay.dao.stub.StubInquiryDao;
import com.hanapbahay.dao.stub.StubPropertyDao;
import com.hanapbahay.dao.stub.StubSaleDao;
import com.hanapbahay.dao.stub.StubUserDao;
import com.hanapbahay.service.InquiryService;
import com.hanapbahay.service.PropertyService;
import com.hanapbahay.service.SaleService;
import com.hanapbahay.service.UserService;

/**
 * The one place where the three layers are connected.
 *
 * UI code gets services from here, e.g. AppContext.userService().login(...)
 *
 * Right now the DAOs are STUBS (fake in-memory data) so the UI and backend can be
 * built and tested before the database is ready. When the Database member finishes
 * the MySQL versions (e.g. MySqlUserDao), swap them in below. Nothing else changes.
 */
public final class AppContext {

	// ---- Data layer (Database member swaps these for MySQL implementations) ----
	private static final UserDao USER_DAO = new StubUserDao();
	private static final PropertyDao PROPERTY_DAO = new StubPropertyDao();
	private static final InquiryDao INQUIRY_DAO = new StubInquiryDao();
	private static final SaleDao SALE_DAO = new StubSaleDao();

	// ---- Business layer (Backend member) ----
	private static final UserService USER_SERVICE = new UserService(USER_DAO);
	private static final PropertyService PROPERTY_SERVICE = new PropertyService(PROPERTY_DAO);
	private static final InquiryService INQUIRY_SERVICE = new InquiryService(INQUIRY_DAO, PROPERTY_DAO);
	private static final SaleService SALE_SERVICE = new SaleService(SALE_DAO, PROPERTY_DAO);

	private AppContext() {
	}

	public static UserService userService() {
		return USER_SERVICE;
	}

	public static PropertyService propertyService() {
		return PROPERTY_SERVICE;
	}

	public static InquiryService inquiryService() {
		return INQUIRY_SERVICE;
	}

	public static SaleService saleService() {
		return SALE_SERVICE;
	}
}
