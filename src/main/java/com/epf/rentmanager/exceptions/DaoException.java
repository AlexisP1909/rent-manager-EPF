package com.epf.rentmanager.exceptions;

public class DaoException extends Exception{
    public DaoException() {
    }
    public DaoException(String message) {
        super(message); // Pass the message to the superclass constructor
    }
}
