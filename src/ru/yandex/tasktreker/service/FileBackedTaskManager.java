package ru.yandex.tasktreker.service;

import ru.yandex.tasktreker.exceptions.ManagerSaveException;
import ru.yandex.tasktreker.model.*;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FileBackedTaskManager extends InMemoryTaskManager {

    public FileBackedTaskManager() {
        super(Managers.getDefaultHistory());
    }

    public String toString(Task task) {
        String result = task.getId() + "," + task.getTaskType() + "," + task.getName()
                + "," + task.getStatus() + "," + task.getDescription() + "," + task.getStartTime()
                + "," + task.getDuration();
        if (task instanceof Subtask subtask) {
            result += "," + subtask.getEpicId();
        }

        return result + "\n";
    }

    private void save() {
        try (FileWriter writer = new FileWriter("tasks.csv")) {
            List<Task> allTasks = new ArrayList<>();
            allTasks.addAll(tasks.values());
            allTasks.addAll(epics.values());
            allTasks.addAll(subtasks.values());
            for (Task task : allTasks) {
                String[] data = {toString(task)};
                writer.write(Arrays.toString(data).replace("[", "").replace("]", ""));
            }
        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка при сохранении задач: " + e.getMessage());
        }
    }

    public static Task fromString(String value) {
        String[] data = value.split(",");
        int id = Integer.parseInt(data[0]);
        TaskType type = TaskType.valueOf(data[1]);
        String name = data[2];
        Status status = Status.valueOf(data[3]);
        String description = data[4];
        LocalDateTime startTime = LocalDateTime.parse(data[5]);
        Duration duration = Duration.parse(data[6]);
        switch (type) {
            case TASK -> {
                Task task = new Task(name, description, startTime, duration);
                task.setId(id);
                task.setStatus(status);
                return task;
            }
            case EPIC -> {
                Epic epic = new Epic(name, description, startTime, duration);
                epic.setId(id);
                epic.setStatus(status);
                return epic;
            }
            case SUBTASK -> {
                int epicId = Integer.parseInt(data[7]);
                Subtask subtask = new Subtask(name, description, startTime, duration, epicId);
                subtask.setId(id);
                subtask.setStatus(status);
                return subtask;
            }
        }
        throw new IllegalArgumentException("Указан недопустимый тип задачи: " + type);
    }

    public static FileBackedTaskManager loadFromFile(File file) {
        FileBackedTaskManager loadedManager = new FileBackedTaskManager();
        try {
            String fileContent = Files.readString(file.toPath()).replaceAll("\r", "");
            if (!fileContent.isEmpty()) {
                String[] taskData = fileContent.split("\n");
                int maxId = 0;
                for (String data : taskData) {
                    Task task = fromString(data);
                    int taskId = task.getId();
                    if (taskId > maxId) {
                        maxId = taskId;
                    }
                    loadedManager.putTask(task);
                }
                InMemoryTaskManager.setCount(maxId);
            }
            return loadedManager;
        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка при загрузке задач: " + e.getMessage());
        }
    }


    @Override
    public void deleteAllTask() {
        super.deleteAllTask();
        save();
    }

    @Override
    public void deleteAllSubtask() {
        super.deleteAllSubtask();
        save();
    }

    @Override
    public void deleteAllEpic() {
        super.deleteAllEpic();
        save();
    }

    @Override
    public void createTask(Task task) {
        super.createTask(task);
        save();
    }

    @Override
    public void createSubtask(Subtask subtask) {
        super.createSubtask(subtask);
        save();
    }

    @Override
    public void createEpic(Epic epic) {
        super.createEpic(epic);
        save();
    }

    @Override
    public void updateTask(Task task, Status status) {
        super.updateTask(task, status);
        save();
    }

    @Override
    public void updateSubtask(Subtask subtask, Status status) {
        super.updateSubtask(subtask, status);
        save();
    }

    @Override
    public void updateEpic(Epic epic) {
        super.updateEpic(epic);
        save();
    }

    @Override
    public void deleteTaskById(int id) {
        super.deleteTaskById(id);
        save();
    }

    @Override
    public void deleteSubtaskById(int id) {
        super.deleteSubtaskById(id);
        save();
    }

    @Override
    public void deleteEpicById(int id) {
        super.deleteEpicById(id);
        save();
    }

}
