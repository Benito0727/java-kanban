package Client;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDateTime;
import java.util.ArrayList;

import Manager.HttpTaskManager;
import Manager.Managers;
import Manager.TaskType;
import Tasks.*;
import com.google.gson.*;
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
            HttpTaskManager manager = Managers.getHttpManager("http://localhost:7540");
            HttpRequest taskRequest = HttpRequest.newBuilder().
                GET().
                uri(URI.create(uri + "/load/task" + "?API_TOKEN=" + API_TOKEN)).
                version(HttpClient.Version.HTTP_1_1).
                build();
            HttpRequest historyRequest = HttpRequest.newBuilder().
                GET().
                uri(URI.create(uri + "/load/history" + "?API_TOKEN=" + API_TOKEN)).
                version(HttpClient.Version.HTTP_1_1).
                build();
            HttpResponse<String> taskResponse = client.send(taskRequest, HttpResponse.BodyHandlers.ofString());
            HttpResponse<String> historyResponse = client.send(historyRequest, HttpResponse.BodyHandlers.ofString());
            if (!historyResponse.body().isEmpty() && historyResponse.statusCode() == 200) {
                String history = historyResponse.body();
                Type listType = new TypeToken<ArrayList<Task>>() {}.getType();
                ArrayList<Task> historyList = gson.fromJson(history, listType);
                manager.taskHistory.addAll(historyList);
            }
            if (!taskResponse.body().isEmpty()) {
                JsonElement jsonElement = JsonParser.parseString(taskResponse.body());
                if (jsonElement.isJsonObject()) {
                     Task task = getTaskFromJsonObject(jsonElement);
                     manager.tasks.put(task.getIndex(), task);
                } else {
                    JsonArray array = jsonElement.getAsJsonArray();
                    for (JsonElement element : array) {
                        Task task = getTaskFromJsonObject(element);
                        manager.tasks.put(task.getIndex(), task);
                    }
                }
            }
            return manager;
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private static Task getTaskFromJsonObject(JsonElement jsonElement) {
        JsonObject jsonObject = jsonElement.getAsJsonObject();
        int index = jsonObject.get("index").getAsInt();
        String type = jsonObject.get("type").getAsString();
        String title = jsonObject.get("title").getAsString();
        String description = jsonObject.get("description").getAsString();
        String status = jsonObject.get("status").getAsString();
        String startTime = jsonObject.get("startTime").getAsString();
        long duration = jsonObject.get("duration").getAsLong();
        int epicId = jsonObject.get("epicTaskId").getAsInt();
        switch (type) {
            case "SIMPLE_TASK" :
                return new SimpleTask(index, title, description, getStatus(status), getType(type),
                        getLocalDateTimeFromString(startTime), duration);
            case "EPIC_TASK" :
                String subID = jsonObject.get("subTaskId").getAsString();
                String[] id = subID.replace("[", "").replace("]", "").split(",");
                ArrayList<Integer> subTaskId = new ArrayList<>();
                for (String s : id) {
                    subTaskId.add(Integer.parseInt(s));
                }
                return new EpicTask(index, title, description, getStatus(status), getType(type), subTaskId);
            case "SUBTASK" :
                return new SubTask(index, epicId, title, description, getStatus(status), getType(type),
                        getLocalDateTimeFromString(startTime), duration);
            default:
                return null;
        }
    }

    private static LocalDateTime getLocalDateTimeFromString(String str) {
        String[] datetime = str.split(" ");
        String[] date = datetime[0].split("-");
        String year = date[0];
        String month = date[1];
        String dayOfMonth = date[2];
        String[] time = datetime[1].split(":");
        String hours = time[0];
        String minutes = time[1];
        String seconds = time[2];
        return LocalDateTime.of(Integer.parseInt(year), Integer.parseInt(month),
                Integer.parseInt(dayOfMonth), Integer.parseInt(hours),
                Integer.parseInt(minutes), Integer.parseInt(seconds));
    }

    private static TaskStatus getStatus(String str) {
        switch (str) {
            case "NEW" :
                return TaskStatus.NEW;
            case "IN_PROGRESS" :
                return TaskStatus.IN_PROGRESS;
            case "DONE" :
                return TaskStatus.DONE;
            default:
                return TaskStatus.UNKNOWN;
        }
    }

    private static TaskType getType(String str) {
        switch (str) {
            case "SIMPLE_TASK" :
                return TaskType.SIMPLE_TASK;
            case "SUBTASK" :
                return TaskType.SUBTASK;
            case "EPIC_TASK" :
                return TaskType.EPIC_TASK;
            default:
                return TaskType.UNKNOWN;
        }
    }
}
