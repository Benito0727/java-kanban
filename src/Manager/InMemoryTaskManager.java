package Manager;
import Tasks.*;

import java.util.*;


public class InMemoryTaskManager implements TaskManager {

    private int taskId = 1;

    static HistoryManager history = Managers.getDefaultHistory();

    static HashMap<Integer, Task> tasks = new HashMap<>();


    @Override
    public void createEpicTask(EpicTask task) { // создание эпика
        if (task != null) {
            for (Task values : getPrioritisedTask()) {
                if (task.overloop(values)) throw new IllegalStateException("Время задач пересекается");
            }
            tasks.put(taskId, task);
            task.setIndex(taskId);
            System.out.println(task.getIndex());
            taskId++;
        } else throw new NullPointerException("Ошибка создания задачи");
    }

    @Override
    public void createSimpleTask(SimpleTask task) { // создание обычной задачи
        if (task != null) {
            for (Task values : getPrioritisedTask()) {
                if (task.overloop(values)) throw new IllegalStateException("Время задач пересекается");
            }
            tasks.put(taskId, task);
            task.setIndex(taskId);
            System.out.println(task.getIndex());
            taskId++;
        } else throw new NullPointerException("Ошибка создания задачи");
    }

    @Override
    public void createSubTask(SubTask task) { // по ид эпика создает ему субтаск
        if (task != null && tasks.containsKey(task.getEpicTaskId())) {
            for (Task values : getPrioritisedTask()) {
                if (task.overloop(values)) throw new IllegalStateException("Время задач пересекается");
            }
            EpicTask epic = (EpicTask) tasks.get(task.getEpicTaskId());
            epic.subTaskId.add(taskId);
            tasks.put(taskId, task);
            task.setIndex(taskId);
            updateEpicTaskStatus(epic);
            System.out.println(task.getIndex());
            taskId++;
        } else throw new NullPointerException("Ошибка создания подзадачи");
    }

    @Override
    public SimpleTask getSimpleTaskById(int id) {  // если есть отдает объект обычной задачи по ид
        if (tasks.get(id) != null) {
            history.add(tasks.get(id)); // добавляет таску в историю просмотренных
            return (SimpleTask) tasks.get(id);
        } throw new NullPointerException("Нет задачи с таким номером");
    }

    @Override
    public SubTask getSubTaskById(int id) { // если есть отдает подзадачу по ид
        if (tasks.get(id) != null) {
            history.add(tasks.get(id)); // добавляет таску в историю просмотренных
            return (SubTask) tasks.get(id);
        } else throw new NullPointerException("Нет задачи с таким номером");
    }

    @Override
    public EpicTask getEpicTaskById(int id) {  // если есть отдает эпик по ид
        if (tasks.get(id) != null) {
            history.add(tasks.get(id)); // добавляет таску в историю просмотренных
            updateEpicTaskStatus((EpicTask) tasks.get(id));
            return (EpicTask) tasks.get(id);
        } else  throw new NullPointerException("Нет задачи с таким номером");
    }

    @Override
    public void updateEpicTaskStatus(EpicTask task) { // изменение статуса эпика в зависимости от его подзадач
        int count = 0;

        for (Integer taskId : task.subTaskId) {
            if (tasks.get(taskId) == null) {
                task.setStatus(TaskStatus.NEW);
            } else {
                for (Integer id : task.subTaskId) {
                    if (tasks.get(id).getStatus().equals(TaskStatus.NEW)) {
                        task.setStatus(TaskStatus.NEW);
                    } else if (tasks.get(id).getStatus().equals(TaskStatus.IN_PROGRESS)){
                        task.setStatus(TaskStatus.IN_PROGRESS);
                    } else if (tasks.get(id).getStatus().equals(TaskStatus.DONE)){
                        count++;
                    } else count = 0;

                }
                if (count == task.subTaskId.size()) {
                    task.setStatus(TaskStatus.DONE);
                }
            }

            if (tasks.get(taskId) != null) {
                if (task.getStartTime() == null) {
                    task.setStartTime(tasks.get(taskId).getStartTime());
                } else {
                    if (tasks.get(taskId).getStartTime() != null) {

                        if (task.getStartTime().isBefore(tasks.get(taskId).getStartTime())) {
                            task.setStartTime(tasks.get(taskId).getStartTime());
                        }
                    }
                    if (tasks.get(taskId).getEndTime() != null) {
                        if (tasks.get(taskId).getEndTime().isAfter(tasks.get(taskId).getEndTime())) {
                            task.setStartTime(tasks.get(taskId).getEndTime());
                        }
                    }
                }
            }
        }
    }

