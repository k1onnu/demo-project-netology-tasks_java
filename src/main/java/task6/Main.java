package task6;

import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) throws InterruptedException {

        //volatile
        System.out.println("volatile: stop the thread");
        StoppableTask task = new StoppableTask();
        Thread worker = new Thread(task, "worker");
        worker.start();
        Thread.sleep(500);
        task.stop();
        worker.join();
        System.out.println();

        // AtomicInteger
        System.out.println("AtomicInteger: counter");
        testAtomicCounter(4, 1_000_000);
        System.out.println();

        // AtomicReference
        System.out.println("AtomicReference: singleton-cache");
        testSingletonCache(16);
    }

    private static void testAtomicCounter(int threads, int increments) throws InterruptedException {
        AtomicCounter counter = new AtomicCounter();
        List<Thread> workers = new ArrayList<>();

        for (int i = 0; i < threads; i++) {
            workers.add(new Thread(() -> {
                for (int j = 0; j < increments; j++) {
                    counter.increment();
                }
            }));
        }

        long start = System.nanoTime();
        for (Thread t : workers) t.start();
        for (Thread t : workers) t.join();
        long end = System.nanoTime();

        int expected = threads * increments;
        System.out.println("Expected: " + expected);
        System.out.println("Actual: " + counter.get());
        System.out.println("Match: " + (counter.get() == expected));
        System.out.println("Time: " + (end - start) / 1_000_000 + " ms");
    }

    private static void testSingletonCache(int threads) throws InterruptedException {
        SingletonCache cache = new SingletonCache();
        List<Thread> workers = new ArrayList<>();

        for (int i = 0; i < threads; i++) {
            final int id = i;
            workers.add(new Thread(() -> {
                String value = cache.getOrCreate("value-from-" + id);

            }));
        }

        for (Thread t : workers) t.start();
        for (Thread t : workers) t.join();

        System.out.println("Cached value: " + cache.get());
    }

    /*
volatile: stop the thread
worker stopped by iteration: 409531554
AtomicInteger: counter
Expected: 4000000
Actual: 4000000
Match: true
Time: 53 ms

AtomicReference: singleton-cache
Cached value: value-from-1
*/
}