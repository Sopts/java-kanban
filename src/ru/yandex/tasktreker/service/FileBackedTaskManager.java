package ru.yandex.tasktreker.service;

import ru.yandex.tasktreker.exceptions.ManagerSaveException;
import ru.yandex.tasktreker.model.*;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FileBackedTaskManager extends InMemoryTaskManager {

    public FileBackedTaskManager(HistoryManager inMemoryHistoryManager) {
        super(inMemoryHistoryManager);
    }

    public String toString(Task task) {
        String result = task.getId() + "," + task.getTaskType() + "," + task.getName()
                + "," + task.getStatus() + "," + task.getDescription();
        if (task instanceof Subtask subtask) {
            result += "," + subtask.getEpicId();
        }

        return result + "\n";
    }

    public void save() {
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
        switch (type) {
            case TASK -> {
                Task task = new Task(name, description);
                task.setId(id);
                task.setStatus(status);
                return task;
            }
            case EPIC -> {
                Epic epic = new Epic(name, description);
                epic.setId(id);
                epic.setStatus(status);
                return epic;
            }
            case SUBTASK -> {
                int epicId = Integer.parseInt(data[5]);
                Subtask subtask = new Subtask(name, description, epicId);
                subtask.setId(id);
                subtask.setStatus(status);
                return subtask;
            }
        }
        throw new IllegalArgumentException("Указан недопустимый тип задачи: " + type);
    }

    public static FileBackedTaskManager loadFromFile(File file) throws IOException {
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
                switch (task.getTaskType()) {
                    case TASK -> tasks.put(task.getId(), task);
                    case EPIC -> epics.put(task.getId(), (Epic) task);
                    case SUBTASK -> subtasks.put(task.getId(), (Subtask) task);
                }
            }
            InMemoryTaskManager.setCount(maxId);
        }
        return new FileBackedTaskManager(inMemoryHistoryManager);
    }


    @Override
    public void deleteAllTask() throws IOException {
        super.deleteAllTask();
        save();
    }

    @Override
    public void deleteAllSubtask() throws IOException {
        super.deleteAllSubtask();
        save();
    }

    @Override
    public void deleteAllEpic() throws IOException {
        super.deleteAllEpic();
        save();
    }

    @Override
    public void createTask(Task task) throws IOException {
        super.createTask(task);
        save();
    }

    @Override
    public void createSubtask(Subtask subtask) throws IOException {
        super.createSubtask(subtask);
        save();
    }

    @Override
    public void createEpic(Epic epic) throws IOException {
        super.createEpic(epic);
        save();
    }

    @Override
    public void updateTask(Task task, Status status) throws IOException {
        super.updateTask(task, status);
        save();
    }

    @Override
    public void updateSubtask(Subtask subtask, Status status) throws IOException {
        super.updateSubtask(subtask, status);
        save();
    }

    @Override
    public void updateEpic(Epic epic) throws IOException {
        super.updateEpic(epic);
        save();
    }

    @Override
    public void deleteTaskById(int id) throws IOException {
        super.deleteTaskById(id);
        save();
    }

    @Override
    public void deleteSubtaskById(int id) throws IOException {
        super.deleteSubtaskById(id);
        save();
    }

    @Override
    public void deleteEpicById(int id) throws IOException {
        super.deleteEpicById(id);
        save();
    }

}
