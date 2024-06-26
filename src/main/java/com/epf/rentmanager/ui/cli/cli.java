package com.epf.rentmanager.ui.cli;
import com.epf.rentmanager.configuration.AppConfiguration;
import com.epf.rentmanager.exceptions.DaoException;
import com.epf.rentmanager.exceptions.ServiceException;
import com.epf.rentmanager.model.Reservation;
import com.epf.rentmanager.utils.IOUtils;
import com.epf.rentmanager.service.VehicleService;
import com.epf.rentmanager.service.ClientService;
import com.epf.rentmanager.model.Client;
import com.epf.rentmanager.model.Vehicle;
import com.epf.rentmanager.service.ReservationService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

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
    ApplicationContext context = new
            AnnotationConfigApplicationContext(AppConfiguration.class);
    ClientService clientService = context.getBean(ClientService.class);
    VehicleService vehicleService = context.getBean(VehicleService.class);
    ReservationService reservationService = context.getBean(ReservationService.class);
    public static void main(String[] args) {
        cli.getInstance().Init();
    }
    public void Init() {
        boolean finish = true;
        UtilLoop:
        try{
        while (finish) {
            Thread.sleep(2000); // 20 secondes = 20 000 millisecondes
            IOUtils.print("---------------Bienvenue dans le CLI-------------");
            IOUtils.print("Taper 1 pour Créer un Client");
            IOUtils.print("Taper 2 pour Lister les Clients existants");
            IOUtils.print("Taper 3 pour Créer un Véhicule");
            IOUtils.print("Taper 4 pour Lister les Vehicles existants");
            IOUtils.print("Taper 5 pour Créer une Réservation");
            IOUtils.print("Taper 6 pour Lister les Réservations existantes");
            IOUtils.print("Taper 7 pour Lister les Réservations existantes liées à l'id d'un Client");
            IOUtils.print("Taper 8 pour Lister les Réservations existantes liées à l'id d'un Client");
            IOUtils.print("Taper 9 pour Compter le nombre de Clients");
            IOUtils.print("Taper 10 pour Compter le nombre de Véhicules");
            IOUtils.print("Taper 11 pour Lister les Réservations existantes liées à l'id d'un Véhicule et triées par date de fin");
            IOUtils.print("Taper 12 pour Supprimer un Client");
            IOUtils.print("Taper 13 pour Supprimer un Véhicule");
            IOUtils.print("Taper 20 pour Quitter");
            int choix = IOUtils.readInt("");
            try {
                switch (choix) {
                    case 1 -> createClient();
                    case 2 -> listAllClients();
                    case 3 -> createVehicle();
                    case 4 -> listAllVehicles();
                    case 5 -> createReservation();
                    case 6 -> listAllReservations();
                    case 7 -> listReservationByClientId();
                    case 8 -> listReservationByVehicleId();
                    case 9 -> countClients();
                    case 10 -> countVehicles();
                    case 11 -> listReservationByVehicleIdSortedByEndDate();
                    case 12 -> deleteClient();
                    case 13 -> deleteVehicle();
                    case 20 -> finish = false;
                }
            }catch (DaoException | ServiceException exception) {
                throw new RuntimeException(exception);
            }
        }
    }catch (InterruptedException e){
            System.out.println("Il y a eu un problème avec le Thread");
            Thread.currentThread().interrupt();
        }
    }
    private void createClient() throws ServiceException,DaoException{
        IOUtils.print("Nous allons entrer les informations pour créer le Client");
        String nom = IOUtils.readString("Entrez le nom du client", true);
        String prenom = IOUtils.readString("Entrez le Prenom du client", true);
        String email = IOUtils.readString("Entrez l'Email du client", false);
        LocalDate naissance = IOUtils.readDate("Entrez la date de naissance du client", false);
        clientService.create(new Client(1, nom, prenom, email, naissance));
    }
    private void createVehicle() throws ServiceException,DaoException{
        IOUtils.print("Nous allons entrer les informations pour créer le Vehicle");
        String constructeur = IOUtils.readString("Entrez le constructeur", true);
        String modele = IOUtils.readString("Entrez le modele", true);
        int nb_places = IOUtils.readInt("Entrez le nb de places du véhicule");
        vehicleService.create(new Vehicle(1, constructeur, modele, nb_places));
    }
    private void createReservation() throws ServiceException,DaoException{
        IOUtils.print("Nous allons entrer les informations pour créer la Réservation");
        int IdVehicle = IOUtils.readInt("Entrez l'id du Véhicule");
        int IdClient = IOUtils.readInt("Entrez l'id du Client");
        LocalDate debut = IOUtils.readDate("Entrez la date de debut de la réservation", false);
        LocalDate fin = IOUtils.readDate("Entrez la date de fin de la réservation", false);
        reservationService.create(new Reservation(1, IdVehicle, IdClient, fin, debut));
    }
    private void listAllClients() throws ServiceException,DaoException{
        IOUtils.print("Voici la liste des Clients");
        clientService.findAll().forEach(element -> IOUtils.print(element.toString()));
    }
    private void listAllReservations() throws ServiceException,DaoException{
        IOUtils.print("Voici la liste des Réservations");
        reservationService.findAll().forEach(element -> IOUtils.print(element.toString()));
    }
    private void listAllVehicles() throws ServiceException,DaoException{
        IOUtils.print("Voici la liste des Vehicules");
        vehicleService.findAll().forEach(element -> IOUtils.print(element.toString()));
    }
    private void listReservationByVehicleId() throws ServiceException,DaoException{
        int IdVehicle = IOUtils.readInt("Entrez l'id du Véhicule");
        IOUtils.print("Voici la liste des Réservations associées à la voiture d'id : " + IdVehicle);
        reservationService.findByVehicleId(IdVehicle).forEach(element -> IOUtils.print(element.toString()));
    }
    private void listReservationByVehicleIdSortedByEndDate() throws ServiceException,DaoException{
        int IdVehicle = IOUtils.readInt("Entrez l'id du Véhicule");
        IOUtils.print("Voici la liste des Réservations associées à la voiture d'id : " + IdVehicle);
        reservationService.findByVehicleIdSortedByEndDate(IdVehicle).forEach(element -> IOUtils.print(element.toString()));
    }
    private void listReservationByClientId() throws ServiceException,DaoException{
        int IdClient = IOUtils.readInt("Entrez l'id du Véhicule");
        IOUtils.print("Voici la liste des Réservations associées à la voiture d'id : " + IdClient);
        reservationService.findByClientId(IdClient).forEach(element -> IOUtils.print(element.toString()));
    }

    private void countClients() throws DaoException,ServiceException{
        IOUtils.print(String.valueOf(clientService.countClients()));
    }
    private void countVehicles() throws DaoException {
        IOUtils.print(String.valueOf(vehicleService.countVehicles()));
    }
    private void deleteClient() throws ServiceException{
        int IdClient = IOUtils.readInt("Entrez l'id du Client");
        clientService.deleteClient(IdClient);
    }
    private void deleteVehicle() throws ServiceException{
        int IdVehicle = IOUtils.readInt("Entrez l'id du Véhicule");
        vehicleService.deleteVehicle(IdVehicle);
    }
}
