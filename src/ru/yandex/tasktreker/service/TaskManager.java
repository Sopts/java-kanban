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

    private int count = 0;

    public int changeCount() {
        count++;
        return count;
    }

    public HashMap<Integer, Task> getTaskMap() {
        return taskMap;
    }

    public void deleteAllTask() {
        taskMap.clear();
    }

    public Task getTaskById(int id) {
        return taskMap.get(id);
    }

    public void createTask(Task task) {
        int id = changeCount();
        task.setId(id);
        taskMap.put(task.getId(), task);
        if (task.getClass().equals(Subtask.class)) {
            Subtask subtask = (Subtask) task;
            Epic epic = subtask.getEpic();
            epic.addSubtask(subtask);
        }
    }

    public void updateTask(Task newTask, Status status) {
        newTask.setStatus(status);
        if (newTask.getClass().equals(Subtask.class)) {
            Subtask subtask = (Subtask) newTask;
            if (subtask.getStatus().equals(Status.IN_PROGRESS)) {
                Epic epic = (Epic) taskMap.get(subtask.getEpicId());
                if (!epic.getStatus().equals(Status.IN_PROGRESS)) {
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
                    Epic epic = (Epic) taskMap.get(subtask.getEpicId());
                    if (!epic.getStatus().equals(Status.DONE)) {
                        epic.setStatus(Status.DONE);
                    }
                }
            }
        }
        taskMap.put(newTask.getId(), newTask);
    }

    public void deleteTaskById(Integer id) {
        if (getTaskById(id).getClass().equals(Epic.class)) {
            List<Subtask> subtasksForEpic = getAllSubTasksForEpicId(id);
            for (Subtask subtask : subtasksForEpic) {
                taskMap.remove(subtask.getId());
            }
        }
        taskMap.remove(id);
    }

    public List<Subtask> getAllSubTasksForEpicId(int epicId) {
        List<Subtask> subtasksForEpic = new ArrayList<>();
        for (Task task : taskMap.values()) {
            if (task.getClass().equals(Subtask.class)) {
                Subtask task1 = (Subtask) task;
                if (task1.getEpicId() == epicId) {
                    subtasksForEpic.add(task1);
                }
            }
        }
        return subtasksForEpic;
    }

}