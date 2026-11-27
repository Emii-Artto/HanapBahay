package model;

/**
 * 5.1.6 HouseAndLot - a house and lot unit.
 * INHERITANCE: IS-A Property. Adds the subtype (townhouse, bungalow, ...) and the floor area.
 * POLYMORPHISM: its own version of getDetails().
 */
public class HouseAndLot extends Property {

	public static final String[] SUBTYPES = { "Townhouse", "Single Attached", "Single Detached", "Duplex", "Bungalow" };

	private String subtype;
	private double floorArea;

	public HouseAndLot(int propertyId, String title, String subtype, String developer, String location, double price,
			double lotArea, double floorArea, String status, int agentId) {
		super(propertyId, title, developer, location, price, lotArea, status, agentId);
		this.subtype = subtype;
		this.floorArea = floorArea;
	}

	@Override
	public String getPropertyType() {
		return HOUSE_AND_LOT;
	}

	@Override
	public String getSubtype() {
		return subtype;
	}

	@Override
	public double getFloorArea() {
		return floorArea;
	}

	@Override
	public String getDetails() {
		return subtype + " house and lot with a " + String.format("%,.0f", floorArea) + " sqm floor area on a "
				+ String.format("%,.0f", getLotArea()) + " sqm lot.";
	}

	public void setSubtype(String subtype) {
		this.subtype = subtype;
	}

	public void setFloorArea(double floorArea) {
		if (floorArea >= 0) {
			this.floorArea = floorArea;
		}
	}
}
