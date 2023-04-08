package Manager;

import Tasks.*;
import Exception.*;

import java.io.*;
import java.nio.file.Files;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class FileBackedTasksManager extends InMemoryTaskManager{

    final private File managerCSV = new File("resources/taskManager.csv");

    void save() {

        try {
            Files.deleteIfExists(managerCSV.toPath());
            Files.createFile(managerCSV.toPath());

        } catch (IOException e) {
            System.out.println(e.getMessage());
        }

        try (FileWriter fileWriter = new FileWriter(managerCSV, true)) {
            fileWriter.write("id,title,description,status,relatedTask,taskType,date,time,duration\n");
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
                throw new ManagerException("Отсутствует файл сохранения");
            }
        } catch (ManagerException e){
            System.out.println(e.getMessage());
        } catch (RuntimeException | IOException e) {
            System.out.println(e.getMessage());
        }
    }

    private static LocalDate getLocalDateFromString(String str) {
        String[] date = str.split(" ");
        String year = date[0];
        String month = date[1];
        String day = date[2];
        return LocalDate.of(Integer.parseInt(year),
                Integer.parseInt(getMonthNumberFromName(month)),
                Integer.parseInt(day));
    }

    private static LocalTime getLocalTimeFromString(String str){
        String[] time = str.split(":");
        String hour = time[0];
        String minute = time[1];
        return LocalTime.of(Integer.parseInt(hour), Integer.parseInt(minute));
    }


    private static String getMonthNumberFromName(String monthName){
        String monthNumber = "";
        switch (monthName) {
            case "JANUARY" :
                monthNumber = "01";
                break;
            case "FEBRUARY" :
                monthNumber = "02";
                break;
            case "MARCH" :
                monthNumber = "03";
                break;
            case "APRIL" :
                monthNumber = "04";
                break;
            case "MAY" :
                monthNumber = "05";
                break;
            case "JUNE" :
                monthNumber = "06";
                break;
            case "JULY" :
                monthNumber = "07";
                break;
            case "AUGUST" :
                monthNumber = "08";
                break;
            case "SEPTEMBER" :
                monthNumber = "09";
                break;
            case "OCTOBER" :
                monthNumber = "10";
                break;
            case "NOVEMBER" :
                monthNumber = "11";
                break;
            case "DECEMBER" :
                monthNumber = "12";
                break;
        }
        return monthNumber;
    }

    public static FileBackedTasksManager loadTaskManagerMemory(File file) {
        FileBackedTasksManager backedManager = new FileBackedTasksManager();
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            if (file.isFile()) {
                while (reader.ready()) {
                    String lines = reader.readLine();
                    String[] line = lines.split(",");
                    if (line.length > 5) if (line[5].equals("SIMPLE_TASK")) {
                        SimpleTask task = new SimpleTask(line[1], line[2],
                                getTaskStatus(line[3]),LocalDateTime.of(getLocalDateFromString(line[6]),
                                getLocalTimeFromString(line[7])) , Integer.parseInt(line[8]));
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
                        backedManager.updateEpicTaskStatus(task);
                        backedManager.tasks.put(task.getIndex(), task);

                    } else if (line[5].equals("SUBTASK")) {
                        SubTask task = new SubTask(Integer.parseInt(line[4]), line[1], line[2], getTaskStatus(line[3]),
                                LocalDateTime.of(getLocalDateFromString(line[6]),
                                        getLocalTimeFromString(line[7])), Integer.parseInt(line[8]));
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
                   if (history != null) {
                       StringBuilder builder = new StringBuilder(history);

                       builder.reverse();
                       line = builder.toString();
                       String[] taskID = line.split(",");

                       for (int i = 1; i < taskID.length; i++) {
                           backedManager.history.add(tasks.get(Integer.parseInt(taskID[i])));
                       }
                   } else throw new NullPointerException("История пуста");
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return backedManager;
    }

    private static TaskStatus getTaskStatus(String status){
        switch (status) {
            case "NEW":
                return TaskStatus.NEW;
            case "IN_PROGRESS":
                return TaskStatus.IN_PROGRESS;
            case "DONE":
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
            line += TaskType.SIMPLE_TASK + ",";
            line += task.getStartTime().getYear() + " " +
                    task.getStartTime().getMonth() + " " +
                    task.getStartTime().getDayOfMonth() + "," +
                    task.getStartTime().getHour() + ":" +
                    task.getStartTime().getMinute() + "," +
                    task.getDuration();
        }
        if (task instanceof SubTask) {
            line += ((SubTask) task).getEpicTaskId() + ",";
            line += TaskType.SUBTASK + ",";
            line += task.getStartTime().getYear() + " " +
                    task.getStartTime().getMonth() + " " +
                    task.getStartTime().getDayOfMonth() + "," +
                    task.getStartTime().getHour() + ":" +
                    task.getStartTime().getMinute() + "," +
                    task.getDuration();
        }

        return line + "\n";
    }
    @Override
    public void createEpicTask(EpicTask task) {
        if (task != null) {
            super.createEpicTask(task);
            save();
        } else throw new NullPointerException("Ошибка создания задачи");
    }

    @Override
    public void createSimpleTask(SimpleTask task) {
        if (task != null) {
            super.createSimpleTask(task);
            save();
        } else throw new NullPointerException("Ошибка создания задачи");
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
    public void updateEpicTaskStatus(EpicTask task) {
        super.updateEpicTaskStatus(task);
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
