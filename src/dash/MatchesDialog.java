package dash;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import hanapbahay.AppContext;
import model.Client;
import model.Property;
import service.BaseService;
import service.ClientService;

/**
 * 5.4.6 Find Matches: the available properties that fit the selected client's budget,
 * preferred location and preferred type (Client.matches() decides).
 */
public class MatchesDialog extends JDialog {

	private static final long serialVersionUID = 1L;
	private final JPanel contentPanel = new JPanel();
	private JLabel lblClient;
	private JLabel lblMessage;
	private AppTable table;
	private ReadOnlyTableModel tableModel;
	private ArrayList<Property> matches = new ArrayList<Property>();

	public MatchesDialog() {
		setTitle("Find Matches");
		setModal(true);
		setResizable(false);
		setBounds(100, 100, 820, 470);
		contentPanel.setBackground(Theme.DARK);
		setContentPane(contentPanel);
		contentPanel.setLayout(null);

		lblClient = new JLabel("Client");
		lblClient.setFont(Theme.HEADING);
		lblClient.setForeground(Theme.GOLD);
		lblClient.setBounds(20, 14, 770, 24);
		contentPanel.add(lblClient);

		lblMessage = new JLabel(" ");
		lblMessage.setFont(Theme.BODY);
		lblMessage.setForeground(Theme.MUTED);
		lblMessage.setBounds(20, 40, 770, 20);
		contentPanel.add(lblMessage);

		AppScrollPane scrollTable = new AppScrollPane();
		scrollTable.setBounds(20, 70, 770, 290);
		contentPanel.add(scrollTable);

		table = new AppTable();
		tableModel = new ReadOnlyTableModel(new String[] { "ID", "Title", "Type", "Subtype", "Location", "Price" });
		table.setModel(tableModel);
		scrollTable.setViewportView(table);

		AppButton btnPhotos = new AppButton("View Photos & Details", Theme.BLUE);
		btnPhotos.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				openGallery();
			}
		});
		btnPhotos.setBounds(480, 374, 200, 40);
		contentPanel.add(btnPhotos);

		AppButton btnClose = new AppButton("Close");
		btnClose.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
		btnClose.setBounds(690, 374, 100, 40);
		contentPanel.add(btnClose);
	}

	/** Finds and shows the matches for this client. */
	public void setClient(Client client) {
		setTitle("Find Matches - " + client.getFullName());
		lblClient.setText(client.getFullName() + "   |   Budget " + BaseService.peso(client.getBudget())
				+ "   |   Location: " + (client.getPreferredLocation().isEmpty() ? "any" : client.getPreferredLocation())
				+ "   |   Type: " + client.getPreferredType());
		ClientService service = AppContext.clientService();
		matches = service.findMatches(client);
		lblMessage.setText(service.getMessage());
		tableModel.setRowCount(0);
		for (Property p : matches) {
			tableModel.addRow(new Object[] { "P" + String.format("%03d", p.getPropertyId()), p.getTitle(),
					p.getPropertyType(), p.getSubtype(), p.getLocation(), BaseService.peso(p.getPrice()) });
		}
		if (matches.isEmpty()) {
			tableModel.addRow(new Object[] { "", "No matching properties", "", "", "", "" });
		}
	}

	private void openGallery() {
		int row = table.getSelectedRow();
		if (row < 0 || row >= matches.size()) {
			JOptionPane.showMessageDialog(this, "Please select a property first.", getTitle(),
					JOptionPane.WARNING_MESSAGE);
			return;
		}
		PhotoGalleryDialog gallery = new PhotoGalleryDialog();
		gallery.setProperty(matches.get(row));
		gallery.setLocationRelativeTo(this);
		gallery.setVisible(true);
	}
}
