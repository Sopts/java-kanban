package ru.yandex.tasktreker.service;

import ru.yandex.tasktreker.model.Epic;
import ru.yandex.tasktreker.model.Status;
import ru.yandex.tasktreker.model.Subtask;
import ru.yandex.tasktreker.model.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class TaskManager {

    private final HashMap<Integer, Task> taskMap = new HashMap<>();
    private final HashMap<Integer, Subtask> subtaskMap = new HashMap<>();
    private final HashMap<Integer, Epic> epicMap = new HashMap<>();

    private int count = 0;

    public int changeCount() {
        count++;
        return count;
    }

    public HashMap<Integer, Task> getTaskMap() {
        return taskMap;
    }

    public HashMap<Integer, Subtask> getSubtaskMap() {
        return subtaskMap;
    }

    public HashMap<Integer, Epic> getEpicMap() {
        return epicMap;
    }

    public void deleteAllTask() {
        taskMap.clear();
    }

    public void deleteAllSubtask() {
        subtaskMap.clear();
    }

    public void deleteAllEpic() {
        epicMap.clear();
        deleteAllSubtask();
    }

    public Task getTaskById(int id) {
        return taskMap.get(id);
    }

    public Subtask getSubtaskById(int id) {
        return subtaskMap.get(id);
    }

    public Epic getEpicById(int id) {
        return epicMap.get(id);
    }

    public void createTask(Task task) {
        int id = changeCount();
        task.setId(id);
        taskMap.put(id, task);
    }

    public void createSubtask(Subtask subtask) {
        Epic epic = getEpicById(subtask.getEpicId());
        if (epic != null) {
            int id = changeCount();
            subtask.setId(id);
            subtaskMap.put(id, subtask);
            epic.addSubtask(subtask);
        } else {
            System.out.println("Subtask не добавлена. Epic с epicId: "
                    + subtask.getEpicId() + " не найден. Попробуйте еще раз");
        }
    }

    public void createEpic(Epic epic) {
        int id = changeCount();
        epic.setId(id);
        epicMap.put(id, epic);
    }

    public void updateTask(Task task, Status status) {
        task.setStatus(status);
        taskMap.put(task.getId(), task);
    }

    public void updateSubtask(Subtask subtask, Status status) {
        subtask.setStatus(status);
        if (subtask.getStatus().equals(Status.IN_PROGRESS)) {
            Epic epic = epicMap.get(subtask.getEpicId());
            if (epic != null && !epic.getStatus().equals(Status.IN_PROGRESS)) {
                epic.setStatus(Status.IN_PROGRESS);
            }
        } else if (subtask.getStatus().equals(Status.DONE)) {
            boolean isDone = true;
            List<Subtask> allSubTask = getAllSubTasksForEpicId(subtask.getEpicId());
            for (Subtask subtask1 : allSubTask) {
                if (!subtask1.getStatus().equals(Status.DONE)) {
                    isDone = false;
                    break;
                }
            }
            if (isDone) {
                Epic epic = epicMap.get(subtask.getEpicId());
                if (epic != null && !epic.getStatus().equals(Status.DONE)) {
                    epic.setStatus(Status.DONE);
                }
            }
            subtaskMap.put(subtask.getId(), subtask);
        }
    }

    public void updateEpic(Epic epic, Status status) {
        epic.setStatus(status);
        if (epic.getStatus().equals(Status.DONE)) {
            List<Subtask> allSubTask = getAllSubTasksForEpicId(epic.getId());
            for (Subtask subtask1 : allSubTask) {
                if (!subtask1.getStatus().equals(Status.DONE)) {
                    subtask1.setStatus(Status.DONE);
                }
            }
        }
        epicMap.put(epic.getId(), epic);
    }

    public void deleteTaskById(int id) {
        taskMap.remove(id);
    }

    public void deleteSubtaskById(int id) {
        subtaskMap.remove(id);

    }

    public void deleteEpicById(int id) {
        List<Subtask> deleteSubtask = getAllSubTasksForEpicId(id);
        for (Subtask subtask : deleteSubtask) {
            deleteSubtaskById(subtask.getId());
        }
        epicMap.remove(id);
    }

    public List<Subtask> getAllSubTasksForEpicId(int epicId) {
        Epic epic = getEpicById(epicId);
        if (epic != null) {
            return epic.getSubtasks();
        } else {
            return new ArrayList<>();
        }
    }

}