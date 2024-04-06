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

import com.epf.rentmanager.persistence.ConnectionManager;
import com.epf.rentmanager.model.Reservation;
import com.epf.rentmanager.exceptions.DaoException;
import org.springframework.stereotype.Repository;

@Repository
public class ReservationDao {

    private ReservationDao() {
    }

    private static final String CREATE_RESERVATION_QUERY = "INSERT INTO Reservation(client_id, vehicle_id, debut, fin) VALUES(?, ?, ?, ?);";
    private static final String DELETE_RESERVATION_QUERY = "DELETE FROM Reservation WHERE id=?;";
    private static final String FIND_RESERVATIONS_BY_CLIENT_QUERY = "SELECT id, vehicle_id, debut, fin FROM Reservation WHERE client_id=?;";
    private static final String FIND_RESERVATIONS_BY_VEHICLE_QUERY = "SELECT id, client_id, debut, fin FROM Reservation WHERE vehicle_id=?;";
    private static final String FIND_RESERVATIONS_BY_VEHICLE_QUERY_SORTED_END_DATE = "SELECT id, client_id, debut, fin FROM Reservation WHERE vehicle_id=? ORDER BY fin;";
    private static final String FIND_RESERVATIONS_QUERY = "SELECT id, client_id, vehicle_id, debut, fin FROM Reservation;";

    private static final String COUNT_RESERVATIONS_QUERY = "SELECT Count(id) FROM Reservation;";

    public long create(Reservation reservation) throws DaoException {
        List<Reservation> listeResa = findResaByVehicleIdSortedByEndDate(reservation.vehicle_id());
        boolean isAvailable = true;

        int days = 0;

        if (!listeResa.isEmpty()) {
            days = (int) ((int) listeResa.get(0).fin().toEpochDay() - listeResa.get(0).debut().toEpochDay());
            for (int i = 0; i < listeResa.size(); i++) {
                if (!(listeResa.get(i)
                        .debut().isAfter(reservation.fin()) || listeResa.get(i)
                        .fin().isBefore(reservation.debut()))) {
                    isAvailable = false;
                }
                //Voiture pas plus de 30 jours
                if (listeResa.get(i)
                        != listeResa.get(0)) {
                    if (listeResa.get(i)
                            .debut().isEqual(listeResa.get(i - 1)
                                    .fin().plusDays(1))) {
                        days += (int) ((int) listeResa.get(i)
                                .fin().toEpochDay() - listeResa.get(i)
                                .debut().toEpochDay());
                        if (days > 22) {
                            if (reservation.debut().isEqual(listeResa.get(i)
                                    .fin().plusDays(1))) {
                                days += (int) ((int) reservation.fin().toEpochDay() - reservation.debut().toEpochDay());
                                break;
                            }
                        }
                    } else {
                        days = 0;
                    }
                }
            }
        }
        if (reservation.debut().isAfter(reservation.fin()) || reservation.fin().isBefore(reservation.debut())) {
            throw new DaoException("ERROR: La date de début doit être avant la date de fin et la date de fin doit être après la date de début.");
        } else if (days > 30) {
            throw new DaoException("ERROR: Le véhicule ne peut pas être réservé plus de 30 jours consécutifs.");
        } else if (!isAvailable) {
            throw new DaoException("ERROR: Le véhicule est déjà réservé pour cette période.");
        } else {
            System.out.println("=====================================================================");
            try (Connection connection = ConnectionManager.getConnection();
                 PreparedStatement stmt =
                         connection.prepareStatement(CREATE_RESERVATION_QUERY,
                                 Statement.RETURN_GENERATED_KEYS);) {
                stmt.setInt(1, reservation.client_id());
                stmt.setInt(2, reservation.vehicle_id());
                stmt.setDate(3, Date.valueOf(reservation.debut()));
                stmt.setDate(4, Date.valueOf(reservation.fin()));

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
    }

    public long delete(Reservation reservation) throws DaoException {
        try (Connection connection = ConnectionManager.getConnection();
             PreparedStatement stmt =
                     connection.prepareStatement(DELETE_RESERVATION_QUERY,
                             Statement.RETURN_GENERATED_KEYS);) {
            stmt.setInt(1, reservation.id());

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


    public List<Reservation> findResaByClientId(long clientId) throws DaoException {
        try (Connection connection = ConnectionManager.getConnection();
             PreparedStatement stmt =
                     connection.prepareStatement(FIND_RESERVATIONS_BY_CLIENT_QUERY);) {
            stmt.setInt(1, (int) clientId);
            ResultSet resultSet = stmt.executeQuery();
            List<Reservation> listeResa = new ArrayList<>();

            while (resultSet.next()) {
                int id = resultSet.getInt(1);
                int vehicle_id = resultSet.getInt(2);
                LocalDate debut = resultSet.getDate(3).toLocalDate();
                LocalDate fin = resultSet.getDate(4).toLocalDate();

                listeResa.add(new Reservation(id, (int) clientId, vehicle_id, debut, fin));
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
            stmt.setInt(1, (int) vehicleId);
            ResultSet resultSet = stmt.executeQuery();
            List<Reservation> listeResa = new ArrayList<>();

            while (resultSet.next()) {
                int id = resultSet.getInt(1);
                int client_id = resultSet.getInt(2);
                LocalDate debut = resultSet.getDate(3).toLocalDate();
                LocalDate fin = resultSet.getDate(4).toLocalDate();

                listeResa.add(new Reservation(id, client_id, (int) vehicleId, debut, fin));
            }
            return listeResa;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Reservation> findResaByVehicleIdSortedByEndDate(long vehicleId) throws DaoException {
        try (Connection connection = ConnectionManager.getConnection();
             PreparedStatement stmt =
                     connection.prepareStatement(FIND_RESERVATIONS_BY_VEHICLE_QUERY_SORTED_END_DATE);) {
            stmt.setInt(1, (int) vehicleId);
            ResultSet resultSet = stmt.executeQuery();
            List<Reservation> listeResa = new ArrayList<>();

            while (resultSet.next()) {
                int id = resultSet.getInt(1);
                int client_id = resultSet.getInt(2);
                LocalDate debut = resultSet.getDate(3).toLocalDate();
                LocalDate fin = resultSet.getDate(4).toLocalDate();

                listeResa.add(new Reservation(id, client_id, (int) vehicleId, debut, fin));
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

            while (resultSet.next()) {
                int id = resultSet.getInt(1);
                int client_id = resultSet.getInt(2);
                int vehicle_id = resultSet.getInt(3);
                LocalDate debut = resultSet.getDate(4).toLocalDate();
                LocalDate fin = resultSet.getDate(5).toLocalDate();

                listeResa.add(new Reservation(id, client_id, vehicle_id, debut, fin));
            }
            return listeResa;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public int count() throws DaoException {
        try (Connection connection = ConnectionManager.getConnection();
             PreparedStatement stmt =
                     connection.prepareStatement(COUNT_RESERVATIONS_QUERY)) {
            ResultSet resultset = stmt.executeQuery();
            if (resultset.next()) {
                return resultset.getInt(1);
            } else {
                throw new DaoException();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }
}
