package Tasks;
public class SubTask extends Task {


    private int epicTaskId;

    public SubTask(int epicTaskId, String title, String description, TaskStatus status) {
        super(title, description, status);
        this.epicTaskId = epicTaskId;
    }

    public SubTask(int epicTaskId, String title,
                   String description, TaskStatus status,
                   String dateToStart, String timeToStart,
                   long duration)
    {
        super(title, description, status, dateToStart, timeToStart, duration);
        this.epicTaskId = epicTaskId;
    }

    public int getEpicTaskId() {
        return epicTaskId;
    }

    public void setEpicTaskId(int epicTaskId) {
        this.epicTaskId = epicTaskId;
    }


}
