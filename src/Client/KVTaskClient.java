package Client;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import static java.net.URI.create;

public class KVTaskClient {

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

    public String load(String key){
        try {
            switch (key) {
                case "task":
                    HttpRequest taskRequest = HttpRequest.newBuilder().
                            GET().
                            uri(URI.create(uri + "/load/task" + "?API_TOKEN=" + API_TOKEN)).
                            version(HttpClient.Version.HTTP_1_1).
                            build();
                    HttpResponse<String> taskResponse = client.send(taskRequest, HttpResponse.BodyHandlers.ofString());
                    if (!taskResponse.body().isEmpty()) {
                        return taskResponse.body();
                    }
                    break;
                case "history":
                    HttpRequest historyRequest = HttpRequest.newBuilder().
                            GET().
                            uri(URI.create(uri + "/load/history" + "?API_TOKEN=" + API_TOKEN)).
                            version(HttpClient.Version.HTTP_1_1).
                            build();
                    HttpResponse<String> historyResponse = client.send(historyRequest, HttpResponse.BodyHandlers.ofString());
                    if (!historyResponse.body().isEmpty() && historyResponse.statusCode() == 200) {
                        return historyResponse.body();
                    }
                    break;
            }
        } catch (RuntimeException | IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
        return "";
    }
}
