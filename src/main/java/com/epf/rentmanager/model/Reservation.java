package com.epf.rentmanager.model;

import java.time.LocalDate;
    public record Reservation(int id, int client_id, int vehicule_id, LocalDate debut, LocalDate fin) {}