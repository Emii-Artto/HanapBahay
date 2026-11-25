package com.hanapbahay.ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
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
import javax.swing.table.DefaultTableModel;

import com.hanapbahay.AppContext;
import com.hanapbahay.model.Agent;
import com.hanapbahay.model.Client;
import com.hanapbahay.model.Property;
import com.hanapbahay.service.BaseService;
import com.hanapbahay.service.ClientService;
import com.hanapbahay.service.ReportGenerator;

/**
 * 3.1.3 Client Management: register and manage buyers, search them (5.4.5),
 * and Find Matches (5.4.6).
 */
public class ClientsPanel extends BasePanel {

	private static final long serialVersionUID = 1L;
	private static final String[] PREFERRED_TYPES = { Client.ANY_TYPE, Property.HOUSE_AND_LOT, Property.VACANT_LOT,
			Property.CONDOMINIUM };

	private ArrayList<Client> shown = new ArrayList<Client>();
	private JTextField txtSearch;
	private JLabel lblResult;
	private DefaultTableModel tableModel;
	private JTable table;

	public ClientsPanel(Agent agent) {
		super(agent);
		add(createSearch(), BorderLayout.NORTH);
		add(createTable(), BorderLayout.CENTER);
		add(createButtons(), BorderLayout.SOUTH);
		refresh();
	}

	@Override
	public String getTitle() {
		return "Clients";
	}

