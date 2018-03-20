package org.egzi.springconfigurator.exceptions;

public class InvalidContextStatusException extends RuntimeException {

    public InvalidContextStatusException(String message) {
        super(message);
    }

    public InvalidContextStatusException(String message, Throwable cause) {
        super(message, cause);
    }
}
