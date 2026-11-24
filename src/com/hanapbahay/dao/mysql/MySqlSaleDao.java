package com.hanapbahay.dao.mysql;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import com.hanapbahay.dao.DBConnection;
import com.hanapbahay.dao.SaleDao;
import com.hanapbahay.model.Sale;

public class MySqlSaleDao implements SaleDao {

	@Override
	public ArrayList<Sale> findAll() {
		ArrayList<Sale> sales = new ArrayList<Sale>();
		Connection conn = DBConnection.getConnection();
		if (conn == null) {
			return sales;
		}
		try {
			PreparedStatement ps = conn.prepareStatement("SELECT * FROM sales ORDER BY sale_date DESC, sale_id DESC");
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				sales.add(toSale(rs));
			}
		} catch (SQLException e) {
			System.out.println("findAll sales failed: " + e.getMessage());
		} finally {
			close(conn);
		}
		return sales;
	}

	@Override
	public void add(Sale sale) {
		Connection conn = DBConnection.getConnection();
		if (conn == null) {
			return;
		}
		try {
			PreparedStatement ps = conn.prepareStatement(
					"INSERT INTO sales (property_id, buyer_name, buyer_contact, agent_id, sale_price, sale_date) "
							+ "VALUES (?, ?, ?, ?, ?, ?)",
					Statement.RETURN_GENERATED_KEYS);
			ps.setInt(1, sale.getPropertyId());
			ps.setString(2, sale.getBuyerName());
			ps.setString(3, sale.getBuyerContact());
			ps.setInt(4, sale.getAgentId());
			ps.setDouble(5, sale.getSalePrice());
			ps.setDate(6, Date.valueOf(sale.getSaleDate())); // "yyyy-mm-dd" -> SQL DATE
			ps.executeUpdate();

			ResultSet keys = ps.getGeneratedKeys();
			if (keys.next()) {
				sale.setSaleId(keys.getInt(1));
			}
		} catch (SQLException | IllegalArgumentException e) {
//date if invalid
			System.out.println("add sale failed: " + e.getMessage());
		} finally {
			close(conn);
		}
	}

//row sales
	private Sale toSale(ResultSet rs) throws SQLException {
		return new Sale(
				rs.getInt("sale_id"),
				rs.getInt("property_id"),
				rs.getString("buyer_name"),
				rs.getString("buyer_contact"),
				rs.getInt("agent_id"),
				rs.getDouble("sale_price"),
				rs.getString("sale_date"));
	}

	private void close(Connection conn) {
		try {
			conn.close();
		} catch (SQLException e) {
			System.out.println("Cannot close the connection: " + e.getMessage());
		}
	}
}
