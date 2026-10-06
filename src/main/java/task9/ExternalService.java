package task9;

import java.util.Random;

public class ExternalService {
    private static final Random RANDOM = new Random();

    public static double fetchData(String entityId) throws Exception {
        Thread.sleep(200 + RANDOM.nextInt(300));

        if (RANDOM.nextInt(10) < 2) {
            throw new RuntimeException("Failed to fetch data for " + entityId);
        }

        return RANDOM.nextDouble() * 100;
    }
}