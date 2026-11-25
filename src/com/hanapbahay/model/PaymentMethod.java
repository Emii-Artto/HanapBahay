package com.hanapbahay.model;

/**
 * 5.1.11 PaymentMethod - a payment option for a deal, such as PAG-IBIG, Bank Financing or Cash.
 */
public class PaymentMethod {

	private int paymentMethodId;
	private String methodName;
	private String description;

	public PaymentMethod(int paymentMethodId, String methodName, String description) {
		this.paymentMethodId = paymentMethodId;
		this.methodName = methodName;
		this.description = description;
	}

	// shown in the drop-down list
	@Override
	public String toString() {
		return methodName;
	}

	public int getPaymentMethodId() {
		return paymentMethodId;
	}

	public String getMethodName() {
		return methodName;
	}

	public String getDescription() {
		return description;
	}
}
