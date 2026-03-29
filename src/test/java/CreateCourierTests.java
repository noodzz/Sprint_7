import io.qameta.allure.Description;
import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.UUID;

import static io.restassured.RestAssured.*;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;

@RunWith(Parameterized.class)
public class CreateCourierTests {

    private final String login;
    private final String password;
    private final String firstName;
    private final int expectedStatus;

    private Courier courier;
    private int courierId;

    public CreateCourierTests(String login, String password, String firstName, int expectedStatus) {
        this.login = login;
        this.password = password;
        this.firstName = firstName;
        this.expectedStatus = expectedStatus;
    }

    @Parameterized.Parameters(name = "login={0}, password={1}, firstName={2} => status {3}")
    public static Object[][] data() {
        return new Object[][]{
                {null, "1234", "Saske", 400},
                {"login_" + UUID.randomUUID(), null, "Saske", 400},
                {"login_" + UUID.randomUUID(), "1234", null, 201},
                {"login_" + UUID.randomUUID(), "1234", "Saske", 201}
        };
    }

    @Before
    public void setUp() {
        baseURI = "https://qa-scooter.praktikum-services.ru";
        courier = new Courier(login, password, firstName);
    }

    @Test
    @DisplayName("Создание курьера с проверкой обязательных полей")
    @Description("Проверяем, что запрос на создание курьера обрабатывает обязательные поля и возвращает корректный статус")
    public void createCourierRequiredFieldsTest() {
        if (expectedStatus == 201) {
            sendCreateRequest(courier)
                    .then()
                    .statusCode(expectedStatus)
                    .body("ok", equalTo(true));
        } else {
            sendCreateRequest(courier)
                    .then()
                    .statusCode(expectedStatus)
                    .body("message", notNullValue());
        }
        if (expectedStatus == 201) {
            courierId = loginCourier(courier);
            if (courierId != 0) {
                deleteCourier(courierId);
            }
        }
    }

    @Step("Отправляем POST /api/v1/courier с данными курьера {courier.login}")
    private Response sendCreateRequest(Courier courier) {
        return given()
                .header("Content-type","application/json")
                .body(courier)
                .when()
                .post("/api/v1/courier");
    }

    @Step("Логинимся курьером {courier.login} и получаем ID")
    private int loginCourier(Courier courier) {
        return given()
                .header("Content-type","application/json")
                .body(new Courier(courier.getLogin(), courier.getPassword(), null))
                .when()
                .post("/api/v1/courier/login")
                .then()
                .statusCode(200)
                .extract()
                .path("id");
    }

    @Step("Удаляем курьера {courier.login}")
    private void deleteCourier(int courierId) {
        given()
                .header("Content-type","application/json")
                .when()
                .delete("/api/v1/courier/{id}", courierId)
                .then()
                .statusCode(200);
    }
}