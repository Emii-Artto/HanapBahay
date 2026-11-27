package model;

import java.util.ArrayList;

/**
 * 5.1.5 Property - ABSTRACT parent class for all listings.
 *
 * ABSTRACTION: you cannot create a plain Property, only a HouseAndLot, VacantLot or Condominium.
 *   Every subclass MUST provide getPropertyType(), getSubtype() and getDetails().
 * ENCAPSULATION: setPrice(), setLotArea() and setStatus() refuse invalid values.
 * POLYMORPHISM: the Properties panel keeps all listings in one ArrayList<Property> and calls
 *   getDetails() on each one; Java runs the HouseAndLot, VacantLot or Condominium version.
 */
public abstract class Property {

	// the three statuses (3.3.3 Data Integrity)
	public static final String AVAILABLE = "AVAILABLE";
	public static final String RESERVED = "RESERVED";
	public static final String SOLD = "SOLD";

	// the three property types
	public static final String HOUSE_AND_LOT = "House and Lot";
	public static final String VACANT_LOT = "Vacant Lot";
	public static final String CONDOMINIUM = "Condominium";

	public static final int MAX_PHOTOS = 4;

	private int propertyId;
	private String title;
	private String developer;
	private String location;
	private double price;
	private double lotArea;
	private String status;
	private int agentId;
	private ArrayList<PropertyPhoto> photos = new ArrayList<PropertyPhoto>();

	public Property(int propertyId, String title, String developer, String location, double price, double lotArea,
			String status, int agentId) {
		this.propertyId = propertyId;
		this.title = title;
		this.developer = developer;
		this.location = location;
		this.price = price;
		this.lotArea = lotArea;
		this.status = status;
		this.agentId = agentId;
	}

	// ---- ABSTRACT METHODS: each property type gives its own answer ----
	public abstract String getPropertyType();

	public abstract String getSubtype();

	public abstract double getFloorArea();

	public abstract String getDetails();

	public boolean isAvailable() {
		return AVAILABLE.equals(status);
	}

	public boolean isSold() {
		return SOLD.equals(status);
	}

	// shown in drop-down lists
	@Override
	public String toString() {
		return title + " (" + location + ")";
	}

	public int getPropertyId() {
		return propertyId;
	}

	public void setPropertyId(int propertyId) {
		this.propertyId = propertyId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDeveloper() {
		return developer;
	}

	public void setDeveloper(String developer) {
		this.developer = developer;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public double getPrice() {
		return price;
	}

	// ENCAPSULATION: only a positive price is accepted
	public boolean setPrice(double price) {
		if (price <= 0) {
			return false;
		}
		this.price = price;
		return true;
	}

	public double getLotArea() {
		return lotArea;
	}

	// ENCAPSULATION: the lot area cannot be below zero
	public boolean setLotArea(double lotArea) {
		if (lotArea < 0) {
			return false;
		}
		this.lotArea = lotArea;
		return true;
	}

	public String getStatus() {
		return status;
	}

	// ENCAPSULATION: only Available, Reserved or Sold
	public boolean setStatus(String status) {
		if (!AVAILABLE.equals(status) && !RESERVED.equals(status) && !SOLD.equals(status)) {
			return false;
		}
		this.status = status;
		return true;
	}

	public int getAgentId() {
		return agentId;
	}

	public void setAgentId(int agentId) {
		this.agentId = agentId;
	}

	public ArrayList<PropertyPhoto> getPhotos() {
		return photos;
	}

	public void setPhotos(ArrayList<PropertyPhoto> photos) {
		this.photos = photos;
	}
}
