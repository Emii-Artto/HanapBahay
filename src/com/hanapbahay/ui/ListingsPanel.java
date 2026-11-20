package com.hanapbahay.ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import javax.swing.DefaultComboBoxModel;
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
import com.hanapbahay.model.User;

/**
 * Property listings page. Owner: UI member. Shown inside MainFrame.
 * Everyone can search and filter. Agents and Admins can add, update and delete (CRUD).
 * Clients can select a listing and send an inquiry.
 */
public class ListingsPanel extends JPanel {

	private static final long serialVersionUID = 1L;
	private JTextField txtSearch;
	private JTextField txtMinPrice;
	private JTextField txtMaxPrice;
	private JScrollPane scrollPane;
	private JTable tblListings;
	private JLabel lblResult;
	private JPanel pnlForm;
	private JTextField txtTitle;
	private JTextField txtLocation;
	private JComboBox<String> cboType;
	private JTextField txtPrice;
	private JComboBox<String> cboStatus;
	private JButton btnInquire;

	private User currentUser;
	private int selectedId = 0; // ID of the listing clicked in the table (0 = none)

	public ListingsPanel() {
		setPreferredSize(new Dimension(934, 581));
		setBackground(Theme.PAGE_BG);
		setLayout(null);

		// ================= SEARCH AND FILTER BAR =================
		JPanel pnlToolbar = new JPanel();
		pnlToolbar.setBackground(Color.WHITE);
		pnlToolbar.setBorder(new LineBorder(Theme.BORDER));
		pnlToolbar.setBounds(25, 20, 884, 60);
		add(pnlToolbar);
		pnlToolbar.setLayout(null);

		JLabel lblSearch = new JLabel("Search");
		lblSearch.setForeground(Theme.TEXT);
		lblSearch.setFont(Theme.BODY_BOLD);
		lblSearch.setBounds(15, 17, 50, 26);
		pnlToolbar.add(lblSearch);

		txtSearch = new JTextField();
		txtSearch.setToolTipText("Title or location, e.g. condo or Quezon");
		txtSearch.setFont(Theme.BODY);
		txtSearch.setBorder(Theme.FIELD_BORDER);
		txtSearch.setBounds(68, 17, 190, 28);
		pnlToolbar.add(txtSearch);

		JButton btnSearch = new JButton("Search");
		btnSearch.setForeground(Color.WHITE);
		btnSearch.setBackground(Theme.ACCENT);
		btnSearch.setFont(Theme.BODY_BOLD);
		btnSearch.setBorder(new LineBorder(Theme.ACCENT));
		btnSearch.setFocusPainted(false);
		btnSearch.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// Overloaded method: one String = search by title or location
				loadTable(AppContext.propertyService().searchListings(txtSearch.getText()));
			}
		});
		btnSearch.setBounds(265, 17, 80, 28);
		pnlToolbar.add(btnSearch);

		JLabel lblPrice = new JLabel("Price");
		lblPrice.setForeground(Theme.TEXT);
		lblPrice.setFont(Theme.BODY_BOLD);
		lblPrice.setBounds(370, 17, 40, 26);
		pnlToolbar.add(lblPrice);

		txtMinPrice = new JTextField();
		txtMinPrice.setToolTipText("Minimum price");
		txtMinPrice.setFont(Theme.BODY);
		txtMinPrice.setBorder(Theme.FIELD_BORDER);
		txtMinPrice.setBounds(410, 17, 105, 28);
		pnlToolbar.add(txtMinPrice);

		JLabel lblTo = new JLabel("to");
		lblTo.setForeground(Theme.TEXT_MUTED);
		lblTo.setFont(Theme.BODY);
		lblTo.setBounds(521, 17, 20, 26);
		pnlToolbar.add(lblTo);

		txtMaxPrice = new JTextField();
		txtMaxPrice.setToolTipText("Maximum price");
		txtMaxPrice.setFont(Theme.BODY);
		txtMaxPrice.setBorder(Theme.FIELD_BORDER);
		txtMaxPrice.setBounds(540, 17, 105, 28);
		pnlToolbar.add(txtMaxPrice);

		JButton btnFilter = new JButton("Filter");
		btnFilter.setForeground(Color.WHITE);
		btnFilter.setBackground(Theme.ACCENT);
		btnFilter.setFont(Theme.BODY_BOLD);
		btnFilter.setBorder(new LineBorder(Theme.ACCENT));
		btnFilter.setFocusPainted(false);
		btnFilter.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				doFilterByPrice();
			}
		});
		btnFilter.setBounds(652, 17, 80, 28);
		pnlToolbar.add(btnFilter);

		JButton btnShowAll = new JButton("Clear filters");
		btnShowAll.setForeground(Theme.TEXT);
		btnShowAll.setBackground(Color.WHITE);
		btnShowAll.setFont(Theme.BODY_BOLD);
		btnShowAll.setBorder(new LineBorder(Theme.BORDER));
		btnShowAll.setFocusPainted(false);
		btnShowAll.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				txtSearch.setText("");
				txtMinPrice.setText("");
				txtMaxPrice.setText("");
				loadTable(AppContext.propertyService().getAllListings());
			}
		});
		btnShowAll.setBounds(759, 17, 110, 28);
		pnlToolbar.add(btnShowAll);

		// ================= TABLE =================
		scrollPane = new JScrollPane();
		scrollPane.setBounds(25, 95, 570, 410);
		add(scrollPane);

		tblListings = new JTable();
		tblListings.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				selectListing();
			}
		});
		tblListings.setModel(new DefaultTableModel(
			new Object[][] {},
			new String[] { "ID", "Title", "Location", "Type", "Price (PHP)", "Status" }
		));
		Theme.styleTable(tblListings, scrollPane);
		tblListings.getColumnModel().getColumn(0).setPreferredWidth(35);
		tblListings.getColumnModel().getColumn(1).setPreferredWidth(170);
		tblListings.getColumnModel().getColumn(2).setPreferredWidth(130);
		scrollPane.setViewportView(tblListings);

		lblResult = new JLabel("");
		lblResult.setForeground(Theme.TEXT_MUTED);
		lblResult.setFont(Theme.SMALL);
		lblResult.setBounds(25, 518, 400, 30);
		add(lblResult);

		btnInquire = new JButton("Send inquiry");
		btnInquire.setForeground(Color.WHITE);
		btnInquire.setBackground(Theme.ACCENT);
		btnInquire.setFont(Theme.BODY_BOLD);
		btnInquire.setBorder(new LineBorder(Theme.ACCENT));
		btnInquire.setFocusPainted(false);
		btnInquire.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				doSendInquiry();
			}
		});
		btnInquire.setBounds(759, 516, 150, 36);
		add(btnInquire);

		// ================= FORM (Agents and Admins) =================
		pnlForm = new JPanel();
		pnlForm.setBackground(Color.WHITE);
		pnlForm.setBorder(new LineBorder(Theme.BORDER));
		pnlForm.setBounds(610, 95, 299, 410);
		add(pnlForm);
		pnlForm.setLayout(null);

		JLabel lblFormTitle = new JLabel("Listing details");
		lblFormTitle.setForeground(Theme.TEXT);
		lblFormTitle.setFont(Theme.HEADING);
		lblFormTitle.setBounds(15, 12, 269, 24);
		pnlForm.add(lblFormTitle);

		JLabel lblTitle = new JLabel("Title");
		lblTitle.setForeground(Theme.TEXT_MUTED);
		lblTitle.setFont(Theme.SMALL);
		lblTitle.setBounds(15, 45, 269, 18);
		pnlForm.add(lblTitle);

		txtTitle = new JTextField();
		txtTitle.setFont(Theme.BODY);
		txtTitle.setBorder(Theme.FIELD_BORDER);
		txtTitle.setBounds(15, 64, 269, 28);
		pnlForm.add(txtTitle);

		JLabel lblLocation = new JLabel("Location");
		lblLocation.setForeground(Theme.TEXT_MUTED);
		lblLocation.setFont(Theme.SMALL);
		lblLocation.setBounds(15, 97, 269, 18);
		pnlForm.add(lblLocation);

		txtLocation = new JTextField();
		txtLocation.setFont(Theme.BODY);
		txtLocation.setBorder(Theme.FIELD_BORDER);
		txtLocation.setBounds(15, 116, 269, 28);
		pnlForm.add(txtLocation);

		JLabel lblType = new JLabel("Type");
		lblType.setForeground(Theme.TEXT_MUTED);
		lblType.setFont(Theme.SMALL);
		lblType.setBounds(15, 149, 269, 18);
		pnlForm.add(lblType);

		cboType = new JComboBox<String>();
		cboType.setBackground(Color.WHITE);
		cboType.setFont(Theme.BODY);
		cboType.setModel(new DefaultComboBoxModel<String>(
				new String[] { "House", "Townhouse", "Condo", "Apartment", "Lot" }));
		cboType.setBounds(15, 168, 269, 28);
		pnlForm.add(cboType);

		JLabel lblPriceField = new JLabel("Price (PHP)");
		lblPriceField.setForeground(Theme.TEXT_MUTED);
		lblPriceField.setFont(Theme.SMALL);
		lblPriceField.setBounds(15, 201, 269, 18);
		pnlForm.add(lblPriceField);

		txtPrice = new JTextField();
		txtPrice.setFont(Theme.BODY);
		txtPrice.setBorder(Theme.FIELD_BORDER);
		txtPrice.setBounds(15, 220, 269, 28);
		pnlForm.add(txtPrice);

		JLabel lblStatus = new JLabel("Status");
		lblStatus.setForeground(Theme.TEXT_MUTED);
		lblStatus.setFont(Theme.SMALL);
		lblStatus.setBounds(15, 253, 269, 18);
		pnlForm.add(lblStatus);

		cboStatus = new JComboBox<String>();
		cboStatus.setBackground(Color.WHITE);
		cboStatus.setFont(Theme.BODY);
		cboStatus.setModel(new DefaultComboBoxModel<String>(new String[] { "AVAILABLE", "RESERVED" }));
		cboStatus.setBounds(15, 272, 269, 28);
		pnlForm.add(cboStatus);

		JButton btnAdd = new JButton("Add");
		btnAdd.setForeground(Color.WHITE);
		btnAdd.setBackground(Theme.ACCENT);
		btnAdd.setFont(Theme.BODY_BOLD);
		btnAdd.setBorder(new LineBorder(Theme.ACCENT));
		btnAdd.setFocusPainted(false);
		btnAdd.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				doAdd();
			}
		});
		btnAdd.setBounds(15, 318, 130, 34);
		pnlForm.add(btnAdd);

		JButton btnUpdate = new JButton("Update");
		btnUpdate.setForeground(Theme.ACCENT);
		btnUpdate.setBackground(Color.WHITE);
		btnUpdate.setFont(Theme.BODY_BOLD);
		btnUpdate.setBorder(new LineBorder(Theme.ACCENT));
		btnUpdate.setFocusPainted(false);
		btnUpdate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				doUpdate();
			}
		});
		btnUpdate.setBounds(154, 318, 130, 34);
		pnlForm.add(btnUpdate);

		JButton btnDelete = new JButton("Delete");
		btnDelete.setForeground(Theme.DANGER);
		btnDelete.setBackground(Color.WHITE);
		btnDelete.setFont(Theme.BODY_BOLD);
		btnDelete.setBorder(new LineBorder(Theme.DANGER));
		btnDelete.setFocusPainted(false);
		btnDelete.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				doDelete();
			}
		});
		btnDelete.setBounds(15, 360, 130, 34);
		pnlForm.add(btnDelete);

		JButton btnClear = new JButton("Clear");
		btnClear.setForeground(Theme.TEXT);
		btnClear.setBackground(Color.WHITE);
		btnClear.setFont(Theme.BODY_BOLD);
		btnClear.setBorder(new LineBorder(Theme.BORDER));
		btnClear.setFocusPainted(false);
		btnClear.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				clearForm();
			}
		});
		btnClear.setBounds(154, 360, 130, 34);
		pnlForm.add(btnClear);
	}

	/** Fills the page for the logged-in user. */
	public void loadFor(User user) {
		currentUser = user;

		// POLYMORPHISM: Admin and Agent return true, Client returns false
		pnlForm.setVisible(user.canManageListings());
		btnInquire.setVisible(!user.canManageListings());
		if (!user.canManageListings()) {
			scrollPane.setBounds(25, 95, 884, 410); // no form for clients, so use the full width
		}

		loadTable(AppContext.propertyService().getAllListings());
	}

	private void loadTable(ArrayList<Property> list) {
		DefaultTableModel model = (DefaultTableModel) tblListings.getModel();
		model.setRowCount(0); // clear the table
		for (Property p : list) {
			model.addRow(new Object[] { p.getPropertyId(), p.getTitle(), p.getLocation(), p.getType(),
					String.format("%,.2f", p.getPrice()), p.getStatus() });
		}
		lblResult.setText("Showing " + list.size() + " listing(s). Click a row to select it.");
		clearForm();
	}

	/** When a row is clicked, copy that listing into the form. */
	private void selectListing() {
		int row = tblListings.getSelectedRow();
		if (row < 0) {
			return;
		}
		selectedId = Integer.parseInt(tblListings.getValueAt(row, 0).toString());

		Property p = AppContext.propertyService().getListing(selectedId);
		if (p != null) {
			txtTitle.setText(p.getTitle());
			txtLocation.setText(p.getLocation());
			cboType.setSelectedItem(p.getType());
			txtPrice.setText(String.format("%.2f", p.getPrice()));
			cboStatus.setSelectedItem(p.getStatus());
		}
	}

	private void clearForm() {
		selectedId = 0;
		txtTitle.setText("");
		txtLocation.setText("");
		txtPrice.setText("");
		cboType.setSelectedIndex(0);
		cboStatus.setSelectedIndex(0);
		tblListings.clearSelection();
	}

	private void doFilterByPrice() {
		// Overloaded method: two Strings = search by price range
		ArrayList<Property> list = AppContext.propertyService().searchListings(txtMinPrice.getText(),
				txtMaxPrice.getText());
		if (list == null) {
			showResult(false, AppContext.propertyService().getMessage());
		} else {
			loadTable(list);
		}
	}

	private void doAdd() {
		boolean ok = AppContext.propertyService().addListing(txtTitle.getText(), txtLocation.getText(),
				(String) cboType.getSelectedItem(), txtPrice.getText(), currentUser.getUserId());
		showResult(ok, AppContext.propertyService().getMessage());
		if (ok) {
			loadTable(AppContext.propertyService().getAllListings());
		}
	}

	private void doUpdate() {
		if (selectedId == 0) {
			showResult(false, "Select a listing in the table first.");
			return;
		}
		boolean ok = AppContext.propertyService().updateListing(selectedId, txtTitle.getText(),
				txtLocation.getText(), (String) cboType.getSelectedItem(), txtPrice.getText(),
				(String) cboStatus.getSelectedItem());
		showResult(ok, AppContext.propertyService().getMessage());
		if (ok) {
			loadTable(AppContext.propertyService().getAllListings());
		}
	}

	private void doDelete() {
		if (selectedId == 0) {
			showResult(false, "Select a listing in the table first.");
			return;
		}
		int answer = JOptionPane.showConfirmDialog(this, "Delete this listing? This can't be undone.",
				"Delete listing", JOptionPane.YES_NO_OPTION);
		if (answer == JOptionPane.YES_OPTION) {
			boolean ok = AppContext.propertyService().removeListing(selectedId);
			showResult(ok, AppContext.propertyService().getMessage());
			if (ok) {
				loadTable(AppContext.propertyService().getAllListings());
			}
		}
	}

	private void doSendInquiry() {
		if (selectedId == 0) {
			showResult(false, "Select a listing in the table first.");
			return;
		}
		String text = JOptionPane.showInputDialog(this, "Your message to the agent:", "Send inquiry",
				JOptionPane.QUESTION_MESSAGE);
		if (text != null) { // null means the user clicked Cancel
			boolean ok = AppContext.inquiryService().submitInquiry(selectedId, currentUser.getUserId(), text);
			showResult(ok, AppContext.inquiryService().getMessage());
		}
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
