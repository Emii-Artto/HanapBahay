package dash;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import hanapbahay.AppContext;
import model.Client;
import model.Property;
import service.ClientService;

/**
 * The Add / Edit Client form. ClientService checks the name, contact number, email and budget.
 */
public class ClientFormDialog extends JDialog {

	private static final long serialVersionUID = 1L;
	private final JPanel contentPanel = new JPanel();
	private AppTextField txtFirstName;
	private AppTextField txtLastName;
	private AppTextField txtContact;
	private AppTextField txtEmail;
	private AppTextField txtBudget;
	private AppTextField txtLocation;
	private AppComboBox<String> cboType;
	private int agentId;
	private Client editing; // null when adding
	private boolean saved = false;

	public ClientFormDialog() {
		setTitle("Add Client");
		setModal(true);
		setResizable(false);
		setBounds(100, 100, 520, 450);
		contentPanel.setBackground(Theme.PANEL);
		setContentPane(contentPanel);
		contentPanel.setLayout(null);

		CaptionLabel lblFirstName = new CaptionLabel("First name *");
		lblFirstName.setBounds(24, 18, 220, 16);
		contentPanel.add(lblFirstName);

		txtFirstName = new AppTextField();
		txtFirstName.setBounds(24, 36, 220, 34);
		contentPanel.add(txtFirstName);

		CaptionLabel lblLastName = new CaptionLabel("Last name *");
		lblLastName.setBounds(260, 18, 220, 16);
		contentPanel.add(lblLastName);

		txtLastName = new AppTextField();
		txtLastName.setBounds(260, 36, 220, 34);
		contentPanel.add(txtLastName);

		CaptionLabel lblContact = new CaptionLabel("Contact number *");
		lblContact.setBounds(24, 82, 220, 16);
		contentPanel.add(lblContact);

		txtContact = new AppTextField();
		txtContact.setBounds(24, 100, 220, 34);
		contentPanel.add(txtContact);

		CaptionLabel lblEmail = new CaptionLabel("Email");
		lblEmail.setBounds(260, 82, 220, 16);
		contentPanel.add(lblEmail);

		txtEmail = new AppTextField();
		txtEmail.setBounds(260, 100, 220, 34);
		contentPanel.add(txtEmail);

		CaptionLabel lblBudget = new CaptionLabel("Budget (PHP) *");
		lblBudget.setBounds(24, 146, 220, 16);
		contentPanel.add(lblBudget);

		txtBudget = new AppTextField();
		txtBudget.setBounds(24, 164, 220, 34);
		contentPanel.add(txtBudget);

		CaptionLabel lblType = new CaptionLabel("Preferred type");
		lblType.setBounds(260, 146, 220, 16);
		contentPanel.add(lblType);

		cboType = new AppComboBox<String>();
		cboType.addItem(Client.ANY_TYPE);
		cboType.addItem(Property.HOUSE_AND_LOT);
		cboType.addItem(Property.VACANT_LOT);
		cboType.addItem(Property.CONDOMINIUM);
		cboType.setBounds(260, 164, 220, 34);
		contentPanel.add(cboType);

		CaptionLabel lblLocation = new CaptionLabel("Preferred location");
		lblLocation.setBounds(24, 210, 456, 16);
		contentPanel.add(lblLocation);

		txtLocation = new AppTextField();
		txtLocation.setBounds(24, 228, 456, 34);
		contentPanel.add(txtLocation);

		JLabel lblRequired = new JLabel("Fields with * are required. Leave the location empty for any location.");
		lblRequired.setFont(Theme.SMALL);
		lblRequired.setForeground(Theme.MUTED);
		lblRequired.setBounds(24, 276, 456, 16);
		contentPanel.add(lblRequired);

		AppButton btnCancel = new AppButton("Cancel", Theme.GRAY);
		btnCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
		btnCancel.setBounds(274, 340, 98, 40);
		contentPanel.add(btnCancel);

		AppButton btnSave = new AppButton("Save");
		btnSave.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				save();
			}
		});
		btnSave.setBounds(382, 340, 98, 40);
		contentPanel.add(btnSave);
		getRootPane().setDefaultButton(btnSave);
	}

	public void setAgentId(int agentId) {
		this.agentId = agentId;
	}

	/** Edit mode: fills the form with this client. */
	public void setClient(Client client) {
		editing = client;
		setTitle("Edit Client");
		txtFirstName.setText(client.getFirstName());
		txtLastName.setText(client.getLastName());
		txtContact.setText(client.getContactNumber());
		txtEmail.setText(client.getEmail());
		txtBudget.setText(String.format("%.2f", client.getBudget()));
		txtLocation.setText(client.getPreferredLocation());
		cboType.setSelectedItem(client.getPreferredType());
	}

	private void save() {
		ClientService service = AppContext.clientService();
		boolean ok;
		if (editing == null) {
			ok = service.addClient(agentId, txtFirstName.getText(), txtLastName.getText(), txtContact.getText(),
					txtEmail.getText(), txtBudget.getText(), txtLocation.getText(), (String) cboType.getSelectedItem());
		} else {
			ok = service.updateClient(agentId, editing.getClientId(), txtFirstName.getText(), txtLastName.getText(),
					txtContact.getText(), txtEmail.getText(), txtBudget.getText(), txtLocation.getText(),
					(String) cboType.getSelectedItem());
		}
		if (ok) {
			saved = true;
			JOptionPane.showMessageDialog(this, service.getMessage(), getTitle(), JOptionPane.INFORMATION_MESSAGE);
			dispose();
		} else {
			JOptionPane.showMessageDialog(this, service.getMessage(), getTitle(), JOptionPane.WARNING_MESSAGE);
		}
	}

	public boolean wasSaved() {
		return saved;
	}
}