	private JPanel createSearch() {
		JPanel card = Theme.card();
		card.setLayout(new BorderLayout(10, 6));
		card.add(Theme.caption("Search by name or contact number"), BorderLayout.NORTH);
		txtSearch = Theme.field();
		card.add(txtSearch, BorderLayout.CENTER);

		JPanel buttons = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
		buttons.setOpaque(false);
		JButton btnSearch = Theme.goldButton("Search");
		JButton btnReset = Theme.grayButton("Reset");
		ActionListener search = new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				refresh();
			}
		};
		btnSearch.addActionListener(search);
		txtSearch.addActionListener(search);
		btnReset.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				txtSearch.setText("");
				refresh();
			}
		});
		buttons.add(btnSearch);
		buttons.add(btnReset);
		card.add(buttons, BorderLayout.EAST);

		lblResult = Theme.mutedLabel(" ");
		card.add(lblResult, BorderLayout.SOUTH);
		return card;
	}

	private JScrollPane createTable() {
		String[] columns = { "Name", "Contact Number", "Email", "Budget", "Preferred Location", "Preferred Type" };
		tableModel = new DefaultTableModel(columns, 0) {
			private static final long serialVersionUID = 1L;

			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};
		table = new JTable(tableModel);
		return Theme.styleTable(table);
	}

	@Override
	public void refresh() {
		ClientService service = AppContext.clientService();
		shown = service.search(agentId(), txtSearch.getText());
		tableModel.setRowCount(0);
		for (Client c : shown) {
			tableModel.addRow(new Object[] { c.getFullName(), c.getContactNumber(), c.getEmail(),
					BaseService.peso(c.getBudget()), c.getPreferredLocation(), c.getPreferredType() });
		}
		lblResult.setText(shown.size() + (shown.size() == 1 ? " client" : " clients")
				+ ". Select a client and click Find Matches to see properties that fit.");
	}

	private JPanel createButtons() {
		JPanel buttons = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
		buttons.setOpaque(false);
		JButton btnMatches = Theme.button("Find Matches", Theme.BLUE, Theme.WHITE);
		JButton btnEdit = Theme.grayButton("Edit");
		JButton btnDelete = Theme.redButton("Delete");
		JButton btnAdd = Theme.goldButton("+ Add Client");
		btnMatches.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				findMatches();
			}
		});
		btnEdit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Client selected = getSelected();
				if (selected != null) {
					showClientForm(selected);
				}
			}
		});
		btnDelete.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				deleteSelected();
			}
		});
		btnAdd.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				showClientForm(null);
			}
		});
		buttons.add(btnMatches);
		buttons.add(btnEdit);
		buttons.add(btnDelete);
		buttons.add(btnAdd);
		return buttons;
	}

	private Client getSelected() {
		int row = table.getSelectedRow();
		if (row < 0) {
			showWarning("Please select a client in the table first.");
			return null;
		}
		return shown.get(row);
	}

	private void deleteSelected() {
		Client selected = getSelected();
		if (selected == null || !confirm("Delete the client " + selected.getFullName() + "?")) {
			return;
		}
		ClientService service = AppContext.clientService();
		if (service.deleteClient(agentId(), selected.getClientId())) {
			refresh();
			showInfo(service.getMessage());
		} else {
			showWarning(service.getMessage());
		}
	}

	/** The Add / Edit client form. client == null means Add. */
	private void showClientForm(Client client) {
		boolean adding = client == null;
		JTextField txtFirst = Theme.field();
		JTextField txtLast = Theme.field();
		JTextField txtContact = Theme.field();
		JTextField txtEmail = Theme.field();
		JTextField txtBudget = Theme.field();
		JTextField txtLocation = Theme.field();
		JComboBox<String> cboType = new JComboBox<String>(PREFERRED_TYPES);
		if (!adding) {
			txtFirst.setText(client.getFirstName());
			txtLast.setText(client.getLastName());
			txtContact.setText(client.getContactNumber());
			txtEmail.setText(client.getEmail());
			txtBudget.setText(String.format("%.2f", client.getBudget()));
			txtLocation.setText(client.getPreferredLocation());
			cboType.setSelectedItem(client.getPreferredType());
		}
		JPanel form = Theme.form(new Object[] { "First name *", txtFirst, "Last name *", txtLast, "Contact number *",
				txtContact, "Email", txtEmail, "Budget (PHP) *", txtBudget, "Preferred location", txtLocation,
				"Preferred type", cboType });

		ClientService service = AppContext.clientService();
		while (showForm(form, adding ? "Add Client" : "Edit Client")) {
			boolean saved;
			if (adding) {
				saved = service.addClient(agentId(), txtFirst.getText(), txtLast.getText(), txtContact.getText(),
						txtEmail.getText(), txtBudget.getText(), txtLocation.getText(),
						(String) cboType.getSelectedItem());
			} else {
				saved = service.updateClient(agentId(), client.getClientId(), txtFirst.getText(), txtLast.getText(),
						txtContact.getText(), txtEmail.getText(), txtBudget.getText(), txtLocation.getText(),
						(String) cboType.getSelectedItem());
			}
			if (saved) {
				refresh();
				showInfo(service.getMessage());
				return;
			}
			showWarning(service.getMessage());
		}
	}

	/** 5.4.6 Find Matches: available properties that fit the selected client. */
	private void findMatches() {
		Client client = getSelected();
		if (client == null) {
			return;
		}
		ClientService service = AppContext.clientService();
		ArrayList<Property> matches = service.findMatches(client);
		if (matches.isEmpty()) {
			showInfo(service.getMessage());
			return;
		}
		DefaultTableModel model = new DefaultTableModel(
				new String[] { "ID", "Title", "Type", "Subtype", "Location", "Price", "Status" }, 0);
		for (Property p : matches) {
			model.addRow(new Object[] { "P" + String.format("%03d", p.getPropertyId()), p.getTitle(),
					p.getPropertyType(), p.getSubtype(), p.getLocation(), BaseService.peso(p.getPrice()),
					ReportGenerator.titleCase(p.getStatus()) });
		}
		JTable matchTable = new JTable(model);
		matchTable.setEnabled(false);
		JScrollPane scroll = Theme.styleTable(matchTable);
		scroll.setPreferredSize(new Dimension(760, Math.min(80 + matches.size() * 36, 360)));

		JPanel content = new JPanel(new BorderLayout(0, 8));
		content.add(new JLabel(service.getMessage() + " Budget: " + BaseService.peso(client.getBudget())
				+ ", location: " + (client.getPreferredLocation().isEmpty() ? "any" : client.getPreferredLocation())
				+ ", type: " + client.getPreferredType()), BorderLayout.NORTH);
		content.add(scroll, BorderLayout.CENTER);
		JOptionPane.showMessageDialog(this, content, "Find Matches - " + client.getFullName(),
				JOptionPane.PLAIN_MESSAGE);
	}
}
