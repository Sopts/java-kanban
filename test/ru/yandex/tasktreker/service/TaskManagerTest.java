package ru.yandex.tasktreker.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.tasktreker.model.Epic;
import ru.yandex.tasktreker.model.Status;
import ru.yandex.tasktreker.model.Subtask;
import ru.yandex.tasktreker.model.Task;

import java.time.Duration;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public abstract class TaskManagerTest {
    protected TaskManager taskManager;

    @BeforeEach
    public void setUp() {
        taskManager = Managers.getDefault();
    }

    @Test
    public void shouldEpicStatusNew() {
        Epic epic = new Epic("Epic", "Description", LocalDateTime.now(), Duration.ofHours(1));
        taskManager.createEpic(epic);
        int epicId = epic.getId();

        Subtask subtask1 = new Subtask("Subtask1", "Description",
                LocalDateTime.now().plusHours(1), Duration.ofHours(1), epic.getId());
        taskManager.createSubtask(subtask1);
        Subtask subtask2 = new Subtask("Subtask2", "Description",
                LocalDateTime.now().plusHours(2), Duration.ofHours(1), epic.getId());
        taskManager.createSubtask(subtask2);

        Epic actualEpic = taskManager.getEpicById(epicId);

        assertEquals(Status.NEW, actualEpic.getStatus());
    }

    @Test
    public void shouldEpicStatusDone() {
        Epic epic = new Epic("Epic", "Description", LocalDateTime.now(), Duration.ofHours(1));
        taskManager.createEpic(epic);
        int epicId = epic.getId();

        Subtask subtask1 = new Subtask("Subtask1", "Description",
                LocalDateTime.now().plusHours(1), Duration.ofHours(1), epic.getId());
        taskManager.createSubtask(subtask1);
        taskManager.updateSubtask(subtask1, Status.DONE);
        Subtask subtask2 = new Subtask("Subtask2", "Description",
                LocalDateTime.now().plusHours(2), Duration.ofHours(1), epic.getId());
        taskManager.createSubtask(subtask2);
        taskManager.updateSubtask(subtask2, Status.DONE);

        Epic actualEpic = taskManager.getEpicById(epicId);

        assertEquals(Status.DONE, actualEpic.getStatus());
    }

    @Test
    public void shouldEpicStatusInProgress() {
        Epic epic = new Epic("Epic", "Description", LocalDateTime.now(), Duration.ofHours(1));
        taskManager.createEpic(epic);
        int epicId = epic.getId();

        Subtask subtask1 = new Subtask("Subtask1", "Description",
                LocalDateTime.now().plusHours(1), Duration.ofHours(1), epic.getId());
        taskManager.createSubtask(subtask1);
        Subtask subtask2 = new Subtask("Subtask2", "Description",
                LocalDateTime.now().plusHours(2), Duration.ofHours(1), epic.getId());
        taskManager.createSubtask(subtask2);
        taskManager.updateSubtask(subtask2, Status.DONE);

        Epic actualEpic = taskManager.getEpicById(epicId);

        assertEquals(Status.IN_PROGRESS, actualEpic.getStatus());
    }

    @Test
    public void shouldEpicStatusInProgressWhenSubtaskInProgress() {
        Epic epic = new Epic("Epic", "Description", LocalDateTime.now(), Duration.ofHours(1));
        taskManager.createEpic(epic);
        int epicId = epic.getId();

        Subtask subtask1 = new Subtask("Subtask1", "Description",
                LocalDateTime.now().plusHours(1), Duration.ofHours(1), epic.getId());
        taskManager.createSubtask(subtask1);
        taskManager.updateSubtask(subtask1, Status.IN_PROGRESS);
        Subtask subtask2 = new Subtask("Subtask2", "Description",
                LocalDateTime.now().plusHours(2), Duration.ofHours(1), epic.getId());
        taskManager.createSubtask(subtask2);
        taskManager.updateSubtask(subtask2, Status.IN_PROGRESS);

        Epic actualEpic = taskManager.getEpicById(epicId);

        assertEquals(Status.IN_PROGRESS, actualEpic.getStatus());
    }

    @Test
    public void shouldGetRightIntervalIntersection() {
        Task task1 = new Task("Task1", "Description",
                LocalDateTime.now().plusHours(1), Duration.ofHours(1));
        taskManager.createTask(task1);
        int task1Id = task1.getId();
        Task task2 = new Task("Task2", "Description",
                LocalDateTime.now().plusHours(2), Duration.ofHours(1));
        taskManager.createTask(task2);
        int task2Id = task1.getId();

        Task actualTask1 = taskManager.getTaskById(task1Id);
        Task actualTask2 = taskManager.getTaskById(task2Id);

        boolean resultManager = taskManager.isTasksIntersect(actualTask1, actualTask2);
        assertTrue(resultManager);

    }
}