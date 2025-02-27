package com.backendsamurai.taskcli;

import com.backendsamurai.taskcli.models.Task;
import com.backendsamurai.taskcli.models.TaskStatus;
import com.backendsamurai.taskcli.models.ListFilter;
import com.backendsamurai.taskcli.services.Storage;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

public class TaskManager {
    private final List<Task> tasks;
    private final Storage storage;

    public TaskManager(Storage storage) {
        this.storage = storage;
        tasks = storage.load();
    }

    public void run(String[] args) {
        if (args.length > 0) {
            String command = args[0].strip();

            switch (command) {
                case "add":
                    if (args.length != 2) return;
                    addTask(args[1]);
                    break;
                case "update":
                    if (args.length != 3) return;
                    updateTask(Long.parseLong(args[1]), args[2]);
                    break;
                case "delete":
                    if (args.length != 2) return;
                    removeTask(Long.parseLong(args[1]));
                    break;
                case "mark-in-progress":
                    if (args.length != 2) return;
                    updateTask(Long.parseLong(args[1]), TaskStatus.InProgress);
                    break;
                case "mark-done":
                    if (args.length != 2) return;
                    updateTask(Long.parseLong(args[1]), TaskStatus.Done);
                    break;
                case "list":
                    ListFilter filter = ListFilter.All;

                    if (args.length == 2)
                        filter = parseStringIntoListFilter(args[1]);

                    listTasks(filter);
                    break;
                default:
                    showUsage();
            }

        } else {
            showUsage();
        }

        storage.save(tasks);
    }

    public void showUsage() {
        System.out.println("""
                Usage: task-cli <command> [parameters]

                Available commands:
                 add - create new task
                 update - update description in task (require id of task)
                 delete - remove a task (require id of task)
                 mark-in-progress - update status of task to "In Progress"
                 mark-in-done - update status of task to "Done"
                 list - show all tasks
                 list done - show all tasks with done status
                 list todo - show all tasks with todo status
                 list in-progress - show all tasks with in-progress status""");
    }

    public void addTask(String description) {
        Task newTask = new Task(description.strip());
        tasks.add(newTask);
        System.out.println("Task added successfully (ID: " + newTask.getId() + ")");
    }

    public void updateTask(long taskId, String description) {
        findTaskById(taskId)
                .ifPresentOrElse(
                        t -> t.update(description.strip()),
                        () -> System.out.println("Cannot find task with ID: " + taskId)
                );
    }

    public void updateTask(long taskId, TaskStatus status) {
        findTaskById(taskId)
                .ifPresentOrElse(
                        t -> t.update(status),
                        () -> System.out.println("Cannot find task with ID: " + taskId)
                );
    }

    public void removeTask(long taskId) {
        findTaskById(taskId)
                .ifPresentOrElse(
                        tasks::remove,
                        () -> System.out.println("Cannot find task with ID: " + taskId)
                );
    }

    public void listTasks(ListFilter filter) {
        if (filter != ListFilter.All) {
            TaskStatus status = TaskStatus.valueOf(filter.name());

            if (getTasksByStatus(status).findAny().isEmpty()) {
                System.out.println("NO STORED TASKS!");
                return;
            }

            String delimiterForStatus = ".".repeat(Math.round(((float)Task.DelimiterLength - status.name().length()) / 2));
            System.out.println(delimiterForStatus + status.name().toUpperCase() + delimiterForStatus);
            getTasksByStatus(status).forEach(System.out::println);
            return;
        }

        if (!tasks.isEmpty())
            tasks.forEach(System.out::println);
        else
            System.out.println("NO STORED TASKS");
    }

    public Optional<Task> findTaskById(long taskId) {
        return tasks.stream().filter(t -> t.getId() == taskId).findFirst();
    }

    public Stream<Task> getTasksByStatus(TaskStatus status) {
        return tasks.stream().filter(t -> t.getStatus() == status);
    }

    private ListFilter parseStringIntoListFilter(String arg) {
        if (!arg.isEmpty()) {
            return switch (arg) {
                case "done" -> ListFilter.Done;
                case "in-progress" -> ListFilter.InProgress;
                case "todo" -> ListFilter.Todo;
                default -> ListFilter.All;
            };
        }

        return ListFilter.All;
    }
}
