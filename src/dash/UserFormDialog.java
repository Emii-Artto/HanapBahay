package dash;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import com.hanapbahay.AppContext;
import com.hanapbahay.model.User;
import com.hanapbahay.service.UserService;

/**
 * The user form of Manage Users (3.1.9). One form, three uses:
 *   ADD_AGENT - login + agent profile (Owner or Admin)
 *   ADD_ADMIN - login only (Owner only)
 *   RESET     - new temporary password for an existing account
 * UserService checks who is allowed to do it (5.3.9).
 */
public class UserFormDialog extends JDialog {

	private static final long serialVersionUID = 1L;
	public static final int ADD_AGENT = 1;
	public static final int ADD_ADMIN = 2;
	public static final int RESET = 3;

	private final JPanel contentPanel = new JPanel();
	private JLabel lblHeading;
	private CaptionLabel lblUsername;
	private AppTextField txtUsername;
	private AppPasswordField txtPassword;
	private AppPasswordField txtConfirm;
	private JPanel pnlProfile;
	private AppTextField txtFirstName;
	private AppTextField txtLastName;
	private AppTextField txtContact;
	private AppTextField txtEmail;
	private AppButton btnCancel;
	private AppButton btnSave;

	private int mode = ADD_AGENT;
	private User actor; // the logged-in Owner or Admin
	private User target; // the account whose password is reset
	private boolean saved = false;

	public UserFormDialog() {
		setTitle("Add Agent Account");
		setModal(true);
		setResizable(false);
		setBounds(100, 100, 460, 520);
		contentPanel.setBackground(Theme.PANEL);
		setContentPane(contentPanel);
		contentPanel.setLayout(null);

		lblHeading = new JLabel("New agent account");
		lblHeading.setFont(Theme.HEADING);
		lblHeading.setForeground(Theme.GOLD);
		lblHeading.setBounds(24, 14, 396, 24);
		contentPanel.add(lblHeading);

		// ---- login (all modes) ----
		lblUsername = new CaptionLabel("Username *");
		lblUsername.setBounds(24, 48, 396, 16);
		contentPanel.add(lblUsername);

		txtUsername = new AppTextField();
		txtUsername.setBounds(24, 66, 396, 34);
		contentPanel.add(txtUsername);

		CaptionLabel lblPassword = new CaptionLabel("Temporary password * (6+ characters)");
		lblPassword.setBounds(24, 112, 396, 16);
		contentPanel.add(lblPassword);

		txtPassword = new AppPasswordField();
		txtPassword.setBounds(24, 130, 190, 34);
		contentPanel.add(txtPassword);

		txtConfirm = new AppPasswordField();
		txtConfirm.setToolTipText("Type the password again");
		txtConfirm.setBounds(230, 130, 190, 34);
		contentPanel.add(txtConfirm);

		JLabel lblConfirmHint = new JLabel("Left: password      Right: type it again");
		lblConfirmHint.setFont(Theme.SMALL);
		lblConfirmHint.setForeground(Theme.MUTED);
		lblConfirmHint.setBounds(24, 166, 396, 16);
		contentPanel.add(lblConfirmHint);

		// ---- agent profile (Add Agent only) ----
		pnlProfile = new JPanel();
		pnlProfile.setOpaque(false);
		pnlProfile.setBounds(0, 190, 444, 210);
		contentPanel.add(pnlProfile);
		pnlProfile.setLayout(null);

		CaptionLabel lblFirstName = new CaptionLabel("First name *");
		lblFirstName.setBounds(24, 4, 190, 16);
		pnlProfile.add(lblFirstName);

		txtFirstName = new AppTextField();
		txtFirstName.setBounds(24, 22, 190, 34);
		pnlProfile.add(txtFirstName);

		CaptionLabel lblLastName = new CaptionLabel("Last name *");
		lblLastName.setBounds(230, 4, 190, 16);
		pnlProfile.add(lblLastName);

		txtLastName = new AppTextField();
		txtLastName.setBounds(230, 22, 190, 34);
		pnlProfile.add(txtLastName);

		CaptionLabel lblContact = new CaptionLabel("Contact number *");
		lblContact.setBounds(24, 68, 396, 16);
		pnlProfile.add(lblContact);

		txtContact = new AppTextField();
		txtContact.setBounds(24, 86, 396, 34);
		pnlProfile.add(txtContact);

		CaptionLabel lblEmail = new CaptionLabel("Email");
		lblEmail.setBounds(24, 132, 396, 16);
		pnlProfile.add(lblEmail);

		txtEmail = new AppTextField();
		txtEmail.setBounds(24, 150, 396, 34);
		pnlProfile.add(txtEmail);

		btnCancel = new AppButton("Cancel", Theme.GRAY);
		btnCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
		btnCancel.setBounds(214, 420, 98, 40);
		contentPanel.add(btnCancel);

		btnSave = new AppButton("Save");
		btnSave.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				save();
			}
		});
		btnSave.setBounds(322, 420, 98, 40);
		contentPanel.add(btnSave);
	}

	/** Chooses what the form is for. target is only used for RESET. */
	public void setMode(int mode, User actor, User target) {
		this.mode = mode;
		this.actor = actor;
		this.target = target;
		boolean agent = mode == ADD_AGENT;
		pnlProfile.setVisible(agent);
		// without the profile part, the buttons move up and the form gets shorter
		int buttonsY = agent ? 420 : 200;
		btnCancel.setBounds(214, buttonsY, 98, 40);
		btnSave.setBounds(322, buttonsY, 98, 40);
		setSize(460, buttonsY + 100);

		if (mode == ADD_ADMIN) {
			setTitle("Add Admin Account");
			lblHeading.setText("New admin account (Owner only)");
		} else if (mode == RESET) {
			setTitle("Reset Password");
			lblHeading.setText("Reset the password of \"" + target.getUsername() + "\"");
			lblUsername.setText("Account");
			txtUsername.setText(target.getUsername() + " (" + target.getRoleLabel() + ")");
			txtUsername.setEditable(false);
		}
	}

	private void save() {
		UserService service = AppContext.userService();
		String password = new String(txtPassword.getPassword());
		String confirm = new String(txtConfirm.getPassword());
		boolean ok;
		if (mode == ADD_ADMIN) {
			ok = service.createAdminAccount(actor, txtUsername.getText(), password, confirm);
		} else if (mode == RESET) {
			ok = service.resetPassword(actor, target.getUserId(), password, confirm);
		} else {
			ok = service.createAgentAccount(actor, txtFirstName.getText(), txtLastName.getText(), txtContact.getText(),
					txtEmail.getText(), txtUsername.getText(), password, confirm);
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
