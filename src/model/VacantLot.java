package model;

/**
 * 5.1.7 VacantLot - a lot without a building.
 * INHERITANCE: IS-A Property. Adds the intended lot use (residential or commercial).
 * POLYMORPHISM: its own version of getDetails().
 */
public class VacantLot extends Property {

	public static final String[] LOT_USES = { "Residential", "Commercial", "Agricultural" };

	private String lotUse;

	public VacantLot(int propertyId, String title, String lotUse, String developer, String location, double price,
			double lotArea, String status, int agentId) {
		super(propertyId, title, developer, location, price, lotArea, status, agentId);
		this.lotUse = lotUse;
	}

	@Override
	public String getPropertyType() {
		return VACANT_LOT;
	}

	// for a vacant lot, the subtype is its lot use
	@Override
	public String getSubtype() {
		return lotUse;
	}

	// a vacant lot has no building, so no floor area
	@Override
	public double getFloorArea() {
		return 0;
	}

	@Override
	public String getDetails() {
		return String.format("%,.0f", getLotArea()) + " sqm vacant lot for " + lotUse.toLowerCase() + " use.";
	}

	public void setLotUse(String lotUse) {
		this.lotUse = lotUse;
	}
}
