import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.UUID;

import static org.hamcrest.Matchers.notNullValue;

public class CourierLoginTests extends BaseTest {

    private Courier courier;
    private CourierApi courierApi;
    private int courierId;

    @Before
    public void setUp() {
        courierApi = new CourierApi();
        courier = CourierGenerator.getRandomCourier();

        courierApi.createCourier(courier)
                .then()
                .statusCode(201);

        courierId = courierApi.loginCourier(courier)
                .then()
                .statusCode(200)
                .extract()
                .path("id");
    }

    @Test
    @DisplayName("Курьер может авторизоваться")
    @Description("Проверяем успешный логин курьера и получение id")
    public void courierCanLogin() {
        courierApi.loginCourier(courier)
                .then()
                .statusCode(200)
                .body("id", notNullValue());
    }

    @Test
    @DisplayName("Ошибка при неверном логине")
    @Description("Если логин неверный - возвращается ошибка")
    public void cannotLoginWithWrongLogin() {
        Courier wrongCourier = new Courier("wrong_login", courier.getPassword(), null);

        courierApi.loginCourier(wrongCourier)
                .then()
                .statusCode(404);
    }

    @Test
    @DisplayName("Ошибка при неверном пароле")
    @Description("Если пароль неверный - возвращается ошибка")
    public void cannotLoginWithWrongPassword() {
        Courier wrongCourier = new Courier(courier.getLogin(), "wrong_pass", null);

        courierApi.loginCourier(wrongCourier)
                .then()
                .statusCode(404);
    }

    @Test
    @DisplayName("Ошибка при отсутствии логина")
    @Description("Если не передан login - возвращается ошибка")
    public void cannotLoginWithoutLogin() {
        Courier courierWithoutLogin = new Courier(null, "1234", null);

        courierApi.loginCourier(courierWithoutLogin)
                .then()
                .statusCode(400);
    }

    @Test
    @DisplayName("Ошибка при отсутствии пароля")
    @Description("Если не передан password - возвращается ошибка")
    public void cannotLoginWithoutPassword() {
        Courier courierWithoutPassword = new Courier(
                "login_" + UUID.randomUUID(),
                null,
                null
        );

        courierApi.loginCourier(courierWithoutPassword)
                .then()
                .statusCode(400);
    }

    @Test
    @DisplayName("Ошибка при логине несуществующего пользователя")
    @Description("Если пользователь не существует - возвращается ошибка")
    public void cannotLoginNonExistentCourier() {
        Courier randomCourier = new Courier(
                "login_" + UUID.randomUUID(),
                "1234",
                null
        );

        courierApi.loginCourier(randomCourier)
                .then()
                .statusCode(404);
    }

    @After
    public void tearDown() {
        if (courierId != 0) {
            courierApi.deleteCourier(courierId);
        }
    }
}