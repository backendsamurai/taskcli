package com.backendsamurai.taskcli.services;

import com.backendsamurai.taskcli.models.Task;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.ArrayList;

public class JsonStorageImpl implements Storage {
    private static final StringBuilder sb = new StringBuilder();
    public static final Path FILE_PATH = Path.of("tasks.json");

    public List<Task> load() {
        var tasks = new ArrayList<Task>();
        try {

            if (!Files.exists(FILE_PATH)) {
                Files.createFile(FILE_PATH);
            }

            String json = Files.readString(FILE_PATH);
            String[] normalized = json
                .replace("[", "")
                .replace("]", "")
                .replace("\n", "")
                .replace("{", "")
                .replaceAll("\"", "")
                .split("},");

            for (String value : normalized) {
                Task task = Task.fromJson(value);

                if (task != null) {
                    tasks.add(task);
                }
            }


        } catch (IOException e) {
            System.out.println(e.getMessage());
        }

        return tasks;
    }

    public void save(List<Task> tasks) {
        sb.append("[\n");
        tasks.forEach(t -> sb.append(t.toJson()));
        sb.append("\n]");

        try {
            Files.writeString(FILE_PATH, sb.toString());
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}
