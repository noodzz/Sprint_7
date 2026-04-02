import io.qameta.allure.junit4.DisplayName;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;

public class CreateCourierPositiveTest extends BaseTest {

    private Courier courier;
    private CourierApi courierApi;
    private int courierId;

    @Before
    public void setUp() {
        courier = CourierGenerator.getRandomCourier();
        courierApi = new CourierApi();
    }

    @Test
    @DisplayName("Курьера можно создать")
    public void createCourierSuccess() {
        courierApi.createCourier(courier)
                .then()
                .statusCode(201)
                .body("ok", equalTo(true));

        courierId = courierApi.loginCourier(courier)
                .then()
                .extract()
                .path("id");
    }

    @After
    public void tearDown() {
        if (courierId != 0) {
            courierApi.deleteCourier(courierId)
                    .then()
                    .statusCode(200);
        }
    }
}