package Manager;

import Manager.InMemoryTaskManager;
import Manager.Managers;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

public class InMemoryTaskManagerTest extends TaskManagerTest<InMemoryTaskManager> {

    @BeforeEach
    void setUp() throws Exception {
        manager = Managers.getDefault();
    }

    @AfterEach
    void tearDown() {
        manager = null;
    }
}