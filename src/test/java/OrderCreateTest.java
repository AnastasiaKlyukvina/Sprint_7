import io.qameta.allure.Description;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class OrderCreateTest {

    @BeforeEach
    public void setUp() {
        RestAssured.baseURI = "https://qa-scooter.praktikum-services.ru";
    }

    @ParameterizedTest
    @MethodSource("provideColorTestData")
    @Description("Проверка создания заказа с разными вариантами цветов")
    public void createOrderWithDifferentColorOptions(String testDescription, String colorJson) {

        String baseJson = "{\n" +
                "    \"firstName\": \"Иван\",\n" +
                "    \"lastName\": \"Михайлов\",\n" +
                "    \"address\": \"Москва\",\n" +
                "    \"metroStation\": 4,\n" +
                "    \"phone\": \"+7 800 355 35 89\",\n" +
                "    \"rentTime\": 5,\n" +
                "    \"deliveryDate\": \"2025-12-30\",\n" +
                "    \"comment\": \"Комментарий\"";

        String fullJson;
        if (colorJson.isEmpty()) {
            fullJson = baseJson + "\n}";
        } else {
            fullJson = baseJson + ",\n" + colorJson + "\n}";
        }

        Response response =
                given()
                        .header("Content-type", "application/json")
                        .body(fullJson)
                        .when()
                        .post("/api/v1/orders");

        response.then().assertThat()
                .body("track", notNullValue())
                .and()
                .statusCode(201);
    }

    static Stream<Arguments> provideColorTestData() {
        return Stream.of(

                Arguments.of("С цветом BLACK",
                        "    \"color\": [\"BLACK\"]"),


                Arguments.of("С цветом GREY",
                        "    \"color\": [\"GREY\"]"),


                Arguments.of("С обоими цветами",
                        "    \"color\": [\"BLACK\", \"GREY\"]"),


                Arguments.of("С пустым массивом цветов",
                        "    \"color\": []"),


                Arguments.of("Без поля color",
                        "")
        );
    }
}
