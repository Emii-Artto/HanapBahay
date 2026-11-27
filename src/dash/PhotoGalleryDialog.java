package dash;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import model.Property;
import model.PropertyPhoto;
import service.BaseService;
import service.ReportGenerator;

/**
 * The photo gallery with the property details (3.1.2 / 3.2.2). Used in Agent Mode and
 * in Client Browse Mode.
 */
public class PhotoGalleryDialog extends JDialog {

	private static final long serialVersionUID = 1L;
	private static final int IMAGE_WIDTH = 600;
	private static final int IMAGE_HEIGHT = 400;

	private final JPanel contentPanel = new JPanel();
	private JLabel lblImage;
	private JLabel lblCounter;
	private JTextArea txtDetails;
	private AppButton btnPrevious;
	private AppButton btnNext;
	private ArrayList<PropertyPhoto> photos = new ArrayList<PropertyPhoto>();
	private int current = 0;

	public PhotoGalleryDialog() {
		setTitle("Photos and Details");
		setModal(true);
		setResizable(false);
		setBounds(100, 100, 960, 520);
		contentPanel.setBackground(Theme.DARK);
		setContentPane(contentPanel);
		contentPanel.setLayout(null);

		lblImage = new JLabel("No photos yet");
		lblImage.setHorizontalAlignment(SwingConstants.CENTER);
		lblImage.setOpaque(true);
		lblImage.setBackground(Theme.PANEL);
		lblImage.setForeground(Theme.MUTED);
		lblImage.setBorder(BorderFactory.createLineBorder(Theme.CARD_BORDER));
		lblImage.setBounds(16, 16, IMAGE_WIDTH, IMAGE_HEIGHT);
		contentPanel.add(lblImage);

		btnPrevious = new AppButton("< Previous", Theme.GRAY);
		btnPrevious.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				showPhoto(current - 1);
			}
		});
		btnPrevious.setBounds(16, 428, 120, 38);
		contentPanel.add(btnPrevious);

		lblCounter = new JLabel("0 / 0");
		lblCounter.setHorizontalAlignment(SwingConstants.CENTER);
		lblCounter.setFont(Theme.BODY);
		lblCounter.setForeground(Theme.MUTED);
		lblCounter.setBounds(146, 428, 340, 38);
		contentPanel.add(lblCounter);

		btnNext = new AppButton("Next >", Theme.GRAY);
		btnNext.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				showPhoto(current + 1);
			}
		});
		btnNext.setBounds(496, 428, 120, 38);
		contentPanel.add(btnNext);

		txtDetails = new JTextArea();
		txtDetails.setEditable(false);
		txtDetails.setLineWrap(true);
		txtDetails.setWrapStyleWord(true);
		txtDetails.setFont(Theme.BODY);
		txtDetails.setForeground(Theme.WHITE);
		txtDetails.setBackground(Theme.CARD);
		txtDetails.setBorder(new EmptyBorder(14, 14, 14, 14));
		txtDetails.setBounds(632, 16, 300, IMAGE_HEIGHT);
		contentPanel.add(txtDetails);

		AppButton btnClose = new AppButton("Close");
		btnClose.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
		btnClose.setBounds(832, 428, 100, 38);
		contentPanel.add(btnClose);
	}

	/** Shows this property's photos and details. */
	public void setProperty(Property property) {
		setTitle(property.getTitle() + " - Photos and Details");
		photos = property.getPhotos();
		txtDetails.setText(property.getTitle() + "\n\n"
				+ property.getDetails() + "\n\n" // POLYMORPHISM: HouseAndLot, VacantLot or Condominium version
				+ "Type: " + property.getPropertyType() + "\n"
				+ "Subtype: " + property.getSubtype() + "\n"
				+ "Developer: " + property.getDeveloper() + "\n"
				+ "Location: " + property.getLocation() + "\n"
				+ "Price: " + BaseService.peso(property.getPrice()) + "\n"
				+ "Lot area: " + String.format("%,.0f", property.getLotArea()) + " sqm\n"
				+ "Floor area: " + String.format("%,.0f", property.getFloorArea()) + " sqm\n"
				+ "Status: " + ReportGenerator.titleCase(property.getStatus()) + "\n"
				+ "ID: P" + String.format("%03d", property.getPropertyId()));
		btnPrevious.setEnabled(photos.size() > 1);
		btnNext.setEnabled(photos.size() > 1);
		showPhoto(0);
	}

	private void showPhoto(int index) {
		if (photos.isEmpty()) {
			lblImage.setIcon(null);
			lblImage.setText("No photos yet");
			lblCounter.setText("0 / 0");
			return;
		}
		// wrap around: after the last photo comes the first
		if (index < 0) {
			index = photos.size() - 1;
		}
		if (index >= photos.size()) {
			index = 0;
		}
		current = index;
		PropertyPhoto photo = photos.get(current);
		ImageIcon image = photo.loadImage(IMAGE_WIDTH, IMAGE_HEIGHT);
		if (image == null) {
			lblImage.setIcon(null);
			lblImage.setText("Photo file not found: " + photo.getImagePath());
		} else {
			lblImage.setText("");
			lblImage.setIcon(image);
		}
		lblCounter.setText((current + 1) + " / " + photos.size() + "   " + photo.getCaption());
	}
}
