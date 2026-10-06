package task9;

import java.util.List;
import java.util.concurrent.*;

public class PeriodicDataAggregator {
    private final ScheduledExecutorService scheduler;
    private final ExecutorService workerPool;
    private final List<String> entityIds;
    private final long periodSeconds;

    public PeriodicDataAggregator(List<String> entityIds, long periodSeconds) {
        this.entityIds = entityIds;
        this.periodSeconds = periodSeconds;
        this.scheduler = Executors.newSingleThreadScheduledExecutor(
                r -> new Thread(r, "aggregator-scheduler"));
        this.workerPool = Executors.newFixedThreadPool(
                Math.min(entityIds.size(), 8),
                r -> new Thread(r, "aggregator-worker"));
    }

    public void start() {
        scheduler.scheduleAtFixedRate(this::aggregateAll,
                0, periodSeconds, TimeUnit.SECONDS);
        System.out.println("[Aggregator] Started, period=" + periodSeconds + "s, entities=" + entityIds);
    }

    private void aggregateAll() {
        System.out.println("\n[Aggregator] === Aggregation round started ===");
        long start = System.currentTimeMillis();

        // Отправляем все задачи асинхронно
        List<Future<Double>> futures = entityIds.stream()
                .map(id -> workerPool.submit(() -> ExternalService.fetchData(id)))
                .toList();

        // Собираем результаты по мере готовности
        int success = 0, failed = 0;
        for (int i = 0; i < futures.size(); i++) {
            String id = entityIds.get(i);
            try {
                Double value = futures.get(i).get(2, TimeUnit.SECONDS);
                System.out.printf("[Aggregator] ✓ %s → %.2f%n", id, value);
                success++;
            } catch (ExecutionException e) {
                System.out.printf("[Aggregator] ✗ %s → %s%n", id,
                        e.getCause().getMessage());
                failed++;
            } catch (TimeoutException e) {
                System.out.printf("[Aggregator] ✗ %s → timeout%n", id);
                futures.get(i).cancel(true);
                failed++;
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return;
            }
        }

        long elapsed = System.currentTimeMillis() - start;
        System.out.printf("[Aggregator] Round done: success=%d, failed=%d, time=%d ms%n",
                success, failed, elapsed);
    }

    public void stop() {
        scheduler.shutdown();
        workerPool.shutdown();
        try {
            if (!scheduler.awaitTermination(2, TimeUnit.SECONDS)) {
                scheduler.shutdownNow();
            }
            if (!workerPool.awaitTermination(2, TimeUnit.SECONDS)) {
                workerPool.shutdownNow();
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        System.out.println("[Aggregator] Stopped");
    }
}
