package Manager;
import Tasks.EpicTask;
import Tasks.SimpleTask;
import Tasks.SubTask;
import java.util.ArrayList;
import java.util.HashMap;



public class TaskManager {

    private int taskId = 1;

    HashMap<Integer, EpicTask> epicTasks = new HashMap<>();
    HashMap<Integer, SubTask> subTasks = new HashMap<>();
    HashMap<Integer, SimpleTask> simpleTasks = new HashMap<>();

    public void createEpicTask(EpicTask task) { // создани эпика
        epicTasks.put(taskId, task);
        task.setIndex(taskId);
        System.out.println(task.getIndex());
        taskId++;
    }

    public void createSimpleTask(SimpleTask task) { // создание обычной задачи
        simpleTasks.put(taskId, task);
        task.setIndex(taskId);
        System.out.println(task.getIndex());
        taskId++;
    }

    public void createSubTask(int id, SubTask task) { // по ид эпика создает ему субтаск
        EpicTask epic = epicTasks.get(id);
        task.setEpicTaskId(id);
        epic.subTaskId.add(taskId);
        subTasks.put(taskId,task);
        task.setIndex(taskId);
        updateStatusEpicTask(epic);
        System.out.println(task.getIndex());
        taskId++;
    }

    public SimpleTask getSimpleTaskById(int id) {  // если есть отдает обьект обычной задачи по ид
        if (simpleTasks.get(id) != null) return simpleTasks.get(id);
        return null;
    }

    public SubTask getSubTaskById(int id) { // если есть отдает подзадачу по ид
        if (subTasks.get(id) != null) return subTasks.get(id);
        return null;
    }

    public EpicTask getEpicTaskById(int id) {  // если есть отдает эпик по ид
        if (epicTasks.get(id) != null) return epicTasks.get(id);
        return null;
    }

    public void updateStatusEpicTask(EpicTask task) { // изменение статуса эпика в зависимости от его подзадач
        for (Integer taskId : task.subTaskId) {
            if (subTasks.get(taskId) == null) {
                task.setStatus("NEW");
            } else if (subTasks.get(taskId) != null && !(subTasks.get(taskId).getStatus().equals("NEW"))) {
                task.setStatus("IN_PROGRESS");
            } else if (subTasks.get(taskId) != null && !(subTasks.get(taskId).getStatus().equals("NEW") &&
                    !(subTasks.get(taskId).getStatus().equals("IN_PROGRESS")))) {
                task.setStatus("DONE");
            }
        }
    }

    public void updateSimpleTask(int id, SimpleTask task) { // обновляет обычную задачу
        if (simpleTasks.get(id) != null) {
            task.setIndex(id);
            simpleTasks.put(id, task);
        }
    }

    public void updateSubTask(int id, SubTask task) { // обновляет подзадачу и...
        if (subTasks.get(id) != null) {
            SubTask obj = subTasks.get(id);
            task.setEpicTaskId(obj.getEpicTaskId());
            task.setIndex(id);
            subTasks.put(id, task);
            updateStatusEpicTask(epicTasks.get(task.getEpicTaskId())); // ...пересматривает статус эпика
        }
    }

    public void updateEpicTask(int id, EpicTask task) { // обновляет эпик
        if (epicTasks.get(id) != null) {
            task.setIndex(id);
            epicTasks.put(id, task);
        }

    }

    public void removeTaskById(int id) {  // проверяет наличие и удаляет обьект по ид...
        if (epicTasks.get(id) != null) { // ...если id принадлежит эпику то...
            EpicTask task = epicTasks.get(id);
            for (Integer subTask : task.subTaskId) {
                subTasks.remove(subTask);           // ...удаляет связанные с ним подзадачи
            }
            epicTasks.remove(id); // ...удаляет эпик
        }

        if (simpleTasks.get(id) != null) simpleTasks.remove(id);
        if (subTasks.get(id) != null) {
            SubTask obj = subTasks.get(id);
            subTasks.remove(id);
            updateStatusEpicTask(epicTasks.get(obj.getEpicTaskId()));
        }
    }

    public void removeAllTask() { // очищает все мапы
        epicTasks.clear();
        simpleTasks.clear();
        subTasks.clear();
    }

    public void cleanSimpleTasks() { // удаляет обычные задачи
        simpleTasks.clear();
    }

    public void cleanEpicTasks() { // удаляет эпики, вместе с этим и подзадачи
        epicTasks.clear();
        subTasks.clear();
    }

    public void cleanSubTasks() { // удаляет подзадачи...
        subTasks.clear();
        for (Integer epicKey : epicTasks.keySet()) { // ...в связи с этим пересматривает статусы эпиков
            updateStatusEpicTask(epicTasks.get(epicKey));
        }
    }

    public ArrayList<String> getListOfTaskNames() { // собирает лист из названий всех задач
        ArrayList<String> taskList = new ArrayList<>();

        for (Integer id : epicTasks.keySet()) {
            EpicTask task = epicTasks.get(id);
            taskList.add(task.getTitle());
        }

        for (Integer id : simpleTasks.keySet()) {
            SimpleTask task = simpleTasks.get(id);
            taskList.add(task.getTitle());
        }

        for (Integer id : subTasks.keySet()) {
            SubTask task = subTasks.get(id);
            taskList.add(task.getTitle());
        }
        return taskList;
    }

    public ArrayList<SubTask> getSubTaskList(EpicTask task) {
        ArrayList<SubTask> subTaskOnEpic = new ArrayList<>();
        for (Integer taskId : task.subTaskId) {
            subTaskOnEpic.add(subTasks.get(taskId));
        }
        return subTaskOnEpic;
    }

    public ArrayList<SimpleTask> getSimpleTaskList() {
        ArrayList<SimpleTask> simpleTaskList = new ArrayList<>();
        for (Integer id : simpleTasks.keySet()) {
            simpleTaskList.add(simpleTasks.get(id));
        }
        return simpleTaskList;
    }

    public ArrayList<EpicTask> getEpicTaskList() {
        ArrayList<EpicTask> epicTaskList = new ArrayList<>();
        for (Integer id : epicTasks.keySet()) {
            epicTaskList.add(epicTasks.get(id));
        }
        return epicTaskList;
    }
}
