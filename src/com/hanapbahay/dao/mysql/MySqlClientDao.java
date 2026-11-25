package com.hanapbahay.dao.mysql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import com.hanapbahay.model.Client;

import com.hanapbahay.dao.DBConnection;
import com.hanapbahay.dao.ClientDao;

/**
 * ClientDAO - the clients table. Implements ClientDao (which extends CrudDAO) and adds a search by name or contact number.
 */
public class MySqlClientDao extends MySqlBaseDao implements ClientDao {

	@Override
	public boolean add(Client client) {
		Connection conn = DBConnection.getConnection();
		if (conn == null) {
			return false;
		}
		try {
			PreparedStatement ps = conn.prepareStatement(
					"INSERT INTO clients (first_name, last_name, contact_number, email, budget, preferred_location, "
							+ "preferred_type, agent_id) VALUES (?, ?, ?, ?, ?, ?, ?, ?)",
					Statement.RETURN_GENERATED_KEYS);
			fillStatement(ps, client);
			ps.setInt(8, client.getAgentId());
			ps.executeUpdate();
			ResultSet keys = ps.getGeneratedKeys();
			if (keys.next()) {
				client.setClientId(keys.getInt(1));
			}
			return true;
		} catch (SQLException e) {
			printError("Add client", e);
			return false;
		} finally {
			close(conn);
		}
	}

	@Override
	public boolean update(Client client) {
		Connection conn = DBConnection.getConnection();
		if (conn == null) {
			return false;
		}
		try {
			PreparedStatement ps = conn.prepareStatement(
					"UPDATE clients SET first_name = ?, last_name = ?, contact_number = ?, email = ?, budget = ?, "
							+ "preferred_location = ?, preferred_type = ? WHERE client_id = ?");
			fillStatement(ps, client);
			ps.setInt(8, client.getClientId());
			return ps.executeUpdate() > 0;
		} catch (SQLException e) {
			printError("Update client", e);
			return false;
		} finally {
			close(conn);
		}
	}

	private void fillStatement(PreparedStatement ps, Client client) throws SQLException {
		ps.setString(1, client.getFirstName());
		ps.setString(2, client.getLastName());
		ps.setString(3, client.getContactNumber());
		ps.setString(4, client.getEmail());
		ps.setDouble(5, client.getBudget());
		ps.setString(6, client.getPreferredLocation());
		ps.setString(7, client.getPreferredType());
	}

	@Override
	public boolean delete(int clientId) {
		Connection conn = DBConnection.getConnection();
		if (conn == null) {
			return false;
		}
		try {
			PreparedStatement ps = conn.prepareStatement("DELETE FROM clients WHERE client_id = ?");
			ps.setInt(1, clientId);
			return ps.executeUpdate() > 0;
		} catch (SQLException e) {
			printError("Delete client", e);
			return false;
		} finally {
			close(conn);
		}
	}

	@Override
	public ArrayList<Client> getAll() {
		ArrayList<Client> clients = new ArrayList<Client>();
		Connection conn = DBConnection.getConnection();
		if (conn == null) {
			return clients;
		}
		try {
			PreparedStatement ps = conn.prepareStatement("SELECT * FROM clients ORDER BY last_name, first_name");
			readRows(ps, clients);
		} catch (SQLException e) {
			printError("Get clients", e);
		} finally {
			close(conn);
		}
		return clients;
	}

	@Override
	public ArrayList<Client> getByAgent(int agentId) {
		ArrayList<Client> clients = new ArrayList<Client>();
		Connection conn = DBConnection.getConnection();
		if (conn == null) {
			return clients;
		}
		try {
			PreparedStatement ps = conn
					.prepareStatement("SELECT * FROM clients WHERE agent_id = ? ORDER BY last_name, first_name");
			ps.setInt(1, agentId);
			readRows(ps, clients);
		} catch (SQLException e) {
			printError("Get agent clients", e);
		} finally {
			close(conn);
		}
		return clients;
	}

	/** 5.4.5 Client Search: by name or contact number, only this agent's clients. */
	@Override
	public ArrayList<Client> search(String keyword, int agentId) {
		ArrayList<Client> clients = new ArrayList<Client>();
		Connection conn = DBConnection.getConnection();
		if (conn == null) {
			return clients;
		}
		try {
			PreparedStatement ps = conn.prepareStatement("SELECT * FROM clients WHERE agent_id = ? AND "
					+ "(first_name LIKE ? OR last_name LIKE ? OR CONCAT(first_name, ' ', last_name) LIKE ? "
					+ "OR contact_number LIKE ?) ORDER BY last_name, first_name");
			String like = "%" + keyword + "%";
			ps.setInt(1, agentId);
			ps.setString(2, like);
			ps.setString(3, like);
			ps.setString(4, like);
			ps.setString(5, like);
			readRows(ps, clients);
		} catch (SQLException e) {
			printError("Search clients", e);
		} finally {
			close(conn);
		}
		return clients;
	}

	@Override
	public Client findById(int clientId) {
		ArrayList<Client> found = new ArrayList<Client>();
		Connection conn = DBConnection.getConnection();
		if (conn == null) {
			return null;
		}
		try {
			PreparedStatement ps = conn.prepareStatement("SELECT * FROM clients WHERE client_id = ?");
			ps.setInt(1, clientId);
			readRows(ps, found);
		} catch (SQLException e) {
			printError("Find client", e);
		} finally {
			close(conn);
		}
		if (found.isEmpty()) {
			return null;
		}
		return found.get(0);
	}

	private void readRows(PreparedStatement ps, ArrayList<Client> clients) throws SQLException {
		ResultSet rs = ps.executeQuery();
		while (rs.next()) {
			clients.add(new Client(rs.getInt("client_id"), rs.getString("first_name"), rs.getString("last_name"),
					rs.getString("contact_number"), rs.getString("email"), rs.getDouble("budget"),
					rs.getString("preferred_location"), rs.getString("preferred_type"), rs.getInt("agent_id")));
		}
	}
}
