package ru.yandex.tasktreker.service;

public class Managers {

    public static TaskManager getDefault() {
        return new InMemoryTaskManager(getDefaultHistory());
    }

    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }

    public static FileBackedTaskManager getDefaultFile(HistoryManager inMemoryHistoryManager) {
        return new FileBackedTaskManager(inMemoryHistoryManager);
    }

}
