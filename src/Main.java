import ru.yandex.tasktreker.model.Epic;
import ru.yandex.tasktreker.model.Status;
import ru.yandex.tasktreker.model.Subtask;
import ru.yandex.tasktreker.model.Task;
import ru.yandex.tasktreker.service.TaskManager;


public class Main {
    public static void main(String[] args) {
        TaskManager taskManager = new TaskManager();

        Task task1 = new Task("Уборка", "Помыть полы");
        Task task2 = new Task("Готовка", "Сварить суп");

        Epic epic1 = new Epic("Переезд", "Переехать в новую квартиру");
        Subtask subtask1 = new Subtask("Взять все вещи", "Собрать вещи", epic1);
        Subtask subtask2 = new Subtask("Взять все деньги", "Собрать деньги", epic1);


        Epic epic2 = new Epic("Магазин", "Сходить в магазин");
        Subtask subtask3 = new Subtask("Список продуктов", "Написать список продуктов", epic2);

        taskManager.createTask(task1);
        taskManager.createTask(task2);
        taskManager.createTask(epic1);
        taskManager.createTask(subtask1);
        taskManager.createTask(subtask2);
        taskManager.createTask(epic2);
        taskManager.createTask(subtask3);

        System.out.println("Tasks:");
        System.out.println(task1);
        System.out.println(task2);

        System.out.println("Epics:");
        System.out.println(epic1);
        System.out.println(epic2);

        System.out.println("Subtasks:");
        System.out.println(subtask1);
        System.out.println(subtask2);
        System.out.println(subtask3);

        taskManager.updateTask(task1, Status.DONE);
        taskManager.updateTask(subtask3, Status.DONE);

        System.out.println("Updated statuses:");
        System.out.println("Task 1: " + task1.getStatus());
        System.out.println("Subtask 3: " + subtask3.getStatus());

        taskManager.deleteTaskById(epic1.getId());

        System.out.println("Deleted subtasks from epics:");
        System.out.println(epic1);
    }

}