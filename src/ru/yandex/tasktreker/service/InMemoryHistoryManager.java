package ru.yandex.tasktreker.service;

import ru.yandex.tasktreker.model.Task;

import java.util.ArrayList;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {

    List<Task> historyTasks = new ArrayList<>(10);


    @Override
    public List<Task> getHistory() {
        return historyTasks;
    }

    @Override
    public void add(Task task) {
        Task copyTask = copyTask(task);
        historyTasks.add(copyTask);
        if (historyTasks.size() > 10) {
            historyTasks.remove(0);
        }
    }

    @Override
    public Task copyTask(Task task) {
        Task copy = new Task(task.getName(), task.getDescription());
        copy.setId(task.getId());
        copy.setStatus(task.getStatus());
        return copy;
    }

}
