package task10;

import java.time.LocalDateTime;

public class CurrencyInfo {
    private final String code;
    private final double rate;
    private final String name;
    private final LocalDateTime fetchedAt;

    public CurrencyInfo(String code, double rate, String name) {
        this.code = code;
        this.rate = rate;
        this.name = name;
        this.fetchedAt = LocalDateTime.now();
    }

    public String getCode() { return code; }
    public double getRate() { return rate; }
    public String getName() { return name; }
    public LocalDateTime getFetchedAt() { return fetchedAt; }

    @Override
    public String toString() {
        return String.format("%s (%s): %.2f ₽ [%s]",
                code, name, rate, fetchedAt.toLocalTime());
    }
}
