package Manager;
import Tasks.Task;

import java.util.List;

public interface HistoryManager {



    public void add(Task task);

    public List<Task> getHistory();


}