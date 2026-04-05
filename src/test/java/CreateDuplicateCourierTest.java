import io.qameta.allure.junit4.DisplayName;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;

public class CreateDuplicateCourierTest extends BaseTest {

    private Courier courier;
    private CourierApi courierApi;
    private int courierId;

    @Before
    public void setUp() {
        courierApi = new CourierApi();
        courier = CourierGenerator.getRandomCourier();
    }

    @Test
    @DisplayName("Нельзя создать двух одинаковых курьеров")
    public void cannotCreateDuplicateCourier() {
        courierApi.createCourier(courier)
                .then()
                .statusCode(201)
                .body("ok", equalTo(true));

        courierApi.createCourier(courier)
                .then()
                .statusCode(409);

        courierId = courierApi.loginCourier(courier)
                .then()
                .extract()
                .path("id");
    }

    @After
    public void tearDown() {
        if (courierId != 0) {
            courierApi.deleteCourier(courierId);
        }
    }
}