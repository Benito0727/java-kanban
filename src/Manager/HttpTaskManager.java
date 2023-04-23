package Manager;

import Client.KVTaskClient;
import Converter.LocalDateTimeJsonConverter;
import Tasks.*;
import com.google.gson.*;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class HttpTaskManager extends FileBackedTasksManager {


    private static final Gson gson = getGson();

    private static KVTaskClient clientKV;

//    public static void main(String[] args) throws IOException {
//        KVServer kv = new KVServer();
//        kv.start();
//        HttpTaskManager manager = Managers.getHttpManager("http://localhost:7540");
//        HttpTaskServer server = new HttpTaskServer(manager);
//        server.start();
//    }

    public HttpTaskManager(String uri){
        clientKV = new KVTaskClient(uri);

    }

    public static HttpTaskManager loadManagerFromServer(){
        HttpTaskManager manager = new HttpTaskManager("http://localhost:7540");
        String jsonHistory = clientKV.load("history");
        String jsonTask = clientKV.load("task");

        if ((jsonTask.length() > 0) && (!jsonTask.isBlank())) {
            JsonElement jsonElement = JsonParser.parseString(jsonTask);
            if (jsonElement.isJsonObject()) {
                Task task = getTaskFromJsonObject(jsonElement);
                if (task != null) {
                    manager.tasks.put(task.getIndex(), task);
                }
            } else {
                JsonArray array = jsonElement.getAsJsonArray();
                for (JsonElement element : array) {
                    Task task = getTaskFromJsonObject(element);
                    if (task != null) {
                        manager.tasks.put(task.getIndex(), task);
                    }
                }
            }
        }
        if ((jsonHistory.length() > 0) && (!jsonHistory.isBlank())) {
            Type listType = new TypeToken<ArrayList<Task>>() {}.getType();
            ArrayList<Task> historyList = gson.fromJson(jsonHistory, listType);

            for (Task task : historyList) {
                manager.history.add(task);
            }
        }
        return manager;
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

    private static Task getTaskFromJsonObject(JsonElement jsonElement) {
        JsonObject jsonObject = jsonElement.getAsJsonObject();
        int index = jsonObject.get("index").getAsInt();
        String type = jsonObject.get("type").getAsString();
        String title = jsonObject.get("title").getAsString();
        String description = jsonObject.get("description").getAsString();
        String status = jsonObject.get("status").getAsString();
        String startTime = jsonObject.get("startTime").getAsString();
        long duration = jsonObject.get("duration").getAsLong();

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
                if (jsonObject.get("epicTaskId") != null) {
                    int epicId = jsonObject.get("epicTaskId").getAsInt();
                    return new SubTask(index, epicId, title, description, getStatus(status), getType(type),
                            getLocalDateTimeFromString(startTime), duration);
                }

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

    private static Gson getGson(){
        return new GsonBuilder()
                .setPrettyPrinting()
                .serializeNulls()
                .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeJsonConverter())
                .setDateFormat("yyyy-MM-dd HH:mm:ss")
                .create();
    }

}

