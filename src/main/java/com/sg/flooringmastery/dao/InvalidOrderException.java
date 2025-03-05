package com.sg.flooringmastery.dao;

/**
 * Class to be used in cases where there is something wrong with the order.
 */
public class InvalidOrderException extends RuntimeException {
    public InvalidOrderException(String message) {
        super(message);
    }

    public InvalidOrderException(String message, Throwable cause) {
        super(message, cause);
    }
}
