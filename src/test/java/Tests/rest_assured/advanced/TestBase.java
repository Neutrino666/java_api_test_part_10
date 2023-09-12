package Tests.rest_assured.advanced;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import org.junit.jupiter.api.BeforeEach;

import static org.hamcrest.Matchers.*;

public class TestBase {

    RequestSpecification specification;

    @BeforeEach
    public void beforeEach() {
        specification = new RequestSpecBuilder()
                .setBaseUri("https://petstore.swagger.io/v2/")
                .addHeader("Content-Type","application/json;;charset=UTF-8")
                .addHeader("Accept","application/json")
                .addHeader("Accept-Encoding","gzip, deflate, br")
                .build();
    }

    /*
    Метод возвращает RequestSpecification с проверкой кода статуса ответа
     */
    protected ResponseSpecification getAssertionSpec() {
        ResponseSpecBuilder builder = new ResponseSpecBuilder();
        builder.expectStatusCode(200);
        return builder.build();
    }

    /*
    Метод возвращает спецификацию проверки тела ответа на запрос GET baseURL/pet/findByStatus
     */
    protected ResponseSpecification getFindByStatusResp() {
        ResponseSpecBuilder builder = new ResponseSpecBuilder();
        builder
                .addResponseSpecification(getAssertionSpec())
                .expectBody("$.size()", greaterThan(1));
        return builder.build();
    }

    protected ResponseSpecification getCreatePetResp() {
        return getPetSpecResp(25, 4, "dog", "Murka");
    }

    protected ResponseSpecification getPetSpecResp(int id, int categoryId, String categoryName, String name) {
        ResponseSpecBuilder builder = new ResponseSpecBuilder();
        builder
                .addResponseSpecification(getAssertionSpec())
                .expectBody("id", equalTo(id))
                .expectBody("category.id", equalTo(categoryId))
                .expectBody("category.name", equalTo(categoryName))
                .expectBody("name", equalTo(name));
        return builder.build();
    }

    protected ResponseSpecification getUpdatePetResp(int id, int categoryId, String categoryName, String name, String tagName) {
        ResponseSpecBuilder builder = new ResponseSpecBuilder();
        builder
                .addResponseSpecification(getPetSpecResp(id, categoryId, categoryName, name))
                .expectBody("tags.name", hasItem("adorable"));
        return builder.build();
    }

    protected ResponseSpecification getUpdatePetWithFromResp(int code, String message) {
        ResponseSpecBuilder builder = new ResponseSpecBuilder();
        builder
                .addResponseSpecification(getAssertionSpec())
                .expectBody("code", equalTo(code))
                .expectBody("message", equalTo(message));
        return builder.build();
    }

    /*
    Метод возвращает базовую RequestSpecification спецификацию со значениями общими для всех вызываемых методов
     */

    private RequestSpecification getBaseSpecification() {
        return new RequestSpecBuilder()
                .setBaseUri("https://petstore.swagger.io/v2/")
                .addHeader("Content-Type","application/json;;charset=UTF-8")
                .addHeader("Accept","application/json")
                .addHeader("Accept-Encoding","gzip, deflate, br")
                .addHeader("api-key","api_key")
                .build();
    }

    /*
    Метод возвращает RequestSpecification для метода GET baseURL/pet/findByStatus
     */
    protected RequestSpecification getPetByStatus(String status) {
        return new RequestSpecBuilder()
                .addRequestSpecification(getBaseSpecification())
                .setBasePath("pet/findByStatus")
                .addQueryParam("status", status)
                .build();
    }

    /*
    Метод возвращает RequestSpecification для метода POST baseURL/pet
     */
    protected RequestSpecification getCreatePet(String body) {
        return new RequestSpecBuilder()
                .addRequestSpecification(getBaseSpecification())
                .setBasePath("pet")
                .setBody(body)
                .build();
    }

    protected RequestSpecification getPetSpec(int petId) {
        return new RequestSpecBuilder()
                .addRequestSpecification(getBaseSpecification())
                .setBasePath("pet/" + petId)
                .build();
    }

    /*

     */
    protected RequestSpecification getUpdatePetSpec(String body) {
        return getCreatePet(body);
    }

    /*
    Метод возвращает RequestSpecification для метода POST baseURL/pet/{petID} с from data
     */
    protected RequestSpecification getUpdatePetWithFromSpec(int petId, String name, String status) {
        return new RequestSpecBuilder()
                .addRequestSpecification(getBaseSpecification())
                .setBasePath("pet/" + petId)
                .setContentType("application/x-www-form-urlencoded")
                .addFormParam("name", name)
                .addFormParam("status", status)
                .build();
    }

    protected RequestSpecification getDeletePetSpec(int petId) {
        return getPetSpec(petId);
    }
}