import org.junit.Before;

import static io.restassured.RestAssured.baseURI;

public class BaseTest {

    @Before
    public void start() {
        baseURI = "https://qa-scooter.praktikum-services.ru";
    }
}