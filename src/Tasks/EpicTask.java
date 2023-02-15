package Tasks;
import java.util.ArrayList;

public class EpicTask extends Task {

    public ArrayList<Integer> subTaskId = new ArrayList<>();

    public EpicTask(String title, String description, TaskStatus status) {
        super(title, description, status);
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
