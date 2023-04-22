package Manager;

import Client.KVTaskClient;
import Converter.LocalDateTimeJsonConverter;
import com.google.gson.*;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;

public class HttpTaskManager extends FileBackedTasksManager {

    private static final String uri = "http://localhost:7540";
    private static final Gson gson = getGson();

    private static KVTaskClient clientKV;


    public HttpTaskManager() throws IOException {
        clientKV = new KVTaskClient(uri);

    }

       protected void save() {
        String task = gson.toJson(tasks);
        String history = gson.toJson(taskHistory);
        clientKV.put("/task", task);
        clientKV.put("/history", history);
        super.save();
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

