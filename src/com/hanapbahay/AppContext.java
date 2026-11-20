package com.hanapbahay;

import com.hanapbahay.dao.UserDao;
import com.hanapbahay.dao.sample.SampleUserDao;
import com.hanapbahay.service.UserService;

/**
 * The one place where the three layers are connected.
 *
 * STATIC VARIABLES (Module 3) - only one copy exists, shared by every screen,
 * so all screens see the same data.
 * STATIC METHODS (Module 3) - the UI calls them by class name, no object needed:
 *   AppContext.userService().login(...)
 *
 * Week 1: user accounts only. The listing, inquiry and sales services are added here
 * as each module is finished.
 */
public class AppContext {

	// ---- Data layer: the variable type is the INTERFACE (Module 5, polymorphism) ----
	// Right now this is a SAMPLE class (data in memory). When the Database member finishes
	// MySqlUserDao, replace "new SampleUserDao()" with "new MySqlUserDao()".
	private static UserDao userDao = new SampleUserDao();

	// ---- Business layer (Backend member) ----
	private static UserService userService = new UserService(userDao);

	public static UserService userService() {
		return userService;
	}
}
