import io.qameta.allure.Description;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class CourierLoginTest {

    @BeforeEach
    public void setUp() {
        RestAssured.baseURI = "https://qa-scooter.praktikum-services.ru";
    }

    @Test
    @Description("Проверка что курьер может авторизоваться и возвращается id")
    public void courierCanLoginSuccessfully() {
        // Создание курьера
        String createJson = "{\"login\": \"nina\", \"password\": \"10203\", \"firstName\": \"dobrev\"}";

        given()
                .header("Content-type", "application/json")
                .body(createJson)
                .when()
                .post("/api/v1/courier")
                .then()
                .statusCode(201);

        // Тест авторизации
        String loginJson = "{\"login\": \"nina\", \"password\": \"10203\"}";

        Response response =
                given()
                        .header("Content-type", "application/json")
                        .body(loginJson)
                        .when()
                        .post("/api/v1/courier/login");

        response.then().assertThat()
                .body("id", notNullValue())
                .and()
                .statusCode(200);
    }

    @Test
    @Description("Проверка что для авторизации обязательно нужен логин")
    public void loginWithoutLoginReturnsError() {
        String json = "{\"password\": \"10203\"}";

        Response response =
                given()
                        .header("Content-type", "application/json")
                        .body(json)
                        .when()
                        .post("/api/v1/courier/login");

        response.then().assertThat()
                .body("message", equalTo("Недостаточно данных для входа"))
                .and()
                .statusCode(400);
    }

    @Test
    @Description("Проверка что для авторизации обязательно нужен пароль")
    public void loginWithoutPasswordReturnsError() {
        String json = "{\"login\": \"nina\"}";

        Response response =
                given()
                        .header("Content-type", "application/json")
                        .body(json)
                        .when()
                        .post("/api/v1/courier/login");

        response.then().assertThat()
                .body("message", equalTo("Недостаточно данных для входа"))
                .and()
                .statusCode(400);
    }

    @Test
    @Description("Проверка ошибки при авторизации несуществующего пользователя")
    public void loginNonExistentUserReturnsError() {
        String json = "{\"login\": \"ghygfcg\", \"password\": \"87656788\"}";

        Response response =
                given()
                        .header("Content-type", "application/json")
                        .body(json)
                        .when()
                        .post("/api/v1/courier/login");

        response.then().assertThat()
                .body("message", equalTo("Учетная запись не найдена"))
                .and()
                .statusCode(404);
    }
}


