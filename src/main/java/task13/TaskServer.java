package task13;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

public class TaskServer {
    private final BlockingQueue<UrlTask> queue;
    private final ExecutorService producers;
    private final ExecutorService consumers;
    private final AtomicInteger taskCounter = new AtomicInteger();
    private final AtomicInteger processedCount = new AtomicInteger();
    private final AtomicInteger finishedConsumers = new AtomicInteger();

    public TaskServer(int queueCapacity, int producerCount, int consumerCount) {
        this.queue = new ArrayBlockingQueue<>(queueCapacity);

        this.producers = Executors.newFixedThreadPool(producerCount, r -> {
            Thread t = new Thread(r, "producer");
            t.setDaemon(true);
            return t;
        });

        this.consumers = Executors.newFixedThreadPool(consumerCount, r -> {
            Thread t = new Thread(r, "consumer");
            t.setDaemon(true);
            return t;
        });

        UrlParser parser = new UrlParser();
        for (int i = 0; i < consumerCount; i++) {
            consumers.submit(new Consumer(queue, parser, processedCount,
                    finishedConsumers, consumerCount));
        }
    }

    public void submitProducer(List<String> urls) {
        producers.submit(new Producer(queue, urls, taskCounter));
    }

    public void awaitCompletion() throws InterruptedException {
        producers.shutdown();
        producers.awaitTermination(1, TimeUnit.MINUTES);

        long deadline = System.currentTimeMillis() + 60_000; // 60 секунд
        while (processedCount.get() < taskCounter.get()) {
            if (System.currentTimeMillis() > deadline) {
                System.err.println("Timeout waiting for processing!");
                break;
            }
            Thread.sleep(50);
        }

        int consumerCount = ((ThreadPoolExecutor) consumers).getCorePoolSize();
        for (int i = 0; i < consumerCount; i++) {
            queue.put(Consumer.POISON_PILL);
        }

        consumers.shutdown();
        consumers.awaitTermination(1, TimeUnit.MINUTES);
    }

    public int getProcessedCount() {
        return processedCount.get();
    }
    public int getQueueSize() {
        return queue.size();
    }
}