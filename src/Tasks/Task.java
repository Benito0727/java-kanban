package Tasks;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

public abstract class Task {

    private int index;
    private final String title;
    private final String description;
    private TaskStatus status;
    private LocalDateTime startTime;
    private long duration;
    private LocalDateTime endTime;

    public Task(String title, String description, TaskStatus status) {
        this.title = title;
        this.description = description;
        this.status = status;
    }

    public Task(String title, String description, TaskStatus status, String dateToStars, String timeToStart, long timeInMinutes) {
        this.title = title;
        this.description = description;
        this.status = status;
        this.startTime = LocalDateTime.of(getLocalDate(dateToStars), getLocalTime(timeToStart));
        this.duration = timeInMinutes;
        endTime = setEndTime();
    }

    public LocalTime getLocalTime(String time) {
        String[] parts = time.split(":");
        return LocalTime.of(Integer.parseInt(parts[0]), Integer.parseInt(parts[1]));
    }


    public LocalDate getLocalDate(String data){
        String[] parts = data.split(" ");
        return LocalDate.of(Integer.parseInt(parts[2]), Integer.parseInt(parts[1]), Integer.parseInt(parts[0]));
    }

    public LocalDateTime setEndTime(){
        endTime = Objects.requireNonNull(startTime).plusMinutes(this.duration);
        return endTime;
    }

    public void setStartTime(LocalDateTime startTime){
        this.startTime = startTime;
    }


    public boolean overloop(Task task){
        if (!getStartTime().isAfter(task.getStartTime())) {
            return overloop(this, task);
        } else {
            return overloop(task, this);
        }
    }

    private static boolean overloop(Task task, Task otherTask) {
        return task.getEndTime().isAfter(otherTask.startTime) ||
                task.getEndTime().equals(otherTask.startTime);
    }


    public LocalDateTime getStartTime(){
        return startTime;
    }

    public LocalDateTime getEndTime(){
        return endTime;
    }

    public long getDuration(){
        return duration;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public TaskStatus getStatus() {
        return status;
    }

    public void setStatus(TaskStatus status) {
        this.status = status;
    }






    @Override
    public String toString() {
        return "Task{" +
                "index=" + index +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", status='" + status + '\'' +
                ", date='" + getStartTime().format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm")) + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return index == task.index &&
                Objects.equals(title, task.title) &&
                Objects.equals(description, task.description) &&
                Objects.equals(status, task.status) &&
                Objects.equals(startTime, task.startTime) &&
                Objects.equals(duration, task.duration) &&
                Objects.equals(endTime, task.endTime);

    }

    @Override
    public int hashCode() {
        return Objects.hash(index, title, description, status);
    }
}
