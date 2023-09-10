package Tests.url_connection;


import org.junit.jupiter.api.*;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
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
    public void test1() throws IOException {

        // Формируем запрос
        URL baseurl = new URL("https://petstore.swagger.io/v2/pet/findByStatus?status=available");
        HttpURLConnection connection = (HttpURLConnection) baseurl.openConnection();
        connection.setRequestMethod("GET");
        connection.setRequestProperty("Content-Type","application/json");
        connection.setRequestProperty("Accept","application/json");
        // connection.setRequestProperty("Accept","application/xml");
        connection.setRequestProperty("Accept-Encoding","gzip, deflate, br");

        // Получаем ответ
        StringBuilder content = new StringBuilder();

        try (BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
            String inputLine;

            while ((inputLine = in.readLine()) != null) {
                content.append(inputLine);
            }
        } catch (final Exception ex) {
            ex.printStackTrace();
        }
        Map<String, List<String>> headerMap = connection.getHeaderFields();
        for (Map.Entry item : headerMap.entrySet()) {
            System.out.println(item.getKey() + " " + item.getValue());
        }
        System.out.println(content);

        Assertions.assertTrue(headerMap.keySet().contains("Server"));
    }

    @Test
    @Order(2)
    @DisplayName("Добавление нового питомца")
    public void test2() throws IOException {

        // Формируем запрос
        URL baseurl = new URL("https://petstore.swagger.io/v2/pet");
        HttpURLConnection connection = (HttpURLConnection) baseurl.openConnection();
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type","application/json");
        connection.setRequestProperty("Accept","application/json");
        connection.setRequestProperty("Accept-Encoding","gzip, deflate, br");
        connection.setDoOutput(true);
        try (OutputStream outputStream = connection.getOutputStream()) {
            outputStream.write(requestBody.getBytes(StandardCharsets.UTF_8));
        }

        // Получаем ответ
        StringBuilder content = new StringBuilder();

        try (BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
            String inputLine;

            while ((inputLine = in.readLine()) != null) {
                content.append(inputLine);
            }
        } catch (final Exception ex) {
            ex.printStackTrace();
        }
        Map<String, List<String>> headerMap = connection.getHeaderFields();
        for (Map.Entry item : headerMap.entrySet()) {
            System.out.println(item.getKey() + " " + item.getValue());
        }
        System.out.println(content);
        Assertions.assertTrue(content.toString().contains("12"));
    }

    @Test
    @Order(3)
    @DisplayName("Поиск питомца по его id")
    public void test3() throws IOException {

        //Формируем запрос
        URL baseurl = new URL("https://petstore.swagger.io/v2/pet/" + petId);
        HttpURLConnection connection = (HttpURLConnection) baseurl.openConnection();
        connection.setRequestMethod("GET");
        connection.setRequestProperty("Content-Type","application/json");
        connection.setRequestProperty("Accept","application/json");
        connection.setRequestProperty("Accept-Encoding","gzip, deflate, br");

        // Получаем ответ
        StringBuilder content = new StringBuilder();

        try (BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
            String inputLine;

            while ((inputLine = in.readLine()) != null) {
                content.append(inputLine);
            }
        } catch (final Exception ex) {
            ex.printStackTrace();
        }
        Map<String, List<String>> headerMap = connection.getHeaderFields();
        for (Map.Entry item : headerMap.entrySet()) {
            System.out.println(item.getKey() + " " + item.getValue());
        }
        System.out.println(content);
        Assertions.assertTrue(content.toString().contains("12"));
        Assertions.assertTrue(content.toString().contains("Murka"));
    }

    @Test
    @Order(4)
    @DisplayName("Обновление информации по существующему питомцу")
    public void test4() throws IOException {

        // Изменяем тело запроса
        JSONObject jsonObject = new JSONObject(requestBody);
        jsonObject.put("name", "Lucky");

        // Формируем запрос
        URL baseurl = new URL("https://petstore.swagger.io/v2/pet");
        HttpURLConnection connection = (HttpURLConnection) baseurl.openConnection();
        connection.setRequestMethod("PUT");
        connection.setRequestProperty("Content-Type","application/json");
        connection.setRequestProperty("Accept","application/json");
        connection.setRequestProperty("Accept-Encoding","gzip, deflate, br");
        connection.setDoOutput(true);
        try (OutputStream outputStream = connection.getOutputStream()) {
            outputStream.write(jsonObject.toString().getBytes(StandardCharsets.UTF_8));
        }

        // Получаем ответ
        StringBuilder content = new StringBuilder();

        try (BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
            String inputLine;

            while ((inputLine = in.readLine()) != null) {
                content.append(inputLine);
            }
        } catch (final Exception ex) {
            ex.printStackTrace();
        }
        Map<String, List<String>> headerMap = connection.getHeaderFields();
        for (Map.Entry item : headerMap.entrySet()) {
            System.out.println(item.getKey() + " " + item.getValue());
        }
        System.out.println(content);
        Assertions.assertTrue(content.toString().contains("Lucky"));
    }

    @Test
    @Order(5)
    @DisplayName("Обновление информации по существующему питомцу через форму")
    public void test5() throws IOException {

        //Создаем тело запроса
        Map<String, String> fromData = new HashMap<>();
        fromData.put("name", "Coco");

        URL baseurl = new URL("https://petstore.swagger.io/v2/pet/" + petId);
        HttpURLConnection connection = (HttpURLConnection) baseurl.openConnection();
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Accept","application/json");
        connection.setRequestProperty("Content-Type","application/x-www-form-urlencoded");
        connection.setRequestProperty("Accept-Encoding","gzip, deflate, br");
        connection.setDoOutput(true);
        try (OutputStream outputStream = connection.getOutputStream()) {
            outputStream.write(getFormDataAsString(fromData).getBytes(StandardCharsets.UTF_8));
        }

        // Получаем ответ
        StringBuilder content = new StringBuilder();

        try (BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
            String inputLine;

            while ((inputLine = in.readLine()) != null) {
                content.append(inputLine);
            }
        } catch (final Exception ex) {
            ex.printStackTrace();
        }
        Map<String, List<String>> headerMap = connection.getHeaderFields();
        for (Map.Entry item : headerMap.entrySet()) {
            System.out.println(item.getKey() + " " + item.getValue());
        }
        System.out.println(content);
//        Assertions.assertTrue(content.toString().contains("12"));
    }

    @Test
    @Order(6)
    @DisplayName("Поиск питомца по его id")
    public void test6() throws IOException {

        //Формируем запрос
        URL baseurl = new URL("https://petstore.swagger.io/v2/pet/" + petId);
        HttpURLConnection connection = (HttpURLConnection) baseurl.openConnection();
        connection.setRequestMethod("GET");
        connection.setRequestProperty("Content-Type","application/json");
        connection.setRequestProperty("Accept","application/json");
        connection.setRequestProperty("Accept-Encoding","gzip, deflate, br");

        // Получаем ответ
        StringBuilder content = new StringBuilder();

        try (BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
            String inputLine;

            while ((inputLine = in.readLine()) != null) {
                content.append(inputLine);
            }
        } catch (final Exception ex) {
            ex.printStackTrace();
        }
        Map<String, List<String>> headerMap = connection.getHeaderFields();
        for (Map.Entry item : headerMap.entrySet()) {
            System.out.println(item.getKey() + " " + item.getValue());
        }
        System.out.println(content);
        Assertions.assertTrue(content.toString().contains("12"));
        Assertions.assertTrue(content.toString().contains("Coco"));
    }

    @Test
    @Order(7)
    @DisplayName("Удаление информации о питомце по его id")
    public void test7() throws IOException {

        //Формируем запрос
        URL baseurl = new URL("https://petstore.swagger.io/v2/pet/" + petId);
        HttpURLConnection connection = (HttpURLConnection) baseurl.openConnection();
        connection.setRequestMethod("DELETE");
        connection.setRequestProperty("Content-Type","application/json");
        connection.setRequestProperty("Accept","application/json");
        connection.setRequestProperty("Accept-Encoding","gzip, deflate, br");

        // Получаем ответ
        StringBuilder content = new StringBuilder();

        try (BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
            String inputLine;

            while ((inputLine = in.readLine()) != null) {
                content.append(inputLine);
            }
        } catch (final Exception ex) {
            ex.printStackTrace();
        }
        Map<String, List<String>> headerMap = connection.getHeaderFields();
        for (Map.Entry item : headerMap.entrySet()) {
            System.out.println(item.getKey() + " " + item.getValue());
        }
        System.out.println(content);
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
