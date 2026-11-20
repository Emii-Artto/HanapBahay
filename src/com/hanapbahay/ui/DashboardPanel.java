package com.hanapbahay.ui;

import java.awt.Color;
import java.awt.Dimension;
import java.util.ArrayList;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableModel;

import com.hanapbahay.AppContext;
import com.hanapbahay.model.Inquiry;
import com.hanapbahay.model.Property;
import com.hanapbahay.model.User;

/**
 * Dashboard page: five summary cards and a table of listings. Owner: UI member.
 * Shown inside MainFrame. Agents/Admins and Clients see different numbers.
 */
public class DashboardPanel extends JPanel {

	private static final long serialVersionUID = 1L;
	private JLabel lblAvailable;
	private JLabel lblReserved;
	private JLabel lblSold;
	private JLabel lblInquiriesTitle;
	private JLabel lblInquiries;
	private JLabel lblSalesTitle;
	private JLabel lblSales;
	private JLabel lblTableTitle;
	private JTable tblListings;
	private JLabel lblResult;

	public DashboardPanel() {
		setPreferredSize(new Dimension(934, 581));
		setBackground(Theme.PAGE_BG);
		setLayout(null);

		// ---- Card 1: Available listings ----
		JPanel pnlCard1 = new JPanel();
		pnlCard1.setBackground(Color.WHITE);
		pnlCard1.setBorder(new LineBorder(Theme.BORDER));
		pnlCard1.setBounds(25, 20, 167, 95);
		add(pnlCard1);
		pnlCard1.setLayout(null);

		JPanel pnlBar1 = new JPanel();
		pnlBar1.setBackground(Theme.ACCENT);
		pnlBar1.setBounds(0, 0, 4, 95);
		pnlCard1.add(pnlBar1);

		JLabel lblCard1 = new JLabel("Available listings");
		lblCard1.setForeground(Theme.TEXT_MUTED);
		lblCard1.setFont(Theme.SMALL);
		lblCard1.setBounds(16, 14, 145, 18);
		pnlCard1.add(lblCard1);

		lblAvailable = new JLabel("0");
		lblAvailable.setForeground(Theme.TEXT);
		lblAvailable.setFont(Theme.BIG_NUMBER);
		lblAvailable.setBounds(16, 38, 145, 40);
		pnlCard1.add(lblAvailable);

		// ---- Card 2: Reserved ----
		JPanel pnlCard2 = new JPanel();
		pnlCard2.setBackground(Color.WHITE);
		pnlCard2.setBorder(new LineBorder(Theme.BORDER));
		pnlCard2.setBounds(204, 20, 167, 95);
		add(pnlCard2);
		pnlCard2.setLayout(null);

		JPanel pnlBar2 = new JPanel();
		pnlBar2.setBackground(Theme.AMBER);
		pnlBar2.setBounds(0, 0, 4, 95);
		pnlCard2.add(pnlBar2);

		JLabel lblCard2 = new JLabel("Reserved");
		lblCard2.setForeground(Theme.TEXT_MUTED);
		lblCard2.setFont(Theme.SMALL);
		lblCard2.setBounds(16, 14, 145, 18);
		pnlCard2.add(lblCard2);

		lblReserved = new JLabel("0");
		lblReserved.setForeground(Theme.TEXT);
		lblReserved.setFont(Theme.BIG_NUMBER);
		lblReserved.setBounds(16, 38, 145, 40);
		pnlCard2.add(lblReserved);

		// ---- Card 3: Sold ----
		JPanel pnlCard3 = new JPanel();
		pnlCard3.setBackground(Color.WHITE);
		pnlCard3.setBorder(new LineBorder(Theme.BORDER));
		pnlCard3.setBounds(383, 20, 167, 95);
		add(pnlCard3);
		pnlCard3.setLayout(null);

		JPanel pnlBar3 = new JPanel();
		pnlBar3.setBackground(Theme.BLUE);
		pnlBar3.setBounds(0, 0, 4, 95);
		pnlCard3.add(pnlBar3);

		JLabel lblCard3 = new JLabel("Sold");
		lblCard3.setForeground(Theme.TEXT_MUTED);
		lblCard3.setFont(Theme.SMALL);
		lblCard3.setBounds(16, 14, 145, 18);
		pnlCard3.add(lblCard3);

		lblSold = new JLabel("0");
		lblSold.setForeground(Theme.TEXT);
		lblSold.setFont(Theme.BIG_NUMBER);
		lblSold.setBounds(16, 38, 145, 40);
		pnlCard3.add(lblSold);

		// ---- Card 4: Inquiries ----
		JPanel pnlCard4 = new JPanel();
		pnlCard4.setBackground(Color.WHITE);
		pnlCard4.setBorder(new LineBorder(Theme.BORDER));
		pnlCard4.setBounds(562, 20, 167, 95);
		add(pnlCard4);
		pnlCard4.setLayout(null);

		JPanel pnlBar4 = new JPanel();
		pnlBar4.setBackground(Theme.CORAL);
		pnlBar4.setBounds(0, 0, 4, 95);
		pnlCard4.add(pnlBar4);

		lblInquiriesTitle = new JLabel("Inquiries");
		lblInquiriesTitle.setForeground(Theme.TEXT_MUTED);
		lblInquiriesTitle.setFont(Theme.SMALL);
		lblInquiriesTitle.setBounds(16, 14, 145, 18);
		pnlCard4.add(lblInquiriesTitle);

		lblInquiries = new JLabel("0");
		lblInquiries.setForeground(Theme.TEXT);
		lblInquiries.setFont(Theme.BIG_NUMBER);
		lblInquiries.setBounds(16, 38, 145, 40);
		pnlCard4.add(lblInquiries);

		// ---- Card 5: Sales total (Agents/Admins) or replies (Clients) ----
		JPanel pnlCard5 = new JPanel();
		pnlCard5.setBackground(Color.WHITE);
		pnlCard5.setBorder(new LineBorder(Theme.BORDER));
		pnlCard5.setBounds(741, 20, 168, 95);
		add(pnlCard5);
		pnlCard5.setLayout(null);

		JPanel pnlBar5 = new JPanel();
		pnlBar5.setBackground(Theme.SIDEBAR);
		pnlBar5.setBounds(0, 0, 4, 95);
		pnlCard5.add(pnlBar5);

		lblSalesTitle = new JLabel("Total sales (PHP)");
		lblSalesTitle.setForeground(Theme.TEXT_MUTED);
		lblSalesTitle.setFont(Theme.SMALL);
		lblSalesTitle.setBounds(16, 14, 145, 18);
		pnlCard5.add(lblSalesTitle);

		lblSales = new JLabel("0");
		lblSales.setForeground(Theme.ACCENT);
		lblSales.setFont(Theme.BIG_NUMBER);
		lblSales.setBounds(16, 38, 150, 40);
		pnlCard5.add(lblSales);

		// ---- Listings table ----
		lblTableTitle = new JLabel("Listings");
		lblTableTitle.setForeground(Theme.TEXT);
		lblTableTitle.setFont(Theme.HEADING);
		lblTableTitle.setBounds(25, 135, 400, 24);
		add(lblTableTitle);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(25, 165, 884, 345);
		add(scrollPane);

		tblListings = new JTable();
		tblListings.setModel(new DefaultTableModel(
			new Object[][] {},
			new String[] { "ID", "Title", "Location", "Type", "Price (PHP)", "Status" }
		));
		Theme.styleTable(tblListings, scrollPane);
		tblListings.getColumnModel().getColumn(0).setPreferredWidth(40);
		tblListings.getColumnModel().getColumn(1).setPreferredWidth(220);
		tblListings.getColumnModel().getColumn(2).setPreferredWidth(180);
		scrollPane.setViewportView(tblListings);

		lblResult = new JLabel("");
		lblResult.setForeground(Theme.TEXT_MUTED);
		lblResult.setFont(Theme.SMALL);
		lblResult.setBounds(25, 518, 400, 22);
		add(lblResult);
	}

