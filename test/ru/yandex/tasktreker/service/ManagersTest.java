package ru.yandex.tasktreker.service;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class ManagersTest {

    @Test
    public void shouldBeGetDefault() {
        TaskManager taskManager = Managers.getDefault();

        assertNotNull(taskManager);
    }

    @Test
    public void shouldBeGetDefaultHistory() {
        HistoryManager historyManager = Managers.getDefaultHistory();

        assertNotNull(historyManager);
    }
}