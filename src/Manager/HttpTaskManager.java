package Manager;

import Client.KVTaskClient;
import Converter.LocalDateTimeJsonConverter;
import Server.HttpTaskServer;
import Server.KVServer;
import Tasks.*;
import com.google.gson.*;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public class HttpTaskManager extends FileBackedTasksManager {

    private final String uri = "http://localhost:8080";
    private static final Gson gson = getGson();

    private static KVTaskClient clientKV;

    public static void main(String[] args) throws IOException {
        HttpTaskManager manager = Managers.getHttpManager();
        HttpTaskServer server = new HttpTaskServer();
        KVServer kvServer = new KVServer();
        server.start();
        kvServer.start();
        clientKV = new KVTaskClient("http://localhost:8078");
        EpicTask task = new EpicTask("name", "description", TaskStatus.NEW);
        SubTask subTask = new SubTask(1, "name", "description", TaskStatus.NEW,
                LocalDateTime.now(), 12);
        manager.createEpicTask(task);
        manager.createSubTask(subTask);
        String jsonTask = gson.toJson(task);
        String jsonSub = gson.toJson(subTask);

        System.out.println(jsonTask);
        System.out.println(jsonSub);


    }
    protected void save() {
        String task = gson.toJson(tasks);
        String history = gson.toJson(taskHistory);
        clientKV.put("/task", task);
        clientKV.put("/history", history);
        super.save();

    }

    private static HttpTaskManager load(){
        HttpTaskManager newManager = new HttpTaskManager();
        Type mapType = new TypeToken<Map<Integer, Task>>() {}.getType();
        Type listType = new TypeToken<List<Task>>() {}.getType();
        String jsonTask = clientKV.load("/task");
        newManager.tasks = gson.fromJson(jsonTask, mapType);
        String jsonHistory = clientKV.load("history");
        newManager.taskHistory = gson.fromJson(jsonHistory,listType);
        return newManager;
    }

    private static Gson getGson(){
        return new GsonBuilder()
                .setPrettyPrinting()
                .serializeNulls()
                .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeJsonConverter())
                .setDateFormat("yyyy-MM-dd HH:mm:ss")
                .create();
    }

}

