package com.hanapbahay.ui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;

import com.hanapbahay.AppContext;
import com.hanapbahay.model.Agent;
import com.hanapbahay.model.Property;
import com.hanapbahay.service.BaseService;
import com.hanapbahay.service.PropertyService;
import com.hanapbahay.service.ReportGenerator;

/**
 * 3.1.1 Property Listing Management and 3.2.2 Property Search & Gallery Views.
 *
 * The same panel is used in two ways:
 *   Agent Mode          - add, edit, delete, search and filter the agent's listings
 *   Client Browse Mode  - read-only, only AVAILABLE properties, no Add/Edit/Delete (3.2.4, 5.4.7)
 */
public class PropertiesPanel extends BasePanel {

	private static final long serialVersionUID = 1L;
	private static final String[] TYPES = { PropertyService.ALL, Property.HOUSE_AND_LOT, Property.VACANT_LOT,
			Property.CONDOMINIUM };
	private static final String[] STATUSES = { PropertyService.ALL, "Available", "Reserved", "Sold" };

	private boolean browseMode;
	private ArrayList<Property> shown = new ArrayList<Property>(); // the properties in the table, row by row
	private ArrayList<File> chosenPhotos; // photos picked in the Add/Edit form

	private JTextField txtKeyword;
	private JComboBox<String> cboType;
	private JComboBox<String> cboSubtype;
	private JTextField txtLocation;
	private JTextField txtMinPrice;
	private JTextField txtMaxPrice;
	private JComboBox<String> cboStatus;
	private JLabel lblResult;
	private DefaultTableModel tableModel;
	private JTable table;

	public PropertiesPanel(Agent agent, boolean browseMode) {
		super(agent);
		this.browseMode = browseMode;
		add(createFilters(), BorderLayout.NORTH);
		add(createTable(), BorderLayout.CENTER);
		add(createButtons(), BorderLayout.SOUTH);
		refresh();
	}

	@Override
	public String getTitle() {
		return browseMode ? "Available Properties" : "Properties";
	}

