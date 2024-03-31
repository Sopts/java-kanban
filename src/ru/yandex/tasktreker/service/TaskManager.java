package ru.yandex.tasktreker.service;

import ru.yandex.tasktreker.model.Epic;
import ru.yandex.tasktreker.model.Status;
import ru.yandex.tasktreker.model.Subtask;
import ru.yandex.tasktreker.model.Task;

import java.util.HashMap;
import java.util.List;

public interface TaskManager {
    int changeCount();

    HashMap<Integer, Task> getTasks();

    HashMap<Integer, Subtask> getSubtasks();

    HashMap<Integer, Epic> getEpics();

    void deleteAllTask();

    void deleteAllSubtask();

    void deleteAllEpic();

    Task getTaskById(int id);

    Subtask getSubtaskById(int id);

    Epic getEpicById(int id);

    void createTask(Task task);

    void createSubtask(Subtask subtask);

    void createEpic(Epic epic);

    void updateTask(Task task, Status status);

    void updateSubtask(Subtask subtask, Status status);

    void updateEpic(Epic epic);

    void deleteTaskById(int id);

    void deleteSubtaskById(int id);

    void deleteEpicById(int id);

    List<Subtask> getAllSubtasksForEpicId(int epicId);

   }