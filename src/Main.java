import Manager.InMemoryHistoryManager;
import Manager.InMemoryTaskManager;
import Tasks.EpicTask;
import Tasks.SimpleTask;
import Tasks.SubTask;

public class Main {
    public static void main(String[] args) {

        InMemoryHistoryManager memoryHistory = new InMemoryHistoryManager();

        InMemoryTaskManager taskManager = new InMemoryTaskManager();
        SimpleTask simpleTask1 = new SimpleTask("Помыть кота", "Кот грязный и его нужно помыть", "NEW");
        SimpleTask simpleTask2 = new SimpleTask("Помыть пса", "Пес грязный и его нужно помыть", "NEW");

        EpicTask epicTask1 = new EpicTask("Слон", "Убрать слона в холодильник", "NEW");
        EpicTask epicTask2 = new EpicTask("Ремонт", "Сделать ремонт в квартире", "NEW");
        SubTask subTask1 = new SubTask(3 ,"Открыть", "Открыть дверцу холодильника", "NEW");
        SubTask subTask2 = new SubTask(3 ,"Положить", "Положить слона в холодильник", "NEW");
        SubTask subTask3 = new SubTask(3 ,"Закрыть", "Закрыть дверцу холодильника", "NEW");

        SubTask subTask = new SubTask(4 ,"Стены", "Поклеить новые обои", "NEW");

        taskManager.createSimpleTask(simpleTask1);
        taskManager.createSimpleTask(simpleTask2);

        taskManager.createEpicTask(epicTask1);
        taskManager.createEpicTask(epicTask2);

        taskManager.createSubTask(subTask1);
        taskManager.createSubTask(subTask2);
        taskManager.createSubTask(subTask3);
        taskManager.createSubTask(subTask);

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
        System.out.println(taskManager.getSubTaskById(8));

        System.out.println(memoryHistory.getHistory().size());

        System.out.println(taskManager.getSimpleTaskById(1));
        System.out.println(taskManager.getSimpleTaskById(2));
        System.out.println(taskManager.getEpicTaskById(3));
        System.out.println(taskManager.getEpicTaskById(4));

        System.out.println(memoryHistory.getHistory().size());

        System.out.println(taskManager.getEpicTaskById(4));
        System.out.println(taskManager.getSubTaskById(5));
        System.out.println(taskManager.getSubTaskById(6));
        System.out.println(taskManager.getSubTaskById(7));

        System.out.println(memoryHistory.getHistory().size());




        System.out.println(taskManager.getSubTaskList(epicTask1));

        taskManager.removeTaskById(4);
        taskManager.removeTaskById(1);
        SubTask newSubTask = new SubTask(3, "Открыть", "открыть дверцу", "IN_PROGRESS");
        taskManager.updateSubTask(5, newSubTask);


        System.out.println(taskManager.getSimpleTaskById(1));
        System.out.println(taskManager.getSimpleTaskById(2));
        System.out.println(taskManager.getEpicTaskById(3));
        System.out.println(taskManager.getEpicTaskById(4));
        System.out.println(taskManager.getSubTaskById(5));
        System.out.println(taskManager.getSimpleTaskById(6));
        System.out.println(taskManager.getSubTaskById(7));
        System.out.println(taskManager.getSubTaskById(8));

        memoryHistory.getHistory();

        System.out.println(taskManager.getSimpleTaskList());
        System.out.println(taskManager.getEpicTaskList());

        taskManager.removeTaskById(10);

        System.out.println(taskManager.getEpicTaskList());
        System.out.println(taskManager.getSimpleTaskList());

    }
}
