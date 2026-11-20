package com.hanapbahay;

import com.hanapbahay.dao.InquiryDao;
import com.hanapbahay.dao.PropertyDao;
import com.hanapbahay.dao.SaleDao;
import com.hanapbahay.dao.UserDao;
import com.hanapbahay.dao.sample.SampleInquiryDao;
import com.hanapbahay.dao.sample.SamplePropertyDao;
import com.hanapbahay.dao.sample.SampleSaleDao;
import com.hanapbahay.dao.sample.SampleUserDao;
import com.hanapbahay.service.InquiryService;
import com.hanapbahay.service.PropertyService;
import com.hanapbahay.service.SaleService;
import com.hanapbahay.service.UserService;

/**
 * The one place where the three layers are connected.
 *
 * STATIC VARIABLES (Module 3) - only one copy exists, shared by every screen,
 * so all screens see the same data.
 * STATIC METHODS (Module 3) - the UI calls them by class name, no object needed:
 *   AppContext.userService().login(...)
 */
public class AppContext {

	// ---- Data layer: the variable type is the INTERFACE (Module 5, polymorphism) ----
	// Right now these are SAMPLE classes (data in memory). When the Database member finishes
	// the MySQL classes, replace e.g. "new SampleUserDao()" with "new MySqlUserDao()".
	private static UserDao userDao = new SampleUserDao();
	private static PropertyDao propertyDao = new SamplePropertyDao();
	private static InquiryDao inquiryDao = new SampleInquiryDao();
	private static SaleDao saleDao = new SampleSaleDao();

	// ---- Business layer (Backend member) ----
	private static UserService userService = new UserService(userDao);
	private static PropertyService propertyService = new PropertyService(propertyDao);
	private static InquiryService inquiryService = new InquiryService(inquiryDao, propertyDao);
	private static SaleService saleService = new SaleService(saleDao, propertyDao);

	public static UserService userService() {
		return userService;
	}

	public static PropertyService propertyService() {
		return propertyService;
	}

	public static InquiryService inquiryService() {
		return inquiryService;
	}

	public static SaleService saleService() {
		return saleService;
	}
}
