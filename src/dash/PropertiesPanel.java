package dash;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;

import com.hanapbahay.AppContext;
import com.hanapbahay.model.Agent;
import com.hanapbahay.model.Property;
import com.hanapbahay.service.BaseService;
import com.hanapbahay.service.PropertyService;
import com.hanapbahay.service.ReportGenerator;

/**
 * 3.1.1 Property Listing Management and 3.2.2 Property Search & Gallery Views
 * (UI member's Properties page: banner, number cards, filters and the Property Records table).
 *
 * The same panel is used in Client Browse Mode (3.2.4): setBrowseMode(true) shows only
 * Available properties, read-only, and hides the cards and the Add/Edit/Delete buttons.
 */
public class PropertiesPanel extends JPanel implements DashboardSection {

	private static final long serialVersionUID = 1L;
	private Agent agent;
	private boolean browseMode = false;
	private ArrayList<Property> shown = new ArrayList<Property>(); // the properties in the table, row by row

	private HeroBanner banner;
	private JPanel pnlCards;
	private StatCard cardProperties;
	private StatCard cardClients;
	private StatCard cardViewings;
	private StatCard cardDeals;
	private AppTextField txtKeyword;
	private AppComboBox<String> cboType;
	private AppComboBox<String> cboSubtype;
	private AppTextField txtLocation;
	private AppTextField txtMinPrice;
	private AppTextField txtMaxPrice;
	private AppComboBox<String> cboStatus;
	private JLabel lblResult;
	private AppTable table;
	private ReadOnlyTableModel tableModel;
	private AppButton btnEdit;
	private AppButton btnDelete;
	private AppButton btnAdd;

