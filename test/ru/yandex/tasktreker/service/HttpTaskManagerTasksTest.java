package ru.yandex.tasktreker.service;

import com.google.gson.Gson;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.tasktreker.model.Epic;
import ru.yandex.tasktreker.model.Status;
import ru.yandex.tasktreker.model.Subtask;
import ru.yandex.tasktreker.model.Task;
import ru.yandex.tasktreker.server.HttpTaskServer;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;


class HttpTaskManagerTasksTest {

    Gson gson = Managers.getGson();
    TaskManager taskManager = Managers.getDefault();
    HttpTaskServer taskServer = new HttpTaskServer(taskManager);

    HttpTaskManagerTasksTest() throws IOException {
    }

    @BeforeEach
    public void setUp() {
        taskManager.deleteAllTask();
        taskManager.deleteAllSubtask();
        taskManager.deleteAllEpic();
        taskServer.start();
    }

    @AfterEach
    public void shutDown() {
        taskServer.stop();
    }


    @Test
    void shouldGetTasks() throws IOException, InterruptedException {
        Task task1 = new Task("Task1", "Description",
                LocalDateTime.now(), Duration.ofHours(2));
        taskManager.createTask(task1);
        Task task2 = new Task("Task2", "Description",
                LocalDateTime.now().plusHours(10), Duration.ofHours(2));
        taskManager.createTask(task2);

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/tasks"))
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode(), "Код ответа не совпадает");

        Map<Integer, Task> tasksFromManager = taskManager.getTasks();