	// ======================= filters (5.4) =======================
	private JPanel createFilters() {
		JPanel card = Theme.card();
		card.setLayout(new BorderLayout(0, 8));

		JPanel grid = new JPanel(new GridLayout(4, 4, 12, 4));
		grid.setOpaque(false);
		txtKeyword = Theme.field();
		cboType = new JComboBox<String>(TYPES);
		cboSubtype = new JComboBox<String>(new String[] { PropertyService.ALL });
		txtLocation = Theme.field();
		txtMinPrice = Theme.field();
		txtMaxPrice = Theme.field();
		cboStatus = new JComboBox<String>(STATUSES);
		Theme.styleCombo(cboType);
		Theme.styleCombo(cboSubtype);
		Theme.styleCombo(cboStatus);

		// the subtype list depends on the chosen type
		cboType.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				fillSubtypes(cboSubtype, (String) cboType.getSelectedItem(), true);
			}
		});

		grid.add(Theme.caption("Keyword (title or location)"));
		grid.add(Theme.caption("Property type"));
		grid.add(Theme.caption("Subtype"));
		grid.add(Theme.caption("Location"));
		grid.add(txtKeyword);
		grid.add(cboType);
		grid.add(cboSubtype);
		grid.add(txtLocation);
		grid.add(Theme.caption("Min price"));
		grid.add(Theme.caption("Max price"));
		grid.add(Theme.caption("Status"));
		grid.add(new JLabel());
		grid.add(txtMinPrice);
		grid.add(txtMaxPrice);
		if (browseMode) {
			// 5.4.7 Browse Mode Search: only Available properties
			cboStatus.setSelectedItem("Available");
			cboStatus.setEnabled(false);
		}
		grid.add(cboStatus);

		JPanel searchButtons = new JPanel(new GridLayout(1, 2, 8, 0));
		searchButtons.setOpaque(false);
		JButton btnSearch = Theme.goldButton("Search");
		JButton btnReset = Theme.grayButton("Reset");
		btnSearch.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				refresh();
			}
		});
		btnReset.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				resetFilters();
			}
		});
		// pressing Enter in a text field also searches
		ActionListener searchOnEnter = new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				refresh();
			}
		};
		txtKeyword.addActionListener(searchOnEnter);
		txtLocation.addActionListener(searchOnEnter);
		txtMinPrice.addActionListener(searchOnEnter);
		txtMaxPrice.addActionListener(searchOnEnter);
		searchButtons.add(btnSearch);
		searchButtons.add(btnReset);
		grid.add(searchButtons);
		card.add(grid, BorderLayout.CENTER);

		lblResult = Theme.mutedLabel(" ");
		card.add(lblResult, BorderLayout.SOUTH);
		return card;
	}

	private void resetFilters() {
		txtKeyword.setText("");
		cboType.setSelectedIndex(0);
		txtLocation.setText("");
		txtMinPrice.setText("");
		txtMaxPrice.setText("");
		if (!browseMode) {
			cboStatus.setSelectedIndex(0);
		}
		refresh();
	}

	/** Fills a subtype list for the chosen type. withAll adds "All" first (for filtering). */
	private void fillSubtypes(JComboBox<String> combo, String type, boolean withAll) {
		combo.removeAllItems();
		if (withAll) {
			combo.addItem(PropertyService.ALL);
			if (PropertyService.ALL.equals(type)) {
				return;
			}
		}
		String[] subtypes = PropertyService.getSubtypes(type);
		for (int i = 0; i < subtypes.length; i++) {
			combo.addItem(subtypes[i]);
		}
	}

	// ======================= table =======================
	private JPanel createTable() {
		String[] columns = { "Photo", "ID", "Title", "Type", "Subtype", "Location", "Price", "Lot Area", "Status" };
		tableModel = new DefaultTableModel(columns, 0) {
			private static final long serialVersionUID = 1L;

			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}

			// column 0 holds a picture, so the table draws it as an image
			@Override
			public Class<?> getColumnClass(int column) {
				return column == 0 ? ImageIcon.class : Object.class;
			}
		};
		table = new JTable(tableModel);
		JPanel wrapper = new JPanel(new BorderLayout());
		wrapper.setOpaque(false);
		wrapper.add(Theme.styleTable(table), BorderLayout.CENTER);
		table.setRowHeight(58);
		table.getColumnModel().getColumn(0).setPreferredWidth(80);
		table.getColumnModel().getColumn(0).setMaxWidth(90);
		table.getColumnModel().getColumn(1).setMaxWidth(60);
		table.getColumnModel().getColumn(8).setCellRenderer(new Theme.StatusRenderer());

		// double-click a row to open the gallery
		table.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() == 2) {
					openGallery();
				}
			}
		});
		return wrapper;
	}

	/** Loads the matching properties from the database (through the service) and shows them. */
	@Override
	public void refresh() {
		PropertyService service = AppContext.propertyService();
		String status = (String) cboStatus.getSelectedItem();
		if (browseMode) {
			status = "Available";
		}
		ArrayList<Property> results = service.findProperties(agentId(), txtKeyword.getText(), txtMinPrice.getText(),
				txtMaxPrice.getText(), (String) cboType.getSelectedItem(), (String) cboSubtype.getSelectedItem(),
				txtLocation.getText(), status);
		if (results == null) { // a price filter was not a number
			showWarning(service.getMessage());
			return;
		}
		shown = results;
		tableModel.setRowCount(0);
		for (Property p : shown) {
			ImageIcon thumbnail = null;
			if (!p.getPhotos().isEmpty()) {
				thumbnail = p.getPhotos().get(0).loadImage(76, 52);
			}
			tableModel.addRow(new Object[] { thumbnail, "P" + String.format("%03d", p.getPropertyId()), p.getTitle(),
					p.getPropertyType(), p.getSubtype(), p.getLocation(), BaseService.peso(p.getPrice()),
					String.format("%,.0f sqm", p.getLotArea()), ReportGenerator.titleCase(p.getStatus()) });
		}
		lblResult.setText("Showing " + service.getMessage() + (browseMode ? " (available only)" : "")
				+ ". Double-click a property to see its photos.");
	}

	// ======================= buttons =======================
	private JPanel createButtons() {
		JPanel buttons = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
		buttons.setOpaque(false);

		JButton btnGallery = Theme.button("View Photos & Details", Theme.BLUE, Theme.WHITE);
		btnGallery.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				openGallery();
			}
		});
		buttons.add(btnGallery);

		if (!browseMode) { // 3.2.4: Add, Edit and Delete are hidden in Client Browse Mode
			JButton btnAdd = Theme.goldButton("+ Add Property");
			JButton btnEdit = Theme.grayButton("Edit");
			JButton btnDelete = Theme.redButton("Delete");
			btnAdd.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					showPropertyForm(null);
				}
			});
			btnEdit.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					Property selected = getSelected();
					if (selected != null) {
						showPropertyForm(selected);
					}
				}
			});
			btnDelete.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					deleteSelected();
				}
			});
			buttons.add(btnEdit);
			buttons.add(btnDelete);
			buttons.add(btnAdd);
		}
		return buttons;
	}

	private Property getSelected() {
		int row = table.getSelectedRow();
		if (row < 0) {
			showWarning("Please select a property in the table first.");
			return null;
		}
		return shown.get(row);
	}

	private void openGallery() {
		Property selected = getSelected();
		if (selected != null) {
			new PhotoGalleryDialog(this, selected).setVisible(true);
		}
	}

	private void deleteSelected() {
		Property selected = getSelected();
		if (selected == null) {
			return;
		}
		if (!confirm("Delete \"" + selected.getTitle() + "\"?\nThis cannot be undone.")) {
			return;
		}
		PropertyService service = AppContext.propertyService();
		if (service.deleteProperty(agentId(), selected.getPropertyId())) {
			refresh();
			showInfo(service.getMessage());
		} else {
			showWarning(service.getMessage());
		}
	}

	/** The Add / Edit form. property == null means Add. */
	private void showPropertyForm(final Property property) {
		final boolean adding = property == null;
		final JTextField txtTitle = Theme.field();
		final JComboBox<String> cboFormType = new JComboBox<String>(
				new String[] { Property.HOUSE_AND_LOT, Property.VACANT_LOT, Property.CONDOMINIUM });
		final JComboBox<String> cboFormSubtype = new JComboBox<String>();
		final JTextField txtDeveloper = Theme.field();
		final JTextField txtFormLocation = Theme.field();
		final JTextField txtPrice = Theme.field();
		final JTextField txtLotArea = Theme.field();
		final JTextField txtFloorArea = Theme.field();
		final JComboBox<String> cboFormStatus = new JComboBox<String>(new String[] { "Available", "Reserved", "Sold" });
		final JLabel lblPhotos = new JLabel();
		JButton btnPhotos = Theme.grayButton("Choose Photos (max 4)...");

		// fields that do not apply are switched off: a condo has no lot, a vacant lot has no floor area
		cboFormType.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String type = (String) cboFormType.getSelectedItem();
				fillSubtypes(cboFormSubtype, type, false);
				txtLotArea.setEnabled(!Property.CONDOMINIUM.equals(type));
				txtFloorArea.setEnabled(!Property.VACANT_LOT.equals(type));
			}
		});

		chosenPhotos = null;
		String startType = adding ? Property.HOUSE_AND_LOT : property.getPropertyType();
		cboFormType.setSelectedItem(startType);
		fillSubtypes(cboFormSubtype, startType, false);
		txtLotArea.setEnabled(!Property.CONDOMINIUM.equals(startType));
		txtFloorArea.setEnabled(!Property.VACANT_LOT.equals(startType));
		if (adding) {
			lblPhotos.setText("No photos chosen");
		} else {
			txtTitle.setText(property.getTitle());
			cboFormSubtype.setSelectedItem(property.getSubtype());
			txtDeveloper.setText(property.getDeveloper());
			txtFormLocation.setText(property.getLocation());
			txtPrice.setText(String.format("%.2f", property.getPrice()));
			txtLotArea.setText(String.format("%.2f", property.getLotArea()));
			txtFloorArea.setText(String.format("%.2f", property.getFloorArea()));
			cboFormStatus.setSelectedItem(ReportGenerator.titleCase(property.getStatus()));
			lblPhotos.setText("Keeping the current " + property.getPhotos().size() + " photo(s)");
		}

		btnPhotos.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFileChooser chooser = new JFileChooser();
				chooser.setMultiSelectionEnabled(true);
				chooser.setFileFilter(new FileNameExtensionFilter("Images (JPG, PNG, GIF, BMP)", "jpg", "jpeg", "png",
						"gif", "bmp"));
				if (chooser.showOpenDialog(PropertiesPanel.this) == JFileChooser.APPROVE_OPTION) {
					chosenPhotos = new ArrayList<File>();
					File[] files = chooser.getSelectedFiles();
					for (int i = 0; i < files.length; i++) {
						chosenPhotos.add(files[i]);
					}
					lblPhotos.setText(chosenPhotos.size() + " photo(s) chosen" + (adding ? "" : " (will replace the old ones)"));
				}
			}
		});

		JPanel photoRow = new JPanel(new BorderLayout(8, 0));
		photoRow.add(btnPhotos, BorderLayout.WEST);
		photoRow.add(lblPhotos, BorderLayout.CENTER);

		JPanel form = Theme.form(new Object[] { "Title *", txtTitle, "Property type *", cboFormType, "Subtype *",
				cboFormSubtype, "Developer", txtDeveloper, "Location *", txtFormLocation, "Price (PHP) *", txtPrice,
				"Lot area (sqm)", txtLotArea, "Floor area (sqm)", txtFloorArea, "Status *", cboFormStatus, "Photos",
				photoRow });

		PropertyService service = AppContext.propertyService();
		// keep showing the form until it is saved or cancelled, so typed values are not lost
		while (showForm(form, adding ? "Add Property" : "Edit Property")) {
			boolean saved;
			if (adding) {
				saved = service.addProperty(agentId(), txtTitle.getText(), (String) cboFormType.getSelectedItem(),
						(String) cboFormSubtype.getSelectedItem(), txtDeveloper.getText(), txtFormLocation.getText(),
						txtPrice.getText(), txtLotArea.getText(), txtFloorArea.getText(),
						(String) cboFormStatus.getSelectedItem(), chosenPhotos);
			} else {
				saved = service.updateProperty(agentId(), property.getPropertyId(), txtTitle.getText(),
						(String) cboFormType.getSelectedItem(), (String) cboFormSubtype.getSelectedItem(),
						txtDeveloper.getText(), txtFormLocation.getText(), txtPrice.getText(), txtLotArea.getText(),
						txtFloorArea.getText(), (String) cboFormStatus.getSelectedItem(), chosenPhotos);
			}
			if (saved) {
				refresh();
				showInfo(service.getMessage());
				return;
			}
			showWarning(service.getMessage());
		}
	}
}
