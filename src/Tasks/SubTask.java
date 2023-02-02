package Tasks;

public class SubTask extends Task {

    private int epicTaskId;

    public SubTask(String title, String description, String status) {
        super(title, description, status);
    }

    public int getEpicTaskId() {
        return epicTaskId;
    }

    public void setEpicTaskId(int epicTaskId) {
        this.epicTaskId = epicTaskId;
    }


}
