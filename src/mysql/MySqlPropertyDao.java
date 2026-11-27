package mysql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import model.Condominium;
import model.HouseAndLot;
import model.Property;
import model.PropertyPhoto;
import model.VacantLot;

import dao.DBConnection;
import dao.PropertyDao;

/**
 * PropertyDAO - the properties and property_photos tables.
 * Implements PropertyDao (which extends CrudDAO) and adds its own search methods.
 *
 * Each row becomes a HouseAndLot, VacantLot or Condominium object depending on property_type (5.2).
 */
public class MySqlPropertyDao extends MySqlBaseDao implements PropertyDao {

	private static final String SELECT = "SELECT * FROM properties ";

	@Override
	public boolean add(Property property) {
		Connection conn = DBConnection.getConnection();
		if (conn == null) {
			return false;
		}
		try {
			PreparedStatement ps = conn.prepareStatement(
					"INSERT INTO properties (title, property_type, property_subtype, developer, location, price, "
							+ "lot_area, floor_area, status, agent_id) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)",
					Statement.RETURN_GENERATED_KEYS);
			fillStatement(ps, property);
			ps.setInt(10, property.getAgentId());
			ps.executeUpdate();
			ResultSet keys = ps.getGeneratedKeys(); // the new AUTO_INCREMENT id
			if (keys.next()) {
				property.setPropertyId(keys.getInt(1));
			}
			return true;
		} catch (SQLException e) {
			printError("Add property", e);
			return false;
		} finally {
			close(conn);
		}
	}

	@Override
	public boolean update(Property property) {
		Connection conn = DBConnection.getConnection();
		if (conn == null) {
			return false;
		}
		try {
			PreparedStatement ps = conn.prepareStatement(
					"UPDATE properties SET title = ?, property_type = ?, property_subtype = ?, developer = ?, "
							+ "location = ?, price = ?, lot_area = ?, floor_area = ?, status = ? WHERE property_id = ?");
			fillStatement(ps, property);
			ps.setInt(10, property.getPropertyId());
			return ps.executeUpdate() > 0;
		} catch (SQLException e) {
			printError("Update property", e);
			return false;
		} finally {
			close(conn);
		}
	}

	// the first 9 ? are the same for INSERT and UPDATE
	private void fillStatement(PreparedStatement ps, Property property) throws SQLException {
		ps.setString(1, property.getTitle());
		ps.setString(2, property.getPropertyType()); // POLYMORPHISM: each subclass returns its own type
		ps.setString(3, property.getSubtype());
		ps.setString(4, property.getDeveloper());
		ps.setString(5, property.getLocation());
		ps.setDouble(6, property.getPrice());
		ps.setDouble(7, property.getLotArea());
		ps.setDouble(8, property.getFloorArea());
		ps.setString(9, property.getStatus());
	}

	@Override
	public boolean updateStatus(int propertyId, String status) {
		Connection conn = DBConnection.getConnection();
		if (conn == null) {
			return false;
		}
		try {
			PreparedStatement ps = conn.prepareStatement("UPDATE properties SET status = ? WHERE property_id = ?");
			ps.setString(1, status);
			ps.setInt(2, propertyId);
			return ps.executeUpdate() > 0;
		} catch (SQLException e) {
			printError("Update property status", e);
			return false;
		} finally {
			close(conn);
		}
	}

	@Override
	public boolean delete(int propertyId) {
		Connection conn = DBConnection.getConnection();
		if (conn == null) {
			return false;
		}
		try {
			// its photos are deleted automatically (ON DELETE CASCADE)
			PreparedStatement ps = conn.prepareStatement("DELETE FROM properties WHERE property_id = ?");
			ps.setInt(1, propertyId);
			return ps.executeUpdate() > 0;
		} catch (SQLException e) {
			printError("Delete property", e);
			return false;
		} finally {
			close(conn);
		}
	}

	@Override
	public ArrayList<Property> getAll() {
		ArrayList<Property> properties = new ArrayList<Property>();
		Connection conn = DBConnection.getConnection();
		if (conn == null) {
			return properties;
		}
		try {
			PreparedStatement ps = conn.prepareStatement(SELECT + "ORDER BY property_id");
			readRows(ps, properties);
		} catch (SQLException e) {
			printError("Get properties", e);
		} finally {
			close(conn);
		}
		attachPhotos(properties);
		return properties;
	}

	@Override
	public ArrayList<Property> getByAgent(int agentId) {
		ArrayList<Property> properties = new ArrayList<Property>();
		Connection conn = DBConnection.getConnection();
		if (conn == null) {
			return properties;
		}
		try {
			PreparedStatement ps = conn.prepareStatement(SELECT + "WHERE agent_id = ? ORDER BY property_id");
			ps.setInt(1, agentId);
			readRows(ps, properties);
		} catch (SQLException e) {
			printError("Get agent properties", e);
		} finally {
			close(conn);
		}
		attachPhotos(properties);
		return properties;
	}

