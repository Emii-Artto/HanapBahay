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
import javax.swing.SpinnerDateModel;
import javax.swing.table.DefaultTableModel;

import com.hanapbahay.AppContext;
import com.hanapbahay.model.Agent;
import com.hanapbahay.model.Client;
import com.hanapbahay.model.Property;
import com.hanapbahay.model.Viewing;
import com.hanapbahay.service.ReportGenerator;
import com.hanapbahay.service.ViewingService;

/**
 * 3.1.4 Viewing Appointment Scheduling: schedule visits and mark them Completed or Cancelled.
 * A property cannot have two viewings at the same date and time (5.3.5).
 */
public class ViewingPanel extends BasePanel {

	private static final long serialVersionUID = 1L;
	private static final String[] STATUSES = { "All", "Scheduled", "Completed", "Cancelled" };

	private ArrayList<Viewing> shown = new ArrayList<Viewing>();
	private JComboBox<String> cboStatus;
	private JLabel lblResult;
	private DefaultTableModel tableModel;
	private JTable table;

	public ViewingPanel(Agent agent) {
		super(agent);
		add(createFilter(), BorderLayout.NORTH);
		add(createTable(), BorderLayout.CENTER);
		add(createButtons(), BorderLayout.SOUTH);
		refresh();
	}

	@Override
	public String getTitle() {
		return "Viewing Schedule";
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
		String[] columns = { "Date", "Time", "Client", "Property", "Status" };
		tableModel = new DefaultTableModel(columns, 0) {
			private static final long serialVersionUID = 1L;

			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};
		table = new JTable(tableModel);
		JScrollPane scroll = Theme.styleTable(table);
		table.getColumnModel().getColumn(4).setCellRenderer(new Theme.StatusRenderer());
		return scroll;
	}

	@Override
	public void refresh() {
		String filter = (String) cboStatus.getSelectedItem();
		ArrayList<Viewing> all = AppContext.viewingService().getViewings(agentId());
		ArrayList<Client> clients = AppContext.clientService().getClients(agentId());
		ArrayList<Property> properties = AppContext.propertyService().getListings(agentId());

		shown = new ArrayList<Viewing>();
		tableModel.setRowCount(0);
		for (Viewing v : all) {
			String status = ReportGenerator.titleCase(v.getStatus());
			if (!"All".equals(filter) && !filter.equals(status)) {
				continue;
			}
			shown.add(v);
			tableModel.addRow(new Object[] { v.getViewingDate(), v.getViewingTime(),
					ReportGenerator.clientName(clients, v.getClientId()),
					ReportGenerator.propertyTitle(properties, v.getPropertyId()), status });
		}
		lblResult.setText(shown.size() + (shown.size() == 1 ? " viewing" : " viewings"));
	}

	private JPanel createButtons() {
		JPanel buttons = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
		buttons.setOpaque(false);
		JButton btnCompleted = Theme.button("Mark Completed", Theme.BLUE, Theme.WHITE);
		JButton btnCancelled = Theme.redButton("Mark Cancelled");
		JButton btnSchedule = Theme.goldButton("+ Schedule Viewing");
		btnCompleted.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				changeStatus(Viewing.COMPLETED);
			}
		});
		btnCancelled.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				changeStatus(Viewing.CANCELLED);
			}
		});
		btnSchedule.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				showScheduleForm();
			}
		});
		buttons.add(btnCompleted);
		buttons.add(btnCancelled);
		buttons.add(btnSchedule);
		return buttons;
	}

	private void changeStatus(String newStatus) {
		int row = table.getSelectedRow();
		if (row < 0) {
			showWarning("Please select a viewing in the table first.");
			return;
		}
		ViewingService service = AppContext.viewingService();
		if (service.updateStatus(agentId(), shown.get(row).getViewingId(), newStatus)) {
			refresh();
		} else {
			showWarning(service.getMessage());
		}
	}

	private void showScheduleForm() {
		ArrayList<Client> clients = AppContext.clientService().getClients(agentId());
		if (clients.isEmpty()) {
			showWarning("Please add a client in the Clients section first.");
			return;
		}
		JComboBox<Client> cboClient = new JComboBox<Client>();
		for (Client c : clients) {
			cboClient.addItem(c);
		}
		JComboBox<Property> cboProperty = new JComboBox<Property>();
		for (Property p : AppContext.propertyService().getListings(agentId())) {
			if (!p.isSold()) {
				cboProperty.addItem(p);
			}
		}
		if (cboProperty.getItemCount() == 0) {
			showWarning("You have no unsold properties to show. Add a property first.");
			return;
		}

		// date and time fields (3.2.3): spinners so only real dates and times can be picked
		Calendar tomorrow = Calendar.getInstance();
		tomorrow.add(Calendar.DAY_OF_MONTH, 1);
		tomorrow.set(Calendar.HOUR_OF_DAY, 10);
		tomorrow.set(Calendar.MINUTE, 0);
		JSpinner spnDate = new JSpinner(new SpinnerDateModel(tomorrow.getTime(), null, null, Calendar.DAY_OF_MONTH));
		spnDate.setEditor(new JSpinner.DateEditor(spnDate, "yyyy-MM-dd"));
		JSpinner spnTime = new JSpinner(new SpinnerDateModel(tomorrow.getTime(), null, null, Calendar.MINUTE));
		spnTime.setEditor(new JSpinner.DateEditor(spnTime, "HH:mm"));

		JPanel form = Theme.form(new Object[] { "Client *", cboClient, "Property *", cboProperty,
				"Date (yyyy-mm-dd) *", spnDate, "Time (24-hour) *", spnTime });

		ViewingService service = AppContext.viewingService();
		while (showForm(form, "Schedule Viewing")) {
			String date = new SimpleDateFormat("yyyy-MM-dd").format((Date) spnDate.getValue());
			String time = new SimpleDateFormat("HH:mm").format((Date) spnTime.getValue());
			if (service.schedule(agentId(), (Client) cboClient.getSelectedItem(),
					(Property) cboProperty.getSelectedItem(), date, time)) {
				refresh();
				showInfo(service.getMessage());
				return;
			}
			showWarning(service.getMessage());
		}
	}
}
