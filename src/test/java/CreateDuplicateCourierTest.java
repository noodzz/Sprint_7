import io.qameta.allure.Description;
import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static io.restassured.RestAssured.baseURI;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class CreateDuplicateCourierTest {
    private Courier courier;

    @Before
    public void setUp() {
        baseURI = "https://qa-scooter.praktikum-services.ru";
        courier = CourierGenerator.getRandomCourier();
    }

    @Test
    @DisplayName("Нельзя создать двух одинаковых курьеров")
    @Description("Если курьер с таким логином уже существует, возвращается ошибка 409")
    public void cannotCreateDuplicateCourier() {
        createCourier(courier);
        createDuplicateCourier(courier);
    }

    @After
    public void tearDown() {
        int courierId = loginCourier(new Courier(courier.getLogin(), courier.getPassword(), null));
        deleteCourier(courierId);
    }

    @Step("Создаём курьера")
    public void createCourier(Courier courier) {
        given()
                .header("Content-type","application/json")
                .body(courier)
                .when()
                .post("/api/v1/courier")
                .then()
                .statusCode(201)
                .body("ok", equalTo(true));
    }

    @Step("Пытаемся создать курьера-двойника")
    public void createDuplicateCourier(Courier courier) {
        given()
                .header("Content-type","application/json")
                .body(courier)
                .when()
                .post("/api/v1/courier")
                .then()
                .statusCode(409);
    }

    @Step("Логинимся курьером {courier.login} и получаем ID")
    public int loginCourier(Courier courier) {
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
    public void deleteCourier(int courierId) {
        given()
                .header("Content-type","application/json")
                .when()
                .delete("/api/v1/courier/{id}", courierId)
                .then()
                .statusCode(200);
    }
}
