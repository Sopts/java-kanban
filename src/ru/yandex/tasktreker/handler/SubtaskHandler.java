package ru.yandex.tasktreker.handler;

import com.sun.net.httpserver.HttpExchange;
import ru.yandex.tasktreker.model.Subtask;
import ru.yandex.tasktreker.service.TaskManager;

import java.io.IOException;
import java.util.regex.Pattern;

public class SubtaskHandler extends BaseHttpHandler {

    public SubtaskHandler(TaskManager taskManager) {
        super(taskManager);
    }


    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        try {
            String path = httpExchange.getRequestURI().getPath();
            String requestMethod = httpExchange.getRequestMethod();

            switch (requestMethod) {
                case "GET": {
                    if (Pattern.matches("^/subtasks$", path)) {
                        String response = gson.toJson(taskManager.getSubtasks());
                        sendText(httpExchange, response);
                    } else if (Pattern.matches("^/subtasks/\\d+$", path)) {
                        String pathId = path.replaceFirst("/subtasks/", "");
                        int id = parsePathId(pathId);
                        if (id != -1) {
                            String response = gson.toJson(taskManager.getSubtaskById(id));
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
                    Subtask subtask = gson.fromJson(requestBody, Subtask.class);
                    if (subtask.getId() == 0) {
                        try {
                            taskManager.createSubtask(subtask);
                            httpExchange.sendResponseHeaders(201, 0);
                            httpExchange.getResponseBody().write(("Subtask была добавлена: "
                                    + subtask.getId()).getBytes());
                        } catch (RuntimeException e) {
                            sendHasInteractions(httpExchange);
                        }
                    } else {
                        try {
                            taskManager.updateSubtask(subtask, subtask.getStatus());
                            httpExchange.sendResponseHeaders(201, 0);
                            httpExchange.getResponseBody().write(("Subtask была обновлена: "
                                    + subtask.getId()).getBytes());
                        } catch (RuntimeException e) {
                            sendNotFound(httpExchange);
                        }
                    }
                    httpExchange.close();
                    break;
                }
                case "DELETE": {
                    String pathId = path.replaceFirst("/subtasks/", "");
                    int id = parsePathId(pathId);
                    if (id != -1) {
                        taskManager.deleteSubtaskById(id);
                        sendText(httpExchange, "Subtask удален");
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