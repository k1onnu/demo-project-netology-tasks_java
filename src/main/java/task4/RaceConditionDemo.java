package task4;

import java.util.concurrent.atomic.AtomicInteger;

public class RaceConditionDemo {
    private static final AtomicInteger counter = new AtomicInteger();

    public static void main(String[] args) throws InterruptedException {
        int threads = 16;
        int perThread = 1_000_000;

        Thread[] workers = new Thread[threads];
        for (int i = 0; i < threads; i++) {
            workers[i] = new Thread(() -> {
                for (int j = 0; j < perThread; j++) {
                    counter.incrementAndGet();
                }
            });
        }

        long start = System.nanoTime();
        for (Thread t : workers) t.start();
        for (Thread t : workers) t.join();
        long end = System.nanoTime();

        int expected = threads * perThread;
        System.out.println("Expected: " + expected);
        System.out.println("Actual:   " + counter.get());
        System.out.println("Lost:     " + (expected - counter.get()));
        System.out.println("Time:     " + (end - start) / 1_000_000 + " ms");
    }
}

/*
Expected: 16000000
Actual:   5711243
Lost:     10288757
Time:     48 ms
*
*
Expected: 16000000
Actual:   16000000
Lost:     0
Time:     257 ms
*
*
Expected: 16000000
Actual:   16000000
Lost:     0
Time:     187 ms
* */
