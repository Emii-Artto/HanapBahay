package com.hanapbahay.dao.mysql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import com.hanapbahay.model.Viewing;

import com.hanapbahay.dao.DBConnection;
import com.hanapbahay.dao.ViewingDao;

/**
 * ViewingDAO - the viewings table. Implements ViewingDao (which extends CrudDAO) and adds the checks
 * for double-booked viewings and for delete protection.
 */
public class MySqlViewingDao extends MySqlBaseDao implements ViewingDao {

	@Override
	public boolean add(Viewing viewing) {
		Connection conn = DBConnection.getConnection();
		if (conn == null) {
			return false;
		}
		try {
			PreparedStatement ps = conn.prepareStatement(
					"INSERT INTO viewings (client_id, property_id, agent_id, viewing_date, viewing_time, status) "
							+ "VALUES (?, ?, ?, ?, ?, ?)",
					Statement.RETURN_GENERATED_KEYS);
			ps.setInt(1, viewing.getClientId());
			ps.setInt(2, viewing.getPropertyId());
			ps.setInt(3, viewing.getAgentId());
			ps.setString(4, viewing.getViewingDate());
			ps.setString(5, viewing.getViewingTime());
			ps.setString(6, viewing.getStatus());
			ps.executeUpdate();
			ResultSet keys = ps.getGeneratedKeys();
			if (keys.next()) {
				viewing.setViewingId(keys.getInt(1));
			}
			return true;
		} catch (SQLException e) {
			printError("Add viewing", e);
			return false;
		} finally {
			close(conn);
		}
	}

	// Only the status changes after a viewing is scheduled
	@Override
	public boolean update(Viewing viewing) {
		Connection conn = DBConnection.getConnection();
		if (conn == null) {
			return false;
		}
		try {
			PreparedStatement ps = conn.prepareStatement("UPDATE viewings SET status = ? WHERE viewing_id = ?");
			ps.setString(1, viewing.getStatus());
			ps.setInt(2, viewing.getViewingId());
			return ps.executeUpdate() > 0;
		} catch (SQLException e) {
			printError("Update viewing", e);
			return false;
		} finally {
			close(conn);
		}
	}

	@Override
	public boolean delete(int viewingId) {
		Connection conn = DBConnection.getConnection();
		if (conn == null) {
			return false;
		}
		try {
			PreparedStatement ps = conn.prepareStatement("DELETE FROM viewings WHERE viewing_id = ?");
			ps.setInt(1, viewingId);
			return ps.executeUpdate() > 0;
		} catch (SQLException e) {
			printError("Delete viewing", e);
			return false;
		} finally {
			close(conn);
		}
	}

	@Override
	public ArrayList<Viewing> getAll() {
		ArrayList<Viewing> viewings = new ArrayList<Viewing>();
		Connection conn = DBConnection.getConnection();
		if (conn == null) {
			return viewings;
		}
		try {
			PreparedStatement ps = conn
					.prepareStatement("SELECT * FROM viewings ORDER BY viewing_date DESC, viewing_time DESC");
			readRows(ps, viewings);
		} catch (SQLException e) {
			printError("Get viewings", e);
		} finally {
			close(conn);
		}
		return viewings;
	}

	@Override
	public ArrayList<Viewing> getByAgent(int agentId) {
		ArrayList<Viewing> viewings = new ArrayList<Viewing>();
		Connection conn = DBConnection.getConnection();
		if (conn == null) {
			return viewings;
		}
		try {
			PreparedStatement ps = conn.prepareStatement(
					"SELECT * FROM viewings WHERE agent_id = ? ORDER BY viewing_date DESC, viewing_time DESC");
			ps.setInt(1, agentId);
			readRows(ps, viewings);
		} catch (SQLException e) {
			printError("Get agent viewings", e);
		} finally {
			close(conn);
		}
		return viewings;
	}

	@Override
	public Viewing findById(int viewingId) {
		ArrayList<Viewing> found = new ArrayList<Viewing>();
		Connection conn = DBConnection.getConnection();
		if (conn == null) {
			return null;
		}
		try {
			PreparedStatement ps = conn.prepareStatement("SELECT * FROM viewings WHERE viewing_id = ?");
			ps.setInt(1, viewingId);
			readRows(ps, found);
		} catch (SQLException e) {
			printError("Find viewing", e);
		} finally {
			close(conn);
		}
		if (found.isEmpty()) {
			return null;
		}
		return found.get(0);
	}

	/** 5.3.5 Is this property already booked (not cancelled) at this date and time? */
	@Override
	public boolean isSlotTaken(int propertyId, String date, String time) {
		return count("SELECT COUNT(*) FROM viewings WHERE property_id = ? AND viewing_date = ? AND viewing_time = ? "
				+ "AND status <> 'CANCELLED'", propertyId, date, time) > 0;
	}

	/** 5.3.7 Delete protection: how many viewings use this property? */
	@Override
	public int countByProperty(int propertyId) {
		return count("SELECT COUNT(*) FROM viewings WHERE property_id = ?", propertyId, null, null);
	}

	@Override
	public int countByClient(int clientId) {
		return count("SELECT COUNT(*) FROM viewings WHERE client_id = ?", clientId, null, null);
	}

	private int count(String sql, int id, String date, String time) {
		Connection conn = DBConnection.getConnection();
		if (conn == null) {
			return 0;
		}
		try {
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setInt(1, id);
			if (date != null) {
				ps.setString(2, date);
				ps.setString(3, time);
			}
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				return rs.getInt(1);
			}
		} catch (SQLException e) {
			printError("Count viewings", e);
		} finally {
			close(conn);
		}
		return 0;
	}

	private void readRows(PreparedStatement ps, ArrayList<Viewing> viewings) throws SQLException {
		ResultSet rs = ps.executeQuery();
		while (rs.next()) {
			String time = rs.getString("viewing_time"); // MySQL gives "14:30:00"
			if (time != null && time.length() > 5) {
				time = time.substring(0, 5); // keep "14:30"
			}
			viewings.add(new Viewing(rs.getInt("viewing_id"), rs.getInt("client_id"), rs.getInt("property_id"),
					rs.getInt("agent_id"), rs.getString("viewing_date"), time, rs.getString("status")));
		}
	}
}
