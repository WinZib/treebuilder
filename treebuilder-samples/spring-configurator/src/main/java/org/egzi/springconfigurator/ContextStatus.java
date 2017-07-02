package org.egzi.springconfigurator;

public enum ContextStatus {
    NOT_STARTED("Not Started"),
    STARTING("Starting"),
    STARTED("Started"),
    FAILED("Failed");

    String value;

    ContextStatus(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }
}
