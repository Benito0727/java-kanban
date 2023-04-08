package Tasks;

import java.time.LocalDateTime;

public class SimpleTask extends Task {

    public SimpleTask(String title, String description, TaskStatus status) {
        super(title, description, status);
    }
    public SimpleTask(String title, String description,
                      TaskStatus status, LocalDateTime startTime,
                      long duration) {
        super(title, description, status, startTime, duration);
    }
}
