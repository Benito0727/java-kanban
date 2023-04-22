package Manager;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
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
        manager.createEpicTask(TaskManagerTest.epicTask);
        manager.createSimpleTask(TaskManagerTest.simpleTask);
        manager.createSubTask(TaskManagerTest.subTask1);
        manager.getEpicTaskById(1);
        manager.getHistory();
        FileBackedTasksManager otherManager = FileBackedTasksManager.loadTaskManagerMemory(
                new File("resources/taskManager.csv")
        );
        assertEquals(TaskManagerTest.epicTask, otherManager.getEpicTaskById(1));
        assertEquals(TaskManagerTest.simpleTask, otherManager.getSimpleTaskById(2));
        assertEquals(TaskManagerTest.subTask1, otherManager.getSubTaskById(3));
   }


}