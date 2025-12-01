import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

public class GetOrdersTest {

    @BeforeEach
    public void setUp() {
        baseURI = "https://qa-scooter.praktikum-services.ru";
    }

    @Test
    public void testGetOrdersReturnsList() {
        given()
                .header("Content-type", "application/json")
                .when()
                .get("/api/v1/orders")
                .then()
                .statusCode(200)
                .body("orders", not(empty()));
    }
}
