package Tests.rest_assured.advanced;

import org.json.JSONObject;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static helpers.Utils.readFile;
import static io.restassured.RestAssured.given;

public class AllInOne extends TestBase {

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
    @DisplayName("Вызов нескольких методов в одно тесте")
    public void test1() {

        /*
        Вызов метода по созданию питомца
         */
        Integer petId = given(getCreatePet(requestBody))
                .post()
                .then()
                .spec(getCreatePetResp()).extract().path("id");

        /*
        вызов метода по получению питомца
         */
        given(getPetSpec(petId))
                .get()
                .then()
                .spec(getPetSpecResp(petId, 4, "dog", "Murka"));

        /*
        Изменение информации по питомцу
         */
        //Изменяем тело запроса
        JSONObject jsonObject = new JSONObject(requestBody);
        jsonObject.put("name", "Lucky");

        given(getUpdatePetSpec(jsonObject.toString()))
                .put()
                .then()
                .spec(getUpdatePetResp(petId, 4, "dog", "Lucky", "adorable"));

        /*
        Удаление информации по питомцу по его ID
         */
        given(getDeletePetSpec(petId))
                .delete()
                .then()
                .spec(getAssertionSpec());

    }
}
