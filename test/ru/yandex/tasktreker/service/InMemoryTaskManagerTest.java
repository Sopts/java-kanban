package ru.yandex.tasktreker.service;

import org.junit.jupiter.api.Test;
import ru.yandex.tasktreker.model.Epic;
import ru.yandex.tasktreker.model.Status;
import ru.yandex.tasktreker.model.Task;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

class InMemoryTaskManagerTest {

    TaskManager taskManager = Managers.getDefault();

    /* согласно ТЗ данные тесты не проверялись:
    проверьте, что объект Epic нельзя добавить в самого себя в виде подзадачи;
    проверьте, что объект Subtask нельзя сделать своим же эпиком;
    ввиду того, что в параметрах метода уже указан тип объекта Epic или Subtask,
    соотвественно код при неправильных типах нескомпилируется.
    Данный вопрос обсуждался в пачке.
    Также не проверялся тест:
    проверьте, что задачи с заданным id и сгенерированным id не конфликтуют внутри менеджера;
    т.к. по коду id всегда генерируется.
    */

    @Test
    public void shouldTasksEqualsByIdAndEqualsWhenAddedToManager() {
        Task task = new Task("Task", "Description");
        Epic epic = new Epic("Epic", "Description");

        taskManager.createTask(task);
        int taskId = task.getId();

        taskManager.createEpic(epic);
        int epicId = epic.getId();

        Task expectedTask = new Task(task.getName(), task.getDescription());
        expectedTask.setId(taskId);

        Task actualTask = taskManager.getTaskById(taskId);

        Epic expectedEpic = new Epic(epic.getName(), epic.getDescription());
        expectedEpic.setId(epicId);

        Epic actualEpic = taskManager.getEpicById(epicId);

        assertEquals(expectedTask, actualTask);
        assertEquals(expectedEpic, actualEpic);
    }

    @Test
    public void shouldSavePreviousVersionTaskOfHistory() {
        Task task = new Task("Task", "Description");

        taskManager.createTask(task);
        taskManager.getTaskById(task.getId());

        taskManager.updateTask(task, Status.DONE);
        taskManager.getTaskById(task.getId());

        assertEquals(2, taskManager.getHistory().size());
        assertNotEquals(taskManager.getHistory().get(0), taskManager.getHistory().get(1));
    }

}