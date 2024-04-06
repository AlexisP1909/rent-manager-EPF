package com.epf.rentmanager.dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import com.epf.rentmanager.persistence.ConnectionManager;
import com.epf.rentmanager.model.Vehicle;
import com.epf.rentmanager.exceptions.DaoException;
import org.springframework.stereotype.Repository;

@Repository
public class VehicleDao {
	private VehicleDao() {}

	private static final String CREATE_VEHICLE_QUERY = "INSERT INTO Vehicle(constructeur, modele, nb_places) VALUES(?, ?,?);";
	private static final String DELETE_VEHICLE_QUERY = "DELETE FROM Vehicle WHERE id=?;";
	private static final String FIND_VEHICLE_QUERY = "SELECT id, constructeur, modele, nb_places FROM Vehicle WHERE id=?;";
	private static final String FIND_VEHICLES_QUERY = "SELECT id, constructeur, modele, nb_places FROM Vehicle;";
	private static final String DELETE_RESERVATION_BY_VEHICLE_QUERY = "DELETE FROM Reservation WHERE vehicle_id=?;";

	private static final String COUNT_VEHICLES_QUERY = "SELECT COUNT(id) FROM Vehicle;";

	public long create(Vehicle vehicle) throws DaoException {
		try(   Connection connection = ConnectionManager.getConnection();
			   PreparedStatement stmt =
					   connection.prepareStatement(CREATE_VEHICLE_QUERY,
							   Statement.RETURN_GENERATED_KEYS);) {
			stmt.setString(1, vehicle.constructeur());
			stmt.setString(2, vehicle.modele());
			stmt.setInt(3, vehicle.nb_places());


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

	public long delete(int id) throws DaoException {
		deleteResaByVehicleId(id);
		try (Connection connection = ConnectionManager.getConnection();
			 PreparedStatement stmt =
					 connection.prepareStatement(DELETE_VEHICLE_QUERY,
							 Statement.RETURN_GENERATED_KEYS);) {
			stmt.setLong(1,id);

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
	private void deleteResaByVehicleId(int id) throws DaoException {
		try (Connection connection = ConnectionManager.getConnection();
			 PreparedStatement stmt =
					 connection.prepareStatement(DELETE_RESERVATION_BY_VEHICLE_QUERY,
							 Statement.RETURN_GENERATED_KEYS);) {
			stmt.setLong(1,id);

			stmt.execute();
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	public Vehicle findById(long id) throws DaoException {
		try (Connection connection = ConnectionManager.getConnection();
			 PreparedStatement stmt =
					 connection.prepareStatement(FIND_VEHICLE_QUERY);) {

			stmt.setLong(1,id);


			ResultSet resultSet = stmt.executeQuery();
			if (resultSet.next()){
				String constructeur = resultSet.getString(2);
				String modele = resultSet.getString(3);
				int nb_places = resultSet.getInt(4);
				return new Vehicle((int) id, constructeur, modele, nb_places);
			}
			else{
				throw new DaoException();
			}
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	public List<Vehicle> findAll() throws DaoException {
		try (Connection connection = ConnectionManager.getConnection();
			 PreparedStatement stmt =
					 connection.prepareStatement(FIND_VEHICLES_QUERY);) {

			ResultSet resultSet = stmt.executeQuery();
			List<Vehicle> listeVehicles = new ArrayList<>();

			while(resultSet.next()){
				int id = resultSet.getInt(1);
				String constructeur = resultSet.getString(2);
				String modele = resultSet.getString(3);
				int nb_places = resultSet.getInt(4);

				listeVehicles.add(new Vehicle(id,constructeur,modele,nb_places));
			}
			return listeVehicles;
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
	public int count() throws DaoException{
		try(Connection connection = ConnectionManager.getConnection();
			PreparedStatement stmt =
					connection.prepareStatement(COUNT_VEHICLES_QUERY)){
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
