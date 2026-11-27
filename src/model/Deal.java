package model;

/**
 * 5.1.12 Deal - a property transaction.
 *
 * When the deal status changes, the property status changes too:
 *   PENDING   -> property is RESERVED
 *   COMPLETED -> property is SOLD
 *   CANCELLED -> property is AVAILABLE again
 */
public class Deal {

	public static final String PENDING = "PENDING";
	public static final String COMPLETED = "COMPLETED";
	public static final String CANCELLED = "CANCELLED";

	private int dealId;
	private int propertyId;
	private int clientId;
	private int agentId;
	private int paymentMethodId;
	private double agreedPrice;
	private String dealDate; // yyyy-mm-dd
	private String status;

	public Deal(int dealId, int propertyId, int clientId, int agentId, int paymentMethodId, double agreedPrice,
			String dealDate, String status) {
		this.dealId = dealId;
		this.propertyId = propertyId;
		this.clientId = clientId;
		this.agentId = agentId;
		this.paymentMethodId = paymentMethodId;
		this.agreedPrice = agreedPrice;
		this.dealDate = dealDate;
		this.status = status;
	}

	// The property status that matches this deal's status
	public String getPropertyStatus() {
		if (COMPLETED.equals(status)) {
			return Property.SOLD;
		}
		if (PENDING.equals(status)) {
			return Property.RESERVED;
		}
		return Property.AVAILABLE; // cancelled
	}

	// An active deal blocks the same property from being offered or sold again (5.3.6)
	public boolean isActive() {
		return PENDING.equals(status) || COMPLETED.equals(status);
	}

	public boolean isCompleted() {
		return COMPLETED.equals(status);
	}

	public int getDealId() {
		return dealId;
	}

	public void setDealId(int dealId) {
		this.dealId = dealId;
	}

	public int getPropertyId() {
		return propertyId;
	}

	public int getClientId() {
		return clientId;
	}

	public int getAgentId() {
		return agentId;
	}

	public int getPaymentMethodId() {
		return paymentMethodId;
	}

	public double getAgreedPrice() {
		return agreedPrice;
	}

	// ENCAPSULATION: only a positive price
	public boolean setAgreedPrice(double agreedPrice) {
		if (agreedPrice <= 0) {
			return false;
		}
		this.agreedPrice = agreedPrice;
		return true;
	}

	public String getDealDate() {
		return dealDate;
	}

	public String getStatus() {
		return status;
	}

	public boolean setStatus(String status) {
		if (!PENDING.equals(status) && !COMPLETED.equals(status) && !CANCELLED.equals(status)) {
			return false;
		}
		this.status = status;
		return true;
	}
}