    @Override
    public void updateSimpleTask(int id, SimpleTask task) { // обновляет обычную задачу
        if (tasks.get(id) != null) {
            for (Task values : getPrioritisedTask()) {
                if (task.overloop(values)) throw new IllegalStateException("Время задач пересекается");
            }
            task.setIndex(id);
            tasks.put(id, task);
            history.remove(id);
        } else throw new IllegalStateException("Нет задачи с таким номером");

    }

    @Override
    public void updateSubTask(int id, SubTask task) { // обновляет подзадачу и...
        if (tasks.get(id) != null) {
            for (Task values : getPrioritisedTask()) {
                if (task.overloop(values)) throw new IllegalStateException("Время задач пересекается");
            }
            SubTask obj = (SubTask) tasks.get(id);
            task.setEpicTaskId(obj.getEpicTaskId());
            task.setIndex(id);
            tasks.put(id, task);
            updateEpicTaskStatus((EpicTask) tasks.get(task.getEpicTaskId())); // ...пересматривает статус эпика
            history.remove(id);
        } else throw new IllegalStateException("Нет задачи с таким номером");
    }

    @Override
    public void updateEpicTask(int id, EpicTask task) { // обновляет эпик
        if (tasks.get(id) != null) {
            for (Task values : getPrioritisedTask()) {
                if (task.overloop(values)) throw new IllegalStateException("Время задач пересекается");
            }
            task.setIndex(id);
            tasks.put(id, task);
            history.remove(id);
        } else throw new IllegalStateException("Нет задачи с таким номером");
    }

    @Override
    public void removeTaskById(int id) {  // проверяет наличие, и удаляет объект по ид...
        if (tasks.get(id) != null) {
            if (tasks.get(id) instanceof EpicTask) { // ...если id принадлежит эпику то...
                EpicTask task = (EpicTask) tasks.get(id);
                for (Integer subTask : task.subTaskId) {
                    tasks.remove(subTask);           // ...удаляет связанные с ним подзадачи
                    history.remove(subTask);
                }
                tasks.remove(id); // ...удаляет эпик
            }
            if (tasks.get(id) instanceof SimpleTask) tasks.remove(id);
            if (tasks.get(id) instanceof SubTask) {
                SubTask obj = (SubTask) tasks.get(id);
                tasks.remove(id);
                updateEpicTaskStatus((EpicTask) tasks.get(obj.getEpicTaskId()));
                history.remove(id);
            }
        } else throw new IllegalStateException("Нет задачи с таким номером");
    }

    @Override
    public void removeAllTask() { // очищает все мапы
        if (!(tasks.isEmpty())) {
            cleanEpicTasks();
            cleanSimpleTasks();
        } else throw new NullPointerException("Список задач пуст");
    }

    @Override
    public void cleanSimpleTasks() { // удаляет обычные задачи
        ArrayList<Integer> idsOfTaskToRemove = new ArrayList<>();
        if (!(tasks.isEmpty())) {
            for (Integer id : tasks.keySet()) {
                if (tasks.get(id) instanceof SimpleTask) {
                    idsOfTaskToRemove.add(id);

                }
            }
            for (Integer id : idsOfTaskToRemove) {
                history.remove(id);
                tasks.remove(id);
            }
        }
        if (idsOfTaskToRemove.isEmpty()) throw new NullPointerException("Список задач пуст");
    }

