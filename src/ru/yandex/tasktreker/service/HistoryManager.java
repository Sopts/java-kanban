package ru.yandex.tasktreker.service;

import ru.yandex.tasktreker.model.Task;

import java.util.List;

public interface HistoryManager {

    List<Task> getHistory();

    void add(Task task);

    void remove(int id);
}