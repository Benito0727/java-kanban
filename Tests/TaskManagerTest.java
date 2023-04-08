import Manager.TaskManager;
import Tasks.*;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

public abstract class TaskManagerTest<T extends TaskManager> {

    T manager;

    static EpicTask epicTask;
    static SimpleTask simpleTask;
    static SubTask subTask1;
    static SubTask subTask2;
    static SubTask subTask3;

    @BeforeEach
    @Test
    public void createTaskStack() {
        epicTask = new EpicTask("name", "description", TaskStatus.NEW);

        LocalDateTime simpleTime = LocalDateTime.of(2023, 4, 7, 8, 0);
        simpleTask = new SimpleTask("name", "description", TaskStatus.NEW,
                simpleTime, 10);

        LocalDateTime subTime1 = LocalDateTime.of(2023, 4, 7, 8, 20);
        subTask1 = new SubTask(1, "name", "description", TaskStatus.NEW,
                subTime1, 10);
        LocalDateTime subTime2 = LocalDateTime.of(2023, 4, 7, 8, 40);
        subTask2 = new SubTask(1, "name", "description", TaskStatus.NEW,
                subTime2, 10);
        LocalDateTime subTime3 = LocalDateTime.of(2023, 4, 7, 10, 30);
        subTask3 = new SubTask(1, "name", "description", TaskStatus.NEW,
                subTime3, 50);
    }




    @Test
    public void shouldCreateEpicTask() {
        manager.createEpicTask(epicTask);
        assertNotNull(manager.getEpicTaskById(1));
        assertEquals(TaskStatus.NEW, manager.getEpicTaskById(1).getStatus());
    }
    @Test
    public void shouldCreateSimpleTask() {
        manager.createSimpleTask(simpleTask);
        assertNotNull(manager.getSimpleTaskById(1));
    }
    @Test
    public void shouldCreateSubTask(){
        manager.createEpicTask(epicTask);
        manager.createSubTask(subTask1);
        assertNotNull(manager.getSubTaskById(2));
    }
    @Test
    public void shouldGetSimpleTaskById() {
        manager.createSimpleTask(simpleTask);
        assertEquals(simpleTask, manager.getSimpleTaskById(1));
    }
    @Test
    public void shouldGetSubTaskById() {
        manager.createEpicTask(epicTask);
        manager.createSubTask(subTask1);
        assertEquals(subTask1, manager.getSubTaskById(2));
    }
    @Test
    public void shouldGetEpicTaskById() {
        manager.createEpicTask(epicTask);
        assertEquals(epicTask, manager.getEpicTaskById(1));
    }
    @Test
    public void shouldUpdateStatusEpicTask() {
        manager.createEpicTask(epicTask);
        manager.createSubTask(subTask1);
        manager.createSubTask(subTask2);
        manager.createSubTask(subTask3);

        assertEquals(TaskStatus.NEW, manager.getEpicTaskById(1).getStatus());
    }
    @Test
    public void shouldUpdateSimpleTask() {
        manager.createSimpleTask(simpleTask);
        LocalDateTime time = LocalDateTime.of(2023, 4, 8, 12, 0);
        SimpleTask simpleTask2 = new SimpleTask("otherTitle", "otherDescription", TaskStatus.NEW,
        time, 60);
        manager.updateSimpleTask(1, simpleTask2);
        assertEquals(manager.getSimpleTaskById(1), simpleTask2);
    }
    @Test
    public void shouldUpdateSubTask() {
        manager.createEpicTask(epicTask);
        manager.createSubTask(subTask1);
        LocalDateTime time = LocalDateTime.of(2023, 4, 7, 9, 40);
        SubTask otherSubtask = new SubTask(1, "otherTitle",
                "otherDescription",
                TaskStatus.IN_PROGRESS, time, 30);
        manager.updateSubTask(2, otherSubtask);
        assertEquals(manager.getSubTaskById(2), otherSubtask);
    }
    @Test
    public void shouldUpdateEpicTask() {
        manager.createEpicTask(epicTask);
        EpicTask epicTask2 = new EpicTask("otherTitle", "otherDescription", TaskStatus.NEW);
        manager.updateEpicTask(1, epicTask2);
        assertEquals(manager.getEpicTaskById(1), epicTask2);
    }
    @Test
    public void shouldRemoveTaskById() {
        manager.createSimpleTask(simpleTask);
        manager.removeTaskById(1);
        NullPointerException exception = assertThrows(
                NullPointerException.class,
                () -> manager.getSimpleTaskById(1)
        );
        assertEquals("Нет задачи с таким номером", exception.getMessage());
    }
    @Test
    public void shouldRemoveAllTask(){
        manager.createEpicTask(epicTask);
        manager.createSimpleTask(simpleTask);
        manager.createSubTask(subTask1);
        manager.createSubTask(subTask2);
        manager.createSubTask(subTask3);

        manager.removeAllTask();

        NullPointerException exception = assertThrows(
                NullPointerException.class,
                () -> manager.getEpicTaskById(1)
        );
        assertEquals("Нет задачи с таким номером", exception.getMessage());
    }
    @Test
    public void shouldCleanSimpleTasks() {
        manager.createSimpleTask(simpleTask);
        manager.cleanSimpleTasks();
        NullPointerException exception = assertThrows(
                NullPointerException.class,
                () -> manager.getSimpleTaskById(1)
        );
        assertEquals("Нет задачи с таким номером", exception.getMessage());
    }
    @Test
    public void shouldCleanEpicTasks() {
        manager.createEpicTask(epicTask);
        TestUtils.createSubtaskOnEpic(manager);

        manager.cleanEpicTasks();
        NullPointerException exception = assertThrows(
                NullPointerException.class,
                () -> manager.getSimpleTaskById(1)
        );
        assertEquals("Нет задачи с таким номером", exception.getMessage());
    }
    @Test
    public void shouldCleanSubTasks() {
        manager.createEpicTask(epicTask);
        TestUtils.createSubtaskOnEpic(manager);
        manager.cleanSubTasks();

        NullPointerException exception = assertThrows(
                NullPointerException.class,
                () -> manager.getSubTaskById(2)
        );
        assertEquals("Нет задачи с таким номером", exception.getMessage());
    }
    @Test
    public void shouldGetListOfTaskNames() {
        manager.createSimpleTask(simpleTask);
        assertFalse(manager.getListOfTaskNames().isEmpty());
    }
    @Test
    public void shouldGetSubTaskList() {
        manager.createEpicTask(epicTask);
        TestUtils.createSubtaskOnEpic(manager);
        assertFalse(manager.getSubTaskList(epicTask).isEmpty());
    }
    @Test
    public void shouldGetSimpleTaskList() {
        manager.createSimpleTask(simpleTask);
        assertFalse(manager.getSimpleTaskList().isEmpty());
    }
    @Test
    public void shouldGetEpicTaskList() {
        manager.createEpicTask(epicTask);
        assertFalse(manager.getEpicTaskList().isEmpty());
    }
    @Test
    public void shouldGetHistory() {
        manager.createEpicTask(epicTask);
        TestUtils.createSubtaskOnEpic(manager);
        manager.getEpicTaskById(1);
        manager.getSubTaskById(3);
        assertFalse(manager.getHistory().isEmpty());
    }

