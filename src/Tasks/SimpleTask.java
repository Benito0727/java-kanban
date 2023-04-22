package Tasks;

import Manager.TaskType;

import java.time.LocalDateTime;

public class SimpleTask extends Task {


    public SimpleTask(String title, String description, TaskStatus status, TaskType type) {
        super(title, description, status, type);

    }
    public SimpleTask(String title, String description,
                      TaskStatus status, TaskType type,
                      LocalDateTime startTime,
                      long duration) {
        super(title, description, status, type, startTime, duration);

    }

    public SimpleTask(int index, String title, String description,
                      TaskStatus status, TaskType type,
                      LocalDateTime startTime,
                      long duration) {
        super(index, title, description, status, type, startTime, duration);

    }
}
