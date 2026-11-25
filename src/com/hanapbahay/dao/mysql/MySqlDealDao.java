package com.hanapbahay.dao.mysql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import com.hanapbahay.model.Deal;
import com.hanapbahay.model.PaymentMethod;

import com.hanapbahay.dao.DBConnection;
import com.hanapbahay.dao.DealDao;

/**
 * DealDAO - the deals and payment_methods tables. Implements DealDao (which extends CrudDAO) and adds the
 * check for a property that already has an active deal (5.3.6).
 */
public class MySqlDealDao extends MySqlBaseDao implements DealDao {

	@Override
	public boolean add(Deal deal) {
		Connection conn = DBConnection.getConnection();
		if (conn == null) {
			return false;
		}
		try {
			PreparedStatement ps = conn.prepareStatement(
					"INSERT INTO deals (property_id, client_id, agent_id, payment_method_id, agreed_price, deal_date, "
							+ "status) VALUES (?, ?, ?, ?, ?, ?, ?)",
					Statement.RETURN_GENERATED_KEYS);
			ps.setInt(1, deal.getPropertyId());
			ps.setInt(2, deal.getClientId());
			ps.setInt(3, deal.getAgentId());
			ps.setInt(4, deal.getPaymentMethodId());
			ps.setDouble(5, deal.getAgreedPrice());
			ps.setString(6, deal.getDealDate());
			ps.setString(7, deal.getStatus());
			ps.executeUpdate();
			ResultSet keys = ps.getGeneratedKeys();
			if (keys.next()) {
				deal.setDealId(keys.getInt(1));
			}
			return true;
		} catch (SQLException e) {
			printError("Add deal", e);
			return false;
		} finally {
			close(conn);
		}
	}

	// After a deal is recorded, its status (and the agreed price) can change
	@Override
	public boolean update(Deal deal) {
		Connection conn = DBConnection.getConnection();
		if (conn == null) {
			return false;
		}
		try {
			PreparedStatement ps = conn
					.prepareStatement("UPDATE deals SET agreed_price = ?, status = ? WHERE deal_id = ?");
			ps.setDouble(1, deal.getAgreedPrice());
			ps.setString(2, deal.getStatus());
			ps.setInt(3, deal.getDealId());
			return ps.executeUpdate() > 0;
		} catch (SQLException e) {
			printError("Update deal", e);
			return false;
		} finally {
			close(conn);
		}
	}

	@Override
	public boolean delete(int dealId) {
		Connection conn = DBConnection.getConnection();
		if (conn == null) {
			return false;
		}
		try {
			PreparedStatement ps = conn.prepareStatement("DELETE FROM deals WHERE deal_id = ?");
			ps.setInt(1, dealId);
			return ps.executeUpdate() > 0;
		} catch (SQLException e) {
			printError("Delete deal", e);
			return false;
		} finally {
			close(conn);
		}
	}

	@Override
	public ArrayList<Deal> getAll() {
		ArrayList<Deal> deals = new ArrayList<Deal>();
		Connection conn = DBConnection.getConnection();
		if (conn == null) {
			return deals;
		}
		try {
			PreparedStatement ps = conn.prepareStatement("SELECT * FROM deals ORDER BY deal_date DESC, deal_id DESC");
			readRows(ps, deals);
		} catch (SQLException e) {
			printError("Get deals", e);
		} finally {
			close(conn);
		}
		return deals;
	}

	@Override
	public ArrayList<Deal> getByAgent(int agentId) {
		ArrayList<Deal> deals = new ArrayList<Deal>();
		Connection conn = DBConnection.getConnection();
		if (conn == null) {
			return deals;
		}
		try {
			PreparedStatement ps = conn
					.prepareStatement("SELECT * FROM deals WHERE agent_id = ? ORDER BY deal_date DESC, deal_id DESC");
			ps.setInt(1, agentId);
			readRows(ps, deals);
		} catch (SQLException e) {
			printError("Get agent deals", e);
		} finally {
			close(conn);
		}
		return deals;
	}

	@Override
	public Deal findById(int dealId) {
		ArrayList<Deal> found = new ArrayList<Deal>();
		Connection conn = DBConnection.getConnection();
		if (conn == null) {
			return null;
		}
		try {
			PreparedStatement ps = conn.prepareStatement("SELECT * FROM deals WHERE deal_id = ?");
			ps.setInt(1, dealId);
			readRows(ps, found);
		} catch (SQLException e) {
			printError("Find deal", e);
		} finally {
			close(conn);
		}
		if (found.isEmpty()) {
			return null;
		}
		return found.get(0);
	}

	/** 5.3.6 Does this property already have a Pending or Completed deal? */
	@Override
	public boolean hasActiveDeal(int propertyId) {
		return count("SELECT COUNT(*) FROM deals WHERE property_id = ? AND status IN ('PENDING', 'COMPLETED')",
				propertyId) > 0;
	}

	/** 5.3.7 Delete protection: how many deals use this property? */
	@Override
	public int countByProperty(int propertyId) {
		return count("SELECT COUNT(*) FROM deals WHERE property_id = ?", propertyId);
	}

	@Override
	public int countByClient(int clientId) {
		return count("SELECT COUNT(*) FROM deals WHERE client_id = ?", clientId);
	}

	private int count(String sql, int id) {
		Connection conn = DBConnection.getConnection();
		if (conn == null) {
			return 0;
		}
		try {
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setInt(1, id);
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				return rs.getInt(1);
			}
		} catch (SQLException e) {
			printError("Count deals", e);
		} finally {
			close(conn);
		}
		return 0;
	}

	private void readRows(PreparedStatement ps, ArrayList<Deal> deals) throws SQLException {
		ResultSet rs = ps.executeQuery();
		while (rs.next()) {
			deals.add(new Deal(rs.getInt("deal_id"), rs.getInt("property_id"), rs.getInt("client_id"),
					rs.getInt("agent_id"), rs.getInt("payment_method_id"), rs.getDouble("agreed_price"),
					rs.getString("deal_date"), rs.getString("status")));
		}
	}

	// ---------------- payment methods ----------------

	@Override
	public ArrayList<PaymentMethod> getPaymentMethods() {
		ArrayList<PaymentMethod> methods = new ArrayList<PaymentMethod>();
		Connection conn = DBConnection.getConnection();
		if (conn == null) {
			return methods;
		}
		try {
			PreparedStatement ps = conn.prepareStatement("SELECT * FROM payment_methods ORDER BY payment_method_id");
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				methods.add(new PaymentMethod(rs.getInt("payment_method_id"), rs.getString("method_name"),
						rs.getString("description")));
			}
		} catch (SQLException e) {
			printError("Get payment methods", e);
		} finally {
			close(conn);
		}
		return methods;
	}
}
