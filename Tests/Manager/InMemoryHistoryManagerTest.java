package Manager;

import Manager.InMemoryHistoryManager;
import Tasks.SimpleTask;
import Tasks.TaskStatus;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryHistoryManagerTest  {

    @Test
    void add() {
        SimpleTask simpleTask = new SimpleTask("name", "description", TaskStatus.NEW);
        InMemoryHistoryManager historyManager = new InMemoryHistoryManager();
        historyManager.add(simpleTask);

        assertFalse(historyManager.getHistory().isEmpty());
    }

    @Test
    void getHistory() {
        SimpleTask simpleTask = new SimpleTask("name", "description", TaskStatus.NEW);
        InMemoryHistoryManager historyManager = new InMemoryHistoryManager();
        historyManager.add(simpleTask);

        assertFalse(historyManager.getHistory().isEmpty());
    }

    @Test
    void remove() {
        SimpleTask simpleTask = new SimpleTask("name", "description", TaskStatus.NEW);
        InMemoryHistoryManager historyManager = new InMemoryHistoryManager();
        historyManager.add(simpleTask);

        historyManager.remove(0);
        assertTrue(historyManager.getHistory().isEmpty());
    }

    @Test
    void clearHistory() {
        SimpleTask simpleTask = new SimpleTask("name", "description", TaskStatus.NEW);
        InMemoryHistoryManager historyManager = new InMemoryHistoryManager();
        historyManager.add(simpleTask);

        historyManager.clearHistory();
        assertTrue(historyManager.getHistory().isEmpty());
    }
}