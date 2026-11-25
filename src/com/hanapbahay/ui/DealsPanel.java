package com.hanapbahay.ui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SpinnerDateModel;
import javax.swing.table.DefaultTableModel;

import com.hanapbahay.AppContext;
import com.hanapbahay.model.Agent;
import com.hanapbahay.model.Client;
import com.hanapbahay.model.Deal;
import com.hanapbahay.model.PaymentMethod;
import com.hanapbahay.model.Property;
import com.hanapbahay.service.BaseService;
import com.hanapbahay.service.DealService;
import com.hanapbahay.service.ReportGenerator;

/**
 * 3.1.5 Deal and Payment Recording.
 * Recording a deal makes the property Reserved (Pending) or Sold (Completed).
 */
public class DealsPanel extends BasePanel {

	private static final long serialVersionUID = 1L;
	private static final String[] STATUSES = { "All", "Pending", "Completed", "Cancelled" };
	private static final String NEW_PENDING = "Pending (property becomes Reserved)";
	private static final String NEW_COMPLETED = "Completed (property becomes Sold)";

	private ArrayList<Deal> shown = new ArrayList<Deal>();
	private JComboBox<String> cboStatus;
	private JLabel lblResult;
	private DefaultTableModel tableModel;
	private JTable table;

	public DealsPanel(Agent agent) {
		super(agent);
		add(createFilter(), BorderLayout.NORTH);
		add(createTable(), BorderLayout.CENTER);
		add(createButtons(), BorderLayout.SOUTH);
		refresh();
	}

	@Override
	public String getTitle() {
		return "Deals";
	}

