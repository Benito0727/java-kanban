package Tasks;
import Manager.TaskType;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Objects;

public class EpicTask extends Task {


    public ArrayList<Integer> subTaskId = new ArrayList<>();

    public EpicTask(String title, String description, TaskStatus status, TaskType type) {
        super(title, description, status, type);

    }

    public EpicTask(int index, String title, String description, TaskStatus status, TaskType type, ArrayList<Integer> id) {
        super(index, title, description, status, type);
        this.subTaskId = id;
    }


    @Override
    public String toString() {
        String str = "Task{" +
                "index=" + getIndex() +
                ", title='" + getTitle() + '\'' +
                ", description='" + getDescription() + '\'' +
                ", status='" + getStatus() + '\'' +
                ", subTaskId='" + subTaskId + '\'';

        if (getStartTime() != null) {
            str += ", date='" + getStartTime().format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm")) + '\'' +
                    '}';
        }
        return str;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        EpicTask epicTask = (EpicTask) o;
        return subTaskId.equals(epicTask.subTaskId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), subTaskId);
    }
}
