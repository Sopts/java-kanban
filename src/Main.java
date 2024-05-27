import ru.yandex.tasktreker.model.Epic;
import ru.yandex.tasktreker.model.Subtask;
import ru.yandex.tasktreker.model.Task;
import ru.yandex.tasktreker.service.FileBackedTaskManager;
import ru.yandex.tasktreker.service.InMemoryTaskManager;
import ru.yandex.tasktreker.service.Managers;

import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;


public class Main {
    public static void main(String[] args) throws IOException {

        FileBackedTaskManager manager = Managers.getDefaultFile();

        Task task1 = new Task("Task1", "Description",
                LocalDateTime.now(), Duration.ofHours(2));
        manager.createTask(task1);
        Task task2 = new Task("Task2", "Description",
                LocalDateTime.now().plusHours(10), Duration.ofHours(2));
        manager.createTask(task2);

        Epic epic1 = new Epic("Epic1", "Description",
                LocalDateTime.of(2024, 6, 1, 8, 0), Duration.ofHours(1));
        manager.createEpic(epic1);
        Subtask subtask1 = new Subtask("Subtask1", "Description",
                LocalDateTime.now().plusHours(2), Duration.ofHours(1), epic1.getId());
        manager.createSubtask(subtask1);
        Subtask subtask2 = new Subtask("Subtask2", "Description",
                LocalDateTime.now().plusDays(1), Duration.ofHours(3), epic1.getId());
        manager.createSubtask(subtask2);
        Subtask subtask3 = new Subtask("Subtask3", "Description",
                LocalDateTime.now().plusDays(2), Duration.ofHours(4), epic1.getId());
        manager.createSubtask(subtask3);

        Epic epic2 = new Epic("Epic2", "Description",
                LocalDateTime.now().plusDays(4), Duration.ofHours(4));
        manager.createEpic(epic2);

        manager.deleteAllTask();

        FileBackedTaskManager loadedManager = FileBackedTaskManager.loadFromFile(new File("tasks.csv"));

        System.out.println(loadedManager.getTasks());
        System.out.println(loadedManager.getEpics());
        System.out.println(loadedManager.getSubtasks());
        System.out.println(InMemoryTaskManager.getCount());

    }

}