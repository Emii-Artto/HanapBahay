package dash;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import hanapbahay.AppContext;
import model.Agent;
import service.UserService;

/** Edit the logged-in agent's own profile. */
public class ProfileDialog extends JDialog {

	private static final long serialVersionUID = 1L;
	private final JPanel contentPanel = new JPanel();
	private AppTextField txtFirstName;
	private AppTextField txtLastName;
	private AppTextField txtContact;
	private AppTextField txtEmail;
	private Agent agent;
	private boolean saved = false;

	public ProfileDialog() {
		setTitle("Edit Profile");
		setModal(true);
		setResizable(false);
		setBounds(100, 100, 440, 330);
		contentPanel.setBackground(Theme.PANEL);
		setContentPane(contentPanel);
		contentPanel.setLayout(null);

		CaptionLabel lblFirstName = new CaptionLabel("First name *");
		lblFirstName.setBounds(24, 20, 180, 16);
		contentPanel.add(lblFirstName);

		txtFirstName = new AppTextField();
		txtFirstName.setBounds(24, 38, 180, 34);
		contentPanel.add(txtFirstName);

		CaptionLabel lblLastName = new CaptionLabel("Last name *");
		lblLastName.setBounds(214, 20, 186, 16);
		contentPanel.add(lblLastName);

		txtLastName = new AppTextField();
		txtLastName.setBounds(214, 38, 186, 34);
		contentPanel.add(txtLastName);

		CaptionLabel lblContact = new CaptionLabel("Contact number *");
		lblContact.setBounds(24, 86, 376, 16);
		contentPanel.add(lblContact);

		txtContact = new AppTextField();
		txtContact.setBounds(24, 104, 376, 34);
		contentPanel.add(txtContact);

		CaptionLabel lblEmail = new CaptionLabel("Email");
		lblEmail.setBounds(24, 152, 376, 16);
		contentPanel.add(lblEmail);

		txtEmail = new AppTextField();
		txtEmail.setBounds(24, 170, 376, 34);
		contentPanel.add(txtEmail);

		AppButton btnCancel = new AppButton("Cancel", Theme.GRAY);
		btnCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
		btnCancel.setBounds(196, 230, 96, 38);
		contentPanel.add(btnCancel);

		AppButton btnSave = new AppButton("Save");
		btnSave.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				save();
			}
		});
		btnSave.setBounds(304, 230, 96, 38);
		contentPanel.add(btnSave);
		getRootPane().setDefaultButton(btnSave);
	}

	/** Fills the form with the agent's current profile. */
	public void setAgent(Agent agent) {
		this.agent = agent;
		txtFirstName.setText(agent.getFirstName());
		txtLastName.setText(agent.getLastName());
		txtContact.setText(agent.getContactNumber());
		txtEmail.setText(agent.getEmail());
	}

	private void save() {
		UserService service = AppContext.userService();
		if (service.updateProfile(agent, txtFirstName.getText(), txtLastName.getText(), txtContact.getText(),
				txtEmail.getText())) {
			saved = true;
			JOptionPane.showMessageDialog(this, service.getMessage(), "Profile", JOptionPane.INFORMATION_MESSAGE);
			dispose();
		} else {
			JOptionPane.showMessageDialog(this, service.getMessage(), "Profile", JOptionPane.WARNING_MESSAGE);
		}
	}

	public boolean wasSaved() {
		return saved;
	}
}
