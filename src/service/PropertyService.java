package service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import dao.DealDao;
import dao.PropertyDao;
import dao.ViewingDao;
import model.Condominium;
import model.HouseAndLot;
import model.Property;
import model.PropertyPhoto;
import model.VacantLot;

/**
 * Property listings and their photos: 3.1.1, 3.1.2, 5.3.1, 5.3.2, 5.3.4, 5.3.7 and 5.4.
 * An agentId of 0 means "all agents" (used by the Admin).
 */
public class PropertyService extends BaseService {

	public static final String ALL = "All";
	public static final String PHOTO_FOLDER = "photos";

	private PropertyDao propertyDAO;
	private ViewingDao viewingDAO;
	private DealDao dealDAO;

	public PropertyService(PropertyDao propertyDAO, ViewingDao viewingDAO, DealDao dealDAO) {
		this.propertyDAO = propertyDAO;
		this.viewingDAO = viewingDAO;
		this.dealDAO = dealDAO;
	}

	public ArrayList<Property> getListings(int agentId) {
		if (agentId == 0) {
			return propertyDAO.getAll();
		}
		return propertyDAO.getByAgent(agentId);
	}

	public Property findById(int propertyId) {
		return propertyDAO.findById(propertyId);
	}

	/** The subtypes that belong to a property type (for the second drop-down list). */
	public static String[] getSubtypes(String type) {
		if (Property.VACANT_LOT.equals(type)) {
			return VacantLot.LOT_USES;
		}
		if (Property.CONDOMINIUM.equals(type)) {
			return Condominium.UNIT_TYPES;
		}
		return HouseAndLot.SUBTYPES;
	}

	/**
	 * 5.4 Search / Filtering. Every filter is optional ("" or "All" means not used).
	 * Uses the two OVERLOADED search() methods of PropertyDao: by keyword, and by price range.
	 * Returns null if a price filter is not a valid number (see getMessage()).
	 */
	public ArrayList<Property> findProperties(int agentId, String keyword, String minPriceText, String maxPriceText,
			String type, String subtype, String location, String status) {
		message = "";
		ArrayList<Property> results;
		if (isBlank(keyword)) {
			results = getListings(agentId);
		} else {
			results = keepAgent(propertyDAO.search(keyword.trim()), agentId); // search(String)
		}

		if (!isBlank(minPriceText) || !isBlank(maxPriceText)) {
			double min = 0;
			double max = 999999999999.0;
			if (!isBlank(minPriceText)) {
				min = readNumber(minPriceText, "minimum price");
			}
			if (!isBlank(maxPriceText)) {
				max = readNumber(maxPriceText, "maximum price");
			}
			if (min < 0 || max < 0) {
				if (message.isEmpty()) {
					message = "Prices cannot be negative.";
				}
				return null;
			}
			if (min > max) {
				message = "The minimum price is higher than the maximum price.";
				return null;
			}
			results = keepSameIds(results, propertyDAO.search(min, max)); // search(double, double)
		}

		ArrayList<Property> filtered = new ArrayList<Property>();
		for (Property p : results) {
			if (!isBlank(type) && !ALL.equals(type) && !type.equals(p.getPropertyType())) {
				continue;
			}
			if (!isBlank(subtype) && !ALL.equals(subtype) && !subtype.equals(p.getSubtype())) {
				continue;
			}
			if (!isBlank(location) && !p.getLocation().toLowerCase().contains(location.trim().toLowerCase())) {
				continue;
			}
			if (!isBlank(status) && !ALL.equalsIgnoreCase(status) && !status.equalsIgnoreCase(p.getStatus())) {
				continue;
			}
			filtered.add(p);
		}
		message = filtered.size() + " matching " + (filtered.size() == 1 ? "property" : "properties");
		return filtered;
	}

	private ArrayList<Property> keepAgent(ArrayList<Property> list, int agentId) {
		if (agentId == 0) {
			return list;
		}
		ArrayList<Property> kept = new ArrayList<Property>();
		for (Property p : list) {
			if (p.getAgentId() == agentId) {
				kept.add(p);
			}
		}
		return kept;
	}

	private ArrayList<Property> keepSameIds(ArrayList<Property> list, ArrayList<Property> allowed) {
		ArrayList<Property> kept = new ArrayList<Property>();
		for (Property p : list) {
			for (Property a : allowed) {
				if (a.getPropertyId() == p.getPropertyId()) {
					kept.add(p);
					break;
				}
			}
		}
		return kept;
	}

