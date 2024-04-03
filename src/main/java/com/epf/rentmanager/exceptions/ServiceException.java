package com.epf.rentmanager.exceptions;

public class ServiceException extends Exception {
    public ServiceException() {
    }
    public ServiceException(String message) {
        super(message); // Pass the message to the superclass constructor
    }
}
