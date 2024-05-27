package ru.yandex.tasktreker.service;

import org.junit.jupiter.api.Test;
import ru.yandex.tasktreker.model.Epic;
import ru.yandex.tasktreker.model.Status;
import ru.yandex.tasktreker.model.Subtask;
import ru.yandex.tasktreker.model.Task;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

class InMemoryHistoryManagerTest extends TaskManagerTest {

    TaskManager taskManager = Managers.getDefault();

    @Test
    public void shouldTasksEqualsByIdAndEqualsWhenAddedToManager() {
        Task task = new Task("Task", "Description",
                LocalDateTime.now(), Duration.ofHours(1));

        Epic epic = new Epic("Epic", "Description",
                LocalDateTime.now().plusHours(1), Duration.ofHours(1));

        taskManager.createTask(task);
        int taskId = task.getId();

        taskManager.createEpic(epic);
        int epicId = epic.getId();

        Task expectedTask = new Task(task.getName(), task.getDescription(), task.getStartTime(), task.getDuration());
        expectedTask.setId(taskId);

        Task actualTask = taskManager.getTaskById(taskId);

        Epic expectedEpic = new Epic(epic.getName(), epic.getDescription(), epic.getStartTime(), epic.getDuration());
        expectedEpic.setId(epicId);

        Epic actualEpic = taskManager.getEpicById(epicId);

        assertEquals(expectedTask, actualTask);
        assertEquals(expectedEpic, actualEpic);
    }

    @Test
    public void shouldRemovePreviousVersionTaskOfHistory() {
        Task task = new Task("Task", "Description",
                LocalDateTime.now(), Duration.ofHours(1));

        taskManager.createTask(task);

        for (int i = 0; i < 100; i++) {
            taskManager.getTaskById(task.getId());
        }

        taskManager.updateTask(task, Status.DONE);
        taskManager.getTaskById(task.getId());

        assertEquals(1, taskManager.getHistory().size());
        assertEquals(Status.DONE, taskManager.getHistory().getFirst().getStatus());
    }

    @Test
    public void shouldGetRightHistoryId() {
        Task task1 = new Task("Task1", "Description",
                LocalDateTime.now().plusHours(1), Duration.ofHours(1));
        Task task2 = new Task("Task2", "Description",
                LocalDateTime.now().plusHours(2), Duration.ofHours(1));
        Epic epic1 = new Epic("Epic1", "Description",
                LocalDateTime.now().plusHours(3), Duration.ofHours(1));
        Epic epic2 = new Epic("Epic2", "Description",
                LocalDateTime.now().plusHours(4), Duration.ofHours(1));
        taskManager.createTask(task1);
        taskManager.createTask(task2);
        taskManager.createEpic(epic1);
        taskManager.createEpic(epic2);

        List<Integer> expectedListHistoryId = new ArrayList<>();

        taskManager.getTaskById(task2.getId());
        expectedListHistoryId.add(task2.getId());
        taskManager.getEpicById(epic1.getId());
        expectedListHistoryId.add(epic1.getId());
        taskManager.getEpicById(epic2.getId());
        expectedListHistoryId.add(epic2.getId());
        taskManager.getTaskById(task1.getId());
        expectedListHistoryId.add(task1.getId());

        List<Integer> actualListHistoryId = new ArrayList<>();

        for (Task task : taskManager.getHistory()) {
            actualListHistoryId.add(task.getId());
        }

        assertEquals(expectedListHistoryId, actualListHistoryId);
    }

    @Test
    public void shouldEpicDontHaveSubtaskIdAfterDelete() {
        Epic epic1 = new Epic("Epic1", "Description",
                LocalDateTime.now().plusHours(1), Duration.ofHours(1));
        taskManager.createEpic(epic1);

        Subtask subtask1 = new Subtask("Subtask1", "Description",
                LocalDateTime.now().plusHours(2), Duration.ofHours(1), epic1.getId());
        taskManager.createSubtask(subtask1);

        Subtask subtask2 = new Subtask("Subtask2", "Description",
                LocalDateTime.now().plusHours(3), Duration.ofHours(1), epic1.getId());
        taskManager.createSubtask(subtask2);

        taskManager.deleteSubtaskById(subtask1.getId());
        taskManager.deleteSubtaskById(subtask2.getId());

        assertFalse(epic1.getSubtasks().contains(subtask1));
        assertFalse(epic1.getSubtasks().contains(subtask2));

    }

    @Test
    public void shouldGetEmptyHistory() {
        assertEquals(0, taskManager.getHistory().size());
    }

}