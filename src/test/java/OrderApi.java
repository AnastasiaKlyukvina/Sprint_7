import io.qameta.allure.Step;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

public class OrderApi {

    @Step("Создать заказ")
    public Response create(Order order) {
        return given()
                .header("Content-type", "application/json")
                .body(order)
                .when()
                .post("/api/v1/orders");
    }

    @Step("Получить список заказов")
    public Response getList() {
        return given()
                .header("Content-type", "application/json")
                .when()
                .get("/api/v1/orders");
    }
}
