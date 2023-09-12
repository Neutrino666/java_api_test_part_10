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
public class Advenced3 extends TestBase {

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

        given(getPetByStatus("available"))
                .get()
                .then()
                .spec(getFindByStatusResp());
    }

    @Test
    @Order(2)
    @DisplayName("Добавление нового питомца")
    public void test2() {

        given(getCreatePet(requestBody))
                .post()
                .then()
                .spec(getCreatePetResp());
    }

    @Test
    @Order(3)
    @DisplayName("Поиск питомца по его id")
    public void test3() {

        given(getPetSpec(petId))
                .get()
                .then()
                .spec(getPetSpecResp(petId, 4, "dog", "Murka"));
    }

    @Test
    @Order(4)
    @DisplayName("Обновление информации по питомцу")
    public void test4() {

        // Изменяем тело запроса
        JSONObject jsonObject = new JSONObject(requestBody);
        jsonObject.put("name", "Lucky");

        given(getUpdatePetSpec(jsonObject.toString()))
                .put()
                .then()
                .spec(getUpdatePetResp(petId, 4, "dog", "Lucky", "adorable"));
    }

    @Test
    @Order(5)
    @DisplayName("Поиск питомца с обновленной информацией по ID")
    public void test5() {

        given(getPetSpec(petId))
                .get()
                .then()
                .spec(getPetSpecResp(petId, 4, "dog", "Lucky"));
    }

    @Test
    @Order(6)
    @DisplayName("Обновление информации по существующему питомцу через форму")
    public void test6() {

        given(getUpdatePetWithFromSpec(petId, "Coco", "sold"))
                .post()
                .then()
                .spec(getUpdatePetWithFromResp(200, "25"));
    }


    @Test
    @Order(7)
    @DisplayName("Поиск питомца с обновленной информацией {from data} по ID")
    public void test7() {

        given(getPetSpec(petId))
                .get()
                .then()
                .spec(getPetSpecResp(petId, 4, "dog", "Coco"));
    }

    @Test
    @Order(8)
    @DisplayName("Удаление питомца")
    public void test8() {

        given(getDeletePetSpec(petId))
                .delete()
                .then()
                .spec(getAssertionSpec());
    }
}