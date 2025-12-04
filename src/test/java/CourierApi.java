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
    public Response delete(String courierId) {
        return given()
                .header("Content-type", "application/json")
                .when()
                .delete("/api/v1/courier/" + courierId);
    }

    private static class LoginRequest {
        private String login;
        private String password;

        public LoginRequest(String login, String password) {
            this.login = login;
            this.password = password;
        }

        public String getLogin() {
            return login;
        }

        public String getPassword() {
            return password;
        }
    }
}
