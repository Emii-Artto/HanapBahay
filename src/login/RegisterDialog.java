package login;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import com.hanapbahay.AppContext;
import com.hanapbahay.service.UserService;

import dash.AppButton;
import dash.AppPasswordField;
import dash.AppTextField;
import dash.CaptionLabel;
import dash.Theme;

/**
 * "Create Account" form: makes a new AGENT account with its profile.
 * UserService checks the fields (required, email, password length and match, username taken).
 */
public class RegisterDialog extends JDialog {

	private static final long serialVersionUID = 1L;
	private final JPanel contentPanel = new JPanel();
	private AppTextField txtFirstName;
	private AppTextField txtLastName;
	private AppTextField txtContact;
	private AppTextField txtEmail;
	private AppTextField txtUsername;
	private AppPasswordField txtPassword;
	private AppPasswordField txtConfirm;
	private String createdUsername; // set when the account was saved

	public RegisterDialog() {
		setTitle("Create Agent Account");
		setModal(true);
		setResizable(false);
		setBounds(100, 100, 460, 560);
		contentPanel.setBackground(Theme.PANEL);
		setContentPane(contentPanel);
		contentPanel.setLayout(null);

		JLabel lblTitle = new JLabel("Create Your Account");
		lblTitle.setFont(Theme.TITLE);
		lblTitle.setForeground(Theme.WHITE);
		lblTitle.setBounds(30, 18, 390, 36);
		contentPanel.add(lblTitle);

		JLabel lblInfo = new JLabel("Fields with * are required.");
		lblInfo.setFont(Theme.BODY);
		lblInfo.setForeground(Theme.MUTED);
		lblInfo.setBounds(30, 54, 390, 18);
		contentPanel.add(lblInfo);

		CaptionLabel lblFirstName = new CaptionLabel("First name *");
		lblFirstName.setBounds(30, 88, 185, 16);
		contentPanel.add(lblFirstName);

		txtFirstName = new AppTextField();
		txtFirstName.setBounds(30, 106, 185, 34);
		contentPanel.add(txtFirstName);

		CaptionLabel lblLastName = new CaptionLabel("Last name *");
		lblLastName.setBounds(225, 88, 185, 16);
		contentPanel.add(lblLastName);

		txtLastName = new AppTextField();
		txtLastName.setBounds(225, 106, 185, 34);
		contentPanel.add(txtLastName);

		CaptionLabel lblContact = new CaptionLabel("Contact number *");
		lblContact.setBounds(30, 152, 380, 16);
		contentPanel.add(lblContact);

		txtContact = new AppTextField();
		txtContact.setBounds(30, 170, 380, 34);
		contentPanel.add(txtContact);

		CaptionLabel lblEmail = new CaptionLabel("Email");
		lblEmail.setBounds(30, 216, 380, 16);
		contentPanel.add(lblEmail);

		txtEmail = new AppTextField();
		txtEmail.setBounds(30, 234, 380, 34);
		contentPanel.add(txtEmail);

		CaptionLabel lblUsername = new CaptionLabel("Username *");
		lblUsername.setBounds(30, 280, 380, 16);
		contentPanel.add(lblUsername);

		txtUsername = new AppTextField();
		txtUsername.setBounds(30, 298, 380, 34);
		contentPanel.add(txtUsername);

		CaptionLabel lblPassword = new CaptionLabel("Password * (6+ characters)");
		lblPassword.setBounds(30, 344, 380, 16);
		contentPanel.add(lblPassword);

		txtPassword = new AppPasswordField();
		txtPassword.setBounds(30, 362, 380, 34);
		contentPanel.add(txtPassword);

		CaptionLabel lblConfirm = new CaptionLabel("Confirm password *");
		lblConfirm.setBounds(30, 408, 380, 16);
		contentPanel.add(lblConfirm);

		txtConfirm = new AppPasswordField();
		txtConfirm.setBounds(30, 426, 380, 34);
		contentPanel.add(txtConfirm);

		AppButton btnCancel = new AppButton("Cancel", Theme.GRAY);
		btnCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
		btnCancel.setBounds(200, 476, 100, 38);
		contentPanel.add(btnCancel);

		AppButton btnCreate = new AppButton("Create Account");
		btnCreate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				createAccount();
			}
		});
		btnCreate.setBounds(310, 476, 100, 38);
		contentPanel.add(btnCreate);
		getRootPane().setDefaultButton(btnCreate);
	}

	private void createAccount() {
		UserService service = AppContext.userService();
		if (service.registerAgent(txtFirstName.getText(), txtLastName.getText(), txtContact.getText(),
				txtEmail.getText(), txtUsername.getText(), new String(txtPassword.getPassword()),
				new String(txtConfirm.getPassword()))) {
			JOptionPane.showMessageDialog(this, service.getMessage(), "Account created",
					JOptionPane.INFORMATION_MESSAGE);
			createdUsername = txtUsername.getText().trim();
			dispose();
		} else {
			// the form stays open so nothing typed is lost
			JOptionPane.showMessageDialog(this, service.getMessage(), "Cannot create account",
					JOptionPane.WARNING_MESSAGE);
		}
	}

	/** The new username, or null if no account was created. */
	public String getCreatedUsername() {
		return createdUsername;
	}
}
