package com.epf.rentmanager.service;

import java.sql.*;
import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;

import com.epf.rentmanager.dao.ClientDao;
import com.epf.rentmanager.exceptions.DaoException;
import com.epf.rentmanager.model.Client;
import com.epf.rentmanager.exceptions.ServiceException;
import com.epf.rentmanager.persistence.ConnectionManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ClientService {

    private ClientDao clientDao;

    @Autowired
    private ClientService(ClientDao clientDao) {
        this.clientDao = clientDao;
    }

    public long create(Client client) throws ServiceException, DaoException {
        boolean clientIsOver18 = Period.between(client.naissance(), LocalDate.now()).getYears() < 18;
        if (client.nom().isEmpty() || client.prenom().isEmpty()) {
            throw new ServiceException("Error: les noms et prenoms sont vides");
        } else if (clientIsOver18) {
            throw new ServiceException("Error: Le client n'est pas majeur");
        } else if (clientDao.IsEmailTaken(client.email())) {
            throw new ServiceException("Email déjà pris");
        }else if(client.nom().length()<3 || client.prenom().length()<3){
            throw new ServiceException("Le nom et le prenom doivent faire 3 caractères minimum");
        }
        else {

            return clientDao.create(new Client(
                    client.id(),
                    client.nom().toUpperCase(),
                    client.prenom(),
                    client.email(),
                    client.naissance())
            );
        }
    }

    public Client findById(long id) throws ServiceException, DaoException {
        Client client = clientDao.findById(id);
        if (client.nom().isEmpty() || client.prenom().isEmpty()) {
            throw new ServiceException();
        } else {
            return client;
        }
    }

    public List<Client> findAll() throws ServiceException, DaoException {
        return clientDao.findAll();
    }

    public int countClients() throws DaoException {
        return clientDao.count();
    }
}
