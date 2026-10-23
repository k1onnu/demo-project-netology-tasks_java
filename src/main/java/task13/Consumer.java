package task13;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

public class Consumer implements Runnable {
    public static final UrlTask POISON_PILL = new UrlTask(-1, "__STOP__");

    private final BlockingQueue<UrlTask> queue;
    private final UrlParser parser;
    private final AtomicInteger processedCount;
    private final AtomicInteger finishedConsumers;   // ← общий с TaskServer
    private final int totalConsumers;

    public Consumer(BlockingQueue<UrlTask> queue, UrlParser parser,
                    AtomicInteger processedCount,
                    AtomicInteger finishedConsumers, int totalConsumers) {
        this.queue = queue;
        this.parser = parser;
        this.processedCount = processedCount;
        this.finishedConsumers = finishedConsumers;
        this.totalConsumers = totalConsumers;
    }

    @Override
    public void run() {
        try {
            while (true) {
                UrlTask task = queue.take();
                if (task == POISON_PILL) {
                    int finished = finishedConsumers.incrementAndGet();
                    if (finished < totalConsumers) {
                        queue.put(POISON_PILL);
                    }
                    return;
                }

                long start = System.nanoTime();
                String title = parser.fetchTitle(task.getUrl());
                long elapsed = (System.nanoTime() - start) / 1_000_000;

                processedCount.incrementAndGet();
                System.out.printf("[%s] %s → \"%s\" (%d ms)%n",
                        Thread.currentThread().getName(), task, title, elapsed);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}