package com.epf.rentmanager.service;

import java.util.List;

import com.epf.rentmanager.dao.ReservationDao;
import com.epf.rentmanager.exceptions.DaoException;
import com.epf.rentmanager.model.Reservation;
import com.epf.rentmanager.exceptions.ServiceException;
import org.springframework.stereotype.Service;

@Service
public class ReservationService {

    private ReservationDao reservationDao;

    private ReservationService(ReservationDao reservationDao) {
        this.reservationDao = reservationDao;
    }

    public long create(Reservation reservation) throws ServiceException, DaoException {
        boolean BookedMoreThan7Days = reservation.fin().toEpochDay()-reservation.debut().toEpochDay() > 7;
        if (BookedMoreThan7Days) {
            throw new ServiceException("ERROR: La durée de la réservation ne doit excéder 7jours");
        } else {
            try {
                return reservationDao.create(new Reservation(
                        reservation.id(),
                        reservation.client_id(),
                        reservation.vehicle_id(),
                        reservation.debut(),
                        reservation.fin())
                );
            } catch (DaoException e) {
                throw new ServiceException(e.getMessage());
            }
        }
    }

    public List<Reservation> findByClientId(long id) throws ServiceException, DaoException {
        return reservationDao.findResaByClientId(id);
    }

    public List<Reservation> findByVehicleId(long id) throws ServiceException, DaoException {
        return reservationDao.findResaByVehicleId(id);
    }
    public List<Reservation> findByVehicleIdSortedByEndDate(long id) throws ServiceException, DaoException {
        return reservationDao.findResaByVehicleIdSortedByEndDate(id);
    }
    public List<Reservation> findAll() throws ServiceException, DaoException {
        return reservationDao.findAll();
    }

    public int countR() throws ServiceException {
        try {
            return reservationDao.count();
        } catch (DaoException e) {
            throw new ServiceException(e.getMessage());
        }
    }

}
