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
import com.hanapbahay.model.Property;
import com.hanapbahay.model.Viewing;
import com.hanapbahay.service.ReportGenerator;
import com.hanapbahay.service.ViewingService;

/**
 * 3.1.4 Viewing Appointment Scheduling: schedule visits and mark them Completed or
 * Cancelled. A property cannot get two viewings at the same date and time (5.3.5).
 */
public class ViewingPanel extends JPanel implements DashboardSection {

	private static final long serialVersionUID = 1L;
	private Agent agent;
	private ArrayList<Viewing> shown = new ArrayList<Viewing>();
	private AppComboBox<String> cboStatus;
	private JLabel lblResult;
	private AppTable table;
	private ReadOnlyTableModel tableModel;

	public ViewingPanel() {
		setBackground(Theme.DARK);
		setBorder(new EmptyBorder(18, 24, 18, 24));
		setLayout(new BorderLayout(0, 14));

		CardPanel pnlFilter = new CardPanel();
		add(pnlFilter, BorderLayout.NORTH);
		pnlFilter.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 0));

		pnlFilter.add(new CaptionLabel("Show"));

		cboStatus = new AppComboBox<String>();
		cboStatus.addItem("All");
		cboStatus.addItem("Scheduled");
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
		tableModel = new ReadOnlyTableModel(new String[] { "Date", "Time", "Client", "Property", "Status" });
		table.setModel(tableModel);
		scrollTable.setViewportView(table);

		JPanel pnlButtons = new JPanel();
		pnlButtons.setOpaque(false);
		add(pnlButtons, BorderLayout.SOUTH);
		pnlButtons.setLayout(new FlowLayout(FlowLayout.RIGHT, 10, 0));

		AppButton btnCompleted = new AppButton("Mark Completed", Theme.BLUE);
		btnCompleted.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				changeStatus(Viewing.COMPLETED);
			}
		});
		pnlButtons.add(btnCompleted);

		AppButton btnCancelled = new AppButton("Mark Cancelled", Theme.RED);
		btnCancelled.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				changeStatus(Viewing.CANCELLED);
			}
		});
		pnlButtons.add(btnCancelled);

		AppButton btnSchedule = new AppButton("+ Schedule Viewing");
		btnSchedule.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				openForm();
			}
		});
		pnlButtons.add(btnSchedule);
	}

	@Override
	public void setAgent(Agent agent) {
		this.agent = agent;
	}

	@Override
	public String getPageTitle() {
		return "Viewing Schedule";
	}

	private int agentId() {
		return agent == null ? 0 : agent.getAgentId();
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

	private void changeStatus(String newStatus) {
		int row = table.getSelectedRow();
		if (row < 0) {
			JOptionPane.showMessageDialog(this, "Please select a viewing in the table first.", getPageTitle(),
					JOptionPane.WARNING_MESSAGE);
			return;
		}
		ViewingService service = AppContext.viewingService();
		if (service.updateStatus(agentId(), shown.get(row).getViewingId(), newStatus)) {
			refresh();
		} else {
			JOptionPane.showMessageDialog(this, service.getMessage(), getPageTitle(), JOptionPane.WARNING_MESSAGE);
		}
	}

	private void openForm() {
		ViewingFormDialog form = new ViewingFormDialog();
		if (!form.load(agentId())) { // no clients or no properties yet
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
