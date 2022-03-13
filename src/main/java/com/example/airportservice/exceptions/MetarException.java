package com.example.airportservice.exceptions;


public class MetarException extends Exception {

    /**
     * default serial version
     */
    private static final long serialVersionUID = 1L;

    /**
     * @param message message
     * @param cause   cause
     */
    public MetarException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * @param message message
     */
    public MetarException(String message) {
        super(message);
    }
}
