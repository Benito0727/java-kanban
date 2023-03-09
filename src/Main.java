import Manager.InMemoryTaskManager;
import Tasks.*;

public class Main {
    public static void main(String[] args) {



        InMemoryTaskManager taskManager = new InMemoryTaskManager();




        EpicTask epicTask1 = new EpicTask("Слон", "Убрать слона в холодильник", TaskStatus.NEW);
        EpicTask epicTask2 = new EpicTask("Ремонт", "Сделать ремонт в квартире", TaskStatus.NEW);
        SubTask subTask1 = new SubTask(1 ,"Открыть", "Открыть дверцу холодильника", TaskStatus.NEW);
        SubTask subTask2 = new SubTask(1 ,"Положить", "Положить слона в холодильник", TaskStatus.NEW);
        SubTask subTask3 = new SubTask(1 ,"Закрыть", "Закрыть дверцу холодильника", TaskStatus.NEW);

        taskManager.createEpicTask(epicTask1);
        taskManager.createEpicTask(epicTask2);

        taskManager.createSubTask(subTask1);
        taskManager.createSubTask(subTask2);
        taskManager.createSubTask(subTask3);

        taskManager.getSubTaskById(3);
        taskManager.getSubTaskById(4);
        taskManager.getSubTaskById(5);

        System.out.println(taskManager.getHistory().size());
        for (Task task : taskManager.getHistory()) {
            System.out.println(task);
        }

        taskManager.getSubTaskById(5);
        taskManager.getSubTaskById(3);
        taskManager.getSubTaskById(4);

        System.out.println(":::::::::");
        for (Task task : taskManager.getHistory()) {
            System.out.println(task);
        }

        taskManager.getSubTaskById(5);

        System.out.println(taskManager.getListOfTaskNames());


        System.out.println("______");

        for (Task task : taskManager.getHistory()) {
            System.out.println(task);
        }

    }

    public static void test1(){
        //        SimpleTask simpleTask1 = new SimpleTask("Помыть кота", "Кот грязный и его нужно помыть", TaskStatus.NEW);
//        SimpleTask simpleTask2 = new SimpleTask("Помыть пса", "Пес грязный и его нужно помыть", TaskStatus.NEW);
//
//        EpicTask epicTask1 = new EpicTask("Слон", "Убрать слона в холодильник", TaskStatus.NEW);
//        EpicTask epicTask2 = new EpicTask("Ремонт", "Сделать ремонт в квартире", TaskStatus.NEW);
//        SubTask subTask1 = new SubTask(3 ,"Открыть", "Открыть дверцу холодильника", TaskStatus.NEW);
//        SubTask subTask2 = new SubTask(3 ,"Положить", "Положить слона в холодильник", TaskStatus.NEW);
//        SubTask subTask3 = new SubTask(3 ,"Закрыть", "Закрыть дверцу холодильника", TaskStatus.NEW);
//
//        SubTask subTask = new SubTask(4 ,"Стены", "Поклеить новые обои", TaskStatus.NEW);
//
//        taskManager.createSimpleTask(simpleTask1);
//        taskManager.createSimpleTask(simpleTask2);
//
//        taskManager.createEpicTask(epicTask1);
//        taskManager.createEpicTask(epicTask2);
//
//        taskManager.createSubTask(subTask1);
//        taskManager.createSubTask(subTask2);
//        taskManager.createSubTask(subTask3);
//        taskManager.createSubTask(subTask);
//
//        System.out.println(taskManager.getEpicTaskList());
//        System.out.println(taskManager.getSimpleTaskList());
//
//        System.out.println(taskManager.getSimpleTaskById(1));
//        SimpleTask simpleTask3 = new SimpleTask("Сходить в магазин", "Закончился хлеб и молоко", TaskStatus.NEW);
//        taskManager.createSimpleTask(simpleTask3);
//        taskManager.updateSimpleTask(1, simpleTask3);
//
//        System.out.println(taskManager.getSimpleTaskList());
//        System.out.println(taskManager.getEpicTaskList());
//
//        System.out.println(taskManager.getSimpleTaskById(1));
//        System.out.println(taskManager.getSimpleTaskById(2));
//        System.out.println(taskManager.getEpicTaskById(3));
//        System.out.println(taskManager.getEpicTaskById(4));
//        System.out.println(taskManager.getSubTaskById(5));
//        System.out.println(taskManager.getSubTaskById(8));
//
//        System.out.println(taskManager.getHistory().size());
//
//        System.out.println(taskManager.getSimpleTaskById(1));
//        System.out.println(taskManager.getSimpleTaskById(2));
//        System.out.println(taskManager.getEpicTaskById(3));
//        System.out.println(taskManager.getEpicTaskById(4));
//
//        System.out.println(taskManager.getHistory().size());
//
//        System.out.println(taskManager.getEpicTaskById(4));
//        System.out.println(taskManager.getSubTaskById(5));
//        System.out.println(taskManager.getSubTaskById(6));
//        System.out.println(taskManager.getSubTaskById(7));
//
//
//        System.out.println(taskManager.getHistory().size());
//        System.out.println(taskManager.getSubTaskList(epicTask1));
//
//        taskManager.removeTaskById(4);
//        taskManager.removeTaskById(1);
//        SubTask newSubTask = new SubTask(3, "Открыть", "открыть дверцу", TaskStatus.IN_PROGRESS);
//        taskManager.updateSubTask(5, newSubTask);
//
//
//        System.out.println(taskManager.getSimpleTaskById(1));
//        System.out.println(taskManager.getSimpleTaskById(2));
//        System.out.println(taskManager.getEpicTaskById(3));
//        System.out.println(taskManager.getEpicTaskById(4));
//        System.out.println(taskManager.getSubTaskById(5));
//        System.out.println(taskManager.getSimpleTaskById(6));
//        System.out.println(taskManager.getSubTaskById(7));
//        System.out.println(taskManager.getSubTaskById(8));
//
//
//
//        System.out.println("--------");
//        for (Task task : taskManager.getHistory()) {
//            System.out.println(task.getIndex());
//            System.out.println(task);
//        }
//        System.out.println("--------");
//        System.out.println(taskManager.getHistory().size());
//
//
//        System.out.println(taskManager.getHistory());
//        System.out.println(taskManager.getHistory().size());
//        System.out.println(taskManager.getSimpleTaskList());
//        System.out.println(taskManager.getEpicTaskList());
//
//        taskManager.removeTaskById(10);
//
//        System.out.println(taskManager.getEpicTaskList());
//        System.out.println(taskManager.getSimpleTaskList());
    }
}
