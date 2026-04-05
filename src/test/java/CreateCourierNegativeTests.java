import io.qameta.allure.junit4.DisplayName;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.UUID;

import static org.hamcrest.CoreMatchers.notNullValue;

@RunWith(Parameterized.class)
public class CreateCourierNegativeTests extends BaseTest {

    private final String login;
    private final String password;
    private final String firstName;

    private Courier courier;
    private CourierApi courierApi;

    public CreateCourierNegativeTests(String login, String password, String firstName) {
        this.login = login;
        this.password = password;
        this.firstName = firstName;
    }

    @Parameterized.Parameters(name = "login={0}, password={1}, firstName={2}")
    public static Object[][] data() {
        return new Object[][]{
                {null, "1234", "Saske"},
                {"login_" + UUID.randomUUID(), null, "Saske"}
        };
    }

    @Before
    public void setUp() {
        courier = new Courier(login, password, firstName);
        courierApi = new CourierApi();
    }

    @Test
    @DisplayName("Ошибка при отсутствии обязательных полей")
    public void createCourierWithoutRequiredFields() {
        courierApi.createCourier(courier)
                .then()
                .statusCode(400)
                .body("message", notNullValue());
    }
}