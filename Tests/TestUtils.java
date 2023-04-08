import Manager.TaskManager;

public class TestUtils {
    public static void createSubtaskOnEpic(TaskManager manager){

        manager.createSubTask(TaskManagerTest.subTask1);
        manager.createSubTask(TaskManagerTest.subTask2);
        manager.createSubTask(TaskManagerTest.subTask3);
    }
}