	private JPanel createFilter() {
		JPanel card = Theme.card();
		card.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 0));
		card.add(Theme.caption("Show"));
		cboStatus = new JComboBox<String>(STATUSES);
		Theme.styleCombo(cboStatus);
		cboStatus.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				refresh();
			}
		});
		card.add(cboStatus);
		lblResult = Theme.mutedLabel(" ");
		card.add(lblResult);
		return card;
	}

	private JScrollPane createTable() {
		String[] columns = { "Date", "Client", "Property", "Payment Method", "Agreed Price", "Status" };
		tableModel = new DefaultTableModel(columns, 0) {
			private static final long serialVersionUID = 1L;

			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};
		table = new JTable(tableModel);
		JScrollPane scroll = Theme.styleTable(table);
		table.getColumnModel().getColumn(5).setCellRenderer(new Theme.StatusRenderer());
		return scroll;
	}

	@Override
	public void refresh() {
		String filter = (String) cboStatus.getSelectedItem();
		DealService service = AppContext.dealService();
		ArrayList<Deal> all = service.getDeals(agentId());
		ArrayList<Client> clients = AppContext.clientService().getClients(agentId());
		ArrayList<Property> properties = AppContext.propertyService().getListings(agentId());
		ArrayList<PaymentMethod> methods = service.getPaymentMethods();

		shown = new ArrayList<Deal>();
		tableModel.setRowCount(0);
		double completedTotal = 0;
		for (Deal d : all) {
			String status = ReportGenerator.titleCase(d.getStatus());
			if (!"All".equals(filter) && !filter.equals(status)) {
				continue;
			}
			shown.add(d);
			if (d.isCompleted()) {
				completedTotal += d.getAgreedPrice();
			}
			tableModel.addRow(new Object[] { d.getDealDate(), ReportGenerator.clientName(clients, d.getClientId()),
					ReportGenerator.propertyTitle(properties, d.getPropertyId()),
					ReportGenerator.methodName(methods, d.getPaymentMethodId()), BaseService.peso(d.getAgreedPrice()),
					status });
		}
		lblResult.setText(shown.size() + (shown.size() == 1 ? " deal" : " deals") + ", completed total "
				+ BaseService.peso(completedTotal));
	}

	private JPanel createButtons() {
		JPanel buttons = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
		buttons.setOpaque(false);
		JButton btnComplete = Theme.button("Mark Completed (Sold)", Theme.BLUE, Theme.WHITE);
		JButton btnCancel = Theme.redButton("Cancel Deal");
		JButton btnRecord = Theme.goldButton("+ Record Deal");
		btnComplete.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				changeStatus(Deal.COMPLETED);
			}
		});
		btnCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				changeStatus(Deal.CANCELLED);
			}
		});
		btnRecord.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				showDealForm();
			}
		});
		buttons.add(btnComplete);
		buttons.add(btnCancel);
		buttons.add(btnRecord);
		return buttons;
	}

	private void changeStatus(String newStatus) {
		int row = table.getSelectedRow();
		if (row < 0) {
			showWarning("Please select a deal in the table first.");
			return;
		}
		if (Deal.CANCELLED.equals(newStatus) && !confirm("Cancel this deal? The property becomes Available again.")) {
			return;
		}
		DealService service = AppContext.dealService();
		if (service.updateStatus(agentId(), shown.get(row).getDealId(), newStatus)) {
			refresh();
			showInfo(service.getMessage());
		} else {
			showWarning(service.getMessage());
		}
	}

	private void showDealForm() {
		ArrayList<Client> clients = AppContext.clientService().getClients(agentId());
		if (clients.isEmpty()) {
			showWarning("Please add the buyer in the Clients section first.");
			return;
		}
		JComboBox<Client> cboClient = new JComboBox<Client>();
		for (Client c : clients) {
			cboClient.addItem(c);
		}
		// only Available properties can get a new deal (5.3.6)
		final JComboBox<Property> cboProperty = new JComboBox<Property>();
		for (Property p : AppContext.propertyService().getListings(agentId())) {
			if (p.isAvailable()) {
				cboProperty.addItem(p);
			}
		}
		if (cboProperty.getItemCount() == 0) {
			showWarning("You have no Available properties. A property that is Reserved or Sold already has a deal.");
			return;
		}
		JComboBox<PaymentMethod> cboMethod = new JComboBox<PaymentMethod>();
		for (PaymentMethod m : AppContext.dealService().getPaymentMethods()) {
			cboMethod.addItem(m);
		}
		final JTextField txtPrice = Theme.field();
		// start with the listing price; the agent can type the agreed price
		txtPrice.setText(String.format("%.2f", ((Property) cboProperty.getSelectedItem()).getPrice()));
		cboProperty.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Property p = (Property) cboProperty.getSelectedItem();
				if (p != null) {
					txtPrice.setText(String.format("%.2f", p.getPrice()));
				}
			}
		});
		JSpinner spnDate = new JSpinner(new SpinnerDateModel(Calendar.getInstance().getTime(), null, null,
				Calendar.DAY_OF_MONTH));
		spnDate.setEditor(new JSpinner.DateEditor(spnDate, "yyyy-MM-dd"));
		JComboBox<String> cboNewStatus = new JComboBox<String>(new String[] { NEW_PENDING, NEW_COMPLETED });

		JPanel form = Theme.form(new Object[] { "Client (buyer) *", cboClient, "Property *", cboProperty,
				"Payment method *", cboMethod, "Agreed price (PHP) *", txtPrice, "Deal date *", spnDate,
				"Deal status *", cboNewStatus });

		DealService service = AppContext.dealService();
		while (showForm(form, "Record Deal")) {
			String date = new SimpleDateFormat("yyyy-MM-dd").format((Date) spnDate.getValue());
			String status = NEW_COMPLETED.equals(cboNewStatus.getSelectedItem()) ? Deal.COMPLETED : Deal.PENDING;
			if (service.recordDeal(agentId(), (Client) cboClient.getSelectedItem(),
					(Property) cboProperty.getSelectedItem(), (PaymentMethod) cboMethod.getSelectedItem(),
					txtPrice.getText(), date, status)) {
				refresh();
				showInfo(service.getMessage());
				return;
			}
			showWarning(service.getMessage());
		}
	}
}
