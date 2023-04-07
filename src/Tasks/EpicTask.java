package Tasks;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class EpicTask extends Task {



    public ArrayList<Integer> subTaskId = new ArrayList<>();

    public EpicTask(String title, String description, TaskStatus status) {
        super(title, description, status);
    }

    public EpicTask(String title,
                    String description,
                    TaskStatus status,
                    String dateToStart,
                    String timeToStart,
                    long duration)
    {
        super(title,description,status,dateToStart,timeToStart,duration);
    }



    @Override
    public String toString() {
        return "Task{" +
                "index=" + getIndex() +
                ", title='" + getTitle() + '\'' +
                ", description='" + getDescription() + '\'' +
                ", status='" + getStatus() + '\'' +
                ", subTaskId='" + subTaskId + '\'' +
                ", date='" + getStartTime().format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm")) + '\'' +
                '}';
    }
}