	public PropertiesPanel() {
		setBackground(Theme.DARK);
		setBorder(new EmptyBorder(18, 24, 18, 24));
		setLayout(new BorderLayout(0, 12));

		JPanel pnlTop = new JPanel();
		pnlTop.setOpaque(false);
		add(pnlTop, BorderLayout.NORTH);
		pnlTop.setLayout(new BorderLayout(0, 12));

		banner = new HeroBanner();
		pnlTop.add(banner, BorderLayout.NORTH);

		// ---- number cards ----
		pnlCards = new JPanel();
		pnlCards.setOpaque(false);
		pnlTop.add(pnlCards, BorderLayout.CENTER);
		pnlCards.setLayout(new GridLayout(1, 4, 14, 0));

		cardProperties = new StatCard();
		cardProperties.setIcon("⌂");
		cardProperties.setTitle("Total Properties");
		pnlCards.add(cardProperties);

		cardClients = new StatCard();
		cardClients.setIcon("●");
		cardClients.setTitle("Total Clients");
		cardClients.setNote("registered buyers");
		pnlCards.add(cardClients);

		cardViewings = new StatCard();
		cardViewings.setIcon("■");
		cardViewings.setTitle("Appointments");
		cardViewings.setNote("upcoming viewings");
		pnlCards.add(cardViewings);

		cardDeals = new StatCard();
		cardDeals.setIcon("₱");
		cardDeals.setTitle("Completed Deals");
		pnlCards.add(cardDeals);

		// ---- filters (5.4) ----
		CardPanel pnlFilter = new CardPanel();
		pnlTop.add(pnlFilter, BorderLayout.SOUTH);
		pnlFilter.setLayout(new BorderLayout(0, 6));

		JPanel pnlFilterGrid = new JPanel();
		pnlFilterGrid.setOpaque(false);
		pnlFilter.add(pnlFilterGrid, BorderLayout.CENTER);
		pnlFilterGrid.setLayout(new GridLayout(4, 4, 12, 3));

		pnlFilterGrid.add(new CaptionLabel("Keyword (title or location)"));
		pnlFilterGrid.add(new CaptionLabel("Property type"));
		pnlFilterGrid.add(new CaptionLabel("Subtype"));
		pnlFilterGrid.add(new CaptionLabel("Location"));

		txtKeyword = new AppTextField();
		pnlFilterGrid.add(txtKeyword);

		cboType = new AppComboBox<String>();
		cboType.addItem(PropertyService.ALL);
		cboType.addItem(Property.HOUSE_AND_LOT);
		cboType.addItem(Property.VACANT_LOT);
		cboType.addItem(Property.CONDOMINIUM);
		cboType.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				fillSubtypes();
			}
		});
		pnlFilterGrid.add(cboType);

		cboSubtype = new AppComboBox<String>();
		cboSubtype.addItem(PropertyService.ALL);
		pnlFilterGrid.add(cboSubtype);

		txtLocation = new AppTextField();
		pnlFilterGrid.add(txtLocation);

		pnlFilterGrid.add(new CaptionLabel("Min price"));
		pnlFilterGrid.add(new CaptionLabel("Max price"));
		pnlFilterGrid.add(new CaptionLabel("Status"));
		pnlFilterGrid.add(new JLabel(""));

		txtMinPrice = new AppTextField();
		pnlFilterGrid.add(txtMinPrice);

		txtMaxPrice = new AppTextField();
		pnlFilterGrid.add(txtMaxPrice);

		cboStatus = new AppComboBox<String>();
		cboStatus.addItem(PropertyService.ALL);
		cboStatus.addItem("Available");
		cboStatus.addItem("Reserved");
		cboStatus.addItem("Sold");
		pnlFilterGrid.add(cboStatus);

		JPanel pnlSearchButtons = new JPanel();
		pnlSearchButtons.setOpaque(false);
		pnlFilterGrid.add(pnlSearchButtons);
		pnlSearchButtons.setLayout(new GridLayout(1, 2, 8, 0));

		AppButton btnSearch = new AppButton("Search");
		btnSearch.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				refresh();
			}
		});
		pnlSearchButtons.add(btnSearch);

		AppButton btnReset = new AppButton("Reset", Theme.GRAY);
		btnReset.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				resetFilters();
			}
		});
		pnlSearchButtons.add(btnReset);

		lblResult = new JLabel(" ");
		lblResult.setFont(Theme.BODY);
		lblResult.setForeground(Theme.MUTED);
		pnlFilter.add(lblResult, BorderLayout.SOUTH);

		// ---- Property Records table ----
		AppScrollPane scrollTable = new AppScrollPane();
		add(scrollTable, BorderLayout.CENTER);

		table = new AppTable();
		tableModel = new ReadOnlyTableModel(
				new String[] { "Photo", "ID", "Title", "Type", "Subtype", "Location", "Price", "Lot Area", "Status" });
		table.setModel(tableModel);
		table.setRowHeight(58);
		table.getColumnModel().getColumn(0).setMaxWidth(90);
		table.getColumnModel().getColumn(1).setMaxWidth(60);
		table.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() == 2) {
					openGallery(); // double-click a row to see its photos
				}
			}
		});
		scrollTable.setViewportView(table);

		// ---- buttons ----
		JPanel pnlButtons = new JPanel();
		pnlButtons.setOpaque(false);
		add(pnlButtons, BorderLayout.SOUTH);
		pnlButtons.setLayout(new FlowLayout(FlowLayout.RIGHT, 10, 0));

		AppButton btnGallery = new AppButton("View Photos & Details", Theme.BLUE);
		btnGallery.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				openGallery();
			}
		});
		pnlButtons.add(btnGallery);

		btnEdit = new AppButton("Edit", Theme.GRAY);
		btnEdit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Property selected = getSelected();
				if (selected != null) {
					openForm(selected);
				}
			}
		});
		pnlButtons.add(btnEdit);

		btnDelete = new AppButton("Delete", Theme.RED);
		btnDelete.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				deleteSelected();
			}
		});
		pnlButtons.add(btnDelete);

		btnAdd = new AppButton("+ Add Property");
		btnAdd.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				openForm(null);
			}
		});
		pnlButtons.add(btnAdd);
	}

	@Override
	public void setAgent(Agent agent) {
		this.agent = agent;
	}

	@Override
	public String getPageTitle() {
		return browseMode ? "Find Your Perfect Property" : "Properties";
	}

	/** Client Browse Mode: read-only, Available properties only (3.2.4, 5.4.7). */
	public void setBrowseMode(boolean browseMode) {
		this.browseMode = browseMode;
		pnlCards.setVisible(!browseMode); // the agent's numbers are hidden from the buyer
		btnEdit.setVisible(!browseMode);
		btnDelete.setVisible(!browseMode);
		btnAdd.setVisible(!browseMode);
		cboStatus.setEnabled(!browseMode);
		if (browseMode) {
			cboStatus.setSelectedItem("Available");
			banner.setTitle("AVAILABLE PROPERTIES");
			banner.setSubtitle("Search and filter, then double-click a property to see its photos.");
		} else {
			banner.setTitle("PROPERTY LISTINGS");
			banner.setSubtitle("Manage and organize your property listings.");
		}
	}

	private int agentId() {
		return agent == null ? 0 : agent.getAgentId();
	}

	private void fillSubtypes() {
		String type = (String) cboType.getSelectedItem();
		cboSubtype.removeAllItems();
		cboSubtype.addItem(PropertyService.ALL);
		if (type == null || PropertyService.ALL.equals(type)) {
			return;
		}
		String[] subtypes = PropertyService.getSubtypes(type);
		for (int i = 0; i < subtypes.length; i++) {
			cboSubtype.addItem(subtypes[i]);
		}
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

	/** Loads the matching properties (through PropertyService) and the number cards. */
	@Override
	public void refresh() {
		PropertyService service = AppContext.propertyService();
		String status = browseMode ? "Available" : (String) cboStatus.getSelectedItem();
		ArrayList<Property> results = service.findProperties(agentId(), txtKeyword.getText(), txtMinPrice.getText(),
				txtMaxPrice.getText(), (String) cboType.getSelectedItem(), (String) cboSubtype.getSelectedItem(),
				txtLocation.getText(), status);
		if (results == null) { // a price filter was not a valid number
			JOptionPane.showMessageDialog(this, service.getMessage(), "Search", JOptionPane.WARNING_MESSAGE);
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

		if (!browseMode) {
			ReportGenerator reports = AppContext.reportGenerator();
			ArrayList<Property> all = reports.getProperties(agentId());
			cardProperties.setValue(String.valueOf(all.size()));
			cardProperties.setNote(reports.countStatus(all, Property.AVAILABLE) + " available");
			cardClients.setValue(String.valueOf(reports.getClients(agentId()).size()));
			cardViewings.setValue(String.valueOf(reports.countUpcomingViewings(agentId())));
			cardDeals.setValue(String.valueOf(reports.countCompletedDeals(agentId())));
			cardDeals.setNote("worth " + BaseService.peso(reports.getTotalSales(agentId())));
		}
	}

	private Property getSelected() {
		int row = table.getSelectedRow();
		if (row < 0) {
			JOptionPane.showMessageDialog(this, "Please select a property in the table first.", "Properties",
					JOptionPane.WARNING_MESSAGE);
			return null;
		}
		return shown.get(row);
	}

	private void openGallery() {
		Property selected = getSelected();
		if (selected != null) {
			PhotoGalleryDialog gallery = new PhotoGalleryDialog();
			gallery.setProperty(selected);
			gallery.setLocationRelativeTo(SwingUtilities.getWindowAncestor(this));
			gallery.setVisible(true);
		}
	}

	/** Opens the Add / Edit form. property == null means Add. */
	private void openForm(Property property) {
		PropertyFormDialog form = new PropertyFormDialog();
		form.setAgentId(agentId());
		if (property != null) {
			form.setProperty(property);
		}
		form.setLocationRelativeTo(SwingUtilities.getWindowAncestor(this));
		form.setVisible(true); // waits here until the form is closed
		if (form.wasSaved()) {
			refresh();
		}
	}

	private void deleteSelected() {
		Property selected = getSelected();
		if (selected == null) {
			return;
		}
		int answer = JOptionPane.showConfirmDialog(this,
				"Delete \"" + selected.getTitle() + "\"?\nThis cannot be undone.", "Delete Property",
				JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
		if (answer != JOptionPane.YES_OPTION) {
			return;
		}
		PropertyService service = AppContext.propertyService();
		if (service.deleteProperty(agentId(), selected.getPropertyId())) {
			refresh();
			JOptionPane.showMessageDialog(this, service.getMessage(), "Properties", JOptionPane.INFORMATION_MESSAGE);
		} else {
			JOptionPane.showMessageDialog(this, service.getMessage(), "Properties", JOptionPane.WARNING_MESSAGE);
		}
	}
}
