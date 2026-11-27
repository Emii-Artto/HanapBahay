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

import hanapbahay.AppContext;
import model.Agent;
import model.Client;
import service.BaseService;
import service.ClientService;

/**
 * 3.1.3 Client Management: register and manage buyers, search by name or contact
 * number (5.4.5), and Find Matches (5.4.6).
 */
public class ClientsPanel extends JPanel implements DashboardSection {

	private static final long serialVersionUID = 1L;
	private Agent agent;
	private ArrayList<Client> shown = new ArrayList<Client>();
	private AppTextField txtSearch;
	private JLabel lblResult;
	private AppTable table;
	private ReadOnlyTableModel tableModel;

	public ClientsPanel() {
		setBackground(Theme.DARK);
		setBorder(new EmptyBorder(18, 24, 18, 24));
		setLayout(new BorderLayout(0, 14));

		// ---- search ----
		CardPanel pnlSearch = new CardPanel();
		add(pnlSearch, BorderLayout.NORTH);
		pnlSearch.setLayout(new BorderLayout(10, 6));

		CaptionLabel lblSearch = new CaptionLabel("Search by name or contact number");
		pnlSearch.add(lblSearch, BorderLayout.NORTH);

		txtSearch = new AppTextField();
		txtSearch.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				refresh(); // pressing Enter searches
			}
		});
		pnlSearch.add(txtSearch, BorderLayout.CENTER);

		JPanel pnlSearchButtons = new JPanel();
		pnlSearchButtons.setOpaque(false);
		pnlSearch.add(pnlSearchButtons, BorderLayout.EAST);
		pnlSearchButtons.setLayout(new FlowLayout(FlowLayout.RIGHT, 8, 0));

		AppButton btnSearch = new AppButton("Search");
		btnSearch.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				refresh();
			}
		});
		pnlSearchButtons.add(btnSearch);

		AppButton btnReset = new AppButton("Reset", Theme.GRAY);
		btnReset.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				txtSearch.setText("");
				refresh();
			}
		});
		pnlSearchButtons.add(btnReset);

		lblResult = new JLabel(" ");
		lblResult.setFont(Theme.BODY);
		lblResult.setForeground(Theme.MUTED);
		pnlSearch.add(lblResult, BorderLayout.SOUTH);

		// ---- table ----
		AppScrollPane scrollTable = new AppScrollPane();
		add(scrollTable, BorderLayout.CENTER);

		table = new AppTable();
		tableModel = new ReadOnlyTableModel(
				new String[] { "Name", "Contact Number", "Email", "Budget", "Preferred Location", "Preferred Type" });
		table.setModel(tableModel);
		scrollTable.setViewportView(table);

		// ---- buttons ----
		JPanel pnlButtons = new JPanel();
		pnlButtons.setOpaque(false);
		add(pnlButtons, BorderLayout.SOUTH);
		pnlButtons.setLayout(new FlowLayout(FlowLayout.RIGHT, 10, 0));

		AppButton btnMatches = new AppButton("Find Matches", Theme.BLUE);
		btnMatches.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				findMatches();
			}
		});
		pnlButtons.add(btnMatches);

		AppButton btnEdit = new AppButton("Edit", Theme.GRAY);
		btnEdit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Client selected = getSelected();
				if (selected != null) {
					openForm(selected);
				}
			}
		});
		pnlButtons.add(btnEdit);

		AppButton btnDelete = new AppButton("Delete", Theme.RED);
		btnDelete.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				deleteSelected();
			}
		});
		pnlButtons.add(btnDelete);

		AppButton btnAdd = new AppButton("+ Add Client");
		btnAdd.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				openForm(null);
			}
		});
		pnlButtons.add(btnAdd);
	}

	@Override
	public void setAgent(Agent agent) {
		this.agent = agent;
	}

	@Override
	public String getPageTitle() {
		return "Clients";
	}

	private int agentId() {
		return agent == null ? 0 : agent.getAgentId();
	}

	@Override
	public void refresh() {
		shown = AppContext.clientService().search(agentId(), txtSearch.getText());
		tableModel.setRowCount(0);
		for (Client c : shown) {
			tableModel.addRow(new Object[] { c.getFullName(), c.getContactNumber(), c.getEmail(),
					BaseService.peso(c.getBudget()), c.getPreferredLocation(), c.getPreferredType() });
		}
		lblResult.setText(shown.size() + (shown.size() == 1 ? " client" : " clients")
				+ ". Select a client and click Find Matches to see properties that fit.");
	}

	private Client getSelected() {
		int row = table.getSelectedRow();
		if (row < 0) {
			JOptionPane.showMessageDialog(this, "Please select a client in the table first.", "Clients",
					JOptionPane.WARNING_MESSAGE);
			return null;
		}
		return shown.get(row);
	}

	private void openForm(Client client) {
		ClientFormDialog form = new ClientFormDialog();
		form.setAgentId(agentId());
		if (client != null) {
			form.setClient(client);
		}
		form.setLocationRelativeTo(SwingUtilities.getWindowAncestor(this));
		form.setVisible(true);
		if (form.wasSaved()) {
			refresh();
		}
	}

	private void deleteSelected() {
		Client selected = getSelected();
		if (selected == null) {
			return;
		}
		int answer = JOptionPane.showConfirmDialog(this, "Delete the client " + selected.getFullName() + "?",
				"Delete Client", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
		if (answer != JOptionPane.YES_OPTION) {
			return;
		}
		ClientService service = AppContext.clientService();
		if (service.deleteClient(agentId(), selected.getClientId())) {
			refresh();
			JOptionPane.showMessageDialog(this, service.getMessage(), "Clients", JOptionPane.INFORMATION_MESSAGE);
		} else {
			JOptionPane.showMessageDialog(this, service.getMessage(), "Clients", JOptionPane.WARNING_MESSAGE);
		}
	}

	private void findMatches() {
		Client selected = getSelected();
		if (selected != null) {
			MatchesDialog dialog = new MatchesDialog();
			dialog.setClient(selected);
			dialog.setLocationRelativeTo(SwingUtilities.getWindowAncestor(this));
			dialog.setVisible(true);
		}
	}
}
