package com.epf.rentmanager.ui.cli;
import com.epf.rentmanager.exceptions.DaoException;
import com.epf.rentmanager.exceptions.ServiceException;
import com.epf.rentmanager.utils.IOUtils;
import com.epf.rentmanager.service.VehicleService;
import com.epf.rentmanager.service.ClientService;
import com.epf.rentmanager.model.Client;
import com.epf.rentmanager.model.Vehicle;

import java.time.LocalDate;

public class cli {
    private static cli instance = null;
    private cli() {}
    public static cli getInstance() {
        if(instance == null) {
            instance = new cli();
        }
        return instance;
    }
    public void Init() {
        boolean finish = true;
        UtilLoop:
        while (finish) {
            IOUtils.print("---------------Bienvenue dans le CLI-------------");
            IOUtils.print("Taper 1 pour Créer un Client");
            IOUtils.print("Taper 2 pour Lister les Clients existants");
            IOUtils.print("Taper 3 pour Créer un Véhicule");
            IOUtils.print("Taper 4 pour Lister les Vehicles existants");
            int choix = IOUtils.readInt("");
            try {
                switch (choix) {
                    case 1 -> createClient();
                    case 2 -> listAllClients();
                    case 3 -> createVehicle();
                    case 4 -> listAllVehicles();
                }
            }catch (DaoException exception){
                System.out.println("Il y a eu un problème avec la Base de Données");
            }
            catch (ServiceException exception){
                System.out.println("Il y a eu un problème avec l'entrée des données");
            }
        }
    }
    private void createClient() throws ServiceException,DaoException{
        IOUtils.print("Nous allons entrer les informations pour créer le Client");
        String nom = IOUtils.readString("Entrez le nom du client", true);
        String prenom = IOUtils.readString("Entrez le Prenom du client", true);
        String email = IOUtils.readString("Entrez l'Email du client", false);
        LocalDate naissance = IOUtils.readDate("Entrez la date de naissance du client", false);
        ClientService.getInstance().create(new Client(1, nom, prenom, email, naissance));
    }
    private void createVehicle() throws ServiceException,DaoException{
        IOUtils.print("Nous allons entrer les informations pour créer le Vehicle");
        String constructeur = IOUtils.readString("Entrez le constructeur", true);
        String modele = IOUtils.readString("Entrez le modele", true);
        int nb_places = IOUtils.readInt("Entrez l'Email du vehicle");
        VehicleService.getInstance().create(new Vehicle(1, constructeur, modele, nb_places));
    }
    private void listAllClients() throws ServiceException,DaoException{
        IOUtils.print("Voici la liste des Clients");
        ClientService.getInstance().findAll().forEach(element -> IOUtils.print(element.toString()));
    }
    private void listAllVehicles() throws ServiceException,DaoException{
        IOUtils.print("Voici la liste des Vehicules");
        VehicleService.getInstance().findAll().forEach(element -> IOUtils.print(element.toString()));
    }
}
