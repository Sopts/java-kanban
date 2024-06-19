package ru.yandex.tasktreker.service;

import ru.yandex.tasktreker.model.Epic;
import ru.yandex.tasktreker.model.Status;
import ru.yandex.tasktreker.model.Subtask;
import ru.yandex.tasktreker.model.Task;

import java.time.LocalDateTime;
import java.util.*;

public class InMemoryTaskManager implements TaskManager {

    protected final Map<Integer, Task> tasks = new HashMap<>();
    protected final Map<Integer, Subtask> subtasks = new HashMap<>();
    protected final Map<Integer, Epic> epics = new HashMap<>();

    public HistoryManager inMemoryHistoryManager;

    protected int count;

    public InMemoryTaskManager(HistoryManager inMemoryHistoryManager) {
        this.inMemoryHistoryManager = inMemoryHistoryManager;
        this.count = 0;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getCount() {
        return count;
    }

    public int changeCount() {
        count++;
        return count;
    }


    public void putTask(Task task) {
        switch (task.getTaskType()) {
            case TASK -> tasks.put(task.getId(), task);
            case EPIC -> epics.put(task.getId(), (Epic) task);
            case SUBTASK -> subtasks.put(task.getId(), (Subtask) task);
        }
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
    public void deleteAllTask() {
        List<Integer> listIdTasks = new ArrayList<>(tasks.keySet());
        tasks.clear();
        for (Integer id : listIdTasks) {
            inMemoryHistoryManager.remove(id);
        }
    }

    @Override
    public void deleteAllSubtask() {
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
    public void deleteAllEpic() {
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
    public void createTask(Task task) {
        TreeSet<Task> prioritizedTasks = getPrioritizedTasks();
        if (prioritizedTasks.stream()
                .anyMatch(t -> isTasksIntersect(task, t))) {
            throw new RuntimeException("Задача не может быть добавлена, так как она пересекается с другой задачей");
        }

        int id = changeCount();
        task.setId(id);
        tasks.put(id, task);
    }

    @Override
    public void createSubtask(Subtask subtask) {
        TreeSet<Task> prioritizedTasks = getPrioritizedTasks();
        if (prioritizedTasks.stream()
                .anyMatch(t -> isTasksIntersect(subtask, t))) {
            throw new RuntimeException("Задача не может быть добавлена, так как она пересекается с другой задачей");
        }

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
        TreeSet<Task> prioritizedTasks = getPrioritizedTasks();
        if (prioritizedTasks.stream()
                .anyMatch(t -> isTasksIntersect(epic, t))) {
            throw new RuntimeException("Задача не может быть добавлена, так как она пересекается с другой задачей");
        }

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
            Epic epic = epics.get(subtask.getEpicId());
            if (epic != null && !epic.getStatus().equals(Status.IN_PROGRESS)) {
                epic.setStatus(Status.IN_PROGRESS);
            }

            boolean isDone = true;
            List<Subtask> allSubTask = getAllSubtasksForEpicId(subtask.getEpicId());
            for (Subtask subtask1 : allSubTask) {
                if (!subtask1.getStatus().equals(Status.DONE)) {
                    isDone = false;
                    break;
                }
            }
            if (isDone) {
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
        inMemoryHistoryManager.remove(id);
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
        inMemoryHistoryManager.remove(id);
    }

    @Override
    public void deleteEpicById(int id) {
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

    @Override
    public TreeSet<Task> getPrioritizedTasks() {
        Set<Task> allTasks = new TreeSet<>(tasks.values());
        allTasks.addAll(subtasks.values());

        List<Task> sortedTasks = allTasks.stream()
                .filter(task -> task.getStartTime() != null)
                .sorted(Comparator.comparing(Task::getStartTime))
                .toList();

        return new TreeSet<>(sortedTasks);
    }

    @Override
    public boolean isTasksIntersect(Task task1, Task task2) {
        LocalDateTime startTime1 = task1.getStartTime();
        LocalDateTime endTime1 = task1.getEndTime();
        LocalDateTime startTime2 = task2.getStartTime();
        LocalDateTime endTime2 = task2.getEndTime();

        return startTime1.isBefore(endTime2) && startTime2.isBefore(endTime1);
    }

}