package ru.yandex.tasktreker.service;

import ru.yandex.tasktreker.model.Epic;
import ru.yandex.tasktreker.model.Status;
import ru.yandex.tasktreker.model.Subtask;
import ru.yandex.tasktreker.model.Task;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InMemoryTaskManager implements TaskManager {

    protected static final Map<Integer, Task> tasks = new HashMap<>();
    protected static final Map<Integer, Subtask> subtasks = new HashMap<>();
    protected static final Map<Integer, Epic> epics = new HashMap<>();

    public static HistoryManager inMemoryHistoryManager;

    protected static int count = 0;

    public InMemoryTaskManager(HistoryManager inMemoryHistoryManager) {
        InMemoryTaskManager.inMemoryHistoryManager = inMemoryHistoryManager;
    }

    public static void setCount(int count) {
        InMemoryTaskManager.count = count;
    }

    public static int getCount() {
        return count;
    }

    public int changeCount() {
        count++;
        return count;
    }

    @Override
    public Map<Integer, Task> getTasks() {
        return tasks;
    }

    @Override
    public Map<Integer, Subtask> getSubtasks() {
        return subtasks;
    }

    @Override
    public Map<Integer, Epic> getEpics() {
        return epics;
    }

    @Override
    public void deleteAllTask() throws IOException {
        List<Integer> listIdTasks = new ArrayList<>(tasks.keySet());
        tasks.clear();
        for (Integer id : listIdTasks) {
            inMemoryHistoryManager.remove(id);
        }
    }

    @Override
    public void deleteAllSubtask() throws IOException {
        for (Subtask subtask : subtasks.values()) {
            for (Epic epic : epics.values()) {
                epic.deleteSubtasksList();
                updateEpic(epics.get(subtask.getEpicId()));
            }
        }
        List<Integer> listIdSubtasks = new ArrayList<>(subtasks.keySet());
        subtasks.clear();
        for (Integer id : listIdSubtasks) {
            inMemoryHistoryManager.remove(id);
        }
    }

    @Override
    public void deleteAllEpic() throws IOException {
        deleteAllSubtask();
        List<Integer> listIdEpics = new ArrayList<>(epics.keySet());
        epics.clear();
        for (Integer id : listIdEpics) {
            inMemoryHistoryManager.remove(id);
        }
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
    public void createTask(Task task) throws IOException {
        int id = changeCount();
        task.setId(id);
        tasks.put(id, task);
    }

    @Override
    public void createSubtask(Subtask subtask) throws IOException {
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
    public void createEpic(Epic epic) throws IOException {
        int id = changeCount();
        epic.setId(id);
        epics.put(id, epic);
    }

    @Override
    public void updateTask(Task task, Status status) throws IOException {
        task.setStatus(status);
        tasks.put(task.getId(), task);
    }

    @Override
    public void updateSubtask(Subtask subtask, Status status) throws IOException {
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
    public void updateEpic(Epic epic) throws IOException {
        epics.put(epic.getId(), epic);
    }

    @Override
    public void deleteTaskById(int id) throws IOException {
        tasks.remove(id);
        inMemoryHistoryManager.remove(id);
    }

    @Override
    public void deleteSubtaskById(int id) throws IOException {
        Subtask subtask = getSubtaskById(id);
        Epic epic = getEpicById(subtask.getEpicId());
        if (epic != null) {
            epic.deleteSubtask(subtask);
            updateEpic(epic);
        }
        subtasks.remove(id);
        inMemoryHistoryManager.remove(id);
    }

    @Override
    public void deleteEpicById(int id) throws IOException {
        List<Subtask> deleteSubtask = getAllSubtasksForEpicId(id);
        for (Subtask subtask : deleteSubtask) {
            subtasks.remove(subtask.getId());
            inMemoryHistoryManager.remove(subtask.getId());
        }
        epics.remove(id);
        inMemoryHistoryManager.remove(id);
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

    @Override
    public List<Task> getHistory() {
        return inMemoryHistoryManager.getHistory();
    }

}