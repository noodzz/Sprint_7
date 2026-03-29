import io.qameta.allure.Description;
import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.UUID;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.notNullValue;

public class CourierLoginTests {

    private Courier courier;
    private int courierId;

    @Before
    public void setUp() {
        baseURI = "https://qa-scooter.praktikum-services.ru";
        courier = CourierGenerator.getRandomCourier();
    }

    @Test
    @DisplayName("Курьер может авторизоваться")
    @Description("Проверяем успешный логин курьера и получение id")
    public void courierCanLogin() {
        createCourier(courier);

        Response response = loginCourier(courier);

        response.then()
                .statusCode(200)
                .body("id", notNullValue());

        courierId = response.then().extract().path("id");
    }

    @Test
    @DisplayName("Ошибка при неверном логине")
    @Description("Если логин неверный — возвращается ошибка")
    public void cannotLoginWithWrongLogin() {
        createCourier(courier);

        Courier wrongCourier = new Courier("wrong_login", courier.getPassword(), null);

        loginCourier(wrongCourier)
                .then()
                .statusCode(404);
    }

    @Test
    @DisplayName("Ошибка при неверном пароле")
    @Description("Если пароль неверный — возвращается ошибка")
    public void cannotLoginWithWrongPassword() {
        createCourier(courier);

        Courier wrongCourier = new Courier(courier.getLogin(), "wrong_pass", null);

        loginCourier(wrongCourier)
                .then()
                .statusCode(404);
    }

    @Test
    @DisplayName("Ошибка при отсутствии логина")
    @Description("Если не передан login — возвращается ошибка")
    public void cannotLoginWithoutLogin() {
        Courier courierWithoutLogin = new Courier(null, "1234", null);

        loginCourier(courierWithoutLogin)
                .then()
                .statusCode(400);
    }

    @Test
    @DisplayName("Ошибка при отсутствии пароля")
    @Description("Если не передан password — возвращается ошибка")
    public void cannotLoginWithoutPassword() {
        Courier courierWithoutPassword = new Courier(
                "login_" + UUID.randomUUID(),
                null,
                null
        );

        loginCourier(courierWithoutPassword)
                .then()
                .statusCode(400);
    }

    @Test
    @DisplayName("Ошибка при логине несуществующего пользователя")
    @Description("Если пользователь не существует — возвращается ошибка")
    public void cannotLoginNonExistentCourier() {
        Courier randomCourier = new Courier(
                "login_" + UUID.randomUUID(),
                "1234",
                null
        );

        loginCourier(randomCourier)
                .then()
                .statusCode(404);
    }

    @After
    public void tearDown() {
        if (courierId != 0) {
            deleteCourier(courierId);
        }
    }

    @Step("Создаём курьера {courier.login}")
    public void createCourier(Courier courier) {
        given()
                .header("Content-type","application/json")
                .body(courier)
                .when()
                .post("/api/v1/courier")
                .then()
                .statusCode(201);
    }

    @Step("Логинимся курьером {courier.login}")
    public Response loginCourier(Courier courier) {
        return given()
                .header("Content-type","application/json")
                .body(courier)
                .when()
                .post("/api/v1/courier/login");
    }

    @Step("Удаляем курьера")
    public void deleteCourier(int courierId) {
        given()
                .header("Content-type","application/json")
                .when()
                .delete("/api/v1/courier/{id}", courierId)
                .then()
                .statusCode(200);
    }
}