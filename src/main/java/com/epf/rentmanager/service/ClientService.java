package com.epf.rentmanager.service;

import java.util.ArrayList;
import java.util.List;

import com.epf.rentmanager.dao.ClientDao;
import com.epf.rentmanager.exceptions.DaoException;
import com.epf.rentmanager.model.Client;
import com.epf.rentmanager.exceptions.ServiceException;
public class ClientService {

	private ClientDao clientDao;
	public static ClientService instance;
	
	private ClientService() {
		this.clientDao = ClientDao.getInstance();
	}
	
	public static ClientService getInstance() {
		if (instance == null) {
			instance = new ClientService();
		}
		
		return instance;
	}
	
	
	public long create(Client client) throws ServiceException, DaoException {
		if(client.nom().isEmpty() || client.prenom().isEmpty()){
			throw new ServiceException();
		}else{

			return clientDao.create(new Client(
					client.id(),
					client.nom().toUpperCase(),
					client.prenom(),
					client.email(),
					client.naissance())
					);
		}
	}

	public Client findById(long id) throws ServiceException,DaoException {
		Client client = ClientDao.getInstance().findById(id);
		if(client.nom().isEmpty() || client.prenom().isEmpty()){
			throw new ServiceException();
		}
		else {return client;}
	}

	public List<Client> findAll() throws ServiceException,DaoException {
		return ClientDao.getInstance().findAll();
	}
	public int countClients() throws DaoException {
		return ClientDao.getInstance().count();
	}
}
