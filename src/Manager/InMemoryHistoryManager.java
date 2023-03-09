package Manager;

import Tasks.Task;

import java.util.*;

public class InMemoryHistoryManager implements HistoryManager {


    public List<Task> tasksHistory;
    CustomLinkedList customLinkedList = new CustomLinkedList();


    @Override
    public void add(Task task) {
        customLinkedList.linkLast(task);
    }

    @Override
    public List<Task> getHistory() {
        tasksHistory = new ArrayList<>();
        tasksHistory.add(customLinkedList.getTail().values);
        Node prev = customLinkedList.getTail().getPreviousNode();
        while (prev != null) {
            tasksHistory.add(prev.values);
            prev = prev.getPreviousNode();
        }
        return tasksHistory;
    }

    @Override
    public void remove(int id) {
        customLinkedList.removeNode(id);
    }

    public void clearHistory(){
        customLinkedList.nodeHashMap.clear();
    }




    @Override
    public String toString() {
        return "InMemoryHistoryManager{" +
                "tasksHistory=" + tasksHistory +
                '}';
    }

}

