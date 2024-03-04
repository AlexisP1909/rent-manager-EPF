package com.epf.rentmanager.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.epf.rentmanager.model.Client;
import com.epf.rentmanager.persistence.ConnectionManager;
import com.epf.rentmanager.model.Reservation;
import com.epf.rentmanager.exceptions.DaoException;

public class ReservationDao {

	private static ReservationDao instance = null;
	private ReservationDao() {}
	public static ReservationDao getInstance() {
		if(instance == null) {
			instance = new ReservationDao();
		}
		return instance;
	}
	
	private static final String CREATE_RESERVATION_QUERY = "INSERT INTO Reservation(client_id, vehicle_id, debut, fin) VALUES(?, ?, ?, ?);";
	private static final String DELETE_RESERVATION_QUERY = "DELETE FROM Reservation WHERE id=?;";
	private static final String FIND_RESERVATIONS_BY_CLIENT_QUERY = "SELECT id, vehicle_id, debut, fin FROM Reservation WHERE client_id=?;";
	private static final String FIND_RESERVATIONS_BY_VEHICLE_QUERY = "SELECT id, client_id, debut, fin FROM Reservation WHERE vehicle_id=?;";
	private static final String FIND_RESERVATIONS_QUERY = "SELECT id, client_id, vehicle_id, debut, fin FROM Reservation;";

	private static final String COUNT_RESERVATIONS_QUERY = "SELECT Count(id) FROM Reservation;";

	public long create(Reservation reservation) throws DaoException {
		try(   Connection connection = ConnectionManager.getConnection();
			   PreparedStatement stmt =
					   connection.prepareStatement(CREATE_RESERVATION_QUERY,
							   Statement.RETURN_GENERATED_KEYS);) {
			stmt.setInt(1, reservation.client_id());
			stmt.setInt(2, reservation.vehicle_id());
			stmt.setDate(3,Date.valueOf(reservation.debut()));
			stmt.setDate(4,Date.valueOf(reservation.fin()));

			stmt.execute();

			ResultSet resultSet = stmt.getGeneratedKeys();
			if (resultSet.next()) {
				return resultSet.getInt(1);
			}
			else{
				throw new DaoException();
			}

		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
	
	public long delete(Reservation reservation) throws DaoException {
		try (Connection connection = ConnectionManager.getConnection();
			 PreparedStatement stmt =
					 connection.prepareStatement(DELETE_RESERVATION_QUERY,
							 Statement.RETURN_GENERATED_KEYS);) {
			stmt.setInt(1,reservation.id());

			stmt.execute();

			ResultSet resultSet = stmt.getGeneratedKeys();
			if (resultSet.next()) {
				return resultSet.getInt(1);
			}
			else{
				throw new DaoException();
			}
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	
	public List<Reservation> findResaByClientId(long clientId) throws DaoException {
		try (Connection connection = ConnectionManager.getConnection();
			 PreparedStatement stmt =
					 connection.prepareStatement(FIND_RESERVATIONS_BY_CLIENT_QUERY);) {
			stmt.setInt(1,(int)clientId);
			ResultSet resultSet = stmt.executeQuery();
			List<Reservation> listeResa = new ArrayList<>();

			while(resultSet.next()){
				int id = resultSet.getInt(1);
				int vehicle_id = resultSet.getInt(2);
				LocalDate debut = resultSet.getDate(3).toLocalDate();
				LocalDate fin = resultSet.getDate(4).toLocalDate();

				listeResa.add(new Reservation(id,(int) clientId,vehicle_id,debut,fin));
			}
			return listeResa;
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
	
	public List<Reservation> findResaByVehicleId(long vehicleId) throws DaoException {
		try (Connection connection = ConnectionManager.getConnection();
			 PreparedStatement stmt =
					 connection.prepareStatement(FIND_RESERVATIONS_BY_VEHICLE_QUERY);) {
			stmt.setInt(1,(int)vehicleId);
			ResultSet resultSet = stmt.executeQuery();
			List<Reservation> listeResa = new ArrayList<>();

			while(resultSet.next()){
				int id = resultSet.getInt(1);
				int client_id = resultSet.getInt(2);
				LocalDate debut = resultSet.getDate(3).toLocalDate();
				LocalDate fin = resultSet.getDate(4).toLocalDate();

				listeResa.add(new Reservation(id,client_id,(int) vehicleId,debut,fin));
			}
			return listeResa;
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	public List<Reservation> findAll() throws DaoException {
		try (Connection connection = ConnectionManager.getConnection();
			 PreparedStatement stmt =
					 connection.prepareStatement(FIND_RESERVATIONS_QUERY);) {

			ResultSet resultSet = stmt.executeQuery();
			List<Reservation> listeResa = new ArrayList<>();

			while(resultSet.next()){
				int id = resultSet.getInt(1);
				int client_id = resultSet.getInt(2);
				int vehicle_id = resultSet.getInt(3);
				LocalDate debut = resultSet.getDate(4).toLocalDate();
				LocalDate fin = resultSet.getDate(5).toLocalDate();

				listeResa.add(new Reservation(id,client_id,vehicle_id,debut,fin));
			}
			return listeResa;
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
	public int count() throws DaoException{
		try(Connection connection = ConnectionManager.getConnection();
			PreparedStatement stmt =
					connection.prepareStatement(COUNT_RESERVATIONS_QUERY)){
			ResultSet resultset = stmt.executeQuery();
			if (resultset.next()) {
				return resultset.getInt(1);
			}
			else {
				throw new DaoException();
			}
		}catch (SQLException e) {
			throw new RuntimeException(e);
		}

	}
}
