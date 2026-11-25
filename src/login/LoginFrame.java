package login;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import com.hanapbahay.AppContext;
import com.hanapbahay.model.Agent;
import com.hanapbahay.model.User;
import com.hanapbahay.service.UserService;

import dash.AdminDashboard;
import dash.AppButton;
import dash.AppComboBox;
import dash.AppPasswordField;
import dash.AppTextField;
import dash.CaptionLabel;
import dash.MainDashboard;
import dash.Theme;

/**
 * 3.2.1 Authentication: the login screen (UI member's design, made in WindowBuilder style).
 * UserService checks the username and password against MySQL. Invalid input shows an error
 * message and the user stays on this screen (5.3.3).
 */
public class LoginFrame extends JFrame {

	private static final long serialVersionUID = 1L;
	private static final String AGENT = "Agent";
	private static final String ADMIN = "Admin";

	private JPanel contentPane;
	private AppComboBox<String> cboAccountType;
	private AppTextField txtUsername;
	private AppPasswordField txtPassword;

	/** Runs the login screen alone (for testing the design). The app normally starts from Main. */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				new LoginFrame().setVisible(true);
			}
		});
	}

	public LoginFrame() {
		setTitle("HanapBahay - Login");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 1100, 700);
		setMinimumSize(new Dimension(900, 620));
		setLocationRelativeTo(null);
		contentPane = new JPanel();
		contentPane.setBackground(Theme.DARK);
		setContentPane(contentPane);
		contentPane.setLayout(new GridLayout(1, 2, 0, 0));

		// ================= LEFT: brand =================
		BrandPanel pnlBrand = new BrandPanel();
		pnlBrand.setBorder(new EmptyBorder(35, 42, 40, 35));
		contentPane.add(pnlBrand);
		pnlBrand.setLayout(new BorderLayout(0, 0));

		JLabel lblBrand = new JLabel("⌂  HANAPBAHAY");
		lblBrand.setFont(new Font("SansSerif", Font.BOLD, 20));
		lblBrand.setForeground(Theme.GOLD);
		pnlBrand.add(lblBrand, BorderLayout.NORTH);

		JPanel pnlCaption = new JPanel();
		pnlCaption.setOpaque(false);
		pnlBrand.add(pnlCaption, BorderLayout.SOUTH);
		pnlCaption.setLayout(new GridLayout(3, 1, 0, 4));

		JLabel lblFindYour = new JLabel("FIND YOUR");
		lblFindYour.setFont(new Font("SansSerif", Font.BOLD, 16));
		lblFindYour.setForeground(Theme.GOLD);
		pnlCaption.add(lblFindYour);

		JLabel lblPerfect = new JLabel("Perfect Property.");
		lblPerfect.setFont(new Font("Serif", Font.BOLD, 38));
		lblPerfect.setForeground(Theme.WHITE);
		pnlCaption.add(lblPerfect);

		JLabel lblDescription = new JLabel(
				"<html>Manage property listings, buyers, viewings<br>and deals in one convenient platform.</html>");
		lblDescription.setFont(new Font("SansSerif", Font.PLAIN, 14));
		lblDescription.setForeground(Theme.WHITE);
		pnlCaption.add(lblDescription);

		// ================= RIGHT: login card (centered) =================
		JPanel pnlRight = new JPanel();
		pnlRight.setBackground(Theme.DARK);
		contentPane.add(pnlRight);
		pnlRight.setLayout(new GridBagLayout());

		JPanel pnlCard = new JPanel();
		pnlCard.setBackground(Theme.PANEL);
		pnlCard.setBorder(BorderFactory.createLineBorder(new Color(65, 62, 54)));
		pnlCard.setPreferredSize(new Dimension(430, 540));
		pnlRight.add(pnlCard);
		pnlCard.setLayout(null);

		JLabel lblWelcome = new JLabel("Welcome Back!");
		lblWelcome.setFont(new Font("Serif", Font.BOLD, 31));
		lblWelcome.setForeground(Theme.WHITE);
		lblWelcome.setBounds(40, 32, 350, 40);
		pnlCard.add(lblWelcome);

		JLabel lblSubtitle = new JLabel("Log in to access your account.");
		lblSubtitle.setFont(Theme.BODY);
		lblSubtitle.setForeground(Theme.MUTED);
		lblSubtitle.setBounds(40, 74, 350, 20);
		pnlCard.add(lblSubtitle);

		CaptionLabel lblAccountType = new CaptionLabel("Account type");
		lblAccountType.setBounds(40, 116, 350, 16);
		pnlCard.add(lblAccountType);

		cboAccountType = new AppComboBox<String>();
		cboAccountType.addItem(AGENT);
		cboAccountType.addItem(ADMIN);
		cboAccountType.setBounds(40, 136, 350, 38);
		pnlCard.add(cboAccountType);

		CaptionLabel lblUsername = new CaptionLabel("Username");
		lblUsername.setBounds(40, 188, 350, 16);
		pnlCard.add(lblUsername);

		txtUsername = new AppTextField();
		txtUsername.setBounds(40, 208, 350, 38);
		pnlCard.add(txtUsername);

		CaptionLabel lblPassword = new CaptionLabel("Password");
		lblPassword.setBounds(40, 260, 350, 16);
		pnlCard.add(lblPassword);

		txtPassword = new AppPasswordField();
		txtPassword.setBounds(40, 280, 350, 38);
		pnlCard.add(txtPassword);

		final JCheckBox chkShowPassword = new JCheckBox("Show password");
		chkShowPassword.setFont(Theme.SMALL);
		chkShowPassword.setForeground(Theme.MUTED);
		chkShowPassword.setBackground(Theme.PANEL);
		chkShowPassword.setFocusPainted(false);
		chkShowPassword.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				txtPassword.setEchoChar(chkShowPassword.isSelected() ? (char) 0 : '•');
			}
		});
		chkShowPassword.setBounds(36, 324, 200, 22);
		pnlCard.add(chkShowPassword);

		AppButton btnSignIn = new AppButton("SIGN IN");
		btnSignIn.setFont(new Font("SansSerif", Font.BOLD, 14));
		btnSignIn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				doLogin();
			}
		});
		btnSignIn.setBounds(40, 364, 350, 44);
		pnlCard.add(btnSignIn);
		getRootPane().setDefaultButton(btnSignIn); // pressing Enter clicks SIGN IN

		JLabel lblNewAgent = new JLabel("New agent?");
		lblNewAgent.setHorizontalAlignment(SwingConstants.RIGHT);
		lblNewAgent.setFont(Theme.BODY);
		lblNewAgent.setForeground(Theme.MUTED);
		lblNewAgent.setBounds(40, 426, 160, 22);
		pnlCard.add(lblNewAgent);

		JButton btnCreateAccount = new JButton("CREATE ACCOUNT");
		btnCreateAccount.setFont(Theme.SMALL_BOLD);
		btnCreateAccount.setForeground(Theme.GOLD);
		btnCreateAccount.setBackground(Theme.PANEL);
		btnCreateAccount.setBorder(null);
		btnCreateAccount.setFocusPainted(false);
		btnCreateAccount.setHorizontalAlignment(SwingConstants.LEFT);
		btnCreateAccount.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				openCreateAccount();
			}
		});
		btnCreateAccount.setBounds(208, 426, 150, 22);
		pnlCard.add(btnCreateAccount);

		JLabel lblAuthorized = new JLabel("AUTHORIZED USERS ONLY");
		lblAuthorized.setHorizontalAlignment(SwingConstants.CENTER);
		lblAuthorized.setFont(new Font("SansSerif", Font.BOLD, 10));
		lblAuthorized.setForeground(Theme.MUTED);
		lblAuthorized.setBounds(40, 480, 350, 18);
		pnlCard.add(lblAuthorized);
	}

	/** Checks the login with the backend and opens the right window. */
	private void doLogin() {
		UserService service = AppContext.userService();
		User user = service.login(txtUsername.getText(), new String(txtPassword.getPassword()));
		if (user == null) {
			JOptionPane.showMessageDialog(this, service.getMessage(), "Cannot log in", JOptionPane.WARNING_MESSAGE);
			return;
		}
		boolean wantsAdmin = ADMIN.equals(cboAccountType.getSelectedItem());
		if (user.isAdmin() != wantsAdmin) {
			JOptionPane.showMessageDialog(this,
					"This is " + (user.isAdmin() ? "an Admin" : "an Agent") + " account.\nPlease choose \""
							+ (user.isAdmin() ? ADMIN : AGENT) + "\" as the account type.",
					"Wrong account type", JOptionPane.WARNING_MESSAGE);
			return;
		}

		if (user.isAdmin()) {
			AdminDashboard admin = new AdminDashboard();
			admin.startSession(user);
			admin.setVisible(true);
		} else {
			Agent agent = service.getAgentProfile(user);
			if (agent == null) {
				JOptionPane.showMessageDialog(this, service.getMessage(), "Cannot log in",
						JOptionPane.WARNING_MESSAGE);
				return;
			}
			MainDashboard main = new MainDashboard();
			main.startSession(user, agent);
			main.setVisible(true);
		}
		dispose(); // close the login window
	}

	private void openCreateAccount() {
		RegisterDialog dialog = new RegisterDialog();
		dialog.setLocationRelativeTo(this);
		dialog.setVisible(true); // waits here until the dialog is closed (modal)
		if (dialog.getCreatedUsername() != null) {
			cboAccountType.setSelectedItem(AGENT);
			txtUsername.setText(dialog.getCreatedUsername());
			txtPassword.setText("");
			txtPassword.requestFocus();
		}
	}
}
