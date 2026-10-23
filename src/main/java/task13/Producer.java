package task13;

import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

public class Producer implements Runnable {
    private final BlockingQueue<UrlTask> queue;
    private final List<String> urls;
    private final AtomicInteger taskCounter;

    public Producer(BlockingQueue<UrlTask> queue, List<String> urls, AtomicInteger taskCounter) {
        this.queue = queue;
        this.urls = urls;
        this.taskCounter = taskCounter;
    }

    @Override
    public void run() {
        try {
            for (String url : urls) {
                UrlTask task = new UrlTask(taskCounter.incrementAndGet(), url);
                queue.put(task);
                System.out.printf("[%s] produced %s%n",
                        Thread.currentThread().getName(), task);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}