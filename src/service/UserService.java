package service;

import java.util.ArrayList;

import dao.UserDao;
import model.Agent;
import model.User;

/**
 * Login (5.3.3), agent profiles, and User Account Management (3.1.9, 5.3.9).
 *
 * There is no sign-up. Accounts are created only by a user with higher authority:
 *   the Owner can add Admins and Agents; an Admin can add Agents only.
 * Every method that changes accounts takes the logged-in user ("actor") and checks
 * what that user is allowed to do before changing anything.
 */
public class UserService extends BaseService {

	private UserDao userDAO;

	public UserService(UserDao userDAO) {
		this.userDAO = userDAO;
	}

	/** Returns the User if the username and password are correct and the account is active. */
	public User login(String username, String password) {
		if (isBlank(username) || isBlank(password)) {
			message = "Please enter your username and password.";
			return null;
		}
		User user = userDAO.findByUsername(username.trim());
		// checkPassword() compares hashes; the stored hash is never read by this class
		if (user == null || !user.checkPassword(password)) {
			message = "Wrong username or password.";
			return null;
		}
		if (!user.isActive()) {
			message = "This account has been deactivated.\nPlease contact your administrator.";
			return null;
		}
		message = "Welcome, " + user.getUsername() + "!";
		return user;
	}

	/** The agent profile of a login account, or null for an Owner or Admin account. */
	public Agent getAgentProfile(User user) {
		if (user == null || !user.isAgent()) {
			return null;
		}
		Agent agent = userDAO.findAgentByUserId(user.getUserId());
		if (agent == null) {
			message = "This account has no agent profile.";
		}
		return agent;
	}

	public ArrayList<Agent> getAllAgents() {
		return userDAO.getAllAgents();
	}

	public ArrayList<User> getAllUsers() {
		return userDAO.getAllUsers();
	}

	// ======================= account management (3.1.9) =======================

	/** Owner or Admin adds an AGENT account with its profile. */
	public boolean createAgentAccount(User actor, String firstName, String lastName, String contactNumber,
			String email, String username, String password, String confirmPassword) {
		if (actor == null || !actor.canManageAgents()) {
			message = "Only the Owner or an Admin can add agent accounts.";
			return false;
		}
		if (isBlank(firstName) || isBlank(lastName) || isBlank(contactNumber)) {
			message = "Please enter the agent's first name, last name and contact number.";
			return false;
		}
		if (!isValidEmail(email)) {
			message = "The email \"" + email.trim() + "\" is not valid. Example: name@email.com";
			return false;
		}
		if (!checkNewLogin(username, password, confirmPassword)) {
			return false;
		}

		// 1. the login account (only the hash of the password is saved)
		int userId = userDAO.addAccount(username.trim(), User.hash(password), User.AGENT, actor.getUserId());
		if (userId == 0) {
			message = "Cannot save the account. Please check the database connection.";
			return false;
		}
		// 2. the agent profile linked to it
		Agent agent = new Agent(0, userId, firstName.trim(), lastName.trim(), contactNumber.trim(), clean(email));
		if (!userDAO.addAgent(agent)) {
			userDAO.deleteAccount(userId); // undo step 1 so no half-made account is left
			message = "Cannot save the agent profile. Please check the database connection.";
			return false;
		}
		message = "Agent account \"" + username.trim() + "\" created for " + agent.getFullName()
				+ ".\nGive the agent the username and the temporary password.\n"
				+ "They must choose their own password at the first login.";
		return true;
	}

	/** Only the Owner adds an ADMIN account (e.g. the CEO gives the secretary admin access). */
	public boolean createAdminAccount(User actor, String username, String password, String confirmPassword) {
		if (actor == null || !actor.canManageAdmins()) {
			message = "Only the Owner can add Admin accounts.";
			return false;
		}
		if (!checkNewLogin(username, password, confirmPassword)) {
			return false;
		}
		int userId = userDAO.addAccount(username.trim(), User.hash(password), User.ADMIN, actor.getUserId());
		if (userId == 0) {
			message = "Cannot save the account. Please check the database connection.";
			return false;
		}
		message = "Admin account \"" + username.trim() + "\" created.\n"
				+ "They must choose their own password at the first login.";
		return true;
	}

	/** Sets a new temporary password for another account. */
	public boolean resetPassword(User actor, int userId, String password, String confirmPassword) {
		User target = findManageable(actor, userId, "reset the password of");
		if (target == null) {
			return false;
		}
		if (isBlank(password) || password.length() < 6) {
			message = "The new password must have at least 6 characters.";
			return false;
		}
		if (!password.equals(confirmPassword)) {
			message = "The passwords do not match.";
			return false;
		}
		// a reset password is temporary: the user must choose a new one at the next login
		if (!userDAO.updatePassword(userId, User.hash(password), true)) {
			message = "Cannot save the new password. Please check the database connection.";
			return false;
		}
		message = "The password of \"" + target.getUsername() + "\" was reset.\n"
				+ "They must choose a new password the next time they log in.";
		return true;
	}

