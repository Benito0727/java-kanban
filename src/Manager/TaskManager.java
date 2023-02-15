package Manager;

import Tasks.EpicTask;
import Tasks.SimpleTask;
import Tasks.SubTask;
import Tasks.Task;

import java.util.ArrayList;
import java.util.List;

public interface TaskManager {

    void createEpicTask(EpicTask task);
    void createSimpleTask(SimpleTask task);
    void createSubTask(SubTask task);
    SimpleTask getSimpleTaskById(int id);
    SubTask getSubTaskById(int id);
    EpicTask getEpicTaskById(int id);
    void updateStatusEpicTask(EpicTask task);
    void updateSimpleTask(int id, SimpleTask task);
    void updateSubTask(int id, SubTask task);
    void updateEpicTask(int id, EpicTask task);
    void removeTaskById(int id);
    void removeAllTask();
    void cleanSimpleTasks();
    void cleanEpicTasks();
    void cleanSubTasks();
    ArrayList<String> getListOfTaskNames();
    ArrayList<SubTask> getSubTaskList(EpicTask task);
    ArrayList<SimpleTask> getSimpleTaskList();
    ArrayList<EpicTask> getEpicTaskList();
    List<Task> getHistory();


}
