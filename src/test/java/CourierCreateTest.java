import io.qameta.allure.Description;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class CourierCreateTest {

    private String courierId;

    @BeforeEach
    public void setUp() {
        RestAssured.baseURI = "https://qa-scooter.praktikum-services.ru";
    }

    @AfterEach
    public void tearDown() {

        if (courierId != null) {
            given()
                    .header("Content-type", "application/json")
                    .when()
                    .delete("/api/v1/courier/" + courierId);
        }
    }

    private String loginCourier(String login, String password) {
        String loginBody = "{\"login\": \"" + login + "\", \"password\": \"" + password + "\"}";
        Response response = given()
                .header("Content-type", "application/json")
                .body(loginBody)
                .when()
                .post("/api/v1/courier/login");
        return response.jsonPath().getString("id");
    }

    @Test
    @Description("Проверка что курьер создается с валидными данными и возвращается ok: true")
    public void createCourierSuccess() {
        String json = "{\"login\": \"ivan\", \"password\": \"12345\", \"firstName\": \"Иван\"}";

        Response response =
                given()
                        .header("Content-type", "application/json")
                        .body(json)
                        .when()
                        .post("/api/v1/courier");

        response.then().assertThat()
                .body("ok", equalTo(true))
                .and()
                .statusCode(201);

        courierId = loginCourier("ivan", "12345");
    }

    @Test
    @Description("Проверка что нельзя создать двух курьеров с одинаковым логином")
    public void createDuplicateCourierError() {

        String firstJson = "{\"login\": \"ivan01\", \"password\": \"12345\", \"firstName\": \"Иван\"}";

        Response firstResponse =
                given()
                        .header("Content-type", "application/json")
                        .body(firstJson)
                        .when()
                        .post("/api/v1/courier");

        firstResponse.then().assertThat()
                .body("ok", equalTo(true))
                .and()
                .statusCode(201);

        courierId = loginCourier("ivan01", "12345");

        String secondJson = "{\"login\": \"ivan01\", \"password\": \"12345\", \"firstName\": \"Иван\"}";

        Response secondResponse =
                given()
                        .header("Content-type", "application/json")
                        .body(secondJson)
                        .when()
                        .post("/api/v1/courier");

        secondResponse.then().assertThat()
                .body("message", equalTo("Этот логин уже используется. Попробуйте другой."))
                .and()
                .statusCode(409);
    }

    @Test
    @Description("Проверка что логин является обязательным полем")
    public void createCourierWithoutLoginError() {

        String json = "{\"password\": \"12345\", \"firstName\": \"Иван\"}";

        Response response =
                given()
                        .header("Content-type", "application/json")
                        .body(json)
                        .when()
                        .post("/api/v1/courier");

        response.then().assertThat()
                .body("message", equalTo("Недостаточно данных для создания учетной записи"))
                .and()
                .statusCode(400);
    }

    @Test
    @Description("Проверка что пароль является обязательным полем")
    public void createCourierWithoutPasswordError() {

        String json = "{\"login\": \"ivan\", \"firstName\": \"Иван\"}";

        Response response =
                given()
                        .header("Content-type", "application/json")
                        .body(json)
                        .when()
                        .post("/api/v1/courier");

        response.then().assertThat()
                .body("message", equalTo("Недостаточно данных для создания учетной записи"))
                .and()
                .statusCode(400);
    }
}