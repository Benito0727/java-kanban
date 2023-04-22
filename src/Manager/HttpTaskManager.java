package Manager;

import Client.KVTaskClient;
import Converter.LocalDateTimeJsonConverter;
import Server.HttpTaskServer;
import Server.KVServer;
import Tasks.Task;
import com.google.gson.*;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class HttpTaskManager extends FileBackedTasksManager {

//    private static final String uri = "http://localhost:7540";
    private static final Gson gson = getGson();

    private static KVTaskClient clientKV;

    public static void main(String[] args) throws IOException {
        KVServer kv = new KVServer();
        kv.start();
        HttpTaskManager manager = Managers.getHttpManager("http://localhost:7540");
        HttpTaskServer server = new HttpTaskServer(manager);
        server.start();
    }

    public HttpTaskManager(String uri){
        clientKV = new KVTaskClient(uri);

    }

    protected void save() {
        if (!tasks.isEmpty()) {
            List<Task> taskList = new ArrayList<>(tasks.values());
            if (!taskList.isEmpty()) {
                String task = gson.toJson(taskList);
                clientKV.put("/task", task);
            }
        }
        if ((taskHistory != null) && (!taskHistory.isEmpty())) {
            String history = gson.toJson(taskHistory);
            clientKV.put("/history", history);
        }
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

