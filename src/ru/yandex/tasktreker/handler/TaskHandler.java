package ru.yandex.tasktreker.handler;

import com.sun.net.httpserver.HttpExchange;
import ru.yandex.tasktreker.model.Task;
import ru.yandex.tasktreker.service.TaskManager;

import java.io.IOException;
import java.util.regex.Pattern;

public class TaskHandler extends BaseHttpHandler {

    public TaskHandler(TaskManager taskManager) {
        super(taskManager);
    }

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        try {
            String path = httpExchange.getRequestURI().getPath();
            String requestMethod = httpExchange.getRequestMethod();

            switch (requestMethod) {
                case "GET": {
                    if (Pattern.matches("^/tasks$", path)) {
                        String response = gson.toJson(taskManager.getTasks());
                        sendText(httpExchange, response);
                    } else if (Pattern.matches("^/tasks/\\d+$", path)) {
                        String pathId = path.replaceFirst("/tasks/", "");
                        int id = parsePathId(pathId);
                        if (id != -1) {
                            String response = gson.toJson(taskManager.getTaskById(id));
                            sendText(httpExchange, response);
                        } else {
                            sendNotFound(httpExchange);
                        }
                    } else {
                        httpExchange.sendResponseHeaders(405, 0);
                    }
                    break;
                }
                case "POST": {
                    String requestBody = new String(httpExchange.getRequestBody().readAllBytes());
                    Task task = gson.fromJson(requestBody, Task.class);
                    if (task.getId() == 0) {
                        try {
                            taskManager.createTask(task);
                            httpExchange.sendResponseHeaders(201, 0);
                            httpExchange.getResponseBody().write(("Task была добавлена: "
                                    + task.getId()).getBytes());
                        } catch (RuntimeException e) {
                            sendHasInteractions(httpExchange);
                        }
                    } else {
                        try {
                            taskManager.updateTask(task, task.getStatus());
                            httpExchange.sendResponseHeaders(201, 0);
                            httpExchange.getResponseBody().write(("Task была обновлена: "
                                    + task.getId()).getBytes());
                        } catch (RuntimeException e) {
                            sendNotFound(httpExchange);
                        }
                    }
                    httpExchange.close();
                    break;
                }
                case "DELETE": {
                    String pathId = path.replaceFirst("/tasks/", "");
                    int id = parsePathId(pathId);
                    if (id != -1) {
                        taskManager.deleteTaskById(id);
                        sendText(httpExchange, "Task удален");
                    } else {
                        sendNotFound(httpExchange);
                    }
                    break;
                }
                default:
                    httpExchange.sendResponseHeaders(405, 0);
            }
        } catch (Exception e) {
            httpExchange.sendResponseHeaders(500, 0);
        }
    }
}