    @Test
    public void shouldReturnNullIfCreateEpicTaskOfNullObject(){
        NullPointerException exception = assertThrows(
                NullPointerException.class,
                () -> manager.createEpicTask(null)
        );
        assertEquals("Ошибка создания задачи", exception.getMessage());
    }

    @Test
    public void shouldGetExceptionIfTaskNotCreate(){
        NullPointerException exception = assertThrows(
                NullPointerException.class,
                () -> manager.getSimpleTaskById(0)
        );
        assertEquals("Нет задачи с таким номером", exception.getMessage());
    }

    @Test
    public void shouldReturnExceptionIfEpicForSubtaskNotCreate(){
        NullPointerException exception = assertThrows(
                NullPointerException.class,
                () -> manager.createSubTask(subTask1)
        );
        assertEquals("Ошибка создания подзадачи", exception.getMessage());
    }

    @Test
    public void shouldReturnExceptionIfEpicNotCreate(){
        NullPointerException exception = assertThrows(
                NullPointerException.class,
                () -> manager.getEpicTaskById(0)
        );
        assertEquals("Нет задачи с таким номером", exception.getMessage());
    }

    @Test
    public void shouldUpdateStatusEpicTaskIfSubtaskStatusInProgress(){
        manager.createEpicTask(epicTask);
        manager.createSubTask(subTask1);
        manager.createSubTask(subTask2);
        subTask3.setStatus(TaskStatus.IN_PROGRESS);
        manager.createSubTask(subTask3);

        assertEquals(TaskStatus.IN_PROGRESS, manager.getEpicTaskById(1).getStatus());
    }

    @Test
    public void shouldUpdateStatusEpicTaskIfSubtaskStatusInProgressOrDone(){
        manager.createEpicTask(epicTask);
        subTask1.setStatus(TaskStatus.IN_PROGRESS);
        subTask2.setStatus(TaskStatus.DONE);
        subTask3.setStatus(TaskStatus.DONE);
        TestUtils.createSubtaskOnEpic(manager);

        assertEquals(TaskStatus.IN_PROGRESS, manager.getEpicTaskById(1).getStatus());
    }

    @Test
    public void shouldUpdateStatusEpicTaskIfAllSubtaskStatusItsDone(){
        manager.createEpicTask(epicTask);
        subTask1.setStatus(TaskStatus.DONE);
        subTask2.setStatus(TaskStatus.DONE);
        subTask3.setStatus(TaskStatus.DONE);
        TestUtils.createSubtaskOnEpic(manager);

        assertEquals(TaskStatus.DONE, manager.getEpicTaskById(1).getStatus());
    }