    @Override
    public void cleanEpicTasks() { // удаляет эпики, вместе с этим и подзадачи
        ArrayList<Integer> idsOfTaskToRemove = new ArrayList<>();
        for (Integer id : tasks.keySet()) {
            if (tasks.get(id) instanceof EpicTask) {
                idsOfTaskToRemove.add(id);
            }
        }
        for (Integer id : tasks.keySet()) {
            if (tasks.get(id) instanceof SubTask) {
                idsOfTaskToRemove.add(id);
            }
        }
        for (Integer id : idsOfTaskToRemove) {
            history.remove(id);
            tasks.remove(id);
        }

        if (idsOfTaskToRemove.isEmpty()) throw new NullPointerException("Список задач пуст");

    }

    @Override
    public void cleanSubTasks() { // удаляет подзадачи...
        ArrayList<Integer> idTasksToRemove = new ArrayList<>();
        for (Integer id : tasks.keySet()) {
            if (tasks.get(id) instanceof SubTask) {
                idTasksToRemove.add(id);
            }
        }

        for (Integer epicKey : tasks.keySet()) { // ...в связи с этим пересматривает статусы эпиков
            if (tasks.get(epicKey) instanceof EpicTask) {
                updateEpicTaskStatus((EpicTask) tasks.get(epicKey));
            }
        }
        for (Integer id : idTasksToRemove) {
            tasks.remove(id);
            history.remove(id);
        }
        if (idTasksToRemove.isEmpty()) throw new NullPointerException("Список задач пуст");
    }


    @Override
    public ArrayList<String> getListOfTaskNames() { // собирает лист из названий всех задач
        ArrayList<String> taskList = new ArrayList<>();

        for (Integer id : tasks.keySet()) {
            taskList.add(tasks.get(id).getTitle());
        }
        if (!(taskList.isEmpty())) {
            return taskList;
        } else throw new NullPointerException("Список задач пуст");
    }

    @Override
    public ArrayList<SubTask> getSubTaskList(EpicTask task) {
        ArrayList<SubTask> subTaskOnEpic = new ArrayList<>();
        for (Integer taskId : task.subTaskId) {
            subTaskOnEpic.add((SubTask) tasks.get(taskId));
        }
        if (!(subTaskOnEpic.isEmpty())) {
            return subTaskOnEpic;
        } else throw new NullPointerException("Список задач пуст");
    }

    @Override
    public ArrayList<SimpleTask> getSimpleTaskList() {
        ArrayList<SimpleTask> simpleTaskList = new ArrayList<>();
        for (Integer id : tasks.keySet()) {
            if (tasks.get(id) instanceof SimpleTask) {
                simpleTaskList.add((SimpleTask) tasks.get(id));
            }
        }
        if (!(simpleTaskList.isEmpty())) {
            return simpleTaskList;
        } else throw new NullPointerException("Список задач пуст");
    }

    @Override
    public ArrayList<EpicTask> getEpicTaskList() {
        ArrayList<EpicTask> epicTaskList = new ArrayList<>();
        for (Integer id : tasks.keySet()) {
            if (tasks.get(id) instanceof EpicTask) epicTaskList.add((EpicTask) tasks.get(id));
        }
        if (!(epicTaskList.isEmpty())) {
            return epicTaskList;
        } else throw new NullPointerException("Список задач пуст");
    }

    @Override
    public List<Task> getHistory(){
        if (!(history.getHistory().isEmpty())) {
            return history.getHistory();
        } else throw new NullPointerException("История пуста");
    }
    @Override
    public Set<Task> getPrioritisedTask(){
        Set<Task> prioritisedTask = new TreeSet<>(Comparator.comparing(Task::getStartTime));
        for (Task task : tasks.values()) {
            if (!(task instanceof EpicTask)) {
                prioritisedTask.add(task);
            }
        }
        return prioritisedTask;
    }

}
