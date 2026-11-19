package com.hanapbahay.ui;

import java.awt.Font;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import com.hanapbahay.AppContext;
import com.hanapbahay.model.Property;
import com.hanapbahay.model.User;

/**
 * Main screen after login. Owner: UI member.
 * Placeholder: shows the current listings. Add menus/tabs for Listings, Inquiries and Sales here.
 */
public class MainFrame extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JLabel lblWelcome;
	private JTable tblListings;

	public MainFrame() {
		setTitle("HanapBahay - Real Estate Listing, Inquiry, and Sales Monitoring");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 700, 450);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		lblWelcome = new JLabel("Welcome!");
		lblWelcome.setFont(new Font("Tahoma", Font.BOLD, 16));
		lblWelcome.setBounds(15, 10, 655, 30);
		contentPane.add(lblWelcome);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(15, 50, 655, 340);
		contentPane.add(scrollPane);

		tblListings = new JTable();
		tblListings.setModel(new DefaultTableModel(
			new Object[][] {},
			new String[] { "ID", "Title", "Location", "Type", "Price", "Status" }
		));
		scrollPane.setViewportView(tblListings);
	}

	/** Called after a successful login. Kept out of the constructor so WindowBuilder can open this file. */
	public void showFor(User user) {
		lblWelcome.setText("Welcome, " + user.getFullName() + " (" + user.getRole() + ")");
		loadListings();
		setLocationRelativeTo(null);
		setVisible(true);
	}

	private void loadListings() {
		DefaultTableModel model = (DefaultTableModel) tblListings.getModel();
		model.setRowCount(0);
		List<Property> listings = AppContext.propertyService().getAllListings();
		for (Property p : listings) {
			model.addRow(new Object[] { p.getPropertyId(), p.getTitle(), p.getLocation(),
					p.getType(), p.getPrice(), p.getStatus() });
		}
	}
}