	/** Fills the cards and the table for the logged-in user. */
	public void loadFor(User user) {
		lblAvailable.setText(String.valueOf(AppContext.propertyService().getListingsByStatus(Property.AVAILABLE).size()));
		lblReserved.setText(String.valueOf(AppContext.propertyService().getListingsByStatus(Property.RESERVED).size()));
		lblSold.setText(String.valueOf(AppContext.propertyService().getListingsByStatus(Property.SOLD).size()));

		if (user.canManageListings()) {
			lblInquiriesTitle.setText("New inquiries");
			lblInquiries.setText(String.valueOf(AppContext.inquiryService().getInquiriesByStatus(Inquiry.NEW).size()));

			// POLYMORPHISM + OVERLOADING: Admin sees the total of ALL sales, an Agent only their own
			double total;
			if (user.canViewAllSales()) {
				lblSalesTitle.setText("Total sales (PHP)");
				total = AppContext.saleService().getTotalSales();
			} else {
				lblSalesTitle.setText("My sales (PHP)");
				total = AppContext.saleService().getTotalSales(user.getUserId());
			}
			lblSales.setText(String.format("%,.0f", total));

			lblTableTitle.setText("All listings");
			loadTable(AppContext.propertyService().getAllListings());
		} else {
			lblInquiriesTitle.setText("My inquiries");
			lblInquiries.setText(String.valueOf(AppContext.inquiryService().getInquiriesByClient(user.getUserId()).size()));
			lblSalesTitle.setText("Replied to me");
			lblSales.setText(String.valueOf(
					AppContext.inquiryService().getInquiriesByClient(user.getUserId(), Inquiry.RESPONDED).size()));

			lblTableTitle.setText("Available properties");
			loadTable(AppContext.propertyService().getAvailableListings());
		}
	}

	private void loadTable(ArrayList<Property> list) {
		DefaultTableModel model = (DefaultTableModel) tblListings.getModel();
		model.setRowCount(0); // clear the table
		for (Property p : list) {
			model.addRow(new Object[] { p.getPropertyId(), p.getTitle(), p.getLocation(), p.getType(),
					String.format("%,.2f", p.getPrice()), p.getStatus() });
		}
		lblResult.setText("Showing " + list.size() + " listing(s)");
	}
}
