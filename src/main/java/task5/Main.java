package task5;

import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        int capacity = 100;
        int producers = 4;
        int consumers = 4;
        int itemsPerProducer = 100_000;

        //eentrantLock
        long lockTime = runWithLock(capacity, producers, consumers, itemsPerProducer);
        System.out.println("ReentrantLock + Condition: " + lockTime + " ms");

        //sync
        long syncTime = runWithSynchronized(capacity, producers, consumers, itemsPerProducer);
        System.out.println("synchronized + wait/notifyAll: " + syncTime + " ms");
    }

    private static long runWithLock(int capacity, int producers, int consumers, int items)
            throws InterruptedException {
        BoundedBuffer<Integer> buffer = new BoundedBuffer<>(capacity);
        return run(buffer::put, buffer::take, producers, consumers, items);
    }

    private static long runWithSynchronized(int capacity, int producers, int consumers, int items)
            throws InterruptedException {
        SynchronizedBuffer<Integer> buffer = new SynchronizedBuffer<>(capacity);
        return run(buffer::put, buffer::take, producers, consumers, items);
    }

    private static long run(ThrowingConsumer<Integer> put,
                            ThrowingSupplier<Integer> take,
                            int producers, int consumers, int itemsPerProducer)
            throws InterruptedException {
        List<Thread> threads = new ArrayList<>();

        for (int i = 0; i < producers; i++) {
            threads.add(new Thread(() -> {
                try {
                    for (int j = 0; j < itemsPerProducer; j++) {
                        put.accept(j);
                    }
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }, "producer-" + i));
        }

        int totalItems = producers * itemsPerProducer;
        int itemsPerConsumer = totalItems / consumers;

        for (int i = 0; i < consumers; i++) {
            threads.add(new Thread(() -> {
                try {
                    for (int j = 0; j < itemsPerConsumer; j++) {
                        take.get();
                    }
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }, "consumer-" + i));
        }

        long start = System.nanoTime();
        for (Thread t : threads) t.start();
        for (Thread t : threads) t.join();
        long end = System.nanoTime();

        return (end - start) / 1_000_000;
    }

    @FunctionalInterface
    interface ThrowingConsumer<T> {
        void accept(T t) throws InterruptedException;
    }

    @FunctionalInterface
    interface ThrowingSupplier<T> {
        T get() throws InterruptedException;
    }

    /*
ReentrantLock + Condition: 144 ms
synchronized + wait/notifyAll: 157 ms

ReentrantLock + Condition: 147 ms
synchronized + wait/notifyAll: 156 ms

ReentrantLock + Condition: 133 ms
synchronized + wait/notifyAll: 166 ms
*/
}