    @Test
    public void shouldGetExceptionIfSimpleTaskOnThisIdNotCreate(){
        IllegalStateException exception = assertThrows(
                IllegalStateException.class,
                () -> manager.updateSimpleTask(0, simpleTask)
        );
        assertEquals(exception.getMessage(), "Нет задачи с таким номером");
    }


    @Test
    public void shouldGetExceptionIfSubtaskOnThisIdNotCreate(){
        IllegalStateException exception = assertThrows(
                IllegalStateException.class,
                () -> manager.updateSubTask(0, subTask1)
        );
        assertEquals(exception.getMessage(), "Нет задачи с таким номером");
    }

    @Test
    public void shouldGetExceptionIfRemoveNotCreatedTask(){
        IllegalStateException exception = assertThrows(
                IllegalStateException.class,
                () -> manager.removeTaskById(0)
        );
        assertEquals("Нет задачи с таким номером", exception.getMessage());
    }

    @Test
    public void shouldGetExceptionIfHashMapOfTasksIsEmpty(){
        NullPointerException exception = assertThrows(
                NullPointerException.class,
                () -> manager.removeAllTask()
        );
        assertEquals("Список задач пуст", exception.getMessage());
    }

    @Test
    public void shouldGetExceptionIfSimpleTaskHashMapIsEmpty(){
        NullPointerException exception = assertThrows(
                NullPointerException.class,
                () -> manager.cleanSimpleTasks()
        );
        assertEquals("Список задач пуст", exception.getMessage());
    }

    @Test
    public void shouldGetExceptionIfHashMapIsEmpty(){
        NullPointerException exception  = assertThrows(
                NullPointerException.class,
                () -> manager.cleanEpicTasks()
        );
        assertEquals("Список задач пуст", exception.getMessage());
    }
    @Test
    public void shouldGetExceptionIfSubtaskHashMapIsEmpty(){
        NullPointerException exception = assertThrows(
                NullPointerException.class,
                () -> manager.cleanSubTasks()
        );
        assertEquals("Список задач пуст", exception.getMessage());
    }

    @Test
    public void shouldGetExceptionIfNotCreatedSubtask(){
        manager.createEpicTask(epicTask);
        NullPointerException exception = assertThrows(
                NullPointerException.class,
                () -> manager.getSubTaskList(epicTask)
        );
        assertEquals("Список задач пуст", exception.getMessage());
    }

    @Test
    public void shouldGetExceptionIfSimpleTaskListEmpty(){
        NullPointerException exception = assertThrows(
                NullPointerException.class,
                () -> manager.getSimpleTaskList()
        );
        assertEquals("Список задач пуст", exception.getMessage());
    }

    @Test
    public void shouldGetExceptionIfEpicTaskListEmpty(){
        NullPointerException exception = assertThrows(
                NullPointerException.class,
                () -> manager.getEpicTaskList()
        );
        assertEquals("Список задач пуст", exception.getMessage());
    }

    @Test
    public void shouldGetPrioritisedTaskSet(){
        manager.createEpicTask(epicTask);
        LocalDateTime time1 = LocalDateTime.of(2023, 4, 6, 16, 0);
        manager.createSubTask(
                new SubTask(1, "name",
                        "description", TaskStatus.NEW,
                        time1,
                        20));
        LocalDateTime time2 = LocalDateTime.of(2023, 4, 6, 16, 30);
        manager.createSubTask(new SubTask(1, "name",
                "description", TaskStatus.NEW,
                time2, 20));
        LocalDateTime time3 = LocalDateTime.of(2023, 4, 6, 17, 0);
        manager.createSimpleTask(new SimpleTask("name", "description",
                TaskStatus.NEW, time3, 20));

        for (Task task : manager.getPrioritisedTask()) {
            System.out.println(task);
        }

        assertFalse(manager.getPrioritisedTask().isEmpty());
    }

    @Test
    public void shouldGetFalseIfTaskNotOverloopTime(){
        manager.createEpicTask(epicTask);
        manager.createSubTask(subTask1);
        manager.createSubTask(subTask2);
        manager.createSimpleTask(simpleTask);

        assertFalse(manager.getSubTaskById(2).overloop(manager.getSubTaskById(3)));
        assertFalse(manager.getEpicTaskById(1).overloop(manager.getSimpleTaskById(4)));
    }

    @Test
    public void shouldGetExceptionIfTaskOverloopTime(){
        manager.createSimpleTask(simpleTask);

        IllegalStateException exception = assertThrows(
                IllegalStateException.class,
                () -> manager.createSimpleTask(simpleTask)
        );
        assertEquals("Время задач пересекается", exception.getMessage());
    }

    @Test
    public void shouldSetEpicTaskStartTimeAndEndTime(){  // тест пересчета времени эпика
        manager.createEpicTask(epicTask);
        TestUtils.createSubtaskOnEpic(manager);

        assertEquals(manager.getEpicTaskById(1).getStartTime(), manager.getSubTaskById(2).getStartTime());
        assertEquals(manager.getEpicTaskById(1).getEndTime(), manager.getSubTaskById(4).getEndTime());
    }

}
