package com.backendsamurai.taskcli.services;

import com.backendsamurai.taskcli.models.Task;

import java.io.IOException;
import java.util.List;

public interface Storage {
    List<Task> load();

    void save(List<Task> tasks);
}
