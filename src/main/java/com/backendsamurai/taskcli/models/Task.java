package com.backendsamurai.taskcli.models;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public final class Task {
    public static final int DelimiterLength = 40;
    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
    private static long lastId = 0;
    private long id;
    private String description;
    private TaskStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public Task(String description) {
        this.id = ++lastId;
        this.description = description;
        this.status = TaskStatus.Todo;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    public Task() {
    }

    public long getId() {
        return this.id;
    }

    public TaskStatus getStatus() {
        return this.status;
    }

    public static Task fromJson(String json) {
        if (json.isEmpty())
            return null;

        Task task = new Task();

        String[] properties = json.split(",");

        for (String property : properties) {
            int colonIdx = property.indexOf(':');

            if (colonIdx == -1) return null;

            String propName = property.substring(0, colonIdx).strip();
            String propValue = property.substring(colonIdx + 1).strip();

            if (propName.isEmpty() || propValue.isEmpty()) return null;

            switch (propName) {
                case "id":
                    long id = Long.parseLong(propValue);
                    if (id > lastId) {
                        lastId = id;
                    }
                    task.id = id;
                    break;
                case "description":
                    task.description = propValue;
                    break;
                case "status":
                    task.status = TaskStatus.valueOf(propValue);
                    break;
                case "createdAt":
                    task.createdAt = LocalDateTime.parse(propValue, dateTimeFormatter);
                    break;
                case "updatedAt":
                    task.updatedAt = LocalDateTime.parse(propValue, dateTimeFormatter);
                    break;
            }

        }

        return task;
    }

    public void update(String description) {
        this.description = description;
        this.updatedAt = LocalDateTime.now();
    }

    public void update(TaskStatus status) {
        this.status = status;
        this.updatedAt = LocalDateTime.now();
    }

    @Override
    public String toString() {
        return id + ". " + description + "\nStatus: " + status.toString() + "\nCreated: "
                + createdAt.format(dateTimeFormatter) + "\nUpdated: "
                + updatedAt.format(dateTimeFormatter) + "\n" + "=".repeat(DelimiterLength);
    }

    public String toJson() {
        return "{\"id\":\"" + id + "\", \"description\":\"" + description.strip() + "\", \"status\":\"" + status.name() +
                "\", \"createdAt\":\"" + createdAt.format(dateTimeFormatter) + "\", \"updatedAt\":\"" + updatedAt.format(dateTimeFormatter) + "\"},\n";
    }
}
