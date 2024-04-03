package com.epf.rentmanager.service;

import java.util.List;

import com.epf.rentmanager.exceptions.DaoException;
import com.epf.rentmanager.model.Vehicle;
import com.epf.rentmanager.model.Vehicle;
import com.epf.rentmanager.dao.ClientDao;
import com.epf.rentmanager.dao.VehicleDao;
import com.epf.rentmanager.exceptions.ServiceException;
import org.springframework.stereotype.Service;

@Service

public class VehicleService {

	private VehicleDao vehicleDao;
	private VehicleService(VehicleDao vehicleDao) {
		this.vehicleDao = vehicleDao;
	}
	
	public long create(Vehicle vehicle) throws ServiceException, DaoException {
		if(vehicle.constructeur().isEmpty() ||vehicle.modele().isEmpty() || vehicle.nb_places()>9 || vehicle.nb_places()<2){
			throw new ServiceException("Error:le véhicule doit avoir un modele, un contructeur et un nombre de places compris entre 2 et 9");
		}else{

			return vehicleDao.create(vehicle);
		}
	}

	public Vehicle findById(long id) throws ServiceException,DaoException {
		Vehicle vehicle = vehicleDao.findById(id);
		if(vehicle.constructeur().isEmpty() || vehicle.nb_places()<1){
			throw new ServiceException();
		}
		else {return vehicle;}
	}

	public List<Vehicle> findAll() throws ServiceException,DaoException {
		return vehicleDao.findAll();
	}
	public int countVehicles() throws DaoException {
		return vehicleDao.count();
	}
}
