import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.Test;

import static org.hamcrest.Matchers.notNullValue;

public class GetOrdersTests extends BaseTest {

    private final OrderApi orderApi = new OrderApi();

    @Test
    @DisplayName("Получение списка заказов")
    @Description("Проверяем, что API возвращает список заказов")
    public void getOrdersTest() {
        Response response = orderApi.getOrders();

        response.then()
                .statusCode(200)
                .body("orders", notNullValue());
    }
}