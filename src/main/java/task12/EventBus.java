package task12;

import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Consumer;

public class EventBus {
    private final ConcurrentHashMap<String, CopyOnWriteArrayList<Consumer<Event>>> subscribers
            = new ConcurrentHashMap<>();
    private final BlockingQueue<Event> queue = new LinkedBlockingQueue<>(10_000);
    private final ConcurrentLinkedQueue<Event> log = new ConcurrentLinkedQueue<>();

    private final AtomicLong publishedCount = new AtomicLong();
    private final AtomicLong processedCount = new AtomicLong();
    private final AtomicLong droppedCount = new AtomicLong();

    private final ExecutorService consumers;
    private volatile boolean running = true;

    public EventBus(int consumerCount) {
        this.consumers = Executors.newFixedThreadPool(consumerCount, r -> {
            Thread t = new Thread(r, "event-consumer");
            t.setDaemon(true);
            return t;
        });

        for (int i = 0; i < consumerCount; i++) {
            consumers.submit(this::consumeLoop);
        }
    }

    public void subscribe(String type, Consumer<Event> handler) {
        subscribers.computeIfAbsent(type, k -> new CopyOnWriteArrayList<>()).add(handler);
    }

    public void publish(Event event) {
        publishedCount.incrementAndGet();
        log.add(event);
        try {
            queue.put(event);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    // Consumer-цикл
    private void consumeLoop() {
        while (running || !queue.isEmpty()) {
            try {
                Event event = queue.poll(200, TimeUnit.MILLISECONDS);
                if (event == null) continue;

                List<Consumer<Event>> handlers = subscribers.get(event.getType());
                if (handlers != null) {
                    for (Consumer<Event> h : handlers) {
                        try {
                            h.accept(event);
                        } catch (Exception e) {
                            System.err.println("Handler error: " + e.getMessage());
                        }
                    }
                }
                processedCount.incrementAndGet();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return;
            }
        }
    }

    public void shutdown() throws InterruptedException {
        running = false;
        consumers.shutdown();
        if (!consumers.awaitTermination(5, TimeUnit.SECONDS)) {
            consumers.shutdownNow();
        }
    }

    public long getPublished() {
        return publishedCount.get();
    }

    public long getProcessed() {
        return processedCount.get();
    }

    public long getDropped() {
        return droppedCount.get();
    }

    public int getLogSize() {
        return log.size();
    }

    public int getQueueSize() {
        return queue.size();
    }
}
