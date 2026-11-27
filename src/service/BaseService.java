package service;

/**
 * Parent class of all services (INHERITANCE). Owner: Backend member.
 *
 * A service method returns true/false (or an object/null). When something fails,
 * getMessage() explains why in plain words, and the screen shows it (3.2.3).
 */
public class BaseService {

	// PROTECTED: child services can set it, other classes can only read it through getMessage()
	protected String message = "";

	public String getMessage() {
		return message;
	}

	protected boolean isBlank(String text) {
		return text == null || text.trim().isEmpty();
	}

	/**
	 * Reads a number typed by the user, e.g. "3,500,000" or "PHP 2500000".
	 * EXCEPTION HANDLING: if it is not a number, NumberFormatException is caught,
	 * the message names the field, and -1 is returned.
	 */
	protected double readNumber(String text, String fieldName) {
		if (isBlank(text)) {
			message = "Please enter the " + fieldName + ".";
			return -1;
		}
		String cleaned = text.replace(",", "").replace("PHP", "").replace("₱", "").trim();
		try {
			return Double.parseDouble(cleaned);
		} catch (NumberFormatException e) {
			message = "The " + fieldName + " must be a number. You typed: \"" + text.trim() + "\"";
			return -1;
		}
	}

	// Formats an amount of money the same way everywhere, e.g. PHP 3,500,000.00
	public static String peso(double amount) {
		return "PHP " + String.format("%,.2f", amount);
	}
}
