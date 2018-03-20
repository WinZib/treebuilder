package org.egzi.springconfigurator.info;

public enum ContextStatus {
    NOT_STARTED("Not Started"),
    STARTING("Starting"),
    STARTED("Started"),
    FAILED("Failed"),
    STOPPED("Stopped");

    String value;

    ContextStatus(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }
}