        assertNotNull(tasksFromManager, "Задачи не возвращаются");
        assertEquals(2, tasksFromManager.size(), "Некорректное количество задач");
        assertEquals("Task1", tasksFromManager.get(1).getName(), "Некорректное имя задачи");

    }

    @Test
    void shouldGetEpics() throws IOException, InterruptedException {
        Epic epic1 = new Epic("Epic1", "Description",
                LocalDateTime.of(2024, 6, 1, 8, 0), Duration.ofHours(1));
        taskManager.createEpic(epic1);
        Subtask subtask1 = new Subtask("Subtask1", "Description",
                LocalDateTime.now().plusHours(2), Duration.ofHours(1), epic1.getId());
        taskManager.createSubtask(subtask1);
        Subtask subtask2 = new Subtask("Subtask2", "Description",
                LocalDateTime.now().plusDays(1), Duration.ofHours(3), epic1.getId());
        taskManager.createSubtask(subtask2);
        Subtask subtask3 = new Subtask("Subtask3", "Description",
                LocalDateTime.now().plusDays(2), Duration.ofHours(4), epic1.getId());
        taskManager.createSubtask(subtask3);

        Epic epic2 = new Epic("Epic2", "Description",
                LocalDateTime.now().plusDays(4), Duration.ofHours(4));
        taskManager.createEpic(epic2);

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/epics"))
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode(), "Код ответа не совпадает");

        Map<Integer, Epic> epicsFromManager = taskManager.getEpics();

        assertNotNull(epicsFromManager, "Задачи не возвращаются");
        assertEquals(2, epicsFromManager.size(), "Некорректное количество задач");
        assertEquals("Epic1", epicsFromManager.get(1).getName(), "Некорректное имя задачи");

    }


    @Test
    void shouldGetSubtasks() throws IOException, InterruptedException {
        Epic epic1 = new Epic("Epic1", "Description",
                LocalDateTime.of(2024, 6, 1, 8, 0), Duration.ofHours(1));
        taskManager.createEpic(epic1);
        Subtask subtask1 = new Subtask("Subtask1", "Description",
                LocalDateTime.now().plusHours(2), Duration.ofHours(1), epic1.getId());
        taskManager.createSubtask(subtask1);
        Subtask subtask2 = new Subtask("Subtask2", "Description",
                LocalDateTime.now().plusDays(1), Duration.ofHours(3), epic1.getId());
        taskManager.createSubtask(subtask2);
        Subtask subtask3 = new Subtask("Subtask3", "Description",
                LocalDateTime.now().plusDays(2), Duration.ofHours(4), epic1.getId());
        taskManager.createSubtask(subtask3);


        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/subtasks"))
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode(), "Код ответа не совпадает");

        Map<Integer, Subtask> subtasksFromManager = taskManager.getSubtasks();

        assertNotNull(subtasksFromManager, "Задачи не возвращаются");
        assertEquals(3, subtasksFromManager.size(), "Некорректное количество задач");
        assertEquals("Subtask3", subtasksFromManager.get(4).getName(), "Некорректное имя задачи");

    }


    @Test
    void shouldGetTaskById() throws IOException, InterruptedException {
        Task task1 = new Task("Task1", "Description",
                LocalDateTime.now(), Duration.ofHours(2));
        taskManager.createTask(task1);

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/tasks/1"))
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode(), "Код ответа не совпадает");

        Map<Integer, Task> tasksFromManager = taskManager.getTasks();

        assertNotNull(tasksFromManager, "Задачи не возвращаются");
        assertEquals(1, tasksFromManager.size(), "Некорректное количество задач");
        assertEquals("Task1", tasksFromManager.get(1).getName(), "Некорректное имя задачи");

    }

    @Test
    void shouldGetEpicById() throws IOException, InterruptedException {
        Epic epic1 = new Epic("Epic1", "Description",
                LocalDateTime.of(2024, 6, 1, 8, 0), Duration.ofHours(1));
        taskManager.createEpic(epic1);

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/epics/1"))
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode(), "Код ответа не совпадает");

        Map<Integer, Epic> tasksFromManager = taskManager.getEpics();

        assertNotNull(tasksFromManager, "Задачи не возвращаются");
        assertEquals(1, tasksFromManager.size(), "Некорректное количество задач");
        assertEquals("Epic1", tasksFromManager.get(1).getName(), "Некорректное имя задачи");

    }

    @Test
    void shouldGetSubtaskById() throws IOException, InterruptedException {
        Epic epic1 = new Epic("Epic1", "Description",
                LocalDateTime.of(2024, 6, 1, 8, 0), Duration.ofHours(1));
        taskManager.createEpic(epic1);
        Subtask subtask1 = new Subtask("Subtask1", "Description",
                LocalDateTime.now().plusHours(2), Duration.ofHours(1), epic1.getId());
        taskManager.createSubtask(subtask1);

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/subtasks/2"))
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode(), "Код ответа не совпадает");

        Map<Integer, Subtask> tasksFromManager = taskManager.getSubtasks();

        assertNotNull(tasksFromManager, "Задачи не возвращаются");
        assertEquals(1, tasksFromManager.size(), "Некорректное количество задач");
        assertEquals("Subtask1", tasksFromManager.get(2).getName(), "Некорректное имя задачи");

    }


    @Test
    public void shouldAddTask() throws IOException, InterruptedException {
        Task task1 = new Task("Task1", "Description",
                LocalDateTime.now(), Duration.ofHours(2));
        String taskJson = gson.toJson(task1);

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .POST(HttpRequest.BodyPublishers.ofString(taskJson))
                .build();


        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(201, response.statusCode());

        Map<Integer, Task> tasksFromManager = taskManager.getTasks();

        assertNotNull(tasksFromManager, "Задачи не возвращаются");
        assertEquals(1, tasksFromManager.size(), "Некорректное количество задач");
        assertEquals("Task1", tasksFromManager.get(1).getName(), "Некорректное имя задачи");
    }

    @Test
    public void shouldUpdateTask() throws IOException, InterruptedException {
        Task task1 = new Task("Task1", "Description",
                LocalDateTime.now(), Duration.ofHours(2));
        taskManager.createTask(task1);

        Task task2 = new Task("Task1", "Description",
                LocalDateTime.now(), Duration.ofHours(2));
        task2.setStatus(Status.DONE);
        task2.setId(1);
        String taskJson = gson.toJson(task2);


        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .POST(HttpRequest.BodyPublishers.ofString(taskJson))
                .build();


        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(201, response.statusCode());

        Map<Integer, Task> tasksFromManager = taskManager.getTasks();

        assertNotNull(tasksFromManager, "Задачи не возвращаются");
        assertEquals(1, tasksFromManager.size(), "Некорректное количество задач");
        assertEquals(Status.DONE, tasksFromManager.get(1).getStatus(), "Некорректное имя задачи");
    }

    @Test
    public void shouldAddEpic() throws IOException, InterruptedException {
        Epic epic1 = new Epic("Epic1", "Description",
                LocalDateTime.now().plusDays(5), Duration.ofHours(1));
        String epicJson = gson.toJson(epic1);

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/epics");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .POST(HttpRequest.BodyPublishers.ofString(epicJson))
                .build();


        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(201, response.statusCode());

        Map<Integer, Epic> tasksFromManager = taskManager.getEpics();

        assertNotNull(tasksFromManager, "Задачи не возвращаются");
        assertEquals(1, tasksFromManager.size(), "Некорректное количество задач");
        assertEquals("Epic1", tasksFromManager.get(1).getName(), "Некорректное имя задачи");
    }

    @Test
    public void shouldAddSubtask() throws IOException, InterruptedException {
        Epic epic1 = new Epic("Epic1", "Description",
                LocalDateTime.of(2024, 6, 1, 8, 0), Duration.ofHours(1));
        taskManager.createEpic(epic1);
        Subtask subtask1 = new Subtask("Subtask1", "Description",
                LocalDateTime.now().plusHours(2), Duration.ofHours(1), epic1.getId());
        String subtaskJson = gson.toJson(subtask1);

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/subtasks");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .POST(HttpRequest.BodyPublishers.ofString(subtaskJson))
                .build();


        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(201, response.statusCode());

        Map<Integer, Subtask> tasksFromManager = taskManager.getSubtasks();

        assertNotNull(tasksFromManager, "Задачи не возвращаются");
        assertEquals(1, tasksFromManager.size(), "Некорректное количество задач");
        assertEquals("Subtask1", tasksFromManager.get(2).getName(), "Некорректное имя задачи");
    }

    @Test
    public void shouldUpdateSubtask() throws IOException, InterruptedException {
        Epic epic1 = new Epic("Epic1", "Description",
                LocalDateTime.of(2024, 6, 1, 8, 0), Duration.ofHours(1));
        taskManager.createEpic(epic1);
        Subtask subtask1 = new Subtask("Subtask1", "Description",
                LocalDateTime.now().plusHours(2), Duration.ofHours(1), epic1.getId());
        taskManager.createSubtask(subtask1);

        Subtask subtask2 = new Subtask("Subtask1", "Description",
                LocalDateTime.now().plusHours(2), Duration.ofHours(1), epic1.getId());
        subtask2.setStatus(Status.DONE);
        subtask2.setId(2);
        String subtaskJson = gson.toJson(subtask2);


        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/subtasks");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .POST(HttpRequest.BodyPublishers.ofString(subtaskJson))
                .build();


        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(201, response.statusCode());

        Map<Integer, Subtask> tasksFromManager = taskManager.getSubtasks();

        assertNotNull(tasksFromManager, "Задачи не возвращаются");
        assertEquals(1, tasksFromManager.size(), "Некорректное количество задач");
        assertEquals(Status.DONE, tasksFromManager.get(2).getStatus(), "Некорректное имя задачи");
    }

    @Test
    void shouldDeleteTaskById() throws IOException, InterruptedException {
        Task task1 = new Task("Task1", "Description", LocalDateTime.now(), Duration.ofHours(2));
        taskManager.createTask(task1);


        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/tasks/1"))
                .DELETE()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode(), "Код ответа не совпадает");

        Map<Integer, Task> tasksFromManager = taskManager.getTasks();

        assertEquals(0, tasksFromManager.size(), "Некорректное количество задач");
    }


    @Test
    void shouldDeleteEpicById() throws IOException, InterruptedException {
        Epic epic1 = new Epic("Epic1", "Description",
                LocalDateTime.of(2024, 6, 1, 8, 0), Duration.ofHours(1));
        taskManager.createEpic(epic1);


        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/epics/1"))
                .DELETE()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode(), "Код ответа не совпадает");

        Map<Integer, Epic> tasksFromManager = taskManager.getEpics();

        assertEquals(0, tasksFromManager.size(), "Некорректное количество задач");
    }

    @Test
    void shouldDeleteSubtaskById() throws IOException, InterruptedException {
        Epic epic1 = new Epic("Epic1", "Description",
                LocalDateTime.of(2024, 6, 1, 8, 0), Duration.ofHours(1));
        taskManager.createEpic(epic1);
        Subtask subtask1 = new Subtask("Subtask1", "Description",
                LocalDateTime.now().plusHours(2), Duration.ofHours(1), epic1.getId());
        taskManager.createSubtask(subtask1);


        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/subtasks/2"))
                .DELETE()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode(), "Код ответа не совпадает");

        Map<Integer, Subtask> tasksFromManager = taskManager.getSubtasks();

        assertEquals(0, tasksFromManager.size(), "Некорректное количество задач");
    }

    @Test
    void shouldSubtaskFromEpic() throws IOException, InterruptedException {
        Epic epic1 = new Epic("Epic1", "Description",
                LocalDateTime.now(), Duration.ofHours(1));
        taskManager.createEpic(epic1);
        Subtask subtask1 = new Subtask("Subtask1", "Description",
                LocalDateTime.now().plusHours(2), Duration.ofHours(1), epic1.getId());
        taskManager.createSubtask(subtask1);
        Subtask subtask2 = new Subtask("Subtask2", "Description",
                LocalDateTime.now().plusDays(1), Duration.ofHours(3), epic1.getId());
        taskManager.createSubtask(subtask2);
        Subtask subtask3 = new Subtask("Subtask3", "Description",
                LocalDateTime.now().plusDays(2), Duration.ofHours(4), epic1.getId());
        taskManager.createSubtask(subtask3);


        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/epics/1/subtasks"))
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode(), "Код ответа не совпадает");

        List<Subtask> tasksFromManager = taskManager.getAllSubtasksForEpicId(epic1.getId());

        assertEquals(3, tasksFromManager.size(), "Некорректное количество задач");
    }


    @Test
    void shouldGetPrioritizedTasks() throws IOException, InterruptedException {
        Task task1 = new Task("Task1", "Description",
                LocalDateTime.now(), Duration.ofHours(2));
        taskManager.createTask(task1);
        Task task2 = new Task("Task2", "Description",
                LocalDateTime.now().plusHours(10), Duration.ofHours(2));
        taskManager.createTask(task2);
        Task task3 = new Task("Task3", "Description",
                LocalDateTime.now().plusHours(4), Duration.ofHours(2));
        taskManager.createTask(task3);


        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/prioritized"))
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode(), "Код ответа не совпадает");

        TreeSet<Task> prioritizedTasks = taskManager.getPrioritizedTasks();
        assertEquals(3, prioritizedTasks.size(), "Некорректное количество задач");

        assertEquals("Task1", prioritizedTasks.getFirst().getName(), "Некорректное количество задач");
        assertEquals("Task2", prioritizedTasks.getLast().getName(), "Некорректное количество задач");
    }


    @Test
    void shouldGetHistory() throws IOException, InterruptedException {
        Task task1 = new Task("Task1", "Description",
                LocalDateTime.now(), Duration.ofHours(2));
        taskManager.createTask(task1);
        Epic epic1 = new Epic("Epic1", "Description",
                LocalDateTime.now().plusHours(10), Duration.ofHours(2));
        taskManager.createEpic(epic1);
        Task task2 = new Task("Task2", "Description",
                LocalDateTime.now().plusHours(4), Duration.ofHours(2));
        taskManager.createTask(task2);

        taskManager.getTaskById(task1.getId());
        taskManager.getEpicById(epic1.getId());
        taskManager.getTaskById(task2.getId());

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/history"))
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode(), "Код ответа не совпадает");

        List<Task> tasksFromManager = taskManager.getHistory();
        assertEquals(3, tasksFromManager.size(), "Некорректное количество задач");

        assertEquals("Task1", tasksFromManager.getFirst().getName(), "Некорректное количество задач");
        assertEquals("Task2", tasksFromManager.getLast().getName(), "Некорректное количество задач");
    }


}


