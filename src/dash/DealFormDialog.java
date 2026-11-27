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
import model.Deal;
import model.PaymentMethod;
import model.Property;
import service.DealService;

/**
 * Record a deal: buyer, property, payment method, agreed price, date and status (3.1.5).
 * Only Available properties are listed, so the same property cannot get two deals (5.3.6).
 */
public class DealFormDialog extends JDialog {

	private static final long serialVersionUID = 1L;
	private static final String PENDING = "Pending (property becomes Reserved)";
	private static final String COMPLETED = "Completed (property becomes Sold)";

	private final JPanel contentPanel = new JPanel();
	private AppComboBox<Client> cboClient;
	private AppComboBox<Property> cboProperty;
	private AppComboBox<PaymentMethod> cboMethod;
	private AppTextField txtPrice;
	private JSpinner spnDate;
	private AppComboBox<String> cboStatus;
	private int agentId;
	private String problem = "";
	private boolean saved = false;

	public DealFormDialog() {
		setTitle("Record Deal");
		setModal(true);
		setResizable(false);
		setBounds(100, 100, 480, 450);
		contentPanel.setBackground(Theme.PANEL);
		setContentPane(contentPanel);
		contentPanel.setLayout(null);

		CaptionLabel lblClient = new CaptionLabel("Client (buyer) *");
		lblClient.setBounds(24, 18, 416, 16);
		contentPanel.add(lblClient);

		cboClient = new AppComboBox<Client>();
		cboClient.setBounds(24, 36, 416, 34);
		contentPanel.add(cboClient);

		CaptionLabel lblProperty = new CaptionLabel("Property (available only) *");
		lblProperty.setBounds(24, 82, 416, 16);
		contentPanel.add(lblProperty);

		cboProperty = new AppComboBox<Property>();
		cboProperty.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// start with the listing price; the agent can type the agreed price
				Property p = (Property) cboProperty.getSelectedItem();
				if (p != null) {
					txtPrice.setText(String.format("%.2f", p.getPrice()));
				}
			}
		});
		cboProperty.setBounds(24, 100, 416, 34);
		contentPanel.add(cboProperty);

		CaptionLabel lblMethod = new CaptionLabel("Payment method *");
		lblMethod.setBounds(24, 146, 200, 16);
		contentPanel.add(lblMethod);

		cboMethod = new AppComboBox<PaymentMethod>();
		cboMethod.setBounds(24, 164, 200, 34);
		contentPanel.add(cboMethod);

		CaptionLabel lblPrice = new CaptionLabel("Agreed price (PHP) *");
		lblPrice.setBounds(240, 146, 200, 16);
		contentPanel.add(lblPrice);

		txtPrice = new AppTextField();
		txtPrice.setBounds(240, 164, 200, 34);
		contentPanel.add(txtPrice);

		CaptionLabel lblDate = new CaptionLabel("Deal date *");
		lblDate.setBounds(24, 210, 200, 16);
		contentPanel.add(lblDate);

		spnDate = new JSpinner();
		spnDate.setFont(Theme.BODY);
		spnDate.setModel(new SpinnerDateModel(new Date(), null, null, Calendar.DAY_OF_MONTH));
		spnDate.setEditor(new JSpinner.DateEditor(spnDate, "yyyy-MM-dd"));
		spnDate.setBounds(24, 228, 200, 34);
		contentPanel.add(spnDate);

		CaptionLabel lblStatus = new CaptionLabel("Deal status *");
		lblStatus.setBounds(24, 274, 416, 16);
		contentPanel.add(lblStatus);

		cboStatus = new AppComboBox<String>();
		cboStatus.addItem(PENDING);
		cboStatus.addItem(COMPLETED);
		cboStatus.setBounds(24, 292, 416, 34);
		contentPanel.add(cboStatus);

		AppButton btnCancel = new AppButton("Cancel", Theme.GRAY);
		btnCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
		btnCancel.setBounds(232, 352, 98, 40);
		contentPanel.add(btnCancel);

		AppButton btnSave = new AppButton("Record");
		btnSave.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				save();
			}
		});
		btnSave.setBounds(342, 352, 98, 40);
		contentPanel.add(btnSave);
	}

	/** Fills the lists. Returns false if there are no clients or no Available properties. */
	public boolean load(int agentId) {
		this.agentId = agentId;
		for (Client c : AppContext.clientService().getClients(agentId)) {
			cboClient.addItem(c);
		}
		for (Property p : AppContext.propertyService().getListings(agentId)) {
			if (p.isAvailable()) {
				cboProperty.addItem(p);
			}
		}
		for (PaymentMethod m : AppContext.dealService().getPaymentMethods()) {
			cboMethod.addItem(m);
		}
		if (cboClient.getItemCount() == 0) {
			problem = "Please add the buyer in the Clients section first.";
			return false;
		}
		if (cboProperty.getItemCount() == 0) {
			problem = "You have no Available properties. A Reserved or Sold property already has a deal.";
			return false;
		}
		return true;
	}

	public String getProblem() {
		return problem;
	}

	private void save() {
		String date = new SimpleDateFormat("yyyy-MM-dd").format((Date) spnDate.getValue());
		String status = COMPLETED.equals(cboStatus.getSelectedItem()) ? Deal.COMPLETED : Deal.PENDING;
		DealService service = AppContext.dealService();
		if (service.recordDeal(agentId, (Client) cboClient.getSelectedItem(), (Property) cboProperty.getSelectedItem(),
				(PaymentMethod) cboMethod.getSelectedItem(), txtPrice.getText(), date, status)) {
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
