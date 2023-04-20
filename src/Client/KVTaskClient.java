package Client;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import com.google.gson.Gson;

import static java.net.URI.create;

public class KVTaskClient {

    private final Gson gson = new Gson();
    private final URI uri;
    private final HttpClient client;
    private final String API_TOKEN;

    public KVTaskClient(String uriString){
        this.client = HttpClient.newHttpClient();
        this.uri = URI.create(uriString);
        API_TOKEN = register(uri);
    }

    private String register(URI uri){
        try {
            HttpRequest request = HttpRequest.newBuilder().GET().uri(URI.create(uri + "/register")).build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            return response.body();
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
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
        HttpRequest request = HttpRequest.newBuilder().
                GET().
                uri(URI.create(uri + key + "?API_TOKEN=" + API_TOKEN)).
                build();
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            return gson.toJson(response.body());
        } catch (IOException | InterruptedException e) {
            e.getStackTrace();
            return "";
        }
    }
}
