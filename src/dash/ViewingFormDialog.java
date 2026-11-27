package dash;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerDateModel;

import hanapbahay.AppContext;
import model.Client;
import model.Property;
import service.ViewingService;

/**
 * Schedule a viewing: choose the client, the property, the date and the time (3.1.4).
 * The date and time fields are spinners, so only real dates and times can be chosen.
 */
public class ViewingFormDialog extends JDialog {

	private static final long serialVersionUID = 1L;
	private final JPanel contentPanel = new JPanel();
	private AppComboBox<Client> cboClient;
	private AppComboBox<Property> cboProperty;
	private JSpinner spnDate;
	private JSpinner spnTime;
	private int agentId;
	private String problem = "";
	private boolean saved = false;

	public ViewingFormDialog() {
		setTitle("Schedule Viewing");
		setModal(true);
		setResizable(false);
		setBounds(100, 100, 480, 330);
		contentPanel.setBackground(Theme.PANEL);
		setContentPane(contentPanel);
		contentPanel.setLayout(null);

		CaptionLabel lblClient = new CaptionLabel("Client *");
		lblClient.setBounds(24, 18, 416, 16);
		contentPanel.add(lblClient);

		cboClient = new AppComboBox<Client>();
		cboClient.setBounds(24, 36, 416, 34);
		contentPanel.add(cboClient);

		CaptionLabel lblProperty = new CaptionLabel("Property *");
		lblProperty.setBounds(24, 82, 416, 16);
		contentPanel.add(lblProperty);

		cboProperty = new AppComboBox<Property>();
		cboProperty.setBounds(24, 100, 416, 34);
		contentPanel.add(cboProperty);

		CaptionLabel lblDate = new CaptionLabel("Date (yyyy-mm-dd) *");
		lblDate.setBounds(24, 146, 200, 16);
		contentPanel.add(lblDate);

		spnDate = new JSpinner();
		spnDate.setFont(Theme.BODY);
		spnDate.setModel(new SpinnerDateModel(new Date(), null, null, Calendar.DAY_OF_MONTH));
		spnDate.setEditor(new JSpinner.DateEditor(spnDate, "yyyy-MM-dd"));
		spnDate.setBounds(24, 164, 200, 34);
		contentPanel.add(spnDate);

		CaptionLabel lblTime = new CaptionLabel("Time (24-hour) *");
		lblTime.setBounds(240, 146, 200, 16);
		contentPanel.add(lblTime);

		spnTime = new JSpinner();
		spnTime.setFont(Theme.BODY);
		spnTime.setModel(new SpinnerDateModel(new Date(), null, null, Calendar.MINUTE));
		spnTime.setEditor(new JSpinner.DateEditor(spnTime, "HH:mm"));
		spnTime.setBounds(240, 164, 200, 34);
		contentPanel.add(spnTime);

		AppButton btnCancel = new AppButton("Cancel", Theme.GRAY);
		btnCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
		btnCancel.setBounds(232, 232, 98, 40);
		contentPanel.add(btnCancel);

		AppButton btnSave = new AppButton("Schedule");
		btnSave.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				save();
			}
		});
		btnSave.setBounds(342, 232, 98, 40);
		contentPanel.add(btnSave);
	}

	/**
	 * Fills the lists with this agent's clients and unsold properties, and sets the
	 * date to tomorrow at 10:00. Returns false if there is nothing to choose from.
	 */
	public boolean load(int agentId) {
		this.agentId = agentId;
		for (Client c : AppContext.clientService().getClients(agentId)) {
			cboClient.addItem(c);
		}
		for (Property p : AppContext.propertyService().getListings(agentId)) {
			if (!p.isSold()) {
				cboProperty.addItem(p);
			}
		}
		if (cboClient.getItemCount() == 0) {
			problem = "Please add a client in the Clients section first.";
			return false;
		}
		if (cboProperty.getItemCount() == 0) {
			problem = "You have no unsold properties to show. Add a property first.";
			return false;
		}
		Calendar tomorrow = Calendar.getInstance();
		tomorrow.add(Calendar.DAY_OF_MONTH, 1);
		tomorrow.set(Calendar.HOUR_OF_DAY, 10);
		tomorrow.set(Calendar.MINUTE, 0);
		spnDate.setValue(tomorrow.getTime());
		spnTime.setValue(tomorrow.getTime());
		return true;
	}

	public String getProblem() {
		return problem;
	}

	private void save() {
		String date = new SimpleDateFormat("yyyy-MM-dd").format((Date) spnDate.getValue());
		String time = new SimpleDateFormat("HH:mm").format((Date) spnTime.getValue());
		ViewingService service = AppContext.viewingService();
		if (service.schedule(agentId, (Client) cboClient.getSelectedItem(), (Property) cboProperty.getSelectedItem(),
				date, time)) {
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
