package Server;

import Converter.LocalDateTimeJsonConverter;
import Manager.Managers;
import Manager.TaskManager;

import Tasks.EpicTask;
import Tasks.SimpleTask;
import Tasks.SubTask;
import com.google.gson.*;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.regex.Pattern;


import static com.sun.net.httpserver.HttpServer.*;
import static java.nio.charset.StandardCharsets.UTF_8;

public class HttpTaskServer{

    private final Gson gson;
    private static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;
    private static final int PORT = 7880;
    private final HttpServer server;
    private final TaskManager manager;

    public HttpTaskServer(TaskManager taskManager) throws IOException {
        manager = taskManager;
        server = HttpServer.create(new InetSocketAddress("localhost", PORT), 0);
        gson = getGson();
        server.createContext("/tasks/epic/", this::epicHandler);
        server.createContext("/tasks/task/", this::handleTask);
        server.createContext("/tasks/sub/", this::subHandler);
        server.createContext("/tasks/", this::tasksHandler);
    }


    public void start() {
        System.out.println("Запускаем сервер на порту " + PORT);
        System.out.println("http://localhost:" + PORT + "/tasks/");
        server.start();
    }
    public void stop(){
        System.out.println("Останавливаем сервер на порту " + PORT);
        server.stop(0);
    }

