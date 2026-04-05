import io.qameta.allure.Step;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

public class CourierApi {

    @Step("Создаём курьера")
    public Response createCourier(Courier courier) {
        return given()
                .header("Content-type", "application/json")
                .body(courier)
                .when()
                .post("/api/v1/courier");
    }

    @Step("Логинимся курьером")
    public Response loginCourier(Courier courier) {
        return given()
                .header("Content-type", "application/json")
                .body(new Courier(courier.getLogin(), courier.getPassword(), null))
                .when()
                .post("/api/v1/courier/login");
    }

    @Step("Удаляем курьера")
    public Response deleteCourier(int courierId) {
        return given()
                .header("Content-type", "application/json")
                .when()
                .delete("/api/v1/courier/{id}", courierId);
    }
}