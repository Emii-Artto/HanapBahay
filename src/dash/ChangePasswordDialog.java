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
 * Change My Password, for every account.
 * It also opens by itself at login when the account still has a temporary password
 * (the Owner's starting password, or a password made or reset in Manage Users).
 */
public class ChangePasswordDialog extends JDialog {

	private static final long serialVersionUID = 1L;
	private final JPanel contentPanel = new JPanel();
	private JLabel lblHeading;
	private JLabel lblInfo;
	private AppPasswordField txtCurrent;
	private AppPasswordField txtNew;
	private AppPasswordField txtConfirm;
	private User user;
	private boolean changed = false;

	public ChangePasswordDialog() {
		setTitle("Change Password");
		setModal(true);
		setResizable(false);
		setBounds(100, 100, 440, 430);
		contentPanel.setBackground(Theme.PANEL);
		setContentPane(contentPanel);
		contentPanel.setLayout(null);

		lblHeading = new JLabel("Change your password");
		lblHeading.setFont(Theme.HEADING);
		lblHeading.setForeground(Theme.GOLD);
		lblHeading.setBounds(24, 16, 376, 24);
		contentPanel.add(lblHeading);

		lblInfo = new JLabel("<html>Type your current password, then the new one twice.</html>");
		lblInfo.setFont(Theme.BODY);
		lblInfo.setForeground(Theme.MUTED);
		lblInfo.setBounds(24, 44, 376, 54);
		contentPanel.add(lblInfo);

		CaptionLabel lblCurrent = new CaptionLabel("Current password *");
		lblCurrent.setBounds(24, 108, 376, 16);
		contentPanel.add(lblCurrent);

		txtCurrent = new AppPasswordField();
		txtCurrent.setBounds(24, 126, 376, 34);
		contentPanel.add(txtCurrent);

		CaptionLabel lblNew = new CaptionLabel("New password * (6+ characters)");
		lblNew.setBounds(24, 172, 376, 16);
		contentPanel.add(lblNew);

		txtNew = new AppPasswordField();
		txtNew.setBounds(24, 190, 376, 34);
		contentPanel.add(txtNew);

		CaptionLabel lblConfirm = new CaptionLabel("New password again *");
		lblConfirm.setBounds(24, 236, 376, 16);
		contentPanel.add(lblConfirm);

		txtConfirm = new AppPasswordField();
		txtConfirm.setBounds(24, 254, 376, 34);
		contentPanel.add(txtConfirm);

		AppButton btnCancel = new AppButton("Cancel", Theme.GRAY);
		btnCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
		btnCancel.setBounds(194, 316, 98, 40);
		contentPanel.add(btnCancel);

		AppButton btnSave = new AppButton("Save");
		btnSave.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				save();
			}
		});
		btnSave.setBounds(302, 316, 98, 40);
		contentPanel.add(btnSave);
		getRootPane().setDefaultButton(btnSave);
	}

	/** firstLogin = true when the account still has a temporary password. */
	public void setUser(User user, boolean firstLogin) {
		this.user = user;
		if (firstLogin) {
			setTitle("Choose a New Password");
			lblHeading.setText("Welcome, " + user.getUsername() + "! Choose a new password");
			lblInfo.setText("<html>You are using a temporary password. Choose your own password before you "
					+ "continue. Your current password is the one you just typed.</html>");
		}
	}

	private void save() {
		UserService service = AppContext.userService();
		if (service.changeOwnPassword(user, new String(txtCurrent.getPassword()), new String(txtNew.getPassword()),
				new String(txtConfirm.getPassword()))) {
			changed = true;
			JOptionPane.showMessageDialog(this, service.getMessage(), getTitle(), JOptionPane.INFORMATION_MESSAGE);
			dispose();
		} else {
			JOptionPane.showMessageDialog(this, service.getMessage(), getTitle(), JOptionPane.WARNING_MESSAGE);
		}
	}

	public boolean wasChanged() {
		return changed;
	}
}
