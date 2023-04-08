import Manager.FileBackedTasksManager;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class FileBackedTaskManagerTest extends TaskManagerTest<FileBackedTasksManager> {

    @BeforeEach
    public void createManager(){
       manager = new FileBackedTasksManager();
   }


//    @AfterEach
//    void tearDown() throws IOException {
//        manager = null;
//        Files.deleteIfExists(Path.of("resources/taskManager.csv"));
//    }

    @Test
    public void shouldLoadTaskManagerFromFile(){
        manager.createEpicTask(epicTask);
        manager.createSimpleTask(simpleTask);
        manager.createSubTask(subTask1);
        manager.getEpicTaskById(1);
        manager.getHistory();

        FileBackedTasksManager otherManager = FileBackedTasksManager.loadTaskManagerMemory(
                new File("resources/taskManager.csv")
        );

        assertEquals(epicTask, otherManager.getEpicTaskById(1));
        assertEquals(simpleTask, otherManager.getSimpleTaskById(2));
        assertEquals(subTask1, otherManager.getSubTaskById(3));
   }


}