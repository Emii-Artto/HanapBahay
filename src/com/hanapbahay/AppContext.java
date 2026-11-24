package com.hanapbahay;

import com.hanapbahay.dao.InquiryDao;
import com.hanapbahay.dao.PropertyDao;
import com.hanapbahay.dao.SaleDao;
import com.hanapbahay.dao.UserDao;
import com.hanapbahay.dao.mysql.MySqlInquiryDao;
import com.hanapbahay.dao.mysql.MySqlPropertyDao;
import com.hanapbahay.dao.mysql.MySqlSaleDao;
import com.hanapbahay.dao.mysql.MySqlUserDao;
import com.hanapbahay.service.DashboardService;
import com.hanapbahay.service.UserService;

public class AppContext {
	// DAOs: the MySQL classes (XAMPP)
	private static UserDao userDao = new MySqlUserDao();
	private static PropertyDao propertyDao = new MySqlPropertyDao();
	private static InquiryDao inquiryDao = new MySqlInquiryDao();
	private static SaleDao saleDao = new MySqlSaleDao();

	// backend services the UI calls
	private static UserService userService = new UserService(userDao);
	private static DashboardService dashboardService = new DashboardService(propertyDao, inquiryDao, saleDao);

	public static UserService userService() {
		return userService;
	}

	public static DashboardService dashboardService() {
		return dashboardService;
	}
}
