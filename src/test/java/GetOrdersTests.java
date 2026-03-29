import io.qameta.allure.Description;
import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.Before;
import org.junit.Test;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.notNullValue;

public class GetOrdersTests {

    @Before
    public void setUp() {
        baseURI = "https://qa-scooter.praktikum-services.ru";
    }

    @Test
    @DisplayName("Получение списка заказов")
    @Description("Проверяем, что API возвращает список заказов")
    public void getOrdersTest() {
        Response response = getOrders();

        response.then()
                .statusCode(200)
                .body("orders", notNullValue());
    }

    @Step("Получаем список заказов")
    public Response getOrders() {
        return given()
                .when()
                .get("/api/v1/orders");
    }
}