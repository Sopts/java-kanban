package ru.yandex.tasktreker.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.tasktreker.model.Epic;
import ru.yandex.tasktreker.model.Task;

import java.io.File;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;

class FileBackedTasManagerTest {

    FileBackedTaskManager manager;

    @BeforeEach
    void setUp() {
        manager = Managers.getDefaultFile(Managers.getDefaultHistory());
    }

    @Test
    public void shouldSaveAndLoadEmptyFile() throws IOException {
        int expectedTasks = manager.getTasks().size();

        File tempFile = File.createTempFile("test", ".csv");

        manager.save();

        FileBackedTaskManager loadedManager = FileBackedTaskManager.loadFromFile(tempFile);

        assertEquals(expectedTasks, manager.getTasks().size());
        assertEquals(expectedTasks, loadedManager.getTasks().size());

    }

    @Test
    public void shouldSaveTasks() throws IOException {
        Task task = new Task("Task", "Description");
        Epic epic = new Epic("Epic", "Description");

        manager.createTask(task);
        int taskId = task.getId();
        int expectedTasks = manager.getTasks().size();

        manager.createEpic(epic);
        int epicId = epic.getId();
        int expectedEpics = manager.getEpics().size();


        Task expectedTask = new Task(task.getName(), task.getDescription());
        expectedTask.setId(taskId);

        Task actualTask = manager.getTaskById(taskId);

        Epic expectedEpic = new Epic(epic.getName(), epic.getDescription());
        expectedEpic.setId(epicId);

        Epic actualEpic = manager.getEpicById(epicId);

        assertEquals(expectedTasks, manager.getTasks().size());
        assertEquals(expectedEpics, manager.getEpics().size());
        assertEquals(expectedTask, actualTask);
        assertEquals(expectedEpic, actualEpic);
    }

    @Test
    public void shouldLoadTasks() throws IOException {
        Task task = new Task("Task", "Description");
        Epic epic = new Epic("Epic", "Description");

        manager.createTask(task);
        int taskId = task.getId();

        manager.createEpic(epic);
        int epicId = epic.getId();

        Task expectedTask = new Task(task.getName(), task.getDescription());
        expectedTask.setId(taskId);

        Epic expectedEpic = new Epic(epic.getName(), epic.getDescription());
        expectedEpic.setId(epicId);

        FileBackedTaskManager loadedManager = FileBackedTaskManager.loadFromFile(new File("tasks.csv"));

        Task actualTask = loadedManager.getTaskById(taskId);
        Epic actualEpic = loadedManager.getEpicById(epicId);

        assertEquals(expectedTask, actualTask);
        assertEquals(expectedEpic, actualEpic);

    }


}
