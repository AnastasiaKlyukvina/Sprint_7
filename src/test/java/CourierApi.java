import io.qameta.allure.Step;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

public class CourierApi {

    @Step("Создать курьера")
    public Response create(Courier courier) {
        return given()
                .header("Content-type", "application/json")
                .body(courier)
                .when()
                .post("/api/v1/courier");
    }

    @Step("Авторизация курьера")
    public Response login(String login, String password) {
        LoginRequest loginRequest = new LoginRequest(login, password);

        return given()
                .header("Content-type", "application/json")
                .body(loginRequest)
                .when()
                .post("/api/v1/courier/login");
    }

    @Step("Удалить курьера с ID: {courierId}")
    public void delete(String courierId) {
        given()
                .header("Content-type", "application/json")
                .when()
                .delete("/api/v1/courier/" + courierId);
    }

}