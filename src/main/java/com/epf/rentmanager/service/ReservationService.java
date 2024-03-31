package com.epf.rentmanager.service;

import java.util.ArrayList;
import java.util.List;

import com.epf.rentmanager.dao.ReservationDao;
import com.epf.rentmanager.exceptions.DaoException;
import com.epf.rentmanager.model.Reservation;
import com.epf.rentmanager.exceptions.ServiceException;
public class ReservationService {

    private ReservationDao reservationDao;
    public static ReservationService instance;

    private ReservationService() {
        this.reservationDao = ReservationDao.getInstance();
    }

    public static ReservationService getInstance() {
        if (instance == null) {
            instance = new ReservationService();
        }

        return instance;
    }


    public long create(Reservation Reservation) throws ServiceException, DaoException {
            return ReservationDao.getInstance().create(new Reservation(
                    Reservation.id(),
                    Reservation.client_id(),
                    Reservation.vehicle_id(),
                    Reservation.debut(),
                    Reservation.fin())
            );
        
    }

    public List<Reservation> findByClientId(long id) throws ServiceException,DaoException {
        return  ReservationDao.getInstance().findResaByClientId(id);
    }

    public List<Reservation> findByVehicleId(long id) throws ServiceException,DaoException {
        return  ReservationDao.getInstance().findResaByVehicleId(id);
    }
    public List<Reservation> findAll() throws ServiceException,DaoException {
        return ReservationDao.getInstance().findAll();
    }

}
