package dash;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;

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
 * 3.1.5 Deal and Payment Recording. A Pending deal makes the property Reserved,
 * a Completed deal makes it Sold, and cancelling makes it Available again.
 */
public class DealsPanel extends JPanel implements DashboardSection {

	private static final long serialVersionUID = 1L;
	private Agent agent;
	private ArrayList<Deal> shown = new ArrayList<Deal>();
	private AppComboBox<String> cboStatus;
	private JLabel lblResult;
	private AppTable table;
	private ReadOnlyTableModel tableModel;

	public DealsPanel() {
		setBackground(Theme.DARK);
		setBorder(new EmptyBorder(18, 24, 18, 24));
		setLayout(new BorderLayout(0, 14));

		CardPanel pnlFilter = new CardPanel();
		add(pnlFilter, BorderLayout.NORTH);
		pnlFilter.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 0));

		pnlFilter.add(new CaptionLabel("Show"));

		cboStatus = new AppComboBox<String>();
		cboStatus.addItem("All");
		cboStatus.addItem("Pending");
		cboStatus.addItem("Completed");
		cboStatus.addItem("Cancelled");
		cboStatus.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (agent != null) {
					refresh();
				}
			}
		});
		pnlFilter.add(cboStatus);

		lblResult = new JLabel(" ");
		lblResult.setFont(Theme.BODY);
		lblResult.setForeground(Theme.MUTED);
		pnlFilter.add(lblResult);

		AppScrollPane scrollTable = new AppScrollPane();
		add(scrollTable, BorderLayout.CENTER);

		table = new AppTable();
		tableModel = new ReadOnlyTableModel(
				new String[] { "Date", "Client", "Property", "Payment Method", "Agreed Price", "Status" });
		table.setModel(tableModel);
		scrollTable.setViewportView(table);

		JPanel pnlButtons = new JPanel();
		pnlButtons.setOpaque(false);
		add(pnlButtons, BorderLayout.SOUTH);
		pnlButtons.setLayout(new FlowLayout(FlowLayout.RIGHT, 10, 0));

		AppButton btnComplete = new AppButton("Mark Completed (Sold)", Theme.BLUE);
		btnComplete.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				changeStatus(Deal.COMPLETED);
			}
		});
		pnlButtons.add(btnComplete);

		AppButton btnCancel = new AppButton("Cancel Deal", Theme.RED);
		btnCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				changeStatus(Deal.CANCELLED);
			}
		});
		pnlButtons.add(btnCancel);

		AppButton btnRecord = new AppButton("+ Record Deal");
		btnRecord.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				openForm();
			}
		});
		pnlButtons.add(btnRecord);
	}

	@Override
	public void setAgent(Agent agent) {
		this.agent = agent;
	}

	@Override
	public String getPageTitle() {
		return "Deals";
	}

	private int agentId() {
		return agent == null ? 0 : agent.getAgentId();
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

	private void changeStatus(String newStatus) {
		int row = table.getSelectedRow();
		if (row < 0) {
			JOptionPane.showMessageDialog(this, "Please select a deal in the table first.", getPageTitle(),
					JOptionPane.WARNING_MESSAGE);
			return;
		}
		if (Deal.CANCELLED.equals(newStatus)) {
			int answer = JOptionPane.showConfirmDialog(this,
					"Cancel this deal? The property becomes Available again.", getPageTitle(),
					JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
			if (answer != JOptionPane.YES_OPTION) {
				return;
			}
		}
		DealService service = AppContext.dealService();
		if (service.updateStatus(agentId(), shown.get(row).getDealId(), newStatus)) {
			refresh();
			JOptionPane.showMessageDialog(this, service.getMessage(), getPageTitle(), JOptionPane.INFORMATION_MESSAGE);
		} else {
			JOptionPane.showMessageDialog(this, service.getMessage(), getPageTitle(), JOptionPane.WARNING_MESSAGE);
		}
	}

	private void openForm() {
		DealFormDialog form = new DealFormDialog();
		if (!form.load(agentId())) {
			JOptionPane.showMessageDialog(this, form.getProblem(), getPageTitle(), JOptionPane.WARNING_MESSAGE);
			return;
		}
		form.setLocationRelativeTo(SwingUtilities.getWindowAncestor(this));
		form.setVisible(true);
		if (form.wasSaved()) {
			refresh();
		}
	}
}
