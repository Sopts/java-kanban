package ru.yandex.tasktreker.service;

import ru.yandex.tasktreker.model.Epic;
import ru.yandex.tasktreker.model.Status;
import ru.yandex.tasktreker.model.Subtask;
import ru.yandex.tasktreker.model.Task;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public interface TaskManager {

    Map<Integer, Task> getTasks();

    Map<Integer, Subtask> getSubtasks();

    Map<Integer, Epic> getEpics();

    void deleteAllTask() throws IOException;

    void deleteAllSubtask() throws IOException;

    void deleteAllEpic() throws IOException;

    Task getTaskById(int id);

    Subtask getSubtaskById(int id);

    Epic getEpicById(int id);

    void createTask(Task task) throws IOException;

    void createSubtask(Subtask subtask) throws IOException;

    void createEpic(Epic epic) throws IOException;

    void updateTask(Task task, Status status) throws IOException;

    void updateSubtask(Subtask subtask, Status status) throws IOException;

    void updateEpic(Epic epic) throws IOException;

    void deleteTaskById(int id) throws IOException;

    void deleteSubtaskById(int id) throws IOException;

    void deleteEpicById(int id) throws IOException;

    List<Subtask> getAllSubtasksForEpicId(int epicId);

    List<Task> getHistory();

}