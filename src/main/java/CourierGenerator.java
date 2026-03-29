import java.util.Random;

public class CourierGenerator {

    public static Courier getRandomCourier() {
        Random random = new Random();
        String login = "ninja_" + System.currentTimeMillis() + random.nextInt(1000);
        String password = "1234";
        String firstName = "saske";

        return new Courier(login, password, firstName);
    }
}
