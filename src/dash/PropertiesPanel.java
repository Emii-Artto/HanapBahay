package dash;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import javax.swing.BoxLayout;
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
 * (Properties page: heading with Add Property, number cards, filters and the property list).
 * Each property is one row of the table, drawn as a card by ListingRenderer.
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
		setBorder(new EmptyBorder(20, 28, 20, 28));
		setLayout(new BorderLayout(0, 14));

		JPanel pnlTop = new JPanel();
		pnlTop.setOpaque(false);
		add(pnlTop, BorderLayout.NORTH);
		pnlTop.setLayout(new BorderLayout(0, 14));

		// ---- page heading, with Add Property on the right ----
		JPanel pnlHeader = new JPanel();
		pnlHeader.setOpaque(false);
		pnlTop.add(pnlHeader, BorderLayout.NORTH);
		pnlHeader.setLayout(new BorderLayout(12, 0));

		banner = new HeroBanner();
		pnlHeader.add(banner, BorderLayout.CENTER);

		JPanel pnlHeaderButtons = new JPanel();
		pnlHeaderButtons.setOpaque(false);
		pnlHeader.add(pnlHeaderButtons, BorderLayout.EAST);
		pnlHeaderButtons.setLayout(new GridBagLayout());

		btnAdd = new AppButton("Add Property");
		btnAdd.setIcon(new NavIcon(NavIcon.PLUS));
		btnAdd.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				openForm(null);
			}
		});
		pnlHeaderButtons.add(btnAdd, new GridBagConstraints());

		// ---- number cards ----
		pnlCards = new JPanel();
		pnlCards.setOpaque(false);
		pnlTop.add(pnlCards, BorderLayout.CENTER);
		pnlCards.setLayout(new GridLayout(1, 4, 14, 0));

		cardProperties = new StatCard();
		cardProperties.setIconType(NavIcon.PROPERTIES);
		cardProperties.setTitle("Total Properties");
		pnlCards.add(cardProperties);

		cardClients = new StatCard();
		cardClients.setIconType(NavIcon.CLIENTS);
		cardClients.setTitle("Total Clients");
		cardClients.setNote("registered buyers");
		pnlCards.add(cardClients);

		cardViewings = new StatCard();
		cardViewings.setIconType(NavIcon.VIEWING);
		cardViewings.setTitle("Appointments");
		cardViewings.setNote("upcoming viewings");
		pnlCards.add(cardViewings);

		cardDeals = new StatCard();
		cardDeals.setIconType(NavIcon.DEALS);
		cardDeals.setTitle("Completed Deals");
		pnlCards.add(cardDeals);

		// ---- filters (5.4) ----
		CardPanel pnlFilter = new CardPanel();
		pnlTop.add(pnlFilter, BorderLayout.SOUTH);
		pnlFilter.setLayout(new BorderLayout(0, 0));

		// four rows: captions, fields, captions, fields (each row is 4 equal columns)
		JPanel pnlFilterGrid = new JPanel();
		pnlFilterGrid.setOpaque(false);
		pnlFilter.add(pnlFilterGrid, BorderLayout.CENTER);
		pnlFilterGrid.setLayout(new BoxLayout(pnlFilterGrid, BoxLayout.Y_AXIS));

		JPanel pnlCaptions1 = new JPanel();
		pnlCaptions1.setOpaque(false);
		pnlFilterGrid.add(pnlCaptions1);
		pnlCaptions1.setLayout(new GridLayout(1, 4, 14, 0));
		pnlCaptions1.add(new CaptionLabel("Keyword (title or location)"));
		pnlCaptions1.add(new CaptionLabel("Property type"));
		pnlCaptions1.add(new CaptionLabel("Subtype"));
		pnlCaptions1.add(new CaptionLabel("Location"));

		JPanel pnlFields1 = new JPanel();
		pnlFields1.setOpaque(false);
		pnlFields1.setBorder(new EmptyBorder(5, 0, 12, 0));
		pnlFilterGrid.add(pnlFields1);
		pnlFields1.setLayout(new GridLayout(1, 4, 14, 0));

		txtKeyword = new AppTextField();
		txtKeyword.setPlaceholder("Search title or location");
		pnlFields1.add(txtKeyword);

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
		pnlFields1.add(cboType);

		cboSubtype = new AppComboBox<String>();
		cboSubtype.addItem(PropertyService.ALL);
		pnlFields1.add(cboSubtype);

		txtLocation = new AppTextField();
		txtLocation.setPlaceholder("Any location");
		pnlFields1.add(txtLocation);

		JPanel pnlCaptions2 = new JPanel();
		pnlCaptions2.setOpaque(false);
		pnlFilterGrid.add(pnlCaptions2);
		pnlCaptions2.setLayout(new GridLayout(1, 4, 14, 0));
		pnlCaptions2.add(new CaptionLabel("Min price"));
		pnlCaptions2.add(new CaptionLabel("Max price"));
		pnlCaptions2.add(new CaptionLabel("Status"));
		pnlCaptions2.add(new JLabel(""));

		JPanel pnlFields2 = new JPanel();
		pnlFields2.setOpaque(false);
		pnlFields2.setBorder(new EmptyBorder(5, 0, 0, 0));
		pnlFilterGrid.add(pnlFields2);
		pnlFields2.setLayout(new GridLayout(1, 4, 14, 0));

		txtMinPrice = new AppTextField();
		txtMinPrice.setPlaceholder("e.g. 1,000,000");
		pnlFields2.add(txtMinPrice);

		txtMaxPrice = new AppTextField();
		txtMaxPrice.setPlaceholder("e.g. 5,000,000");
		pnlFields2.add(txtMaxPrice);

		cboStatus = new AppComboBox<String>();
		cboStatus.addItem(PropertyService.ALL);
		cboStatus.addItem("Available");
		cboStatus.addItem("Reserved");
		cboStatus.addItem("Sold");
		pnlFields2.add(cboStatus);

		JPanel pnlSearchButtons = new JPanel();
		pnlSearchButtons.setOpaque(false);
		pnlFields2.add(pnlSearchButtons);
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

		// ---- the property list ----
		JPanel pnlList = new JPanel();
		pnlList.setOpaque(false);
		add(pnlList, BorderLayout.CENTER);
		pnlList.setLayout(new BorderLayout(0, 8));

		JPanel pnlToolbar = new JPanel();
		pnlToolbar.setOpaque(false);
		pnlList.add(pnlToolbar, BorderLayout.NORTH);
		pnlToolbar.setLayout(new BorderLayout(12, 0));

		lblResult = new JLabel(" ");
		lblResult.setFont(Theme.BODY);
		lblResult.setForeground(Theme.MUTED);
		pnlToolbar.add(lblResult, BorderLayout.CENTER);

		JPanel pnlButtons = new JPanel();
		pnlButtons.setOpaque(false);
		pnlToolbar.add(pnlButtons, BorderLayout.EAST);
		pnlButtons.setLayout(new FlowLayout(FlowLayout.RIGHT, 8, 0));

		AppButton btnGallery = new AppButton("View Photos & Details", Theme.CARD);
		btnGallery.setIcon(new NavIcon(NavIcon.PHOTO));
		btnGallery.setOutline(Theme.CARD_BORDER);
		btnGallery.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				openGallery();
			}
		});
		pnlButtons.add(btnGallery);

		btnEdit = new AppButton("Edit", Theme.CARD);
		btnEdit.setIcon(new NavIcon(NavIcon.EDIT));
		btnEdit.setOutline(Theme.CARD_BORDER);
		btnEdit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Property selected = getSelected();
				if (selected != null) {
					openForm(selected);
				}
			}
		});
		pnlButtons.add(btnEdit);

		btnDelete = new AppButton("Delete", Theme.CARD);
		btnDelete.setIcon(new NavIcon(NavIcon.DELETE));
		btnDelete.setForeground(Theme.RED);
		btnDelete.setOutline(Theme.CARD_BORDER);
		btnDelete.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				deleteSelected();
			}
		});
		pnlButtons.add(btnDelete);

		AppScrollPane scrollTable = new AppScrollPane();
		scrollTable.setBorder(new EmptyBorder(0, 0, 0, 0));
		pnlList.add(scrollTable, BorderLayout.CENTER);

		table = new AppTable();
		tableModel = new ReadOnlyTableModel(
				new String[] { "Photo", "Property", "Location and Price", "Type and Area", "Availability" });
		table.setModel(tableModel);
		table.setTableHeader(null); // every card already labels its values
		table.setRowHeight(100);
		table.setShowGrid(false);
		table.setIntercellSpacing(new Dimension(0, 0));
		table.getColumnModel().getColumn(0).setCellRenderer(new ListingRenderer(ListingRenderer.PHOTO));
		table.getColumnModel().getColumn(1).setCellRenderer(new ListingRenderer(ListingRenderer.TITLE));
		table.getColumnModel().getColumn(2).setCellRenderer(new ListingRenderer(ListingRenderer.DETAILS));
		table.getColumnModel().getColumn(3).setCellRenderer(new ListingRenderer(ListingRenderer.DETAILS));
		table.getColumnModel().getColumn(4).setCellRenderer(new ListingRenderer(ListingRenderer.STATUS));
		table.getColumnModel().getColumn(0).setMinWidth(150);
		table.getColumnModel().getColumn(0).setMaxWidth(150);
		table.getColumnModel().getColumn(1).setPreferredWidth(280);
		table.getColumnModel().getColumn(2).setPreferredWidth(220);
		table.getColumnModel().getColumn(3).setPreferredWidth(220);
		table.getColumnModel().getColumn(4).setMinWidth(140);
		table.getColumnModel().getColumn(4).setMaxWidth(170);
		table.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() == 2) {
					openGallery(); // double-click a row to see its photos
				}
			}
		});
		scrollTable.setViewportView(table);
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
			banner.setTitle("Available Properties");
			banner.setSubtitle("Search and filter, then double-click a property to see its photos.");
		} else {
			banner.setTitle("Property Listings");
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
				thumbnail = p.getPhotos().get(0).loadImage(126, 72);
			}
			String id = "P" + String.format("%03d", p.getPropertyId());
			String caption = p.getDeveloper() == null || p.getDeveloper().trim().isEmpty() ? p.getPropertyType()
					: p.getDeveloper();
			// one row = one card: photo | title | location and price | type and area | status
			tableModel.addRow(new Object[] { thumbnail, new String[] { p.getTitle(), caption, id },
					new String[] { p.getLocation(), "Location", BaseService.peso(p.getPrice()), "Price" },
					new String[] { p.getSubtype(), p.getPropertyType(), String.format("%,.0f sqm", p.getLotArea()),
							"Lot area" },
					ReportGenerator.titleCase(p.getStatus()) });
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
