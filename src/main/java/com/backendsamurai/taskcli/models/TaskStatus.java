package com.backendsamurai.taskcli.models;

public enum TaskStatus {
    InProgress("in-progress"), Todo("todo"), Done("done");

    private final String value;

    TaskStatus(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return value;
    }
}
