package ru.yandex.tasktreker.handler;

import com.sun.net.httpserver.HttpExchange;
import ru.yandex.tasktreker.model.Epic;
import ru.yandex.tasktreker.service.TaskManager;

import java.io.IOException;
import java.util.regex.Pattern;

public class EpicHandler extends BaseHttpHandler {

    public EpicHandler(TaskManager taskManager) {
        super(taskManager);
    }

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        try {
            String path = httpExchange.getRequestURI().getPath();
            String requestMethod = httpExchange.getRequestMethod();

            switch (requestMethod) {
                case "GET": {
                    if (Pattern.matches("^/epics$", path)) {
                        String response = gson.toJson(taskManager.getEpics());
                        sendText(httpExchange, response);
                    } else if (Pattern.matches("^/epics/\\d+$", path)) {
                        String pathId = path.replaceFirst("/epics/", "");
                        int id = parsePathId(pathId);
                        if (id != -1) {
                            String response = gson.toJson(taskManager.getEpicById(id));
                            sendText(httpExchange, response);
                        } else {
                            sendNotFound(httpExchange);
                        }
                    } else if (Pattern.matches("^/epics/\\d+/subtasks$", path)) {
                        String pathId = path.replaceFirst("/epics/", "").
                                replaceFirst("/subtasks", "");
                        int id = parsePathId(pathId);
                        if (id != -1) {
                            String response = gson.toJson(taskManager.getAllSubtasksForEpicId(id));
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
                    Epic epic = gson.fromJson(requestBody, Epic.class);
                    if (epic.getId() == 0) {
                        try {
                            taskManager.createEpic(epic);
                            httpExchange.sendResponseHeaders(201, 0);
                            httpExchange.getResponseBody().write(("Epic была добавлена: " + epic.getId()).getBytes());
                        } catch (RuntimeException e) {
                            sendHasInteractions(httpExchange);
                        }
                    } else {
                        httpExchange.sendResponseHeaders(400, 0);
                        httpExchange.getResponseBody().write("Нельзя обновлять Epic с существующим id".getBytes());
                    }
                    httpExchange.close();
                    break;
                }
                case "DELETE": {
                    String pathId = path.replaceFirst("/epics/", "");
                    int id = parsePathId(pathId);
                    if (id != -1) {
                        taskManager.deleteEpicById(id);
                        sendText(httpExchange, "Epic удален");
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