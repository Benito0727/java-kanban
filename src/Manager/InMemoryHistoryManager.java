package Manager;

import Tasks.Task;


import java.util.*;

public class InMemoryHistoryManager implements HistoryManager {




    public List<Task> tasksHistory;
    CustomLinkedList customLinkedList = new CustomLinkedList();


    @Override
    public void add(Task task) {
        if (task != null) {
            customLinkedList.linkLast(task);
        } else throw new NullPointerException("В качестве задачи передан пустой объект");
    }

    @Override
    public List<Task> getHistory() {
        tasksHistory = new ArrayList<>();
        if (customLinkedList.getTail() != null) {
            tasksHistory.add(customLinkedList.getTail().content);
            Node prev = customLinkedList.getTail().getPreviousNode();
            while (prev != null) {
                tasksHistory.add(prev.content);
                prev = prev.getPreviousNode();
            }
            return tasksHistory;
        }
        return tasksHistory;
    }

    @Override
    public void remove(int id) {
        if (!(customLinkedList.nodeHashMap.isEmpty())) customLinkedList.removeNode(id);
    }

    public void clearHistory(){
        for (Integer id : customLinkedList.nodeHashMap.keySet()) {
            remove(id);
        }
        customLinkedList.nodeHashMap.clear();

    }




    @Override
    public String toString() {
        return "InMemoryHistoryManager{" +
                "tasksHistory=" + tasksHistory +
                '}';
    }

}

