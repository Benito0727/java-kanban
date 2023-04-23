package Manager;

import Converter.LocalDateTimeJsonConverter;
import Server.HttpTaskServer;
import Server.KVServer;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpClient.Version;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class HttpTaskManagerTest {

    HttpTaskServer taskServer;
    KVServer kvServer = new KVServer();
    HttpTaskManager manager;


    HttpClient client;
    private static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;
    String uriString;

    HttpTaskManagerTest() throws IOException {
        kvServer.start();
        manager = new HttpTaskManager("http://localhost:7540");
        taskServer = new HttpTaskServer(manager);
        client = HttpClient.newHttpClient();
        uriString = "http://localhost:7880/tasks/";
    }


    @BeforeEach
    public void startServer(){
        taskServer.start();
    }

    @AfterEach
    public void stopServer(){
        taskServer.stop();
    }

    private static Gson getGson(){
        return new GsonBuilder()
                .setPrettyPrinting()
                .serializeNulls()
                .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeJsonConverter())
                .setDateFormat("yyyy-MM-dd HH:mm:ss")
                .create();
    }


    @Test
    public void shouldCreateSimpleTask() throws IOException, InterruptedException {
            URI uri = URI.create(uriString + "task/");
            HttpRequest request = HttpRequest.newBuilder().
                    POST(HttpRequest.BodyPublishers.ofString(TestUtils.simpleInString(), DEFAULT_CHARSET)).
                    uri(uri).
                    version(Version.HTTP_1_1).
                    header("Content-Type", "application/json").
                    build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString(DEFAULT_CHARSET));
        assertEquals(200, response.statusCode());
        assertNotNull(manager.getSimpleTaskById(1));
    }

    @Test
    public void shouldCreateEpicTask() throws IOException, InterruptedException {
        URI uri = URI.create(uriString + "epic/");
        HttpRequest request = HttpRequest.newBuilder().
            POST(HttpRequest.BodyPublishers.ofString(TestUtils.epicInString(), DEFAULT_CHARSET)).
            uri(uri).
            version(Version.HTTP_1_1).
            header("Content-Type", "application/json").
            build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString(DEFAULT_CHARSET));

        assertEquals(200, response.statusCode());
        assertNotNull(manager.getEpicTaskById(1));
    }

    @Test
    public void shouldCreateSubTask() throws IOException, InterruptedException {
        URI uri = URI.create(uriString + "epic/");
        HttpRequest request = HttpRequest.newBuilder().
                POST(HttpRequest.BodyPublishers.ofString(TestUtils.epicInString(), DEFAULT_CHARSET)).
                uri(uri).
                version(Version.HTTP_1_1).
                header("Content-Type", "application/json").
                build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString(DEFAULT_CHARSET));

        HttpRequest request1 = HttpRequest.newBuilder().
                POST(HttpRequest.BodyPublishers.ofString(TestUtils.sub1InString(), DEFAULT_CHARSET)).
                uri(URI.create(uriString + "sub/")).
                version(Version.HTTP_1_1).
                header("Content-Type", "application/json").
                build();
        HttpResponse<String> response1 = client.send(request1, HttpResponse.BodyHandlers.ofString(DEFAULT_CHARSET));

        assertNotNull(manager.getEpicTaskById(1));
        assertNotNull(manager.getSubTaskById(2));
        assertEquals(200, response1.statusCode());
    }

    @Test
    public void shouldLoadHttpManager() throws IOException, InterruptedException {
        URI uri = URI.create(uriString + "task/");
        HttpRequest request = HttpRequest.newBuilder().
                POST(HttpRequest.BodyPublishers.ofString(TestUtils.simpleInString())).
                uri(uri).
                version(Version.HTTP_1_1).
                header("Content-Type", "application/json").
                build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        HttpRequest request1 = HttpRequest.newBuilder().
                GET().
                uri(URI.create(uriString + "task/?id=1")).
                version(Version.HTTP_1_1).
                header("Content-Type", "application/json").
                build();
        HttpResponse<String> response1 = client.send(request1, HttpResponse.BodyHandlers.ofString());
        HttpRequest request2 = HttpRequest.newBuilder().
                GET().
                uri(URI.create(uriString + "history/")).
                version(Version.HTTP_1_1).
                header("Content-Type", "application/json").
                build();
        HttpResponse<String> response2 = client.send(request2, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response2.statusCode());
        assertEquals(200, response.statusCode());
        assertEquals(200, response1.statusCode());

        HttpTaskManager newManager = HttpTaskManager.loadManagerFromServer();

        assertNotNull(newManager);
        assertNotNull(newManager.getSimpleTaskById(1));
        assertNotNull(newManager.getHistory());
        assertEquals(1, newManager.getHistory().size());
    }
}