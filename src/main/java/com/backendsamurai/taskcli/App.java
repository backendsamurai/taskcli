package com.backendsamurai.taskcli;

import com.backendsamurai.taskcli.services.JsonStorageImpl;

public class App {
    public static void main(String[] args) {
        var storage = new JsonStorageImpl();

        var taskManager = new TaskManager(storage);
        taskManager.run(args);
    }
}
