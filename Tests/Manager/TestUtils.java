package Manager;

import Manager.TaskManager;
import Manager.TaskManagerTest;

public class TestUtils {
    public static void createSubtaskOnEpic(TaskManager manager){

        manager.createSubTask(TaskManagerTest.subTask1);
        manager.createSubTask(TaskManagerTest.subTask2);
        manager.createSubTask(TaskManagerTest.subTask3);
    }

    public static String epicInString(){
        return "{\n" +
                "  \"subTaskId\": [],\n" +
                "  \"index\": 0,\n" +
                "  \"title\": \"epicName\",\n" +
                "  \"description\": \"epicDescription\",\n" +
                "  \"status\": \"NEW\",\n" +
                "  \"startTime\": null,\n" +
                "  \"duration\": 0,\n" +
                "  \"endTime\": null\n" +
                "}";
    }
    public static String sub1InString() {
        return "{\n" +
                "  \"epicTaskId\": 1,\n" +
                "  \"index\": 0,\n" +
                "  \"title\": \"subName1\",\n" +
                "  \"description\": \"subDescription1\",\n" +
                "  \"status\": \"NEW\",\n" +
                "  \"startTime\": \"2023-04-21 10:15:00\",\n" +
                "  \"duration\": 15,\n" +
                "  \"endTime\": \"2023-04-21 10:30:00\"\n" +
                "}";
    }
    public static String sub2InString() {
        return "{\n" +
                "  \"epicTaskId\": 1,\n" +
                "  \"index\": 0,\n" +
                "  \"title\": \"subName2\",\n" +
                "  \"description\": \"subDescription2\",\n" +
                "  \"status\": \"NEW\",\n" +
                "  \"startTime\": \"2023-04-21 10:30:00\",\n" +
                "  \"duration\": 15,\n" +
                "  \"endTime\": \"2023-04-21 10:45:00\"\n" +
                "}";
    }
    public static String sub3InString() {
        return "{\n" +
                "  \"epicTaskId\": 1,\n" +
                "  \"index\": 0,\n" +
                "  \"title\": \"subName3\",\n" +
                "  \"description\": \"subDescription3\",\n" +
                "  \"status\": \"NEW\",\n" +
                "  \"startTime\": \"2023-04-21 10:45:00\",\n" +
                "  \"duration\": 15,\n" +
                "  \"endTime\": \"2023-04-21 11:00:00\"\n" +
                "}";
    }
    public static String simpleInString() {
        return "{\n" +
                "  \"index\": 0,\n" +
                "  \"title\": \"simpleName\",\n" +
                "  \"description\": \"simpleDescription\",\n" +
                "  \"status\": \"NEW\",\n" +
                "  \"startTime\": \"2023-04-21 11:30:00\",\n" +
                "  \"duration\": 30,\n" +
                "  \"endTime\": \"2023-04-21 12:00:00\"\n" +
                "}";
    }
}
