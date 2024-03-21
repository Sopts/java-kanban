import ru.yandex.tasktreker.model.Epic;
import ru.yandex.tasktreker.model.Status;
import ru.yandex.tasktreker.model.Subtask;
import ru.yandex.tasktreker.model.Task;
import ru.yandex.tasktreker.service.TaskManager;


public class Main {
    public static void main(String[] args) {
        TaskManager taskManager = new TaskManager();

        Task task1 = new Task("Уборка", "Помыть полы");
        taskManager.createTask(task1);
        Task task2 = new Task("Готовка", "Сварить суп");
        taskManager.createTask(task2);

        Epic epic1 = new Epic("Переезд", "Переехать в новую квартиру");
        taskManager.createEpic(epic1);
        Subtask subtask1 = new Subtask("Взять все вещи", "Собрать вещи", epic1.getId());
        taskManager.createSubtask(subtask1);
        Subtask subtask2 = new Subtask("Взять все деньги", "Собрать деньги", epic1.getId());
        taskManager.createSubtask(subtask2);

        Epic epic2 = new Epic("Магазин", "Сходить в магазин");
        taskManager.createEpic(epic2);
        Subtask subtask3 = new Subtask("Список продуктов", "Написать список продуктов", epic2.getId());
        taskManager.createSubtask(subtask3);

        System.out.println("Добавление в базу:");
        System.out.println("Tasks:");
        System.out.println(taskManager.getTasks());
        System.out.println("Epics:");
        System.out.println(taskManager.getEpics());
        System.out.println("Subtasks:");
        System.out.println(taskManager.getSubtasks());

        System.out.println();

        System.out.println("Обновление статусов:");
        taskManager.updateTask(task1, Status.DONE);
        System.out.println("Task 1: " + task1.getStatus());
        taskManager.updateSubtask(subtask3, Status.DONE);
        System.out.println("Subtask 3: " + subtask3.getStatus());
        System.out.println("Epic 2:" + epic2.getStatus());
        taskManager.updateSubtask(subtask1, Status.IN_PROGRESS);
        System.out.println("Subtask 1: " + subtask1.getStatus());
        System.out.println("Epic 1:" + epic1.getStatus());

        System.out.println();
        System.out.println("Добавляем Subtask 4 к Epic 1 и проверяем изменение статуса Epic 1");
        Subtask subtask4 = new Subtask("Перевезти мебель", "Упаковать мебель", epic1.getId());
        taskManager.createSubtask(subtask4);
        System.out.println("Epic 1:" + epic1.getStatus());

        System.out.println();

        System.out.println("Текущая база Epics:");
        System.out.println(taskManager.getEpics());
        System.out.println("Текущая база Subtasks:");
        System.out.println(taskManager.getSubtasks());

        System.out.println();

        System.out.println("Удаляем Subtask2:");
        taskManager.deleteSubtaskById(subtask2.getId());
        System.out.println("Текущая база Subtask:");
        System.out.println(taskManager.getSubtasks());

        System.out.println();

        System.out.println("Удаляем Epic1:");
        taskManager.deleteEpicById(epic1.getId());
        System.out.println("Текущая база Epics:");
        System.out.println(taskManager.getEpics());
        System.out.println("Текущая база Subtasks:");
        System.out.println(taskManager.getSubtasks());

        System.out.println();

        System.out.println("Текущая база Task:");
        System.out.println(taskManager.getTasks());
        System.out.println("Чистим базу");
        taskManager.deleteAllTask();
        System.out.println("Текущая база Task:");
        System.out.println(taskManager.getTasks());

    }

}