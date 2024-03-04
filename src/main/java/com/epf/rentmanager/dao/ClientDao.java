package com.epf.rentmanager.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.epf.rentmanager.persistence.ConnectionManager;
import com.epf.rentmanager.model.Client;
import com.epf.rentmanager.exceptions.DaoException;

public class ClientDao {
	private static ClientDao instance = null;

	private ClientDao() {
	}

	public static ClientDao getInstance() {
		if (instance == null) {
			instance = new ClientDao();
		}
		return instance;
	}

	private static final String CREATE_CLIENT_QUERY = "INSERT INTO Client(nom, prenom, email, naissance) VALUES(?, ?, ?, ?);";
	private static final String DELETE_CLIENT_QUERY = "DELETE FROM Client WHERE id=?;";
	private static final String FIND_CLIENT_QUERY = "SELECT nom, prenom, email, naissance FROM Client WHERE id=?;";
	private static final String FIND_CLIENTS_QUERY = "SELECT id, nom, prenom, email, naissance FROM Client;";

	private static final String COUNT_CLIENTS_QUERY = "SELECT COUNT(id) FROM Client;";

	public long create(Client client) throws DaoException {
		try (Connection connection = ConnectionManager.getConnection();
			 PreparedStatement stmt =
					 connection.prepareStatement(CREATE_CLIENT_QUERY,
							 Statement.RETURN_GENERATED_KEYS);) {
			stmt.setString(1, client.nom());
			stmt.setString(2, client.prenom());
			stmt.setString(3, client.email());
			stmt.setDate(4, Date.valueOf(client.naissance()));

			stmt.execute();

			ResultSet resultSet = stmt.getGeneratedKeys();
			if (resultSet.next()) {
				return resultSet.getInt(1);
			} else {
				throw new DaoException();
			}

		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	public long delete(Client client) throws DaoException {
		try (Connection connection = ConnectionManager.getConnection();
			 PreparedStatement stmt =
					 connection.prepareStatement(DELETE_CLIENT_QUERY,
							 Statement.RETURN_GENERATED_KEYS);) {
			stmt.setInt(1, client.id());

			stmt.execute();

			ResultSet resultSet = stmt.getGeneratedKeys();
			if (resultSet.next()) {
				return resultSet.getInt(1);
			} else {
				throw new DaoException();
			}
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	public Client findById(long id) throws DaoException {
		try (Connection connection = ConnectionManager.getConnection();
			 PreparedStatement stmt =
					 connection.prepareStatement(FIND_CLIENT_QUERY);) {

			stmt.setLong(1, id);


			ResultSet resultSet = stmt.executeQuery();
			if (resultSet.next()) {
				String nom = resultSet.getString(1);
				String prenom = resultSet.getString(2);
				String email = resultSet.getString(3);
				LocalDate naissance = resultSet.getDate(4).toLocalDate();
				return new Client((int) id, nom, prenom, email, naissance);
			} else {
				throw new DaoException();
			}
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	public List<Client> findAll() throws DaoException {
		try (Connection connection = ConnectionManager.getConnection();
			 PreparedStatement stmt =
					 connection.prepareStatement(FIND_CLIENTS_QUERY)) {

			ResultSet resultSet = stmt.executeQuery();
			List<Client> listeClients = new ArrayList<>();

			while (resultSet.next()) {
				int id = resultSet.getInt(1);
				String nom = resultSet.getString(2);
				String prenom = resultSet.getString(3);
				String email = resultSet.getString(4);
				LocalDate naissance = resultSet.getDate(5).toLocalDate();

				listeClients.add(new Client(id, nom, prenom, email, naissance));
			}
			return listeClients;
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
	public int count() throws DaoException {
		try(Connection connection = ConnectionManager.getConnection();
			PreparedStatement stmt =
					connection.prepareStatement(COUNT_CLIENTS_QUERY)) {
			ResultSet resultset = stmt.executeQuery();
			if (resultset.next()) {
				return resultset.getInt(1);
			}
			else {
				throw new DaoException();
			}
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}

	}
}

