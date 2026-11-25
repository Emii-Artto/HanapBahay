package com.hanapbahay.dao.mysql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import com.hanapbahay.dao.DBConnection;
import com.hanapbahay.dao.PropertyDao;
import com.hanapbahay.model.Property;

public class MySqlPropertyDao implements PropertyDao {

	@Override
	public Property findById(int propertyId) {
		Property property = null;
		Connection conn = DBConnection.getConnection();
		if (conn == null) {
			return null; // database is not reachable
		}
		try {
			PreparedStatement ps = conn.prepareStatement("SELECT * FROM properties WHERE property_id = ?");
			ps.setInt(1, propertyId);
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				property = toProperty(rs);
			}
		} catch (SQLException e) {
			System.out.println("findById property failed: " + e.getMessage());
		} finally {
			close(conn);
		}
		return property;
	}

	@Override
	public ArrayList<Property> findAll() {
		ArrayList<Property> properties = new ArrayList<Property>();
		Connection conn = DBConnection.getConnection();
		if (conn == null) {
			return properties; // empty list
		}
		try {
			PreparedStatement ps = conn.prepareStatement("SELECT * FROM properties ORDER BY property_id");
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				properties.add(toProperty(rs));
			}
		} catch (SQLException e) {
			System.out.println("findAll properties failed: " + e.getMessage());
		} finally {
			close(conn);
		}
		return properties;
	}

	@Override
	public void add(Property property) {
		Connection conn = DBConnection.getConnection();
		if (conn == null) {
			return;
		}
		try {
			PreparedStatement ps = conn.prepareStatement(
					"INSERT INTO properties (title, location, type, price, status, agent_id) VALUES (?, ?, ?, ?, ?, ?)",
					Statement.RETURN_GENERATED_KEYS);
			ps.setString(1, property.getTitle());
			ps.setString(2, property.getLocation());
			ps.setString(3, property.getType());
			ps.setDouble(4, property.getPrice());
			ps.setString(5, property.getStatus());
			ps.setInt(6, property.getAgentId());
			ps.executeUpdate();

			ResultSet keys = ps.getGeneratedKeys(); // the new property_id made by AUTO_INCREMENT
			if (keys.next()) {
				property.setPropertyId(keys.getInt(1));
			}
		} catch (SQLException e) {
			System.out.println("add property failed: " + e.getMessage());
		} finally {
			close(conn);
		}
	}

	@Override
	public void update(Property property) {
		Connection conn = DBConnection.getConnection();
		if (conn == null) {
			return;
		}
		try {
			PreparedStatement ps = conn.prepareStatement(
					"UPDATE properties SET title = ?, location = ?, type = ?, price = ?, status = ?, agent_id = ? "
							+ "WHERE property_id = ?");
			ps.setString(1, property.getTitle());
			ps.setString(2, property.getLocation());
			ps.setString(3, property.getType());
			ps.setDouble(4, property.getPrice());
			ps.setString(5, property.getStatus());
			ps.setInt(6, property.getAgentId());
			ps.setInt(7, property.getPropertyId());
			ps.executeUpdate();
		} catch (SQLException e) {
			System.out.println("update property failed: " + e.getMessage());
		} finally {
			close(conn);
		}
	}

	@Override
	public void delete(int propertyId) {
		Connection conn = DBConnection.getConnection();
		if (conn == null) {
			return;
		}
		try {
			PreparedStatement ps = conn.prepareStatement("DELETE FROM properties WHERE property_id = ?");
			ps.setInt(1, propertyId);
			ps.executeUpdate();
		} catch (SQLException e) {
			// fails if the property already has inquiries or a sale (foreign keys)
			System.out.println("delete property failed: " + e.getMessage());
		} finally {
			close(conn);
		}
	}

//row of porperties
	private Property toProperty(ResultSet rs) throws SQLException {
		return new Property(
				rs.getInt("property_id"),
				rs.getString("title"),
				rs.getString("location"),
				rs.getString("type"),
				rs.getDouble("price"),
				rs.getString("status"),
				rs.getInt("agent_id"));
	}

	private void close(Connection conn) {
		try {
			conn.close();
		} catch (SQLException e) {
			System.out.println("Cannot close the connection: " + e.getMessage());
		}
	}
}
