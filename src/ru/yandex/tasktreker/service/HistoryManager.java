package ru.yandex.tasktreker.service;

import ru.yandex.tasktreker.model.Task;

import java.util.List;

public interface HistoryManager {

    List<Task> getHistory();

    public void add(Task task);

    public Task copyTask(Task task);

}
