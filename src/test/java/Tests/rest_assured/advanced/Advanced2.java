package Tests.rest_assured.advanced;

import org.json.JSONObject;
import org.junit.jupiter.api.*;

import static helpers.Utils.readFile;
import static io.restassured.RestAssured.given;

/**
 * Класс с базовыми местами
 */

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class Advanced2 extends TestBase {

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
                .spec(getFindByStatusResp());
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
                .spec(getCreatePetResp());
    }

    @Test
    @Order(3)
    @DisplayName("Поиск питомца по его id")
    public void test3() {

        given(specification)
                .basePath("pet/" + petId)
                .get()
                .then()
                .spec(getPetSpecResp(25, 4, "dog", "Murka"));
    }

    @Test
    @Order(4)
    @DisplayName("Обновление информации по питомцу")
    public void test4() {

        // Изменяем тело запроса
        JSONObject jsonObject = new JSONObject(requestBody);
        jsonObject.put("name", "Lucky");

        given(specification)
                .basePath("pet")
                .body(jsonObject.toString())
                .put()
                .then()
                .spec(getUpdatePetResp(25, 4, "dog", "Lucky", "adorable"));
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
                .spec(getUpdatePetWithFromResp(200, "25"));
    }

    @Test
    @Order(6)
    @DisplayName("Удаление питомца")
    public void test6() {

        given(specification)
                .basePath("pet/" + petId)
                .delete()
                .then()
                .spec(getAssertionSpec());
    }
}