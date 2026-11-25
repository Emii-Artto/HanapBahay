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
import com.hanapbahay.model.User;
import com.hanapbahay.service.UserService;

/**
 * 3.1.9 User Account Management (Owner and Admins).
 * Admins add, reset and deactivate Agents. Only the Owner can do that for Admins.
 * The Owner account cannot be changed, and there is no sign-up on the login screen.
 */
public class ManageUsersPanel extends JPanel implements DashboardSection {

	private static final long serialVersionUID = 1L;
	private User actor; // the logged-in Owner or Admin
	private ArrayList<User> shown = new ArrayList<User>();
	private JLabel lblInfo;
	private AppTable table;
	private ReadOnlyTableModel tableModel;
	private AppButton btnAddAdmin;

	public ManageUsersPanel() {
		setBackground(Theme.DARK);
		setBorder(new EmptyBorder(18, 24, 18, 24));
		setLayout(new BorderLayout(0, 14));

		CardPanel pnlInfo = new CardPanel();
		add(pnlInfo, BorderLayout.NORTH);
		pnlInfo.setLayout(new BorderLayout(0, 0));

		lblInfo = new JLabel("Owner: manages Admins and Agents.   Admin: manages Agents.   Agents cannot manage accounts.");
		lblInfo.setFont(Theme.BODY);
		lblInfo.setForeground(Theme.MUTED);
		pnlInfo.add(lblInfo, BorderLayout.CENTER);

		AppScrollPane scrollTable = new AppScrollPane();
		add(scrollTable, BorderLayout.CENTER);

		table = new AppTable();
		tableModel = new ReadOnlyTableModel(
				new String[] { "Username", "Role", "Name", "Contact Number", "Created By", "Status" });
		table.setModel(tableModel);
		scrollTable.setViewportView(table);

		JPanel pnlButtons = new JPanel();
		pnlButtons.setOpaque(false);
		add(pnlButtons, BorderLayout.SOUTH);
		pnlButtons.setLayout(new FlowLayout(FlowLayout.RIGHT, 10, 0));

		AppButton btnReset = new AppButton("Reset Password", Theme.BLUE);
		btnReset.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				resetPassword();
			}
		});
		pnlButtons.add(btnReset);

		AppButton btnActivate = new AppButton("Activate", Theme.GRAY);
		btnActivate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				changeActive(true);
			}
		});
		pnlButtons.add(btnActivate);

		AppButton btnDeactivate = new AppButton("Deactivate", Theme.RED);
		btnDeactivate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				changeActive(false);
			}
		});
		pnlButtons.add(btnDeactivate);

		btnAddAdmin = new AppButton("+ Add Admin", Theme.GRAY);
		btnAddAdmin.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				openForm(UserFormDialog.ADD_ADMIN, null);
			}
		});
		pnlButtons.add(btnAddAdmin);

		AppButton btnAddAgent = new AppButton("+ Add Agent");
		btnAddAgent.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				openForm(UserFormDialog.ADD_AGENT, null);
			}
		});
		pnlButtons.add(btnAddAgent);
	}

	/** The logged-in Owner or Admin. Only the Owner sees "+ Add Admin". */
	public void setActor(User actor) {
		this.actor = actor;
		btnAddAdmin.setVisible(actor.canManageAdmins());
		if (actor.isOwner()) {
			lblInfo.setText("You are the Owner (highest authority): you can add, reset and deactivate Admins and Agents.");
		} else {
			lblInfo.setText("You are an Admin: you can add, reset and deactivate Agents. Only the Owner can manage Admins.");
		}
	}

	@Override
	public void setAgent(Agent agent) {
		// account management is for the Owner and Admins, not for one agent
	}

	@Override
	public String getPageTitle() {
		return "Manage Users";
	}

	@Override
	public void refresh() {
		UserService service = AppContext.userService();
		shown = service.getAllUsers();
		ArrayList<Agent> agents = service.getAllAgents();
		tableModel.setRowCount(0);
		for (User u : shown) {
			String name = "-";
			String contact = "-";
			for (Agent a : agents) {
				if (a.getUserId() == u.getUserId()) {
					name = a.getFullName();
					contact = a.getContactNumber();
				}
			}
			tableModel.addRow(new Object[] { u.getUsername(), u.getRoleLabel(), name, contact,
					creatorName(u.getCreatedBy()), u.isActive() ? "Active" : "Deactivated" });
		}
	}

	// who created the account ("Installation" for the Owner made by schema.sql)
	private String creatorName(int userId) {
		if (userId == 0) {
			return "Installation";
		}
		for (User u : shown) {
			if (u.getUserId() == userId) {
				return u.getUsername();
			}
		}
		return "User #" + userId;
	}

	private User getSelected() {
		int row = table.getSelectedRow();
		if (row < 0) {
			JOptionPane.showMessageDialog(this, "Please select an account in the table first.", getPageTitle(),
					JOptionPane.WARNING_MESSAGE);
			return null;
		}
		return shown.get(row);
	}

	private void openForm(int mode, User target) {
		UserFormDialog form = new UserFormDialog();
		form.setMode(mode, actor, target);
		form.setLocationRelativeTo(SwingUtilities.getWindowAncestor(this));
		form.setVisible(true);
		if (form.wasSaved()) {
			refresh();
		}
	}

	private void resetPassword() {
		User selected = getSelected();
		if (selected != null) {
			openForm(UserFormDialog.RESET, selected);
		}
	}

	private void changeActive(boolean active) {
		User selected = getSelected();
		if (selected == null) {
			return;
		}
		if (!active) {
			int answer = JOptionPane.showConfirmDialog(this, "Deactivate \"" + selected.getUsername()
					+ "\"?\nThe account cannot log in, but its records are kept.", getPageTitle(),
					JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
			if (answer != JOptionPane.YES_OPTION) {
				return;
			}
		}
		UserService service = AppContext.userService();
		if (service.setActive(actor, selected.getUserId(), active)) {
			refresh();
			JOptionPane.showMessageDialog(this, service.getMessage(), getPageTitle(), JOptionPane.INFORMATION_MESSAGE);
		} else {
			JOptionPane.showMessageDialog(this, service.getMessage(), getPageTitle(), JOptionPane.WARNING_MESSAGE);
		}
	}
}
