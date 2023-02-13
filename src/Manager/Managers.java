package Manager;

import Tasks.EpicTask;
import Tasks.SimpleTask;
import Tasks.SubTask;

import java.util.ArrayList;

public class Managers implements TaskManager {

    public static InMemoryHistoryManager getDefaultHistory(){
        return new InMemoryHistoryManager();
    }

    public TaskManager getDefault() {
        return new TaskManager() {
            @Override
            public void createEpicTask(EpicTask task) {

            }

            @Override
            public void createSimpleTask(SimpleTask task) {

            }

            @Override
            public void createSubTask(SubTask task) {

            }

            @Override
            public SimpleTask getSimpleTaskById(int id) {
                return null;
            }

            @Override
            public SubTask getSubTaskById(int id) {
                return null;
            }

            @Override
            public EpicTask getEpicTaskById(int id) {
                return null;
            }

            @Override
            public void updateStatusEpicTask(EpicTask task) {

            }

            @Override
            public void updateSimpleTask(int id, SimpleTask task) {

            }

            @Override
            public void updateSubTask(int id, SubTask task) {

            }

            @Override
            public void updateEpicTask(int id, EpicTask task) {

            }

            @Override
            public void removeTaskById(int id) {

            }

            @Override
            public void removeAllTask() {

            }

            @Override
            public void cleanSimpleTasks() {

            }

            @Override
            public void cleanEpicTasks() {

            }

            @Override
            public void cleanSubTasks() {

            }

            @Override
            public ArrayList<String> getListOfTaskNames() {
                return null;
            }

            @Override
            public ArrayList<SubTask> getSubTaskList(EpicTask task) {
                return null;
            }

            @Override
            public ArrayList<SimpleTask> getSimpleTaskList() {
                return null;
            }

            @Override
            public ArrayList<EpicTask> getEpicTaskList() {
                return null;
            }
        };
    }

    @Override
    public void createEpicTask(EpicTask task) {

    }

    @Override
    public void createSimpleTask(SimpleTask task) {

    }

    @Override
    public void createSubTask(SubTask task) {

    }

    @Override
    public SimpleTask getSimpleTaskById(int id) {
        return null;
    }

    @Override
    public SubTask getSubTaskById(int id) {
        return null;
    }

    @Override
    public EpicTask getEpicTaskById(int id) {
        return null;
    }

    @Override
    public void updateStatusEpicTask(EpicTask task) {

    }

    @Override
    public void updateSimpleTask(int id, SimpleTask task) {

    }

    @Override
    public void updateSubTask(int id, SubTask task) {

    }

    @Override
    public void updateEpicTask(int id, EpicTask task) {

    }

    @Override
    public void removeTaskById(int id) {

    }

    @Override
    public void removeAllTask() {

    }

    @Override
    public void cleanSimpleTasks() {

    }

    @Override
    public void cleanEpicTasks() {

    }

    @Override
    public void cleanSubTasks() {

    }

    @Override
    public ArrayList<String> getListOfTaskNames() {
        return null;
    }

    @Override
    public ArrayList<SubTask> getSubTaskList(EpicTask task) {
        return null;
    }

    @Override
    public ArrayList<SimpleTask> getSimpleTaskList() {
        return null;
    }

    @Override
    public ArrayList<EpicTask> getEpicTaskList() {
        return null;
    }
}
