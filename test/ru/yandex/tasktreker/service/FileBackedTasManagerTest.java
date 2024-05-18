package ru.yandex.tasktreker.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.tasktreker.model.Epic;
import ru.yandex.tasktreker.model.Task;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.time.Duration;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class FileBackedTasManagerTest extends TaskManagerTest {

    FileBackedTaskManager manager;

    @BeforeEach
    public void setup() {
        manager = Managers.getDefaultFile();
    }

    @Test
    public void shouldSaveAndLoadEmptyFile() throws IOException {
        int expectedTasks = manager.getTasks().size();

        File tempFile = File.createTempFile("test", ".csv");


        FileBackedTaskManager loadedManager = FileBackedTaskManager.loadFromFile(tempFile);

        assertEquals(expectedTasks, manager.getTasks().size());
        assertEquals(expectedTasks, loadedManager.getTasks().size());

    }

    @Test
    public void shouldSaveTasks() {
        Task task = new Task("Task", "Description",
                LocalDateTime.now(), Duration.ofHours(1));
        Epic epic = new Epic("Epic", "Description",
                LocalDateTime.now().plusHours(1), Duration.ofHours(1));

        manager.createTask(task);
        int taskId = task.getId();
        int expectedTasks = manager.getTasks().size();

        manager.createEpic(epic);
        int epicId = epic.getId();
        int expectedEpics = manager.getEpics().size();


        Task expectedTask = new Task(task.getName(), task.getDescription(), task.getStartTime(), task.getDuration());
        expectedTask.setId(taskId);

        Task actualTask = manager.getTaskById(taskId);

        Epic expectedEpic = new Epic(epic.getName(), epic.getDescription(), epic.getStartTime(), epic.getDuration());
        expectedEpic.setId(epicId);

        Epic actualEpic = manager.getEpicById(epicId);

        assertEquals(expectedTasks, manager.getTasks().size());
        assertEquals(expectedEpics, manager.getEpics().size());
        assertEquals(expectedTask, actualTask);
        assertEquals(expectedEpic, actualEpic);

    }

    @Test
    public void shouldLoadTasks() {
        Task task = new Task("Task", "Description",
                LocalDateTime.now(), Duration.ofHours(1));
        Epic epic = new Epic("Epic", "Description",
                LocalDateTime.now().plusHours(1), Duration.ofHours(1));

        manager.createTask(task);
        int taskId = task.getId();

        manager.createEpic(epic);
        int epicId = epic.getId();

        Task expectedTask = new Task(task.getName(), task.getDescription(), task.getStartTime(), task.getDuration());
        expectedTask.setId(taskId);

        Epic expectedEpic = new Epic(epic.getName(), epic.getDescription(), epic.getStartTime(), epic.getDuration());
        expectedEpic.setId(epicId);

        FileBackedTaskManager loadedManager = FileBackedTaskManager.loadFromFile(new File("tasks.csv"));

        Task actualTask = loadedManager.getTaskById(taskId);
        Epic actualEpic = loadedManager.getEpicById(epicId);

        assertEquals(expectedTask, actualTask);
        assertEquals(expectedEpic, actualEpic);

    }

    @Test
    public void shouldThrowExceptionOnLoad() throws IOException {
        File file = new File("tasks.csv");
        try (FileWriter writer = new FileWriter(file)) {
            writer.write("Невалидный данные");
        }
        assertThrows(NumberFormatException.class, () -> {
            FileBackedTaskManager.loadFromFile(file);
        }, "Ожидалось исключение NumberFormatException");
        Files.delete(file.toPath());
    }

}