	/**
	 * Change My Password: any logged-in user changes their own password.
	 * The current password must be typed again, so nobody can change it from a computer left logged in.
	 */
	public boolean changeOwnPassword(User user, String currentPassword, String newPassword, String confirmPassword) {
		if (user == null) {
			message = "No user is logged in.";
			return false;
		}
		if (isBlank(currentPassword) || isBlank(newPassword) || isBlank(confirmPassword)) {
			message = "Please fill in all three password fields.";
			return false;
		}
		if (!user.checkPassword(currentPassword)) {
			message = "Your current password is wrong.";
			return false;
		}
		if (newPassword.length() < 6) {
			message = "The new password must have at least 6 characters.";
			return false;
		}
		if (!newPassword.equals(confirmPassword)) {
			message = "The new passwords do not match.";
			return false;
		}
		if (newPassword.equals(currentPassword)) {
			message = "The new password must be different from the current one.";
			return false;
		}
		if (!userDAO.updatePassword(user.getUserId(), User.hash(newPassword), false)) {
			message = "Cannot save the new password. Please check the database connection.";
			return false;
		}
		user.setNewPassword(newPassword); // keep the logged-in User object up to date
		message = "Your password was changed.";
		return true;
	}

	/**
	 * Deactivates or reactivates an account. A deactivated account cannot log in, but its
	 * records stay. The Owner can never be deactivated, and nobody can deactivate themselves.
	 */
	public boolean setActive(User actor, int userId, boolean active) {
		User target = findManageable(actor, userId, active ? "activate" : "deactivate");
		if (target == null) {
			return false;
		}
		if (target.isActive() == active) {
			message = "\"" + target.getUsername() + "\" is already " + (active ? "active." : "deactivated.");
			return false;
		}
		if (!userDAO.setActive(userId, active)) {
			message = "Cannot change the account. Please check the database connection.";
			return false;
		}
		message = "\"" + target.getUsername() + "\" is now " + (active ? "active." : "deactivated and cannot log in.");
		return true;
	}

	/**
	 * The account the actor wants to change, if the actor is allowed:
	 * the Owner can change Admins and Agents; an Admin can change Agents only;
	 * nobody can change the Owner or their own account here.
	 */
	private User findManageable(User actor, int userId, String action) {
		if (actor == null || !actor.canManageAgents()) {
			message = "Only the Owner or an Admin can manage accounts.";
			return null;
		}
		User target = userDAO.findById(userId);
		if (target == null) {
			message = "That account was not found.";
			return null;
		}
		if (target.getUserId() == actor.getUserId()) {
			message = "You cannot " + action + " your own account here.";
			return null;
		}
		if (target.isOwner()) {
			message = "The Owner account cannot be changed by anyone. It is the highest authority.";
			return null;
		}
		if (target.isAdmin() && !actor.canManageAdmins()) {
			message = "Only the Owner can " + action + " an Admin account.";
			return null;
		}
		return target;
	}

	// 5.3.9: unique username, password of 6+ characters, typed twice the same
	private boolean checkNewLogin(String username, String password, String confirmPassword) {
		if (isBlank(username) || isBlank(password) || isBlank(confirmPassword)) {
			message = "Please enter the username, the password and the password again.";
			return false;
		}
		if (username.trim().contains(" ")) {
			message = "The username cannot contain spaces.";
			return false;
		}
		if (password.length() < 6) {
			message = "The password must have at least 6 characters.";
			return false;
		}
		if (!password.equals(confirmPassword)) {
			message = "The passwords do not match.";
			return false;
		}
		if (userDAO.findByUsername(username.trim()) != null) {
			message = "The username \"" + username.trim() + "\" is already taken.";
			return false;
		}
		return true;
	}

	/** AgentPanel: edit the logged-in agent's own profile. */
	public boolean updateProfile(Agent agent, String firstName, String lastName, String contactNumber, String email) {
		if (isBlank(firstName) || isBlank(lastName) || isBlank(contactNumber)) {
			message = "First name, last name and contact number are required.";
			return false;
		}
		if (!isValidEmail(email)) {
			message = "The email \"" + email.trim() + "\" is not valid. Example: name@email.com";
			return false;
		}
		agent.setFirstName(firstName.trim());
		agent.setLastName(lastName.trim());
		agent.setContactNumber(contactNumber.trim());
		agent.setEmail(clean(email));
		if (!userDAO.updateAgent(agent)) {
			message = "Cannot save your profile. Please check the database connection.";
			return false;
		}
		message = "Profile updated.";
		return true;
	}

	// email is optional; if typed, it must look like name@domain
	private boolean isValidEmail(String email) {
		if (isBlank(email)) {
			return true;
		}
		String e = email.trim();
		int at = e.indexOf('@');
		return at > 0 && e.indexOf('.', at) > at + 1 && !e.endsWith(".") && !e.contains(" ");
	}

	private String clean(String text) {
		if (text == null) {
			return "";
		}
		return text.trim();
	}
}
