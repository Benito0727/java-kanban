package Manager;

import Tasks.*;
import Exception.*;

import java.io.*;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

public class FileBackedTasksManager extends InMemoryTaskManager{



    public static void main(String[] args) {
        FileBackedTasksManager manager = new FileBackedTasksManager();

        SimpleTask simpleTask = new SimpleTask("test1", "testing", TaskStatus.NEW);
        SimpleTask simpleTask1 = new SimpleTask("test2", "testing2", TaskStatus.NEW);
        manager.createSimpleTask(simpleTask);
        manager.createSimpleTask(simpleTask1);

        EpicTask epicTask = new EpicTask("epicTask", "description", TaskStatus.NEW);
        manager.createEpicTask(epicTask);

        SubTask subTask = new SubTask(3, "subtask", "description", TaskStatus.NEW);
        manager.createSubTask(subTask);


        manager.getEpicTaskById(3);

        System.out.println(manager.getHistory());

        manager.getSimpleTaskById(1);
        manager.getSimpleTaskById(2);
        manager.getSimpleTaskById(1);
        manager.getSubTaskById(4);
        manager.getSimpleTaskById(1);

        System.out.println("_____");

        for (Task task : manager.getHistory()) {
            System.out.print(task.getIndex() + " ");
        }

        System.out.println(" \n _____ \n");
        File taskManager = new File("resources/taskManager.csv");
        FileBackedTasksManager manager1 = loadTaskManagerMemory(taskManager);




        for (Task task : manager1.getHistory()) {
            System.out.print(task.getIndex() + " ");
        }
        System.out.println("\n");
        System.out.println(manager1.getEpicTaskById(3));

    }

    final private File managerCSV = new File("resources/taskManager.csv");

