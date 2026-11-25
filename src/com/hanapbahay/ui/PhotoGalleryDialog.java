package com.hanapbahay.ui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import com.hanapbahay.model.Property;
import com.hanapbahay.model.PropertyPhoto;
import com.hanapbahay.service.BaseService;
import com.hanapbahay.service.ReportGenerator;

/**
 * The photo gallery with the property details (3.1.2 / 3.2.2).
 * Opens when a property is selected, in Agent Mode and in Client Browse Mode.
 */
public class PhotoGalleryDialog extends JDialog {

	private static final long serialVersionUID = 1L;
	private static final int IMAGE_WIDTH = 620;
	private static final int IMAGE_HEIGHT = 400;

	private ArrayList<PropertyPhoto> photos;
	private int current = 0;
	private JLabel lblImage;
	private JLabel lblCounter;

	public PhotoGalleryDialog(Component parent, Property property) {
		setTitle(property.getTitle() + " - Photos and Details");
		setModal(true);
		this.photos = property.getPhotos();

		JPanel content = new JPanel(new BorderLayout(14, 0));
		content.setBackground(Theme.DARK);
		content.setBorder(new EmptyBorder(16, 16, 16, 16));
		setContentPane(content);

		// ---- left: the big photo with Previous / Next ----
		JPanel left = new JPanel(new BorderLayout(0, 8));
		left.setOpaque(false);
		lblImage = new JLabel("", SwingConstants.CENTER);
		lblImage.setPreferredSize(new Dimension(IMAGE_WIDTH, IMAGE_HEIGHT));
		lblImage.setOpaque(true);
		lblImage.setBackground(Theme.PANEL);
		lblImage.setForeground(Theme.MUTED);
		lblImage.setBorder(BorderFactory.createLineBorder(Theme.CARD_BORDER));
		left.add(lblImage, BorderLayout.CENTER);

		JPanel nav = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
		nav.setOpaque(false);
		JButton btnPrevious = Theme.grayButton("< Previous");
		JButton btnNext = Theme.grayButton("Next >");
		lblCounter = new JLabel();
		lblCounter.setForeground(Theme.MUTED);
		lblCounter.setFont(Theme.BODY);
		btnPrevious.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				showPhoto(current - 1);
			}
		});
		btnNext.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				showPhoto(current + 1);
			}
		});
		nav.add(btnPrevious);
		nav.add(lblCounter);
		nav.add(btnNext);
		left.add(nav, BorderLayout.SOUTH);
		content.add(left, BorderLayout.CENTER);

		// ---- right: the details ----
		JTextArea details = new JTextArea();
		details.setEditable(false);
		details.setLineWrap(true);
		details.setWrapStyleWord(true);
		details.setBackground(Theme.CARD);
		details.setForeground(Theme.WHITE);
		details.setFont(Theme.BODY);
		details.setBorder(new EmptyBorder(14, 14, 14, 14));
		details.setPreferredSize(new Dimension(300, IMAGE_HEIGHT));
		details.setText(property.getTitle() + "\n\n"
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
		content.add(details, BorderLayout.EAST);

		JButton btnClose = Theme.goldButton("Close");
		btnClose.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
		JPanel bottom = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		bottom.setOpaque(false);
		bottom.add(btnClose);
		content.add(bottom, BorderLayout.SOUTH);

		btnPrevious.setEnabled(photos.size() > 1);
		btnNext.setEnabled(photos.size() > 1);
		showPhoto(0);
		pack();
		setLocationRelativeTo(parent);
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
		lblCounter.setText((current + 1) + " / " + photos.size());
	}
}
