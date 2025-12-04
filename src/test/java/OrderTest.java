import io.qameta.allure.Description;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.Matchers.*;

public class OrderTest extends BaseTest {

    private OrderApi OrderApi = new OrderApi();

    @Test
    @Description("Проверка что возвращается список заказов")
    public void getOrderList() {
        Response response = OrderApi.getList();
        response.then()
                .statusCode(200)
                .body("orders", notNullValue())
                .body("orders", not(empty()));
    }

    @Test
    @Description("Проверка создания заказа с цветом BLACK")
    public void createOrderWithBlackColor() {
        Order order = createTestOrder(Arrays.asList("BLACK"));
        Response response = OrderApi.create(order);
        verifyOrderCreated(response);
    }

    @Test
    @Description("Проверка создания заказа с цветом GREY")
    public void createOrderWithGreyColor() {
        Order order = createTestOrder(Arrays.asList("GREY"));
        Response response = OrderApi.create(order);
        verifyOrderCreated(response);
    }

    @Test
    @Description("Проверка создания заказа без указания цвета")
    public void createOrderWithoutColor() {
        Order order = createTestOrder(null);
        Response response = OrderApi.create(order);
        verifyOrderCreated(response);
    }

    @Test
    @Description("Проверка создания заказа с цветами BLACK и GREY")
    public void createOrderWithBothColors() {
        Order order = createTestOrder(Arrays.asList("BLACK", "GREY"));
        Response response = OrderApi.create(order);
        verifyOrderCreated(response);
    }

    @Test
    @Description("Проверка создания заказа с пустым массивом цветов")
    public void createOrderWithEmptyColors() {
        Order order = createTestOrder(Collections.emptyList());
        Response response = OrderApi.create(order);
        verifyOrderCreated(response);
    }

    private Order createTestOrder(List<String> colors) {
        return new Order(
                "Антон",
                "Михайлов",
                "Москва",
                4,
                "89993456789",
                5,
                "2025-12-10",
                "Комментарий тест",
                colors
        );
    }

    private void verifyOrderCreated(Response response) {
        response.then()
                .statusCode(201)
                .body("track", notNullValue());
    }

    public OrderApi getOrderApi() {
        return OrderApi;
    }

    public void setOrderApi(OrderApi orderApi) {
        OrderApi = orderApi;
    }
}