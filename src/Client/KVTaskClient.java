package Client;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import Manager.HttpTaskManager;
import Tasks.Task;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;


import static java.net.URI.create;

public class KVTaskClient {

    private static final Gson gson = new Gson();
    private static URI uri;
    private static  HttpClient client;
    private static String API_TOKEN;


    public KVTaskClient(String uriString){
        client = HttpClient.newHttpClient();
        uri = URI.create(uriString);
        API_TOKEN = register(uri);
    }

    private String register(URI uri){
        try {
            HttpRequest request = HttpRequest.newBuilder().GET().uri(URI.create(uri + "/register")).build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            return response.body();
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e.getCause());
        }
    }

    public void put(String key , String value){
        try {
            if (value != null) {
                HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(value);
                HttpRequest request = HttpRequest.newBuilder().
                        uri(create(uri + "/save" + key + "?API_TOKEN=" + API_TOKEN)).
                        POST(body)
                        .build();
                HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
                System.out.println(response.statusCode());
            }
        } catch (IOException | InterruptedException e) {
            e.getStackTrace();
        }
    }

    public static HttpTaskManager load(){
        try {
            HttpTaskManager manager = new HttpTaskManager();
            HttpRequest taskRequest = HttpRequest.newBuilder().
                GET().
                uri(URI.create(uri + "/load/task" + "?API_TOKEN=" + API_TOKEN)).
                build();
            HttpRequest historyRequest = HttpRequest.newBuilder().
                GET().
                uri(URI.create(uri + "/load/history" + "?API_TOKEN=" + API_TOKEN)).
                build();
            HttpResponse<String> taskResponse = client.send(taskRequest, HttpResponse.BodyHandlers.ofString());
            String task = taskResponse.body();
            HttpResponse<String> historyResponse = client.send(historyRequest, HttpResponse.BodyHandlers.ofString());
            String history = historyResponse.body();
            if (task != null) {
                Type mapType = new TypeToken<HashMap<Integer, Task>>() {}.getType();
                HashMap<Integer, Task> map = gson.fromJson(task, mapType);
            }
            if (history != null) {
                Type listType = new TypeToken<ArrayList<Task>>(){}.getType();
                ArrayList<Task> list = gson.fromJson(history, listType);
                manager.taskHistory.addAll(list);
            }
            return manager;
        } catch (IOException | InterruptedException e) {
            e.getStackTrace();
            return null;
        }
    }
}
