package task6;

import java.util.concurrent.atomic.AtomicReference;

public class SingletonCache {
    private final AtomicReference<String> cacheValue = new AtomicReference<>(null);

    public String getOrCreate(String newValue) {
        String existing = cacheValue.get();
        if (existing != null) {
            return existing;
        }

        if (cacheValue.compareAndSet(null, newValue)) {
            return newValue;
        }

        return cacheValue.get();
    }

    public String get() {
        return cacheValue.get();
    }

    public boolean compareAndSet(String expected, String newValue) {
        return cacheValue.compareAndSet(expected, newValue);
    }
}