	// ======================= add / edit / delete =======================

	/** Adds a listing for this agent with up to 4 photos (photoFiles may be empty). */
	public boolean addProperty(int agentId, String title, String type, String subtype, String developer,
			String location, String priceText, String lotAreaText, String floorAreaText, String status,
			ArrayList<File> photoFiles) {
		Property property = buildProperty(0, agentId, title, type, subtype, developer, location, priceText,
				lotAreaText, floorAreaText, status);
		if (property == null || !checkPhotos(photoFiles)) {
			return false; // message already explains which field is wrong
		}
		if (!propertyDAO.add(property)) {
			message = "Cannot save the property. Please check the database connection.";
			return false;
		}
		if (!savePhotos(property.getPropertyId(), photoFiles)) {
			message = "The property was saved, but some photos could not be copied.";
			return true;
		}
		message = "Property \"" + property.getTitle() + "\" added (ID P" + String.format("%03d", property.getPropertyId())
				+ ").";
		return true;
	}

	/** Changes a listing. newPhotoFiles == null keeps the current photos; a list replaces them. */
	public boolean updateProperty(int agentId, int propertyId, String title, String type, String subtype,
			String developer, String location, String priceText, String lotAreaText, String floorAreaText,
			String status, ArrayList<File> newPhotoFiles) {
		Property old = findOwnProperty(agentId, propertyId);
		if (old == null) {
			return false;
		}
		Property property = buildProperty(propertyId, old.getAgentId(), title, type, subtype, developer, location,
				priceText, lotAreaText, floorAreaText, status);
		if (property == null) {
			return false;
		}
		if (newPhotoFiles != null && !checkPhotos(newPhotoFiles)) {
			return false;
		}
		if (!property.getStatus().equals(old.getStatus()) && dealDAO.hasActiveDeal(propertyId)) {
			message = "\"" + old.getTitle() + "\" has an active deal, so its status is set by the deal.\n"
					+ "Change the deal's status in the Deals section instead.";
			return false;
		}
		if (!propertyDAO.update(property)) {
			message = "Cannot update the property. Please check the database connection.";
			return false;
		}
		if (newPhotoFiles != null) {
			propertyDAO.deletePhotos(propertyId);
			savePhotos(propertyId, newPhotoFiles);
		}
		message = "Property \"" + property.getTitle() + "\" updated.";
		return true;
	}

	/** 5.3.7 Delete Protection: a property with viewings or deals cannot be deleted. */
	public boolean deleteProperty(int agentId, int propertyId) {
		Property property = findOwnProperty(agentId, propertyId);
		if (property == null) {
			return false;
		}
		int viewings = viewingDAO.countByProperty(propertyId);
		int deals = dealDAO.countByProperty(propertyId);
		if (viewings > 0 || deals > 0) {
			message = "Cannot delete \"" + property.getTitle() + "\" because it has " + viewings + " viewing(s) and "
					+ deals + " deal(s).\nThese records must stay so the history is not lost.";
			return false;
		}
		if (!propertyDAO.delete(propertyId)) {
			message = "Cannot delete the property. Please check the database connection.";
			return false;
		}
		message = "Property \"" + property.getTitle() + "\" was deleted.";
		return true;
	}

	public boolean setStatus(int propertyId, String status) {
		return propertyDAO.updateStatus(propertyId, status);
	}

	// The property if it exists and belongs to this agent (the Admin, agentId 0, may use any)
	private Property findOwnProperty(int agentId, int propertyId) {
		Property property = propertyDAO.findById(propertyId);
		if (property == null) {
			message = "That property was not found. It may have been deleted.";
			return null;
		}
		if (agentId != 0 && property.getAgentId() != agentId) {
			message = "You can only change your own listings.";
			return null;
		}
		return property;
	}

