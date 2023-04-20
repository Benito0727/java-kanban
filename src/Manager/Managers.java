package Manager;

public class Managers {

    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();

    }

    public static InMemoryTaskManager getDefault() {
        return new InMemoryTaskManager();
    }

    public static FileBackedTasksManager getFileManager() {
        return new FileBackedTasksManager();
    }

    public static HttpTaskManager getHttpManager() {
        return new HttpTaskManager();
    }

}