public class Main {
    public static void main(String[] args) {
        TaskManager taskManager = new TaskManager();
        SimpleTask simpleTask1 = new SimpleTask("Помыть кота", "Кот грязный и его нужно помыть");
        SimpleTask simpleTask2 = new SimpleTask("Помыть пса", "Пес грязный и его нужно помыть");

        EpicTask epicTask1 = new EpicTask("Слон", "Убрать слона в холодильник");
        EpicTask epicTask2 = new EpicTask("Ремонт", "Сделать ремонт в квартире");

        SubTask subTask1 = new SubTask("Открыть", "Открыть дверцу холодильника");
        SubTask subTask2 = new SubTask("Положить", "Положить слона в холодильник");
        SubTask subTask3 = new SubTask("Закрыть", "Закрыть дверцу холодильника");

        SubTask subTask = new SubTask("Стены", "Поклеить новые обои");

        taskManager.createSimpleTask(simpleTask1);
        taskManager.createSimpleTask(simpleTask2);

        taskManager.createEpicTask(epicTask1);
        taskManager.createEpicTask(epicTask2);

        taskManager.createSubTask(3, subTask1);
        taskManager.createSubTask(3, subTask2);
        taskManager.createSubTask(3, subTask3);

        taskManager.createSubTask(4, subTask);

        System.out.println(taskManager.getTaskList());

        System.out.println(taskManager.getTaskById(1));
        SimpleTask simpleTask3 = new SimpleTask("Сходить в магазин", "Закончился хлеб и молоко");
        taskManager.createSimpleTask(simpleTask3);
        taskManager.updateTaskById(1, simpleTask3);

        taskManager.updateStatusSimpleTask(simpleTask3);
        taskManager.updateStatusSubTask(subTask1);
        taskManager.updateStatusEpicTask(epicTask1);
        taskManager.updateStatusEpicTask(epicTask2);

        System.out.println(taskManager.getTaskList());

        System.out.println(taskManager.getTaskById(1));
        System.out.println(taskManager.getTaskById(2));
        System.out.println(taskManager.getTaskById(3));
        System.out.println(taskManager.getTaskById(4));
        System.out.println(taskManager.getTaskById(5));
        System.out.println(taskManager.getTaskById(6));
        System.out.println(taskManager.getTaskById(7));
        System.out.println(taskManager.getTaskById(8));

        System.out.println(taskManager.getSubTaskList(epicTask1));

        taskManager.removeTaskById(3);
        taskManager.removeTaskById(1);
        taskManager.removeTaskById(8);
        taskManager.updateStatusEpicTask(epicTask2);

        System.out.println(taskManager.getTaskById(1));
        System.out.println(taskManager.getTaskById(2));
        System.out.println(taskManager.getTaskById(3));
        System.out.println(taskManager.getTaskById(4));
        System.out.println(taskManager.getTaskById(5));
        System.out.println(taskManager.getTaskById(6));
        System.out.println(taskManager.getTaskById(7));
        System.out.println(taskManager.getTaskById(8));

        System.out.println(taskManager.getTaskList());

        taskManager.removeAllTask();

        System.out.println(taskManager.getTaskList());
    }
}
