package Tasks;

import Manager.TaskType;

import java.time.LocalDateTime;

public class SubTask extends Task {

    private final String taskType = "SUB";
    private int epicTaskId;

    public SubTask(int epicTaskId, String title, String description, TaskStatus status, TaskType type) {
        super(title, description, status, type);
        this.epicTaskId = epicTaskId;

    }

    public SubTask(int epicTaskId, String title,
                   String description, TaskStatus status,
                   TaskType type, LocalDateTime startTime,
                   long duration)
    {
        super(title, description, status, type, startTime, duration);
        this.epicTaskId = epicTaskId;


    }

    public SubTask(int index, int epicTaskId, String title,
                   String description, TaskStatus status,
                   TaskType type, LocalDateTime startTime,
                   long duration)
    {
        super(index, title,description, status, type, startTime, duration);
        this.epicTaskId = epicTaskId;

    }

    public int getEpicTaskId() {
        return epicTaskId;
    }

    public void setEpicTaskId(int epicTaskId) {
        this.epicTaskId = epicTaskId;
    }


}
