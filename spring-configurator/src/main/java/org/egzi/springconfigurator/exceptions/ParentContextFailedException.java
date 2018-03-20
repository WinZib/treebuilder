package org.egzi.springconfigurator.exceptions;

public class ParentContextFailedException extends RuntimeException {

    public ParentContextFailedException(String message, Throwable cause) {
        super(message, cause);
    }
}
