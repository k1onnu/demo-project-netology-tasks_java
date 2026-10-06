package task9;

import java.util.concurrent.Callable;

public class LongTask implements Callable<String> {
    private final String name;
    private final long durationMs;

    public LongTask(String name, long durationMs) {
        this.name = name;
        this.durationMs = durationMs;
    }

    @Override
    public String call() throws Exception {
        System.out.println("[" + name + "] started, will run " + durationMs + " ms");
        long start = System.currentTimeMillis();

        while (System.currentTimeMillis() - start < durationMs) {
            if (Thread.currentThread().isInterrupted()) {
                System.out.println("[" + name + "] interrupted!");
                throw new InterruptedException(name + " was interrupted");
            }
            Thread.sleep(100);
        }

        System.out.println("[" + name + "] finished normally");
        return name + " completed";
    }
}