	@Override
	public Property findById(int propertyId) {
		ArrayList<Property> found = new ArrayList<Property>();
		Connection conn = DBConnection.getConnection();
		if (conn == null) {
			return null;
		}
		try {
			PreparedStatement ps = conn.prepareStatement(SELECT + "WHERE property_id = ?");
			ps.setInt(1, propertyId);
			readRows(ps, found);
		} catch (SQLException e) {
			printError("Find property", e);
		} finally {
			close(conn);
		}
		if (found.isEmpty()) {
			return null;
		}
		attachPhotos(found);
		return found.get(0);
	}

	// ---- METHOD OVERLOADING (4.3): the same name, search(), with different parameters ----

	/** Search by a keyword in the title or location. */
	@Override
	public ArrayList<Property> search(String keyword) {
		ArrayList<Property> properties = new ArrayList<Property>();
		Connection conn = DBConnection.getConnection();
		if (conn == null) {
			return properties;
		}
		try {
			PreparedStatement ps = conn
					.prepareStatement(SELECT + "WHERE title LIKE ? OR location LIKE ? ORDER BY property_id");
			ps.setString(1, "%" + keyword + "%");
			ps.setString(2, "%" + keyword + "%");
			readRows(ps, properties);
		} catch (SQLException e) {
			printError("Search properties", e);
		} finally {
			close(conn);
		}
		attachPhotos(properties);
		return properties;
	}

	/** Search by a minimum and maximum price. */
	@Override
	public ArrayList<Property> search(double minPrice, double maxPrice) {
		ArrayList<Property> properties = new ArrayList<Property>();
		Connection conn = DBConnection.getConnection();
		if (conn == null) {
			return properties;
		}
		try {
			PreparedStatement ps = conn.prepareStatement(SELECT + "WHERE price BETWEEN ? AND ? ORDER BY price");
			ps.setDouble(1, minPrice);
			ps.setDouble(2, maxPrice);
			readRows(ps, properties);
		} catch (SQLException e) {
			printError("Search properties by price", e);
		} finally {
			close(conn);
		}
		attachPhotos(properties);
		return properties;
	}

	// runs the query and adds one Property object per row
	private void readRows(PreparedStatement ps, ArrayList<Property> properties) throws SQLException {
		ResultSet rs = ps.executeQuery();
		while (rs.next()) {
			properties.add(toProperty(rs));
		}
	}

	/** Turns one row into the right kind of Property object (5.2). */
	private Property toProperty(ResultSet rs) throws SQLException {
		int id = rs.getInt("property_id");
		String type = rs.getString("property_type");
		String title = rs.getString("title");
		String subtype = rs.getString("property_subtype");
		String developer = rs.getString("developer");
		String location = rs.getString("location");
		double price = rs.getDouble("price");
		double lotArea = rs.getDouble("lot_area");
		double floorArea = rs.getDouble("floor_area");
		String status = rs.getString("status");
		int agentId = rs.getInt("agent_id");

		if (Property.VACANT_LOT.equals(type)) {
			return new VacantLot(id, title, subtype, developer, location, price, lotArea, status, agentId);
		}
		if (Property.CONDOMINIUM.equals(type)) {
			return new Condominium(id, title, subtype, developer, location, price, floorArea, status, agentId);
		}
		return new HouseAndLot(id, title, subtype, developer, location, price, lotArea, floorArea, status, agentId);
	}

	// ---------------- photos (property_photos table) ----------------

	/** Loads the photos of every property in the list with one query. */
	private void attachPhotos(ArrayList<Property> properties) {
		if (properties.isEmpty()) {
			return;
		}
		Connection conn = DBConnection.getConnection();
		if (conn == null) {
			return;
		}
		try {
			PreparedStatement ps = conn.prepareStatement("SELECT * FROM property_photos ORDER BY photo_id");
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				PropertyPhoto photo = new PropertyPhoto(rs.getInt("photo_id"), rs.getInt("property_id"),
						rs.getString("image_path"), rs.getString("caption"));
				for (Property property : properties) {
					if (property.getPropertyId() == photo.getPropertyId()) {
						property.getPhotos().add(photo);
					}
				}
			}
		} catch (SQLException e) {
			printError("Get photos", e);
		} finally {
			close(conn);
		}
	}

	@Override
	public boolean addPhoto(PropertyPhoto photo) {
		Connection conn = DBConnection.getConnection();
		if (conn == null) {
			return false;
		}
		try {
			PreparedStatement ps = conn.prepareStatement(
					"INSERT INTO property_photos (property_id, image_path, caption) VALUES (?, ?, ?)");
			ps.setInt(1, photo.getPropertyId());
			ps.setString(2, photo.getImagePath());
			ps.setString(3, photo.getCaption());
			return ps.executeUpdate() > 0;
		} catch (SQLException e) {
			printError("Add photo", e);
			return false;
		} finally {
			close(conn);
		}
	}

	@Override
	public boolean deletePhotos(int propertyId) {
		Connection conn = DBConnection.getConnection();
		if (conn == null) {
			return false;
		}
		try {
			PreparedStatement ps = conn.prepareStatement("DELETE FROM property_photos WHERE property_id = ?");
			ps.setInt(1, propertyId);
			ps.executeUpdate();
			return true;
		} catch (SQLException e) {
			printError("Delete photos", e);
			return false;
		} finally {
			close(conn);
		}
	}
}
