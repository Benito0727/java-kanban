package Tasks;

import java.time.LocalDateTime;

public class SubTask extends Task {


    private int epicTaskId;

    public SubTask(int epicTaskId, String title, String description, TaskStatus status) {
        super(title, description, status);
        this.epicTaskId = epicTaskId;
    }

    public SubTask(int epicTaskId, String title,
                   String description, TaskStatus status,
                   LocalDateTime startTime,
                   long duration)
    {
        super(title, description, status, startTime, duration);
        this.epicTaskId = epicTaskId;
    }

    public int getEpicTaskId() {
        return epicTaskId;
    }

    public void setEpicTaskId(int epicTaskId) {
        this.epicTaskId = epicTaskId;
    }


}
