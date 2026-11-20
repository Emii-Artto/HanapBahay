package com.hanapbahay.ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableModel;

import com.hanapbahay.AppContext;
import com.hanapbahay.model.Property;
import com.hanapbahay.model.Sale;
import com.hanapbahay.model.User;

/**
 * Sales page and sales report. Owner: UI member. Shown inside MainFrame (Agents and Admins only).
 * Left: record a sale and the summary. Right: the sales report.
 * An Admin sees ALL sales; an Agent sees only their own (canViewAllSales()).
 */
public class SalesPanel extends JPanel {

	private static final long serialVersionUID = 1L;
	private JComboBox<Property> cboProperty;
	private JComboBox<User> cboClient;
	private JTextField txtSalePrice;
	private JLabel lblReportTitle;
	private JTable tblSales;
	private JLabel lblNumberOfSales;
	private JLabel lblTotalTitle;
	private JLabel lblTotalSales;

	private User currentUser;

	public SalesPanel() {
		setPreferredSize(new Dimension(934, 581));
		setBackground(Theme.PAGE_BG);
		setLayout(null);

		// ================= RECORD A SALE =================
		JPanel pnlRecord = new JPanel();
		pnlRecord.setBackground(Color.WHITE);
		pnlRecord.setBorder(new LineBorder(Theme.BORDER));
		pnlRecord.setBounds(25, 20, 300, 285);
		add(pnlRecord);
		pnlRecord.setLayout(null);

		JLabel lblRecordTitle = new JLabel("Record a sale");
		lblRecordTitle.setForeground(Theme.TEXT);
		lblRecordTitle.setFont(Theme.HEADING);
		lblRecordTitle.setBounds(15, 12, 270, 24);
		pnlRecord.add(lblRecordTitle);

		JLabel lblProperty = new JLabel("Property");
		lblProperty.setForeground(Theme.TEXT_MUTED);
		lblProperty.setFont(Theme.SMALL);
		lblProperty.setBounds(15, 45, 270, 18);
		pnlRecord.add(lblProperty);

		cboProperty = new JComboBox<Property>();
		cboProperty.setBackground(Color.WHITE);
		cboProperty.setFont(Theme.BODY);
		cboProperty.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				fillListingPrice(); // suggest the listing price when a property is picked
			}
		});
		cboProperty.setBounds(15, 64, 270, 28);
		pnlRecord.add(cboProperty);

		JLabel lblClient = new JLabel("Client (buyer)");
		lblClient.setForeground(Theme.TEXT_MUTED);
		lblClient.setFont(Theme.SMALL);
		lblClient.setBounds(15, 100, 270, 18);
		pnlRecord.add(lblClient);

		cboClient = new JComboBox<User>();
		cboClient.setBackground(Color.WHITE);
		cboClient.setFont(Theme.BODY);
		cboClient.setBounds(15, 119, 270, 28);
		pnlRecord.add(cboClient);

		JLabel lblSalePrice = new JLabel("Final sale price (PHP)");
		lblSalePrice.setForeground(Theme.TEXT_MUTED);
		lblSalePrice.setFont(Theme.SMALL);
		lblSalePrice.setBounds(15, 155, 270, 18);
		pnlRecord.add(lblSalePrice);

		txtSalePrice = new JTextField();
		txtSalePrice.setFont(Theme.BODY);
		txtSalePrice.setBorder(Theme.FIELD_BORDER);
		txtSalePrice.setBounds(15, 174, 270, 28);
		pnlRecord.add(txtSalePrice);

		JButton btnRecord = new JButton("Record sale");
		btnRecord.setForeground(Color.WHITE);
		btnRecord.setBackground(Theme.ACCENT);
		btnRecord.setFont(Theme.BODY_BOLD);
		btnRecord.setBorder(new LineBorder(Theme.ACCENT));
		btnRecord.setFocusPainted(false);
		btnRecord.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				doRecordSale();
			}
		});
		btnRecord.setBounds(15, 225, 270, 38);
		pnlRecord.add(btnRecord);

		// ================= SUMMARY =================
		JPanel pnlSummary = new JPanel();
		pnlSummary.setBackground(Color.WHITE);
		pnlSummary.setBorder(new LineBorder(Theme.BORDER));
		pnlSummary.setBounds(25, 320, 300, 185);
		add(pnlSummary);
		pnlSummary.setLayout(null);

		JPanel pnlBar = new JPanel();
		pnlBar.setBackground(Theme.ACCENT);
		pnlBar.setBounds(0, 0, 4, 185);
		pnlSummary.add(pnlBar);

		JLabel lblCountTitle = new JLabel("Number of sales");
		lblCountTitle.setForeground(Theme.TEXT_MUTED);
		lblCountTitle.setFont(Theme.SMALL);
		lblCountTitle.setBounds(18, 18, 270, 18);
		pnlSummary.add(lblCountTitle);

		lblNumberOfSales = new JLabel("0");
		lblNumberOfSales.setForeground(Theme.TEXT);
		lblNumberOfSales.setFont(Theme.BIG_NUMBER);
		lblNumberOfSales.setBounds(18, 38, 270, 38);
		pnlSummary.add(lblNumberOfSales);

		lblTotalTitle = new JLabel("Total sales (PHP)");
		lblTotalTitle.setForeground(Theme.TEXT_MUTED);
		lblTotalTitle.setFont(Theme.SMALL);
		lblTotalTitle.setBounds(18, 95, 270, 18);
		pnlSummary.add(lblTotalTitle);

		lblTotalSales = new JLabel("0.00");
		lblTotalSales.setForeground(Theme.ACCENT);
		lblTotalSales.setFont(Theme.BIG_NUMBER);
		lblTotalSales.setBounds(18, 115, 270, 38);
		pnlSummary.add(lblTotalSales);

		// ================= SALES REPORT =================
		lblReportTitle = new JLabel("Sales report");
		lblReportTitle.setForeground(Theme.TEXT);
		lblReportTitle.setFont(Theme.HEADING);
		lblReportTitle.setBounds(345, 20, 564, 24);
		add(lblReportTitle);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(345, 52, 564, 453);
		add(scrollPane);

		tblSales = new JTable();
		tblSales.setModel(new DefaultTableModel(
			new Object[][] {},
			new String[] { "ID", "Property", "Client", "Agent", "Price (PHP)", "Date" }
		));
		Theme.styleTable(tblSales, scrollPane);
		tblSales.getColumnModel().getColumn(0).setPreferredWidth(35);
		tblSales.getColumnModel().getColumn(1).setPreferredWidth(150);
		scrollPane.setViewportView(tblSales);
	}

	/** Fills the page for the logged-in Agent or Admin. */
	public void loadFor(User user) {
		currentUser = user;
		loadChoices();
		loadReport();
	}

	/** Fills the Property and Client drop-downs. */
	private void loadChoices() {
		cboProperty.removeAllItems();
		for (Property property : AppContext.propertyService().getUnsoldListings()) {
			cboProperty.addItem(property); // the drop-down shows property.toString()
		}

		cboClient.removeAllItems();
		for (User client : AppContext.userService().getClients()) {
			cboClient.addItem(client); // the drop-down shows client.toString()
		}
	}

	private void fillListingPrice() {
		Property property = (Property) cboProperty.getSelectedItem();
		if (property == null) {
			txtSalePrice.setText("");
		} else {
			txtSalePrice.setText(String.format("%.2f", property.getPrice()));
		}
	}

	private void loadReport() {
		ArrayList<Sale> list;
		double total;

		// POLYMORPHISM: Admin's canViewAllSales() returns true, Agent's returns false.
		// OVERLOADING: getTotalSales() for everyone, getTotalSales(agentId) for one agent.
		if (currentUser.canViewAllSales()) {
			lblReportTitle.setText("Sales report - all agents");
			lblTotalTitle.setText("Total sales (PHP)");
			list = AppContext.saleService().getAllSales();
			total = AppContext.saleService().getTotalSales();
		} else {
			lblReportTitle.setText("Sales report - my sales");
			lblTotalTitle.setText("My total sales (PHP)");
			list = AppContext.saleService().getSalesByAgent(currentUser.getUserId());
			total = AppContext.saleService().getTotalSales(currentUser.getUserId());
		}

		DefaultTableModel model = (DefaultTableModel) tblSales.getModel();
		model.setRowCount(0); // clear the table
		for (Sale sale : list) {
			model.addRow(new Object[] { sale.getSaleId(), getPropertyTitle(sale.getPropertyId()),
					getUserName(sale.getClientId()), getUserName(sale.getAgentId()),
					String.format("%,.2f", sale.getSalePrice()), sale.getSaleDate() });
		}

		lblNumberOfSales.setText(String.valueOf(list.size()));
		lblTotalSales.setText(String.format("%,.2f", total));
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

	private void doRecordSale() {
		Property property = (Property) cboProperty.getSelectedItem();
		User client = (User) cboClient.getSelectedItem();

		int propertyId = 0;
		if (property != null) {
			propertyId = property.getPropertyId();
		}
		int clientId = 0;
		if (client != null) {
			clientId = client.getUserId();
		}

		boolean ok = AppContext.saleService().recordSale(propertyId, clientId, txtSalePrice.getText());
		if (ok) {
			JOptionPane.showMessageDialog(this, AppContext.saleService().getMessage(), "Done",
					JOptionPane.INFORMATION_MESSAGE);
			loadChoices(); // the sold property disappears from the drop-down
			loadReport();
		} else {
			JOptionPane.showMessageDialog(this, AppContext.saleService().getMessage(), "Please check",
					JOptionPane.WARNING_MESSAGE);
		}
	}
}
