import io.qameta.allure.Description;
import io.restassured.response.Response;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.hamcrest.Matchers.*;

public class CourierTest extends BaseTest {

    private CourierApi courierApi = new CourierApi();
    private Courier testCourier;
    private String courierId;

    @BeforeEach
    public void setUpTestCourier() {

        String uniqueLogin = "courier_" + System.currentTimeMillis();
        testCourier = new Courier(uniqueLogin, "password123", "Иван");

        Response createResponse = courierApi.create(testCourier);
        createResponse.then().statusCode(201);

        Response loginResponse = courierApi.login(testCourier.getLogin(), testCourier.getPassword());
        courierId = loginResponse.jsonPath().getString("id");
    }

    @AfterEach
    public void deleteTestCourier() {
        if (courierId != null) {
            courierApi.delete(courierId);
        }
    }

    @Test
    @Description("Проверка создания курьера с валидными данными")
    public void createCourierSuccess() {
        String uniqueLogin = "new_courier_" + System.currentTimeMillis();
        Courier newCourier = new Courier(uniqueLogin, "password123", "Иван");

        Response response = courierApi.create(newCourier);
        response.then().statusCode(201);

        String newCourierId = courierApi.login(uniqueLogin, "password123")
                .jsonPath().getString("id");
        courierApi.delete(newCourierId);
    }

    @Test
    @Description("Проверка что курьер может авторизоваться")
    public void loginCourierSuccess() {
        Response response = courierApi.login(testCourier.getLogin(), testCourier.getPassword());
        response.then()
                .statusCode(200)
                .body("id", notNullValue());
    }

    @Test
    @Description("Проверка ошибки при неверном пароле")
    public void loginWithWrongPassword() {
        Response response = courierApi.login(testCourier.getLogin(), "wrong_password");
        response.then()
                .statusCode(404)
                .body("message", equalTo("Учетная запись не найдена"));
    }

    @Test
    @Description("Проверка что нельзя создать двух курьеров с одинаковым логином")
    public void createDuplicateCourier() {
        Response response = courierApi.create(testCourier);
        response.then()
                .statusCode(409)
                .body("message", equalTo("Этот логин уже используется"));
    }

    @Test
    @Description("Проверка что пароль является обязательным полем")
    public void createCourierWithoutPassword() {
        Courier courier = new Courier("test_login_" + System.currentTimeMillis(), null, "Иван");
        Response response = courierApi.create(courier);
        response.then()
                .statusCode(400)
                .body("message", equalTo("Недостаточно данных для создания учетной записи"));
    }

    @Test
    @Description("Проверка что для авторизации нужен пароль")
    public void loginWithoutPassword() {
        Response response = courierApi.login(testCourier.getLogin(), "");
        response.then()
                .statusCode(400)
                .body("message", equalTo("Недостаточно данных для входа"));
    }
}