    private void handleTask(HttpExchange exchange) {
        try {
            String param = getParam(exchange);
            int id = -1;
            if (param != null) id = parseIntFromString(param.replaceFirst("id=", ""));
            String path = getPath(exchange);
            String method = exchange.getRequestMethod();
            switch (method) {
                case "GET" :
                    if ((id > 0) && (Pattern.matches("/tasks/task/", path))) {
                        String response = gson.toJson(manager.getSimpleTaskById(id));
                        sendText(exchange, response);
                        exchange.sendResponseHeaders(200, 0);
                    }
                    if ((id == -1) && (Pattern.matches("^/tasks/task/$", path))) {
                        String response = gson.toJson(manager.getSimpleTaskList());
                        if (response == null) {
                            exchange.sendResponseHeaders(404, 0);
                        } else {
                            sendText(exchange, response);
                            exchange.sendResponseHeaders(200, 0);
                            exchange.close();
                        }
                    } else {
                        exchange.sendResponseHeaders(400, 0);
                    }
                    break;
                case "POST" :
                    if (Pattern.matches("^/tasks/task/$", path) && (id == -1)) {
                        SimpleTask task = gson.fromJson(readText(exchange), SimpleTask.class);
                        manager.createSimpleTask(task);
                        exchange.sendResponseHeaders(200, 0);
                        System.out.println("Задача " + task.getTitle() + " c ID "+ task.getIndex() + " успешно создана");
                    }
                    if (Pattern.matches("/tasks/task/", path) && (id > 0)) {
                        SimpleTask task = gson.fromJson(readText(exchange), SimpleTask.class);
                        manager.updateSimpleTask(id, task);
                        exchange.sendResponseHeaders(200, 0);
                        System.out.println("Задача с ID " + id + "успешно обновлена");
                    }
                    exchange.sendResponseHeaders(404, 0);
                    break;
                case "DELETE" :
                    try {
                        if (Pattern.matches("/tasks/task/", path) && (id > 0)) {
                            if (manager.getSimpleTaskById(id) != null) {
                                manager.removeTaskById(id);
                                exchange.sendResponseHeaders(200, 0);
                                System.out.println("Задача успешно удалена");
                            } else {
                                exchange.sendResponseHeaders(404, 0);
                                System.out.println("Задача для удаления не найдена");
                            }
                        }
                        if (Pattern.matches("^/tasks/task/$", path) && (id == -1)) {
                            manager.cleanSimpleTasks();
                            exchange.sendResponseHeaders(200, 0);
                        }
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }

            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            exchange.close();
        }
    }

    private void epicHandler(HttpExchange exchange) {
        try {
            String param = getParam(exchange);
            int id = -1;
            if (param != null) id = parseIntFromString(param.replaceFirst("id=", ""));
            String path = getPath(exchange);
            String method = exchange.getRequestMethod();

            switch (method) {
                case "GET" :
                    if ((id > 0) &&
                            (Pattern.matches("/tasks/epic/", path)))
                    {
                        if (manager.getEpicTaskById(id) != null) {
                            String response = gson.toJson(manager.getEpicTaskById(id));
                            sendText(exchange, response);
                            exchange.sendResponseHeaders(200, 0);
                        } else {
                            exchange.sendResponseHeaders(404, 0);
                            System.out.println("Задачи с таким ID нет");
                        }
                    }
                    if ((id == -1) &&
                            (Pattern.matches("^/tasks/epic/$", path))) {
                        if (manager.getEpicTaskList() != null) {
                            String response = gson.toJson(manager.getEpicTaskList());
                            sendText(exchange, response);
                            exchange.sendResponseHeaders(200, 0);
                        } else {
                            exchange.sendResponseHeaders(404, 0);
                            System.out.println("Список эпиков пуст");
                        }
                    }
                    break;
                case "POST" :
                    if ((id == -1) &&
                            (Pattern.matches("^/tasks/epic/$", path))) {
                        EpicTask task = gson.fromJson(readText(exchange), EpicTask.class);
                        manager.createEpicTask(task);
                        exchange.sendResponseHeaders(200, 0);
                        System.out.println("Задача " + manager.getEpicTaskById(task.getIndex()) + " с ID " +
                                task.getIndex() + " успешно создана");
                    }
                    if ((id > 0) &&
                            (Pattern.matches("/tasks/epic/", path))) {
                        EpicTask task = gson.fromJson(readText(exchange), EpicTask.class);
                        manager.updateEpicTask(id, task);
                        exchange.sendResponseHeaders(200, 0);
                        System.out.println("Задача с ID " + id + " успешно обновлена");
                    }
                    break;
                case "DELETE" :
                    if ((id == -1) &&
                            (Pattern.matches("^/tasks/epic/$", path))) {
                        manager.cleanEpicTasks();
                        exchange.sendResponseHeaders(200, 0);
                    }
                    if ((id > 0) &&
                            (Pattern.matches("/tasks/epic/", path))) {
                        if (manager.getEpicTaskById(id) != null) {
                            manager.removeTaskById(id);
                            exchange.sendResponseHeaders(200, 0);
                        } else {
                            exchange.sendResponseHeaders(404, 0);
                            System.out.println("Задача для удаления не найдена");
                        }
                    }
                    break;
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            exchange.close();
        }
    }

    private void subHandler(HttpExchange exchange) {
        try {
            String param = getParam(exchange);
            int id = -1;
            if (param != null) id = parseIntFromString(param.replaceFirst("id=", ""));
            String path = getPath(exchange);
            String method = exchange.getRequestMethod();

            switch (method) {
                case "GET" :
                    if ((id > 0) && (Pattern.matches("/tasks/sub/", path))) {
                        if (manager.getSubTaskById(id) != null) {
                            String response = gson.toJson(manager.getSubTaskById(id));
                            sendText(exchange, response);
                            exchange.sendResponseHeaders(200, 0);
                        } else {
                            exchange.sendResponseHeaders(404, 0);
                            System.out.println("Задачи с таким ID нет");
                        }
                    }
                    if ((id == -1) && (Pattern.matches("^/tasks/sub/$", path)))  {
                            try {
                                EpicTask task = gson.fromJson(readText(exchange), EpicTask.class);
                                if (manager.getSubTaskList(task) != null) {
                                    String response = gson.toJson(manager.getSubTaskList(task));
                                    sendText(exchange, response);
                                    exchange.sendResponseHeaders(200, 0);
                                }
                            } catch (JsonSyntaxException | IOException e) {
                                throw new RuntimeException(e);
                            }

                    }
                    break;
                case "POST" :
                    if ((id == -1) && (Pattern.matches("^/tasks/sub/$", path))) {
                        SubTask task = gson.fromJson(readText(exchange), SubTask.class);
                        manager.createSubTask(task);
                        exchange.sendResponseHeaders(200, 0);
                        System.out.println("Подзадача для эпика c ID: " + task.getEpicTaskId() + " под ID: " +
                                task.getIndex() + " успешно создана");
                    }
                    if ((id > 0) && (Pattern.matches("/tasks/sub", path))) {
                        SubTask task = gson.fromJson(readText(exchange), SubTask.class);
                        manager.updateSubTask(id, task);
                        exchange.sendResponseHeaders(200, 0);
                    }
                    break;
                case "DELETE" :
                    if ((id == -1) && (Pattern.matches("^/tasks/sub/$", path))) {
                        manager.cleanSubTasks();
                        exchange.sendResponseHeaders(200, 0);
                    }
                    if ((id > 0) && (Pattern.matches("/tasks/sub/", path))) {
                        manager.removeTaskById(id);
                        exchange.sendResponseHeaders(200, 0);
                    }
                    break;
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            exchange.close();
        }
    }

    private void tasksHandler(HttpExchange exchange) {
        try {
            String path = getPath(exchange);
            String method = exchange.getRequestMethod();

            switch (path) {
                case "/tasks/" :
                    if (method.equals("DELETE")) {
                        try {
                            manager.removeAllTask();
                            exchange.sendResponseHeaders(200, 0);
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }
                    break;
                case "/tasks/names/" :
                    if (method.equals("GET")) {
                        String response = gson.toJson(manager.getListOfTaskNames());
                        sendText(exchange, response);
                        exchange.sendResponseHeaders(200, 0);
                    }
                    break;
                case "/tasks/history/" :
                    if (method.equals("GET")) {
                        String response = gson.toJson(manager.getHistory());
                        sendText(exchange, response);
                        exchange.sendResponseHeaders(200, 0);
                    }
                    break;
                case "/tasks/prioritised/" :
                    if (method.equals("GET")) {
                        String response = gson.toJson(manager.getPrioritisedTask());
                        sendText(exchange, response);
                        exchange.sendResponseHeaders(200, 0);
                    }
                    break;
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            exchange.close();
        }
    }

    private int parseIntFromString(String str) {
        try {
            return Integer.parseInt(str);
        } catch (NumberFormatException exception) {
            return -1;
        }
    }

    private String readText(HttpExchange exchange) throws IOException {
        return new String(exchange.getRequestBody().readAllBytes(), DEFAULT_CHARSET);
    }

    private void sendText(HttpExchange exchange, String text) throws IOException {
        byte[] resp = text.getBytes(UTF_8);
        exchange.getResponseHeaders().add("Content-Type", "application/json");
        exchange.sendResponseHeaders(200, resp.length);
        exchange.getResponseBody().write(resp);
    }

    private String getPath(HttpExchange exchange) {
        return exchange.getRequestURI().getPath();
    }

    private String getParam(HttpExchange exchange) {
        if (exchange.getRequestURI().getQuery() != null) {
            return exchange.getRequestURI().getQuery();
        } else {
            return null;
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