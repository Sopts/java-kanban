package ru.yandex.tasktreker.handler;

import com.sun.net.httpserver.HttpExchange;
import ru.yandex.tasktreker.service.TaskManager;

import java.io.IOException;

public class HistoryHandler extends BaseHttpHandler {

    public HistoryHandler(TaskManager taskManager) {
        super(taskManager);
    }

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        try {
            String path = httpExchange.getRequestURI().getPath();
            String requestMethod = httpExchange.getRequestMethod();

            if (requestMethod.equals("GET")) {
                if (path.equals("/history")) {
                    String response = gson.toJson(taskManager.getHistory());
                    sendText(httpExchange, response);
                    httpExchange.sendResponseHeaders(200, response.length());
                } else {
                    httpExchange.sendResponseHeaders(405, 0);
                }
            } else {
                httpExchange.sendResponseHeaders(405, 0);
            }
        } catch (Exception e) {
            httpExchange.sendResponseHeaders(500, 0);
        }
    }
}

