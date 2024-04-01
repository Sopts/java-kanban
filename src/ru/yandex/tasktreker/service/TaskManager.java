package ru.yandex.tasktreker.service;

import ru.yandex.tasktreker.model.Epic;
import ru.yandex.tasktreker.model.Status;
import ru.yandex.tasktreker.model.Subtask;
import ru.yandex.tasktreker.model.Task;

import java.util.List;
import java.util.Map;

public interface TaskManager {

    Map<Integer, Task> getTasks();

    Map<Integer, Subtask> getSubtasks();

    Map<Integer, Epic> getEpics();

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

    List<Task> getHistory();

   }