	/**
	 * Checks the form (5.3.1 and 5.3.2) and creates the right subclass.
	 * POLYMORPHISM: the variable is a Property, the object is a HouseAndLot, VacantLot or Condominium.
	 */
	private Property buildProperty(int propertyId, int agentId, String title, String type, String subtype,
			String developer, String location, String priceText, String lotAreaText, String floorAreaText,
			String status) {
		message = "";
		if (isBlank(title)) {
			message = "Please enter the property title.";
			return null;
		}
		if (isBlank(location)) {
			message = "Please enter the location of \"" + title.trim() + "\".";
			return null;
		}
		if (!Property.HOUSE_AND_LOT.equals(type) && !Property.VACANT_LOT.equals(type)
				&& !Property.CONDOMINIUM.equals(type)) {
			message = "Please choose a property type.";
			return null;
		}
		if (isBlank(subtype)) {
			message = "Please choose the subtype of \"" + title.trim() + "\".";
			return null;
		}
		double price = readNumber(priceText, "price");
		if (price <= 0) {
			if (message.isEmpty()) { // a number was typed, but it is zero or negative
				message = "The price of \"" + title.trim() + "\" must be greater than zero.";
			}
			return null;
		}
		if (price > 999999999999.0) {
			message = "The price of \"" + title.trim() + "\" is too large.";
			return null;
		}
		double lotArea = 0;
		if (!Property.CONDOMINIUM.equals(type)) {
			lotArea = readNumber(lotAreaText, "lot area");
			if (lotArea < 0) {
				if (message.isEmpty() || message.startsWith("Please enter")) {
					message = "Please enter a lot area of zero or more (in square meters).";
				}
				return null;
			}
		}
		double floorArea = 0;
		if (!Property.VACANT_LOT.equals(type)) {
			floorArea = readNumber(floorAreaText, "floor area");
			if (floorArea <= 0) {
				if (message.isEmpty() || floorArea == 0) {
					message = "Please enter a floor area greater than zero (in square meters).";
				}
				return null;
			}
		}
		String upperStatus = status == null ? "" : status.toUpperCase();
		if (!Property.AVAILABLE.equals(upperStatus) && !Property.RESERVED.equals(upperStatus)
				&& !Property.SOLD.equals(upperStatus)) {
			message = "The status must be Available, Reserved or Sold.";
			return null;
		}

		String cleanTitle = title.trim();
		String cleanDeveloper = developer == null ? "" : developer.trim();
		String cleanLocation = location.trim();
		if (Property.VACANT_LOT.equals(type)) {
			return new VacantLot(propertyId, cleanTitle, subtype, cleanDeveloper, cleanLocation, price, lotArea,
					upperStatus, agentId);
		}
		if (Property.CONDOMINIUM.equals(type)) {
			return new Condominium(propertyId, cleanTitle, subtype, cleanDeveloper, cleanLocation, price, floorArea,
					upperStatus, agentId);
		}
		return new HouseAndLot(propertyId, cleanTitle, subtype, cleanDeveloper, cleanLocation, price, lotArea,
				floorArea, upperStatus, agentId);
	}

	// ======================= photos =======================

	/** 5.3.4 Photo Validation: at most 4 files, and every file must be a real image. */
	private boolean checkPhotos(ArrayList<File> files) {
		if (files == null) {
			return true;
		}
		if (files.size() > Property.MAX_PHOTOS) {
			message = "You chose " + files.size() + " photos. A property can have at most " + Property.MAX_PHOTOS
					+ ".";
			return false;
		}
		for (File file : files) {
			if (!isImage(file)) {
				message = "\"" + file.getName() + "\" is not an image. Please choose JPG, PNG, GIF or BMP files.";
				return false;
			}
		}
		return true;
	}

	public static boolean isImage(File file) {
		String name = file.getName().toLowerCase();
		if (!(name.endsWith(".jpg") || name.endsWith(".jpeg") || name.endsWith(".png") || name.endsWith(".gif")
				|| name.endsWith(".bmp"))) {
			return false;
		}
		try {
			return ImageIO.read(file) != null; // null means the file is not really an image
		} catch (IOException e) {
			return false;
		}
	}

	/** Copies each photo into the photos folder and saves its path in the database. */
	private boolean savePhotos(int propertyId, ArrayList<File> files) {
		if (files == null || files.isEmpty()) {
			return true;
		}
		File folder = new File(PHOTO_FOLDER);
		if (!folder.exists()) {
			folder.mkdirs();
		}
		boolean allSaved = true;
		for (int i = 0; i < files.size(); i++) {
			File source = files.get(i);
			String name = source.getName();
			String extension = name.substring(name.lastIndexOf('.'));
			String path = PHOTO_FOLDER + "/property" + propertyId + "_" + (i + 1) + "_" + System.currentTimeMillis()
					+ extension;
			try {
				Files.copy(source.toPath(), new File(path).toPath(), StandardCopyOption.REPLACE_EXISTING);
				propertyDAO.addPhoto(new PropertyPhoto(0, propertyId, path, name));
			} catch (IOException e) {
				System.out.println("Cannot copy photo " + name + ": " + e.getMessage());
				allSaved = false;
			}
		}
		return allSaved;
	}
}
