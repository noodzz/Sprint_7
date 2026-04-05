import io.qameta.allure.Step;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

public class OrderApi {

    @Step("Создаём заказ с цветом {order.color}")
    public Response createOrder(Order order) {
        return given()
                .header("Content-type", "application/json")
                .body(order)
                .when()
                .post("/api/v1/orders");
    }

    @Step("Получаем список заказов")
    public Response getOrders() {
        return given()
                .header("Content-type", "application/json")
                .when()
                .get("/api/v1/orders");
    }
}