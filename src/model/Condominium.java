package model;

/**
 * 5.1.8 Condominium - a condominium unit.
 * INHERITANCE: IS-A Property. Adds the unit type (studio, 1 bedroom, ...) and the floor area.
 * POLYMORPHISM: its own version of getDetails().
 */
public class Condominium extends Property {

	public static final String[] UNIT_TYPES = { "Studio", "1 Bedroom", "2 Bedroom", "3 Bedroom", "Loft", "Penthouse" };

	private String unitType;
	private double floorArea;

	public Condominium(int propertyId, String title, String unitType, String developer, String location, double price,
			double floorArea, String status, int agentId) {
		super(propertyId, title, developer, location, price, 0, status, agentId); // a condo unit has no lot
		this.unitType = unitType;
		this.floorArea = floorArea;
	}

	@Override
	public String getPropertyType() {
		return CONDOMINIUM;
	}

	// for a condominium, the subtype is its unit type
	@Override
	public String getSubtype() {
		return unitType;
	}

	@Override
	public double getFloorArea() {
		return floorArea;
	}

	@Override
	public String getDetails() {
		return unitType + " condominium unit with a " + String.format("%,.0f", floorArea) + " sqm floor area.";
	}

	public void setUnitType(String unitType) {
		this.unitType = unitType;
	}

	public void setFloorArea(double floorArea) {
		if (floorArea >= 0) {
			this.floorArea = floorArea;
		}
	}
}
