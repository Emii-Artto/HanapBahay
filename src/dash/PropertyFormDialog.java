package dash;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;

import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.filechooser.FileNameExtensionFilter;

import hanapbahay.AppContext;
import model.Property;
import service.PropertyService;
import service.ReportGenerator;

/**
 * The Add / Edit Property form (3.1.1 and 3.1.2), with a file chooser for up to 4 photos.
 * PropertyService checks every field (5.3.1, 5.3.2, 5.3.4); if something is wrong the form
 * stays open with a message, so nothing typed is lost.
 */
public class PropertyFormDialog extends JDialog {

	private static final long serialVersionUID = 1L;
	private final JPanel contentPanel = new JPanel();
	private AppTextField txtTitle;
	private AppComboBox<String> cboType;
	private AppComboBox<String> cboSubtype;
	private AppTextField txtDeveloper;
	private AppTextField txtLocation;
	private AppTextField txtPrice;
	private AppTextField txtLotArea;
	private AppTextField txtFloorArea;
	private AppComboBox<String> cboStatus;
	private JLabel lblPhotos;

	private int agentId;
	private Property editing; // null when adding
	private ArrayList<File> chosenPhotos; // null = keep the current photos
	private boolean saved = false;

	public PropertyFormDialog() {
		setTitle("Add Property");
		setModal(true);
		setResizable(false);
		setBounds(100, 100, 520, 560);
		contentPanel.setBackground(Theme.PANEL);
		setContentPane(contentPanel);
		contentPanel.setLayout(null);

		CaptionLabel lblTitle = new CaptionLabel("Title *");
		lblTitle.setBounds(24, 18, 456, 16);
		contentPanel.add(lblTitle);

		txtTitle = new AppTextField();
		txtTitle.setBounds(24, 36, 456, 34);
		contentPanel.add(txtTitle);

		CaptionLabel lblType = new CaptionLabel("Property type *");
		lblType.setBounds(24, 82, 220, 16);
		contentPanel.add(lblType);

		cboType = new AppComboBox<String>();
		cboType.addItem(Property.HOUSE_AND_LOT);
		cboType.addItem(Property.VACANT_LOT);
		cboType.addItem(Property.CONDOMINIUM);
		cboType.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				typeChanged();
			}
		});
		cboType.setBounds(24, 100, 220, 34);
		contentPanel.add(cboType);

		CaptionLabel lblSubtype = new CaptionLabel("Subtype *");
		lblSubtype.setBounds(260, 82, 220, 16);
		contentPanel.add(lblSubtype);

		cboSubtype = new AppComboBox<String>();
		cboSubtype.setBounds(260, 100, 220, 34);
		contentPanel.add(cboSubtype);

		CaptionLabel lblDeveloper = new CaptionLabel("Developer");
		lblDeveloper.setBounds(24, 146, 220, 16);
		contentPanel.add(lblDeveloper);

		txtDeveloper = new AppTextField();
		txtDeveloper.setBounds(24, 164, 220, 34);
		contentPanel.add(txtDeveloper);

		CaptionLabel lblLocation = new CaptionLabel("Location *");
		lblLocation.setBounds(260, 146, 220, 16);
		contentPanel.add(lblLocation);

		txtLocation = new AppTextField();
		txtLocation.setBounds(260, 164, 220, 34);
		contentPanel.add(txtLocation);

		CaptionLabel lblPrice = new CaptionLabel("Price (PHP) *");
		lblPrice.setBounds(24, 210, 220, 16);
		contentPanel.add(lblPrice);

		txtPrice = new AppTextField();
		txtPrice.setBounds(24, 228, 220, 34);
		contentPanel.add(txtPrice);

		CaptionLabel lblStatus = new CaptionLabel("Status *");
		lblStatus.setBounds(260, 210, 220, 16);
		contentPanel.add(lblStatus);

		cboStatus = new AppComboBox<String>();
		cboStatus.addItem("Available");
		cboStatus.addItem("Reserved");
		cboStatus.addItem("Sold");
		cboStatus.setBounds(260, 228, 220, 34);
		contentPanel.add(cboStatus);

		CaptionLabel lblLotArea = new CaptionLabel("Lot area (sqm)");
		lblLotArea.setBounds(24, 274, 220, 16);
		contentPanel.add(lblLotArea);

		txtLotArea = new AppTextField();
		txtLotArea.setBounds(24, 292, 220, 34);
		contentPanel.add(txtLotArea);

		CaptionLabel lblFloorArea = new CaptionLabel("Floor area (sqm)");
		lblFloorArea.setBounds(260, 274, 220, 16);
		contentPanel.add(lblFloorArea);

		txtFloorArea = new AppTextField();
		txtFloorArea.setBounds(260, 292, 220, 34);
		contentPanel.add(txtFloorArea);

		CaptionLabel lblPhotoCaption = new CaptionLabel("Photos (max 4)");
		lblPhotoCaption.setBounds(24, 340, 220, 16);
		contentPanel.add(lblPhotoCaption);

		AppButton btnChoosePhotos = new AppButton("Choose Photos...", Theme.BLUE);
		btnChoosePhotos.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				choosePhotos();
			}
		});
		btnChoosePhotos.setBounds(24, 360, 160, 36);
		contentPanel.add(btnChoosePhotos);

		lblPhotos = new JLabel("No photos chosen");
		lblPhotos.setFont(Theme.BODY);
		lblPhotos.setForeground(Theme.MUTED);
		lblPhotos.setBounds(196, 360, 284, 36);
		contentPanel.add(lblPhotos);

		JLabel lblRequired = new JLabel("Fields with * are required.");
		lblRequired.setFont(Theme.SMALL);
		lblRequired.setForeground(Theme.MUTED);
		lblRequired.setBounds(24, 414, 456, 16);
		contentPanel.add(lblRequired);

		AppButton btnCancel = new AppButton("Cancel", Theme.GRAY);
		btnCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
		btnCancel.setBounds(274, 456, 98, 40);
		contentPanel.add(btnCancel);

		AppButton btnSave = new AppButton("Save");
		btnSave.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				save();
			}
		});
		btnSave.setBounds(382, 456, 98, 40);
		contentPanel.add(btnSave);

		typeChanged();
	}

	public void setAgentId(int agentId) {
		this.agentId = agentId;
	}

	/** Edit mode: fills the form with this property. */
	public void setProperty(Property property) {
		editing = property;
		setTitle("Edit Property");
		txtTitle.setText(property.getTitle());
		cboType.setSelectedItem(property.getPropertyType());
		typeChanged();
		cboSubtype.setSelectedItem(property.getSubtype());
		txtDeveloper.setText(property.getDeveloper());
		txtLocation.setText(property.getLocation());
		txtPrice.setText(String.format("%.2f", property.getPrice()));
		txtLotArea.setText(String.format("%.2f", property.getLotArea()));
		txtFloorArea.setText(String.format("%.2f", property.getFloorArea()));
		cboStatus.setSelectedItem(ReportGenerator.titleCase(property.getStatus()));
		lblPhotos.setText("Keeping the current " + property.getPhotos().size() + " photo(s)");
	}

	// the subtype list depends on the type; fields that do not apply are switched off
	private void typeChanged() {
		String type = (String) cboType.getSelectedItem();
		cboSubtype.removeAllItems();
		String[] subtypes = PropertyService.getSubtypes(type);
		for (int i = 0; i < subtypes.length; i++) {
			cboSubtype.addItem(subtypes[i]);
		}
		txtLotArea.setEnabled(!Property.CONDOMINIUM.equals(type)); // a condo unit has no lot
		txtFloorArea.setEnabled(!Property.VACANT_LOT.equals(type)); // a vacant lot has no building
	}

	private void choosePhotos() {
		JFileChooser chooser = new JFileChooser();
		chooser.setMultiSelectionEnabled(true);
		chooser.setFileFilter(new FileNameExtensionFilter("Images (JPG, PNG, GIF, BMP)", "jpg", "jpeg", "png", "gif",
				"bmp"));
		if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
			chosenPhotos = new ArrayList<File>();
			File[] files = chooser.getSelectedFiles();
			for (int i = 0; i < files.length; i++) {
				chosenPhotos.add(files[i]);
			}
			lblPhotos.setText(chosenPhotos.size() + " photo(s) chosen" + (editing == null ? "" : " (replaces the old ones)"));
		}
	}

	private void save() {
		PropertyService service = AppContext.propertyService();
		boolean ok;
		if (editing == null) {
			ok = service.addProperty(agentId, txtTitle.getText(), (String) cboType.getSelectedItem(),
					(String) cboSubtype.getSelectedItem(), txtDeveloper.getText(), txtLocation.getText(),
					txtPrice.getText(), txtLotArea.getText(), txtFloorArea.getText(),
					(String) cboStatus.getSelectedItem(), chosenPhotos);
		} else {
			ok = service.updateProperty(agentId, editing.getPropertyId(), txtTitle.getText(),
					(String) cboType.getSelectedItem(), (String) cboSubtype.getSelectedItem(), txtDeveloper.getText(),
					txtLocation.getText(), txtPrice.getText(), txtLotArea.getText(), txtFloorArea.getText(),
					(String) cboStatus.getSelectedItem(), chosenPhotos);
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
