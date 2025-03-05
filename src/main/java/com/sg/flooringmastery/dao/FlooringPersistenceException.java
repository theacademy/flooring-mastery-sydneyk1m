package com.sg.flooringmastery.dao;

/**
 * Class to be used as an exception in instances where I/O exceptions might usually be expected.
 */
public class FlooringPersistenceException extends RuntimeException {
    public FlooringPersistenceException(String message) {
        super(message);
    }

    public FlooringPersistenceException(String message, Throwable cause) {
        super(message, cause);
    }
}
