package Tasks;

public class SimpleTask extends Task {

    public SimpleTask(String title, String description, TaskStatus status) {
        super(title, description, status);
    }
    public SimpleTask(String title, String description,
                      TaskStatus status, String dateToStart,
                      String timeToStart, long duration) {
        super(title, description, status, dateToStart, timeToStart, duration);
    }
}
