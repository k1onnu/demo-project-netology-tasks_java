package task10;

import java.util.Map;
import java.util.Random;

public class DataSources {
    private static final Random RANDOM = new Random();

    private static final Map<String, Double> BASE_RATES = Map.of(
            "USD", 92.5, "EUR", 100.1, "GBP", 117.3,
            "JPY", 0.61, "CNY", 12.7, "CHF", 104.2,
            "AUD", 60.5, "CAD", 68.2, "SEK", 8.7, "NOK", 8.6
    );

    private static final Map<String, String> NAMES = Map.of(
            "USD", "Доллар США", "EUR", "Евро", "GBP", "Фунт стерлингов",
            "JPY", "Японская йена", "CNY", "Юань", "CHF", "Швейцарский франк",
            "AUD", "Австралийский доллар", "CAD", "Канадский доллар",
            "SEK", "Шведская крона", "NOK", "Норвежская крона"
    );

    public static double fetchRate(String code) {
        String thread = Thread.currentThread().getName();
        System.out.printf("[%s] fetchRate(%s) started%n", thread, code);
        sleep(300 + RANDOM.nextInt(400)); // 300–700 мс

        if ("JPY".equals(code) && RANDOM.nextInt(2) == 0) {
            throw new RuntimeException("Rate source failed for " + code);
        }

        Double base = BASE_RATES.get(code);
        if (base == null) {
            throw new IllegalArgumentException("Unknown currency: " + code);
        }
        double rate = base + (RANDOM.nextDouble() - 0.5) * 2; // ±1
        System.out.printf("[%s] fetchRate(%s) → %.2f%n", thread, code, rate);
        return rate;
    }

    public static String fetchName(String code) {
        String thread = Thread.currentThread().getName();
        System.out.printf("[%s] fetchName(%s) started%n", thread, code);
        sleep(200 + RANDOM.nextInt(300)); // 200–500 мс

        String name = NAMES.get(code);
        if (name == null) {
            throw new IllegalArgumentException("Unknown currency: " + code);
        }
        System.out.printf("[%s] fetchName(%s) → %s%n", thread, code, name);
        return name;
    }

    private static void sleep(long ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("Interrupted", e);
        }
    }
}
