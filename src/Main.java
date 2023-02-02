import Manager.TaskManager;
import Tasks.EpicTask;
import Tasks.SimpleTask;
import Tasks.SubTask;

public class Main {
    public static void main(String[] args) {
        TaskManager taskManager = new TaskManager();
        SimpleTask simpleTask1 = new SimpleTask("Помыть кота", "Кот грязный и его нужно помыть", "NEW");
        SimpleTask simpleTask2 = new SimpleTask("Помыть пса", "Пес грязный и его нужно помыть", "NEW");

        EpicTask epicTask1 = new EpicTask("Слон", "Убрать слона в холодильник", "NEW");
        EpicTask epicTask2 = new EpicTask("Ремонт", "Сделать ремонт в квартире", "NEW");
        SubTask subTask1 = new SubTask("Открыть", "Открыть дверцу холодильника", "NEW");
        SubTask subTask2 = new SubTask("Положить", "Положить слона в холодильник", "NEW");
        SubTask subTask3 = new SubTask("Закрыть", "Закрыть дверцу холодильника", "NEW");

        SubTask subTask = new SubTask("Стены", "Поклеить новые обои", "NEW");

        taskManager.createSimpleTask(simpleTask1);
        taskManager.createSimpleTask(simpleTask2);

        taskManager.createEpicTask(epicTask1);
        taskManager.createEpicTask(epicTask2);

        taskManager.createSubTask(3, subTask1);
        taskManager.createSubTask(3, subTask2);
        taskManager.createSubTask(3, subTask3);
        taskManager.createSubTask(4, subTask);

        System.out.println(taskManager.getEpicTaskList());
        System.out.println(taskManager.getSimpleTaskList());

        System.out.println(taskManager.getSimpleTaskById(1));
        SimpleTask simpleTask3 = new SimpleTask("Сходить в магазин", "Закончился хлеб и молоко", "NEW");
        taskManager.createSimpleTask(simpleTask3);
        taskManager.updateSimpleTask(1, simpleTask3);

        System.out.println(taskManager.getSimpleTaskList());
        System.out.println(taskManager.getEpicTaskList());

        System.out.println(taskManager.getSimpleTaskById(1));
        System.out.println(taskManager.getSimpleTaskById(2));
        System.out.println(taskManager.getEpicTaskById(3));
        System.out.println(taskManager.getEpicTaskById(4));
        System.out.println(taskManager.getSubTaskById(5));
        System.out.println(taskManager.getSubTaskById(6));
        System.out.println(taskManager.getSubTaskById(7));
        System.out.println(taskManager.getSubTaskById(8));

        System.out.println(taskManager.getSubTaskList(epicTask1));

        taskManager.removeTaskById(4);
        taskManager.removeTaskById(1);
        SubTask newSubTask = new SubTask("Открыть", "открыть дверцу", "IN_PROGRESS");
        taskManager.updateSubTask(5, newSubTask);


        System.out.println(taskManager.getSimpleTaskById(1));
        System.out.println(taskManager.getSimpleTaskById(2));
        System.out.println(taskManager.getEpicTaskById(3));
        System.out.println(taskManager.getEpicTaskById(4));
        System.out.println(taskManager.getSubTaskById(5));
        System.out.println(taskManager.getSimpleTaskById(6));
        System.out.println(taskManager.getSubTaskById(7));
        System.out.println(taskManager.getSubTaskById(8));

        System.out.println(taskManager.getSimpleTaskList());
        System.out.println(taskManager.getEpicTaskList());

        taskManager.removeTaskById(10);

        System.out.println(taskManager.getEpicTaskList());
        System.out.println(taskManager.getSimpleTaskList());

    }
}
