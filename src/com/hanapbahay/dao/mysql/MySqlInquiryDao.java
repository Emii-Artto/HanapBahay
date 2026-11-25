package com.hanapbahay.dao.mysql;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import com.hanapbahay.dao.DBConnection;
import com.hanapbahay.dao.InquiryDao;
import com.hanapbahay.model.Inquiry;

public class MySqlInquiryDao implements InquiryDao {

	@Override
	public Inquiry findById(int inquiryId) {
		Inquiry inquiry = null;
		Connection conn = DBConnection.getConnection();
		if (conn == null) {
			return null;
		}
		try {
			PreparedStatement ps = conn.prepareStatement("SELECT * FROM inquiries WHERE inquiry_id = ?");
			ps.setInt(1, inquiryId);
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				inquiry = toInquiry(rs);
			}
		} catch (SQLException e) {
			System.out.println("findById inquiry failed: " + e.getMessage());
		} finally {
			close(conn);
		}
		return inquiry;
	}

	@Override
	public ArrayList<Inquiry> findAll() {
		ArrayList<Inquiry> inquiries = new ArrayList<Inquiry>();
		Connection conn = DBConnection.getConnection();
		if (conn == null) {
			return inquiries;
		}
		try {
			PreparedStatement ps = conn.prepareStatement(
					"SELECT * FROM inquiries ORDER BY inquiry_date DESC, inquiry_id DESC"); // newest first
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				inquiries.add(toInquiry(rs));
			}
		} catch (SQLException e) {
			System.out.println("findAll inquiries failed: " + e.getMessage());
		} finally {
			close(conn);
		}
		return inquiries;
	}

	@Override
	public void add(Inquiry inquiry) {
		Connection conn = DBConnection.getConnection();
		if (conn == null) {
			return;
		}
		try {
			PreparedStatement ps = conn.prepareStatement(
					"INSERT INTO inquiries (property_id, client_name, client_contact, message, agent_id, status, inquiry_date) "
							+ "VALUES (?, ?, ?, ?, ?, ?, ?)",
					Statement.RETURN_GENERATED_KEYS);
			ps.setInt(1, inquiry.getPropertyId());
			ps.setString(2, inquiry.getClientName());
			ps.setString(3, inquiry.getClientContact());
			ps.setString(4, inquiry.getMessage());
			ps.setInt(5, inquiry.getAgentId());
			ps.setString(6, inquiry.getStatus());
			ps.setDate(7, Date.valueOf(inquiry.getInquiryDate())); // "yyyy-mm-dd" -> SQL DATE
			ps.executeUpdate();

			ResultSet keys = ps.getGeneratedKeys();
			if (keys.next()) {
				inquiry.setInquiryId(keys.getInt(1));
			}
		} catch (SQLException | IllegalArgumentException e) {
			// IllegalArgumentException: the date text is not in yyyy-mm-dd format
			System.out.println("add inquiry failed: " + e.getMessage());
		} finally {
			close(conn);
		}
	}

	@Override
	public void update(Inquiry inquiry) {
		Connection conn = DBConnection.getConnection();
		if (conn == null) {
			return;
		}
		try {
			PreparedStatement ps = conn.prepareStatement(
					"UPDATE inquiries SET property_id = ?, client_name = ?, client_contact = ?, message = ?, "
							+ "agent_id = ?, status = ?, inquiry_date = ? WHERE inquiry_id = ?");
			ps.setInt(1, inquiry.getPropertyId());
			ps.setString(2, inquiry.getClientName());
			ps.setString(3, inquiry.getClientContact());
			ps.setString(4, inquiry.getMessage());
			ps.setInt(5, inquiry.getAgentId());
			ps.setString(6, inquiry.getStatus());
			ps.setDate(7, Date.valueOf(inquiry.getInquiryDate()));
			ps.setInt(8, inquiry.getInquiryId());
			ps.executeUpdate();
		} catch (SQLException | IllegalArgumentException e) {
			System.out.println("update inquiry failed: " + e.getMessage());
		} finally {
			close(conn);
		}
	}

	/** Turns one row of the "inquiries" table into an Inquiry object. */
	private Inquiry toInquiry(ResultSet rs) throws SQLException {
		return new Inquiry(
				rs.getInt("inquiry_id"),
				rs.getInt("property_id"),
				rs.getString("client_name"),
				rs.getString("client_contact"),
				rs.getString("message"),
				rs.getInt("agent_id"),
				rs.getString("status"),
				rs.getString("inquiry_date")); // DATE comes back as "yyyy-mm-dd"
	}

	private void close(Connection conn) {
		try {
			conn.close();
		} catch (SQLException e) {
			System.out.println("Cannot close the connection: " + e.getMessage());
		}
	}
}
