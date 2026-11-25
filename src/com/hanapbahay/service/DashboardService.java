package com.hanapbahay.service;

import java.util.ArrayList;

import com.hanapbahay.dao.InquiryDao;
import com.hanapbahay.dao.PropertyDao;
import com.hanapbahay.dao.SaleDao;
import com.hanapbahay.model.Inquiry;
import com.hanapbahay.model.Property;
import com.hanapbahay.model.Sale;
import com.hanapbahay.model.User;

public class DashboardService extends BaseService {

	private PropertyDao propertyDao;
	private InquiryDao inquiryDao;
	private SaleDao saleDao;

	public DashboardService(PropertyDao propertyDao, InquiryDao inquiryDao, SaleDao saleDao) {
		this.propertyDao = propertyDao;
		this.inquiryDao = inquiryDao;
		this.saleDao = saleDao;
	}

	public DashboardSummary getSummary(User user) {
		if (user == null) {
			message = "No user is logged in.";
			return null;
		}

		boolean allAgents = user.canViewAllSales(); // Admin: true, Agent: false
		DashboardSummary summary = new DashboardSummary(allAgents);

		for (Property property : propertyDao.findAll()) {
			if (canSee(user, property.getAgentId())) {
				summary.addListing(property.getStatus());
			}
		}

		for (Inquiry inquiry : inquiryDao.findAll()) {
			if (canSee(user, inquiry.getAgentId())) {
				summary.addInquiry(inquiry.getStatus());
			}
		}

		for (Sale sale : saleDao.findAll()) {
			if (canSee(user, sale.getAgentId())) {
				summary.addSale(sale.getSalePrice());
			}
		}

		message = summary.getScopeLabel();
		return summary;
	}

	public ArrayList<Inquiry> getRecentInquiries(User user, int limit) {
		ArrayList<Inquiry> recent = new ArrayList<Inquiry>();
		if (user == null) {
			message = "No user is logged in.";
			return recent;
		}

		for (Inquiry inquiry : inquiryDao.findAll()) { 
			if (recent.size() >= limit) {
				break;
			}
			if (canSee(user, inquiry.getAgentId())) {
				recent.add(inquiry);
			}
		}
		message = recent.size() + " recent inquiries";
		return recent;
	}

	private boolean canSee(User user, int agentId) {
		return user.canViewAllSales() || agentId == user.getUserId();
	}
}
