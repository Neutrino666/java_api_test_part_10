package Tests.rest_assured.advanced;

import org.json.JSONObject;
import org.junit.jupiter.api.*;

import static helpers.Utils.readFile;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.Matchers.equalTo;

/**
 * Класс с базовыми местами
 */

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class Advanced1 extends TestBase {

    String requestBody = null;
    Integer petId = 25;
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
    public void test1() {

        given(specification)
                .basePath("pet/findByStatus")
                .queryParam("status", "available")
                .get()
                .then()
                .statusCode(200)
                .body("$.size()", greaterThan(1));
    }

    @Test
    @Order(2)
    @DisplayName("Добавление нового питомца")
    public void test2() {

        given(specification)
                .basePath("pet")
                .body(requestBody)
                .post()
                .then()
                .statusCode(200)
                .body("id", equalTo(petId))
                .body("category.id", equalTo(4))
                .body("category.name", equalTo("dog"))
                .body("name", equalTo(petName));
    }

    @Test
    @Order(3)
    @DisplayName("Поиск питомца по его id")
    public void test3() {

        given(specification)
                .basePath("pet/" + petId)
                .get()
                .then()
                .statusCode(200)
                .body("id", equalTo(petId))
                .body("category.id", equalTo(4))
                .body("category.name", equalTo("dog"))
                .body("name", equalTo(petName));
    }

    @Test
    @Order(4)
    @DisplayName("Обновление информации по существующему питомцу")
    public void test4() {

        // Изменяем тело запроса
        JSONObject jsonObject = new JSONObject(requestBody);
        jsonObject.put("name", "Lucky");

        given(specification)
                .basePath("pet")
                .body(jsonObject.toString())
                .put()
                .then()
                .statusCode(200)
                .body("id", equalTo(petId))
                .body("category.id", equalTo(4))
                .body("category.name", equalTo("dog"))
                .body("name", equalTo("Lucky"))
                .body("tags.name", hasItem("adorable"));
    }

    @Test
    @Order(5)
    @DisplayName("Обновление информации по существующему питомцу через форму")
    public void test5() {

        given(specification)
                .basePath("pet/" + petId)
                .header("Content-Type","application/x-www-form-urlencoded")
                .formParam("name", "Coco")
                .formParam("status", "sold")
                .post()
                .then()
                .statusCode(200)
                .body("code", equalTo(200))
                .body("message", equalTo("25"));
    }

    @Test
    @Order(6)
    @DisplayName("Удаление питомца")
    public void test6() {

        given(specification)
                .basePath("pet/" + petId)
                .delete()
                .then()
                .statusCode(200);
    }
}