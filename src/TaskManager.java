import java.util.ArrayList;
import java.util.HashMap;


public class TaskManager {

    private int taskId = 1;

    HashMap<Integer, EpicTask> epicTask = new HashMap<>();
    HashMap<Integer, SubTask> subTask = new HashMap<>();
    HashMap<Integer, SimpleTask> simpleTask = new HashMap<>();

    public void createEpicTask(Object task){ // создани эпика

        epicTask.put(taskId, (EpicTask) task);
        ((EpicTask) task).setIndex(taskId);
        ((EpicTask) task).setStatus("NEW");
        taskId++;
    }

    public void createSimpleTask(Object task) { // создание обычной задачи
        simpleTask.put(taskId, (SimpleTask) task);
        ((SimpleTask) task).setIndex(taskId);
        ((SimpleTask) task).setStatus("NEW");
        taskId++;
    }

    public void createSubTask(int id, Object task){ // по ид эпика создает ему субтаск
        EpicTask epic = epicTask.get(id);
        epic.subTaskId.add(taskId);
        subTask.put(taskId, (SubTask) task);
        ((SubTask) task).setIndex(taskId);
        ((SubTask) task).setStatus("NEW");
        taskId++;

    }

    public Object getTaskById(int id) { // проверяет наличие и отдает обьек по ид
        if (epicTask.get(id) != null) return epicTask.get(id);
        if (subTask.get(id) != null) return subTask.get(id);
        if (simpleTask.get(id) != null) return simpleTask.get(id);
        return null;
    }



    public void updateStatusSimpleTask(SimpleTask task){ // изменение статуса обычной задачи
        if (task.getStatus().equals("IN_PROGRESS")){
            task.setStatus("DONE");
        }

        if (task.getStatus().equals("NEW")){
            task.setStatus("IN_PROGRESS");
        }
    }

    public void updateStatusSubTask(SubTask task) { // изменение статуса подзадачи
        if (task.getStatus().equals("IN_PROGRESS")){
            task.setStatus("DONE");
        }

        if (task.getStatus().equals("NEW")){
            task.setStatus("IN_PROGRESS");
        }
    }

    public void updateStatusEpicTask(EpicTask task) { // изменение статуса эпика в зависимости от его подзадач
        for (Integer taskId : task.subTaskId) {
            if (subTask.get(taskId) != null && !(subTask.get(taskId).getStatus().equals("NEW"))) {
                task.setStatus("IN_PROGRESS");
            }
            if (subTask.get(taskId) != null && !(subTask.get(taskId).getStatus().equals("NEW") &&
                    !(subTask.get(taskId).getStatus().equals("IN_PROGRESS")))) {
                task.setStatus("DONE");
            }

            if (subTask.get(taskId) == null) {
                task.setStatus("NEW");
            }
        }
    }


    public void updateTaskById(int id, Object task) {  // по ид заменяет существующий объект полученым
        if (epicTask.get(id) != null) {
            EpicTask obj = (EpicTask) task;
            obj.setIndex(id);
            epicTask.put(id,(EpicTask) task);
        }
        if (subTask.get(id) != null) {
            SubTask obj = (SubTask) task;
            obj.setIndex(id);
            subTask.put(id, (SubTask) task);
        }
        if (simpleTask.get(id) != null) {
            SimpleTask obj = (SimpleTask) task;
            obj.setIndex(id);
            simpleTask.put(id, (SimpleTask) task);
        }
    }

    public void removeTaskById(int id){  // проверяет наличие и удаляет обьект по ид
        if (epicTask.get(id) != null) epicTask.remove(id);
        if (simpleTask.get(id) != null) simpleTask.remove(id);
        if (subTask.get(id) != null) subTask.remove(id);
    }

    public void removeAllTask(){ // очищает все мапы
        epicTask.clear();
        simpleTask.clear();
        subTask.clear();
    }

    public ArrayList<String> getTaskList(){ // собирает лист из названий всех задач
        ArrayList<String> taskList = new ArrayList<>();

        for (Integer id : epicTask.keySet()) {
            EpicTask task = epicTask.get(id);
            taskList.add(task.getTitle());
        }

        for (Integer id : simpleTask.keySet()) {
            SimpleTask task = simpleTask.get(id);
            taskList.add(task.getTitle());
        }

        for (Integer id : subTask.keySet()) {
            SubTask task = subTask.get(id);
            taskList.add(task.getTitle());
        }
        return taskList;
    }




    public ArrayList<String> getSubTaskList(EpicTask task) {
        ArrayList<String> subTaskById = new ArrayList<>();
        for (Integer taskId : task.subTaskId) {
            SubTask sub = subTask.get(taskId);
            subTaskById.add(sub.getTitle());
        }
        return subTaskById;
    }

}
