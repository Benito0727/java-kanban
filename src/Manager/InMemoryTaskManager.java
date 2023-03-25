package Manager;
import Tasks.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class InMemoryTaskManager implements TaskManager {

    private int taskId = 1;

    static HistoryManager history = Managers.getDefaultHistory();

    static HashMap<Integer, Task> tasks = new HashMap<>();


    @Override
    public void createEpicTask(EpicTask task) { // создани эпика
        tasks.put(taskId, task);
        task.setIndex(taskId);
        System.out.println(task.getIndex());
        taskId++;
    }

    @Override
    public void createSimpleTask(SimpleTask task) { // создание обычной задачи
        tasks.put(taskId, task);
        task.setIndex(taskId);
        System.out.println(task.getIndex());
        taskId++;
    }

    @Override
    public void createSubTask(SubTask task) { // по ид эпика создает ему субтаск
        EpicTask epic = (EpicTask) tasks.get(task.getEpicTaskId());
        epic.subTaskId.add(taskId);
        tasks.put(taskId,task);
        task.setIndex(taskId);
        updateStatusEpicTask(epic);
        System.out.println(task.getIndex());
        taskId++;
    }

    @Override
    public SimpleTask getSimpleTaskById(int id) {  // если есть отдает обьект обычной задачи по ид
        if (tasks.get(id) != null) {
            history.add(tasks.get(id)); // добавляет таску в историю просмотренных
            return (SimpleTask) tasks.get(id);
        }
        return null;
    }

    @Override
    public SubTask getSubTaskById(int id) { // если есть отдает подзадачу по ид
        if (tasks.get(id) != null) {
            history.add(tasks.get(id)); // добавляет таску в историю просмотренных
            return (SubTask) tasks.get(id);
        }
        return null;
    }

    @Override
    public EpicTask getEpicTaskById(int id) {  // если есть отдает эпик по ид
        if (tasks.get(id) != null) {
            history.add(tasks.get(id)); // добавляет таску в историю просмотренных
            return (EpicTask) tasks.get(id);
        }
        return null;
    }

    @Override
    public void updateStatusEpicTask(EpicTask task) { // изменение статуса эпика в зависимости от его подзадач
        for (Integer taskId : task.subTaskId) {
            if (tasks.get(taskId) == null) {
                task.setStatus(TaskStatus.NEW);
            } else if (tasks.get(taskId) != null) {
                task.setStatus(TaskStatus.IN_PROGRESS);
            } else if (tasks.get(taskId) != null && !(tasks.get(taskId).getStatus().equals(TaskStatus.NEW) &&
                    !(tasks.get(taskId).getStatus().equals(TaskStatus.IN_PROGRESS)))) {
                task.setStatus(TaskStatus.DONE);
            }
        }
    }

    @Override
    public void updateSimpleTask(int id, SimpleTask task) { // обновляет обычную задачу
        if (tasks.get(id) != null) {
            task.setIndex(id);
            tasks.put(id, task);
        }
        history.remove(id);
    }

    @Override
    public void updateSubTask(int id, SubTask task) { // обновляет подзадачу и...
        if (tasks.get(id) != null) {
            SubTask obj = (SubTask) tasks.get(id);
            task.setEpicTaskId(obj.getEpicTaskId());
            task.setIndex(id);
            tasks.put(id, task);
            updateStatusEpicTask((EpicTask) tasks.get(task.getEpicTaskId())); // ...пересматривает статус эпика
        }
        history.remove(id);
    }

    @Override
    public void updateEpicTask(int id, EpicTask task) { // обновляет эпик
        if (tasks.get(id) != null) {
            task.setIndex(id);
            tasks.put(id, task);
        }
        history.remove(id);
    }

    @Override
    public void removeTaskById(int id) {  // проверяет наличие и удаляет обьект по ид...
        if (tasks.get(id) != null) { // ...если id принадлежит эпику то...
            EpicTask task = (EpicTask) tasks.get(id);
            for (Integer subTask : task.subTaskId) {
                tasks.remove(subTask);           // ...удаляет связанные с ним подзадачи
                history.remove(subTask);
            }
            tasks.remove(id); // ...удаляет эпик
        }

        if (tasks.get(id) != null) tasks.remove(id);
        if (tasks.get(id) != null) {
            SubTask obj = (SubTask) tasks.get(id);
            tasks.remove(id);
            updateStatusEpicTask((EpicTask) tasks.get(obj.getEpicTaskId()));
        }
        history.remove(id);
    }

    @Override
    public void removeAllTask() { // очищает все мапы
        cleanEpicTasks();
        cleanSimpleTasks();
    }

    @Override
    public void cleanSimpleTasks() { // удаляет обычные задачи
        for (Integer id : tasks.keySet()) {
            if (tasks.get(id) instanceof SimpleTask) {
                history.remove(id);
                tasks.remove(id);
            }
        }
    }

    @Override
    public void cleanEpicTasks() { // удаляет эпики, вместе с этим и подзадачи
        for (Integer id : tasks.keySet()) {
            if (tasks.get(id) instanceof EpicTask) {
                history.remove(id);
                tasks.remove(id);
            }
        }
        for (Integer id : tasks.keySet()) {
            if (tasks.get(id) instanceof SubTask) {
                history.remove(id);
                tasks.remove(id);
            }
        }
    }

    @Override
    public void cleanSubTasks() { // удаляет подзадачи...

        for (Integer id : tasks.keySet()) {
            if (tasks.get(id) instanceof SubTask) {
                tasks.remove(id);
                history.remove(id);
            }
        }

        for (Integer epicKey : tasks.keySet()) { // ...в связи с этим пересматривает статусы эпиков
            if (tasks.get(epicKey) instanceof EpicTask) {
                updateStatusEpicTask((EpicTask) tasks.get(epicKey));
            }
        }
    }


    @Override
    public ArrayList<String> getListOfTaskNames() { // собирает лист из названий всех задач
        ArrayList<String> taskList = new ArrayList<>();

        for (Integer id : tasks.keySet()) {
            taskList.add(tasks.get(id).getTitle());
        }
        return taskList;
    }

    @Override
    public ArrayList<SubTask> getSubTaskList(EpicTask task) {
        ArrayList<SubTask> subTaskOnEpic = new ArrayList<>();
        for (Integer taskId : task.subTaskId) {
            subTaskOnEpic.add((SubTask) tasks.get(taskId));
        }
        return subTaskOnEpic;
    }

    @Override
    public ArrayList<SimpleTask> getSimpleTaskList() {
        ArrayList<SimpleTask> simpleTaskList = new ArrayList<>();
        for (Integer id : tasks.keySet()) {
            if (tasks.get(id) instanceof SimpleTask) {
                simpleTaskList.add((SimpleTask) tasks.get(id));
            }
        }
        return simpleTaskList;
    }

    @Override
    public ArrayList<EpicTask> getEpicTaskList() {
        ArrayList<EpicTask> epicTaskList = new ArrayList<>();
        for (Integer id : tasks.keySet()) {
            if (tasks.get(id) instanceof EpicTask) epicTaskList.add((EpicTask) tasks.get(id));
        }
        return epicTaskList;
    }

    @Override
    public List<Task> getHistory(){
        return history.getHistory();
    }
}
