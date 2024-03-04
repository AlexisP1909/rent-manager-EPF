package com.epf.rentmanager.service;

import java.util.List;

import com.epf.rentmanager.exceptions.DaoException;
import com.epf.rentmanager.model.Vehicle;
import com.epf.rentmanager.model.Vehicle;
import com.epf.rentmanager.dao.ClientDao;
import com.epf.rentmanager.dao.VehicleDao;
import com.epf.rentmanager.exceptions.ServiceException;

public class VehicleService {

	private VehicleDao vehicleDao;
	public static VehicleService instance;
	
	private VehicleService() {
		this.vehicleDao = VehicleDao.getInstance();
	}
	
	public static VehicleService getInstance() {
		if (instance == null) {
			instance = new VehicleService();
		}
		
		return instance;
	}
	
	
	public long create(Vehicle vehicle) throws ServiceException, DaoException {
		if(vehicle.constructeur().isEmpty() || vehicle.nb_places()<1){
			throw new ServiceException();
		}else{

			return vehicleDao.create(vehicle);
		}
	}

	public Vehicle findById(long id) throws ServiceException,DaoException {
		Vehicle vehicle = VehicleDao.getInstance().findById(id);
		if(vehicle.constructeur().isEmpty() || vehicle.nb_places()<1){
			throw new ServiceException();
		}
		else {return vehicle;}
	}

	public List<Vehicle> findAll() throws ServiceException,DaoException {
		return VehicleDao.getInstance().findAll();
	}
	
}
