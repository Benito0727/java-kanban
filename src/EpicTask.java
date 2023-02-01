import java.util.ArrayList;

public class EpicTask extends Task {

    ArrayList<Integer> subTaskId = new ArrayList<>();

    public EpicTask(String title, String description) {
        super(title, description);
    }

    @Override
    public String toString() {
        return "Task{" +
                "index=" + getIndex() +
                ", title='" + getTitle() + '\'' +
                ", description='" + getDescription() + '\'' +
                ", status='" + getStatus() + '\'' +
                ", subTaskId='" + subTaskId + '\'' +
                '}';
    }
}
