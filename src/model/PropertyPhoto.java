package model;

import java.awt.Image;
import java.io.File;

import javax.swing.ImageIcon;

/**
 * 5.1.9 PropertyPhoto - one photo of a property.
 * The image file is in the "photos" folder; the database only stores its path.
 */
public class PropertyPhoto {

	private int photoId;
	private int propertyId;
	private String imagePath;
	private String caption;

	public PropertyPhoto(int photoId, int propertyId, String imagePath, String caption) {
		this.photoId = photoId;
		this.propertyId = propertyId;
		this.imagePath = imagePath;
		this.caption = caption;
	}

	/**
	 * Loads the image, resized to fit inside width x height, for the table or the gallery.
	 * Returns null if the file is missing, so the screen can show "No photo" instead of crashing.
	 */
	public ImageIcon loadImage(int width, int height) {
		File file = new File(imagePath);
		if (!file.exists()) {
			return null;
		}
		ImageIcon original = new ImageIcon(file.getAbsolutePath());
		int w = original.getIconWidth();
		int h = original.getIconHeight();
		if (w <= 0 || h <= 0) {
			return null; // not a readable image
		}
		double scale = Math.min((double) width / w, (double) height / h);
		Image scaled = original.getImage().getScaledInstance((int) (w * scale), (int) (h * scale), Image.SCALE_SMOOTH);
		return new ImageIcon(scaled);
	}

	public int getPhotoId() {
		return photoId;
	}

	public void setPhotoId(int photoId) {
		this.photoId = photoId;
	}

	public int getPropertyId() {
		return propertyId;
	}

	public void setPropertyId(int propertyId) {
		this.propertyId = propertyId;
	}

	public String getImagePath() {
		return imagePath;
	}

	public void setImagePath(String imagePath) {
		this.imagePath = imagePath;
	}

	public String getCaption() {
		return caption;
	}

	public void setCaption(String caption) {
		this.caption = caption;
	}
}
