import ru.yandex.tasktreker.model.Epic;
import ru.yandex.tasktreker.model.Subtask;
import ru.yandex.tasktreker.model.Task;
import ru.yandex.tasktreker.service.Managers;
import ru.yandex.tasktreker.service.TaskManager;


public class Main {
    public static void main(String[] args) {
        TaskManager taskManager = Managers.getDefault();

        Task task1 = new Task("Task1", "Description");
        taskManager.createTask(task1);
        Task task2 = new Task("Task2", "Description");
        taskManager.createTask(task2);

        Epic epic1 = new Epic("Epic1", "Description");
        taskManager.createEpic(epic1);
        Subtask subtask1 = new Subtask("Subtask1", "Description", epic1.getId());
        taskManager.createSubtask(subtask1);
        Subtask subtask2 = new Subtask("Subtask2", "Description", epic1.getId());
        taskManager.createSubtask(subtask2);
        Subtask subtask3 = new Subtask("Subtask3", "Description", epic1.getId());
        taskManager.createSubtask(subtask3);

        Epic epic2 = new Epic("Epic2", "Description");
        taskManager.createEpic(epic2);

        System.out.println("Получение истории:");
        taskManager.getEpicById(epic2.getId());
        taskManager.getTaskById(task2.getId());
        taskManager.getTaskById(task1.getId());
        taskManager.getEpicById(epic1.getId());
        taskManager.getSubtaskById(subtask3.getId());
        taskManager.getSubtaskById(subtask2.getId());
        taskManager.getSubtaskById(subtask1.getId());
        for (Task task : taskManager.getHistory()) {
            System.out.println(task);
        }
        System.out.println();


        System.out.println("Вызвали еще раз Task 1 и Subtask 2:");
        taskManager.getTaskById(task1.getId());
        taskManager.getSubtaskById(subtask2.getId());
        for (Task task : taskManager.getHistory()) {
            System.out.println(task);
        }
        System.out.println();


        System.out.println("Удаление Subtask 1:");
        taskManager.deleteSubtaskById(subtask1.getId());
        for (Task task : taskManager.getHistory()) {
            System.out.println(task);
        }
        System.out.println();


        System.out.println("Удаление Epic 1:");
        taskManager.deleteEpicById(epic1.getId());
        for (Task task : taskManager.getHistory()) {
            System.out.println(task);
        }
        System.out.println();


        System.out.println("Удаление всех Tasks:");
        taskManager.deleteAllTask();
        for (Task task : taskManager.getHistory()) {
            System.out.println(task);
        }
        System.out.println();

    }

}