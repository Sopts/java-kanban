package ru.yandex.tasktreker.service;

import ru.yandex.tasktreker.model.Epic;
import ru.yandex.tasktreker.model.Status;
import ru.yandex.tasktreker.model.Subtask;
import ru.yandex.tasktreker.model.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class InMemoryTaskManager implements TaskManager {

    private final HashMap<Integer, Task> tasks = new HashMap<>();
    private final HashMap<Integer, Subtask> subtasks = new HashMap<>();
    private final HashMap<Integer, Epic> epics = new HashMap<>();

    private int count = 0;

    private final HistoryManager inMemoryHistoryManager;

    public InMemoryTaskManager(HistoryManager inMemoryHistoryManager) {
        this.inMemoryHistoryManager = inMemoryHistoryManager;
    }

    public HistoryManager getInMemoryHistoryManager() {
        return inMemoryHistoryManager;
    }

    @Override
    public int changeCount() {
        count++;
        return count;
    }

    @Override
    public HashMap<Integer, Task> getTasks() {
        return tasks;
    }

    @Override
    public HashMap<Integer, Subtask> getSubtasks() {
        return subtasks;
    }

    @Override
    public HashMap<Integer, Epic> getEpics() {
        return epics;
    }

    @Override
    public void deleteAllTask() {
        tasks.clear();
    }

    @Override
    public void deleteAllSubtask() {
        for (Subtask subtask : subtasks.values()) {
            for (Epic epic : epics.values()) {
                epic.deleteSubtasksList();
                updateEpic(epics.get(subtask.getEpicId()));
            }
        }
        subtasks.clear();
    }

    @Override
    public void deleteAllEpic() {
        deleteAllSubtask();
        epics.clear();
    }

    @Override
    public Task getTaskById(int id) {
        inMemoryHistoryManager.add(tasks.get(id));
        return tasks.get(id);
    }

    @Override
    public Subtask getSubtaskById(int id) {
        inMemoryHistoryManager.add(subtasks.get(id));
        return subtasks.get(id);
    }

    @Override
    public Epic getEpicById(int id) {
        inMemoryHistoryManager.add(epics.get(id));
        return epics.get(id);
    }

    @Override
    public void createTask(Task task) {
        int id = changeCount();
        task.setId(id);
        tasks.put(id, task);
    }

    @Override
    public void createSubtask(Subtask subtask) {
        Epic epic = getEpicById(subtask.getEpicId());
        if (epic != null) {
            int id = changeCount();
            subtask.setId(id);
            subtasks.put(id, subtask);
            epic.addSubtask(subtask);
            if (epic.getStatus().equals(Status.IN_PROGRESS)) {
                updateEpic(epic);
                epic.setStatus(Status.IN_PROGRESS);
            } else {
                updateEpic(epic);
            }
        } else {
            System.out.println("Subtask не добавлена. Epic с epicId: "
                    + subtask.getEpicId() + " не найден. Попробуйте еще раз");
        }
    }

    @Override
    public void createEpic(Epic epic) {
        int id = changeCount();
        epic.setId(id);
        epics.put(id, epic);
    }

    @Override
    public void updateTask(Task task, Status status) {
        task.setStatus(status);
        tasks.put(task.getId(), task);
    }

    @Override
    public void updateSubtask(Subtask subtask, Status status) {
        subtask.setStatus(status);
        if (subtask.getStatus().equals(Status.IN_PROGRESS)) {
            Epic epic = epics.get(subtask.getEpicId());
            if (epic != null && !epic.getStatus().equals(Status.IN_PROGRESS)) {
                epic.setStatus(Status.IN_PROGRESS);
            }
        } else if (subtask.getStatus().equals(Status.DONE)) {
            boolean isDone = true;
            List<Subtask> allSubTask = getAllSubtasksForEpicId(subtask.getEpicId());
            for (Subtask subtask1 : allSubTask) {
                if (!subtask1.getStatus().equals(Status.DONE)) {
                    isDone = false;
                    break;
                }
            }
            if (isDone) {
                Epic epic = epics.get(subtask.getEpicId());
                if (epic != null && !epic.getStatus().equals(Status.DONE)) {
                    epic.setStatus(Status.DONE);
                }
            }
            subtasks.put(subtask.getId(), subtask);
        }
    }

    @Override
    public void updateEpic(Epic epic) {
        epics.put(epic.getId(), epic);
    }

    @Override
    public void deleteTaskById(int id) {
        tasks.remove(id);
    }

    @Override
    public void deleteSubtaskById(int id) {
        Subtask subtask = getSubtaskById(id);
        Epic epic = getEpicById(subtask.getEpicId());
        if (epic != null) {
            epic.deleteSubtask(subtask);
            updateEpic(epic);
        }
        subtasks.remove(id);
    }

    @Override
    public void deleteEpicById(int id) {
        List<Subtask> deleteSubtask = getAllSubtasksForEpicId(id);
        for (Subtask subtask : deleteSubtask) {
            subtasks.remove(subtask.getId());
        }
        epics.remove(id);
    }

    @Override
    public List<Subtask> getAllSubtasksForEpicId(int epicId) {
        Epic epic = getEpicById(epicId);
        if (epic != null) {
            return epic.getSubtasks();
        } else {
            return new ArrayList<>();
        }
    }

}