    void save() {

        try {
            Files.deleteIfExists(managerCSV.toPath());
            Files.createFile(managerCSV.toPath());

        } catch (IOException e) {
            System.out.println(e.getMessage());
        }

        try (FileWriter fileWriter = new FileWriter(managerCSV, true)) {
            fileWriter.write("id,title,description,status,relatedTask,taskType\n");
            if (!(tasks.isEmpty())) {
                for (Integer id : tasks.keySet()) {
                    fileWriter.write(taskToString(tasks.get(id)));
                }
            } else {
                throw new ManagerException("Список задач пуст");
            }
        } catch (ManagerException e) {
            System.out.println(e.getMessage());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        try (FileWriter fileWriter = new FileWriter(managerCSV, true)) {
            fileWriter.write("history\n");
            if (managerCSV.isFile()) {
                for (Task task : history.getHistory()) {
                    fileWriter.write(task.getIndex() + ",");
                }
            } else {
                throw new ManagerException("Отсутсвует файл сохранени");
            }
        } catch (ManagerException e){
            System.out.println(e.getMessage());
        } catch (RuntimeException | IOException e) {
            System.out.println(e.getMessage());
        }

    }


    public static FileBackedTasksManager loadTaskManagerMemory(File file) {
        FileBackedTasksManager backedManager = new FileBackedTasksManager();
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            if (file.isFile()) {
                while (reader.ready()) {
                    String lines = reader.readLine();
                    String[] line = lines.split(",");
                    if (line.length > 5) if (line[5].equals("SIMPLE_TASK")) {
                        SimpleTask task = new SimpleTask(line[1], line[2], getTaskStatus(line[3]));
                        task.setIndex(Integer.parseInt(line[0]));
                        backedManager.tasks.put(task.getIndex(), task);
                    } else if (line[5].equals("EPIC_TASK")) {
                        EpicTask task = new EpicTask(line[1], line[2], getTaskStatus(line[3]));

                        task.setIndex(Integer.parseInt(line[0]));
                        String subTaskList = line[4];
                        StringBuilder stringBuilder = new StringBuilder(subTaskList);
                        stringBuilder.deleteCharAt(stringBuilder.indexOf("["));
                        stringBuilder.deleteCharAt(stringBuilder.indexOf("]"));
                        String[] ids = stringBuilder.toString().split(",");
                        for (String id : ids) {
                            task.subTaskId.add(Integer.parseInt(id));
                        }
                        backedManager.tasks.put(task.getIndex(), task);

                    } else if (line[5].equals("SUBTASK")) {
                        SubTask task = new SubTask(Integer.parseInt(line[4]), line[1], line[2], getTaskStatus(line[3]));
                        task.setIndex(Integer.parseInt(line[0]));
                       backedManager.tasks.put(task.getIndex(), task);
                    }

                }
            } else {
                throw new ManagerException("Файл сохранения не найден");
            }
        } catch (ManagerException e) {
            System.out.println(e.getMessage());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            while (br.ready()){
                String line = br.readLine();
                if (line.equals("history")) {
                    String history = br.readLine();
                    StringBuilder builder = new StringBuilder(history);

                    builder.reverse();
                    line = builder.toString();
                    String[] taskID = line.split(",");

                    for (int i = 1; i < taskID.length; i++) {
                        backedManager.history.add(tasks.get(Integer.parseInt(taskID[i])));
                    }
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return backedManager;
    }

    private static TaskStatus getTaskStatus(String status){
        if (status.equals("NEW")){
            return TaskStatus.NEW;
        } else if (status.equals("IN_PROGRESS")){
            return TaskStatus.IN_PROGRESS;
        } else if (status.equals("DONE")){
            return TaskStatus.DONE;
        }
        return null;
    }

    private String taskToString(Task task){
        String line;
        line = task.getIndex() + "," +
                task.getTitle() + "," +
                task.getDescription() + "," +
                task.getStatus() + ",";

        if (task instanceof EpicTask) {
            line += ((EpicTask) task).subTaskId + ",";
            line += TaskType.EPIC_TASK;
        }

        if (task instanceof SimpleTask) {
            line += " ,";
            line += TaskType.SIMPLE_TASK;
        }
        if (task instanceof SubTask) {
            line += ((SubTask) task).getEpicTaskId() + ",";
            line += TaskType.SUBTASK;
        }

        return line + "\n";
    }
    @Override
    public void createEpicTask(EpicTask task) {
        super.createEpicTask(task);
        save();
    }

    @Override
    public void createSimpleTask(SimpleTask task) {
        super.createSimpleTask(task);
        save();
    }

    @Override
    public void createSubTask(SubTask task) {
        super.createSubTask(task);
        save();
    }

    @Override
    public SimpleTask getSimpleTaskById(int id) {
        save();
        return super.getSimpleTaskById(id);

    }

    @Override
    public SubTask getSubTaskById(int id) {
        save();
        return super.getSubTaskById(id);
    }

    @Override
    public EpicTask getEpicTaskById(int id) {
        save();
        return super.getEpicTaskById(id);
    }

    @Override
    public void updateStatusEpicTask(EpicTask task) {
        super.updateStatusEpicTask(task);
    }

    @Override
    public void updateSimpleTask(int id, SimpleTask task) {
        super.updateSimpleTask(id, task);
    }

    @Override
    public void updateSubTask(int id, SubTask task) {
        super.updateSubTask(id, task);
    }

    @Override
    public void updateEpicTask(int id, EpicTask task) {
        super.updateEpicTask(id, task);
    }

    @Override
    public void removeTaskById(int id) {
        super.removeTaskById(id);
    }

    @Override
    public void removeAllTask() {
        super.removeAllTask();
    }

    @Override
    public void cleanSimpleTasks() {
        super.cleanSimpleTasks();
    }

    @Override
    public void cleanEpicTasks() {
        super.cleanEpicTasks();
    }

    @Override
    public void cleanSubTasks() {
        super.cleanSubTasks();
    }

    @Override
    public ArrayList<String> getListOfTaskNames() {
        return super.getListOfTaskNames();
    }

    @Override
    public ArrayList<SubTask> getSubTaskList(EpicTask task) {
        return super.getSubTaskList(task);
    }

    @Override
    public ArrayList<SimpleTask> getSimpleTaskList() {
        return super.getSimpleTaskList();
    }

    @Override
    public ArrayList<EpicTask> getEpicTaskList() {
        return super.getEpicTaskList();
    }

    @Override
    public List<Task> getHistory() {

        save();
        return super.getHistory();
    }


}
