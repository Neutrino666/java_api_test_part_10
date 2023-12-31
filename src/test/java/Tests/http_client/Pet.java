package Tests.http_client;

import org.junit.jupiter.api.*;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import static helpers.Utils.readFile;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class Pet {

    String requestBody = null;
    Integer petId = 12;
    String petName = "Murka";

    {
        // Получаем тело запроса
        String initialBody = null;
        initialBody = readFile ("src/test/resources/addNewPet.json");
        JSONObject jsonObject = new JSONObject(initialBody);
        jsonObject.put("id", petId);
        jsonObject.put("name", petName);
        requestBody = jsonObject.toString();
    }

    @Test
    @Order(1)
    @DisplayName("Поиск Питомцев по статусу")
    public void test1() throws IOException, InterruptedException {

        HttpClient client = HttpClient.newBuilder().build();

        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .setHeader("Content-Type","application/json")
                .setHeader("Accept","application/json")
                .setHeader("Accept-Encoding","gzip, deflate, br")
                .uri(URI.create("https://petstore.swagger.io/v2/pet/findByStatus?status=available"))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println(response.body());
        System.out.println(response.headers());
        System.out.println(request.headers());
        Assertions.assertEquals(200, response.statusCode());
    }

    @Test
    @Order(2)
    @DisplayName("Добавление нового питомца")
    public void test2() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newBuilder().build();

        HttpRequest request = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                .setHeader("Content-Type","application/json")
                .setHeader("Accept","application/json")
                .setHeader("Accept-Encoding","gzip, deflate, br")
                .uri(URI.create("https://petstore.swagger.io/v2/pet"))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println(response.body());
        Assertions.assertEquals(200, response.statusCode());
    }

    @Test
    @Order(3)
    @DisplayName("Поиск питомца по его id")
    public void test3() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newBuilder().build();

        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .setHeader("Content-Type","application/json")
                .setHeader("Accept","application/json")
                .setHeader("Accept-Encoding","gzip, deflate, br")
                .uri(URI.create("https://petstore.swagger.io/v2/pet/" + petId))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println(response.body());
        Assertions.assertEquals(200, response.statusCode());
    }

    @Test
    @Order(4)
    @DisplayName("Обновление информации по существующему питомцу")
    public void test4() throws IOException, InterruptedException {
        // Изменяем тело запроса
        JSONObject jsonObject = new JSONObject(requestBody);
        jsonObject.put("name", "Lucky");

        HttpClient client = HttpClient.newBuilder().build();

        HttpRequest request = HttpRequest.newBuilder()
                .PUT(HttpRequest.BodyPublishers.ofString(jsonObject.toString()))
                .setHeader("Content-Type","application/json")
                .setHeader("Accept","application/json")
                .setHeader("Accept-Encoding","gzip, deflate, br")
                .uri(URI.create("https://petstore.swagger.io/v2/pet"))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println(response.body());
    }

    @Test
    @Order(5)
    @DisplayName("Обновление информации по существующему питомцу через форму")
    public void test5() throws IOException, InterruptedException {

        //Создаем тело запроса
        Map<String, String> fromData = new HashMap<>();
        fromData.put("name", "Coco");

        HttpClient client = HttpClient.newBuilder().build();

        HttpRequest request = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(getFormDataAsString(fromData)))
                .setHeader("Content-Type","application/x-www-form-urlencoded")
                .setHeader("Accept","application/json")
                .setHeader("Accept-Encoding","gzip, deflate, br")
                .uri(URI.create("https://petstore.swagger.io/v2/pet/" + petId))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println(response.body());
    }

    @Test
    @Order(6)
    @DisplayName("Удаление информации о питомце по его id")
    public void test6() throws IOException, InterruptedException {

        HttpClient client = HttpClient.newBuilder().build();

        HttpRequest request = HttpRequest.newBuilder()
                .DELETE()
                .setHeader("Content-Type","application/json")
                .setHeader("Accept","application/json")
                .setHeader("Accept-Encoding","gzip, deflate, br")
                .uri(URI.create("https://petstore.swagger.io/v2/pet/" + petId))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println(response.body());
        Assertions.assertEquals(200, response.statusCode());
    }

    private String getFormDataAsString(Map<String, String> fromData) {
        StringBuilder fromBodyBuilder = new StringBuilder();
        for (Map.Entry<String, String> singleEntry : fromData.entrySet()) {
            if (fromBodyBuilder.length() > 0) {
                fromBodyBuilder.append("&");
            }
            fromBodyBuilder.append(URLEncoder.encode(singleEntry.getKey(), StandardCharsets.UTF_8));
            fromBodyBuilder.append("=");
            fromBodyBuilder.append(URLEncoder.encode(singleEntry.getValue(), StandardCharsets.UTF_8));
        }
        return fromBodyBuilder.toString();
    }
}
