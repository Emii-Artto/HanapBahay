package com.hanapbahay.ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableModel;

import com.hanapbahay.AppContext;
import com.hanapbahay.model.Inquiry;
import com.hanapbahay.model.Property;
import com.hanapbahay.model.User;

/**
 * Inquiries page. Owner: UI member. Shown inside MainFrame.
 * Agents and Admins see all inquiries, can filter by status, and mark them responded or closed.
 * Clients see only their own inquiries ("My inquiries").
 */
public class InquiriesPanel extends JPanel {

	private static final long serialVersionUID = 1L;
	private JLabel lblShow;
	private JComboBox<String> cboFilter;
	private JTable tblInquiries;
	private JButton btnResponded;
	private JButton btnClose;
	private JLabel lblResult;

	private User currentUser;

	public InquiriesPanel() {
		setPreferredSize(new Dimension(934, 581));
		setBackground(Theme.PAGE_BG);
		setLayout(null);

		// ================= FILTER BAR =================
		JPanel pnlToolbar = new JPanel();
		pnlToolbar.setBackground(Color.WHITE);
		pnlToolbar.setBorder(new LineBorder(Theme.BORDER));
		pnlToolbar.setBounds(25, 20, 884, 60);
		add(pnlToolbar);
		pnlToolbar.setLayout(null);

		lblShow = new JLabel("Status");
		lblShow.setForeground(Theme.TEXT);
		lblShow.setFont(Theme.BODY_BOLD);
		lblShow.setBounds(15, 17, 50, 26);
		pnlToolbar.add(lblShow);

		cboFilter = new JComboBox<String>();
		cboFilter.setBackground(Color.WHITE);
		cboFilter.setFont(Theme.BODY);
		cboFilter.setModel(new DefaultComboBoxModel<String>(new String[] { "ALL", "NEW", "RESPONDED", "CLOSED" }));
		cboFilter.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				loadInquiries(); // reload when a different status is picked
			}
		});
		cboFilter.setBounds(68, 17, 170, 28);
		pnlToolbar.add(cboFilter);

		lblResult = new JLabel("");
		lblResult.setHorizontalAlignment(SwingConstants.RIGHT);
		lblResult.setForeground(Theme.TEXT_MUTED);
		lblResult.setFont(Theme.SMALL);
		lblResult.setBounds(480, 17, 389, 26);
		pnlToolbar.add(lblResult);

		// ================= TABLE =================
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(25, 95, 884, 410);
		add(scrollPane);

		tblInquiries = new JTable();
		tblInquiries.setModel(new DefaultTableModel(
			new Object[][] {},
			new String[] { "ID", "Property", "Client", "Message", "Status" }
		));
		Theme.styleTable(tblInquiries, scrollPane);
		tblInquiries.getColumnModel().getColumn(0).setPreferredWidth(40);
		tblInquiries.getColumnModel().getColumn(1).setPreferredWidth(160);
		tblInquiries.getColumnModel().getColumn(2).setPreferredWidth(140);
		tblInquiries.getColumnModel().getColumn(3).setPreferredWidth(360);
		scrollPane.setViewportView(tblInquiries);

		// ================= ACTIONS (Agents and Admins) =================
		btnResponded = new JButton("Mark as responded");
		btnResponded.setForeground(Color.WHITE);
		btnResponded.setBackground(Theme.ACCENT);
		btnResponded.setFont(Theme.BODY_BOLD);
		btnResponded.setBorder(new LineBorder(Theme.ACCENT));
		btnResponded.setFocusPainted(false);
		btnResponded.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				doMarkResponded();
			}
		});
		btnResponded.setBounds(599, 516, 170, 36);
		add(btnResponded);

		btnClose = new JButton("Close inquiry");
		btnClose.setForeground(Theme.TEXT);
		btnClose.setBackground(Color.WHITE);
		btnClose.setFont(Theme.BODY_BOLD);
		btnClose.setBorder(new LineBorder(Theme.BORDER));
		btnClose.setFocusPainted(false);
		btnClose.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				doClose();
			}
		});
		btnClose.setBounds(779, 516, 130, 36);
		add(btnClose);
	}

	/** Fills the page for the logged-in user. */
	public void loadFor(User user) {
		currentUser = user;

		boolean staff = user.canManageListings(); // POLYMORPHISM: Admin/Agent true, Client false
		lblShow.setVisible(staff);
		cboFilter.setVisible(staff);
		btnResponded.setVisible(staff);
		btnClose.setVisible(staff);

		loadInquiries();
	}

	private void loadInquiries() {
		if (currentUser == null) {
			return;
		}

		ArrayList<Inquiry> list;
		String status = (String) cboFilter.getSelectedItem();
		if (!currentUser.canManageListings()) {
			list = AppContext.inquiryService().getInquiriesByClient(currentUser.getUserId());
		} else if (status.equals("ALL")) {
			list = AppContext.inquiryService().getAllInquiries();
		} else {
			list = AppContext.inquiryService().getInquiriesByStatus(status);
		}

		DefaultTableModel model = (DefaultTableModel) tblInquiries.getModel();
		model.setRowCount(0); // clear the table
		for (Inquiry inquiry : list) {
			model.addRow(new Object[] { inquiry.getInquiryId(), getPropertyTitle(inquiry.getPropertyId()),
					getUserName(inquiry.getClientId()), inquiry.getMessage(), inquiry.getStatus() });
		}
		lblResult.setText("Showing " + list.size() + " inquiry(ies)");
	}

	private String getPropertyTitle(int propertyId) {
		Property property = AppContext.propertyService().getListing(propertyId);
		if (property == null) {
			return "(deleted listing)";
		}
		return property.getTitle();
	}

	private String getUserName(int userId) {
		User user = AppContext.userService().getUser(userId);
		if (user == null) {
			return "(unknown)";
		}
		return user.getFullName();
	}

	/** Returns the ID of the selected row, or 0 if no row is selected. */
	private int getSelectedId() {
		int row = tblInquiries.getSelectedRow();
		if (row < 0) {
			return 0;
		}
		return Integer.parseInt(tblInquiries.getValueAt(row, 0).toString());
	}

	private void doMarkResponded() {
		int id = getSelectedId();
		if (id == 0) {
			showResult(false, "Select an inquiry in the table first.");
			return;
		}
		boolean ok = AppContext.inquiryService().markResponded(id);
		showResult(ok, AppContext.inquiryService().getMessage());
		loadInquiries();
	}

	private void doClose() {
		int id = getSelectedId();
		if (id == 0) {
			showResult(false, "Select an inquiry in the table first.");
			return;
		}
		boolean ok = AppContext.inquiryService().closeInquiry(id);
		showResult(ok, AppContext.inquiryService().getMessage());
		loadInquiries();
	}

	/** Shows an information box if it worked, or a warning box if not. */
	private void showResult(boolean ok, String message) {
		if (ok) {
			JOptionPane.showMessageDialog(this, message, "Done", JOptionPane.INFORMATION_MESSAGE);
		} else {
			JOptionPane.showMessageDialog(this, message, "Please check", JOptionPane.WARNING_MESSAGE);
		}
	}
}
