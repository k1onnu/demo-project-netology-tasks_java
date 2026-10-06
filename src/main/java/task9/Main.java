package task9;

import java.util.List;
import java.util.concurrent.*;

public class Main {
    public static void main(String[] args) throws Exception {
        System.out.println("1. Future and cancel");
        ExecutorService pool = Executors.newFixedThreadPool(2);

        Future<String> future = pool.submit(new LongTask("long-task", 8000));
        Thread.sleep(2000);
        System.out.println("Main: cancelling task...");
        boolean cancelled = future.cancel(true);
        System.out.println("Main: cancel returned " + cancelled);
        System.out.println("Main: isCancelled=" + future.isCancelled());

        pool.shutdown();
        pool.awaitTermination(2, TimeUnit.SECONDS);
        System.out.println();

        System.out.println("2. invokeAll with 5 tasks");
        ExecutorService pool2 = Executors.newFixedThreadPool(5);

        List<RandomTask> tasks = List.of(
                new RandomTask(1),
                new RandomTask(2),
                new RandomTask(3),
                new RandomTask(4),
                new RandomTask(5)
        );

        long start = System.currentTimeMillis();
        List<Future<Integer>> results = pool2.invokeAll(tasks);
        long elapsed = System.currentTimeMillis() - start;

        int sum = 0;
        for (Future<Integer> f : results) {
            sum += f.get();
        }
        System.out.println("Total result = " + sum);
        System.out.println("Wall time = " + elapsed + " ms (max task was ≤ 3s)");

        pool2.shutdown();
        pool2.awaitTermination(2, TimeUnit.SECONDS);
        System.out.println();

        System.out.println("3. Periodic monitor (5 sec)");
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
        ScheduledFuture<?> monitorFuture = scheduler.scheduleAtFixedRate(
                new PeriodicMonitor("system-check"),
                0, 1, TimeUnit.SECONDS);

        Thread.sleep(5000);
        monitorFuture.cancel(false);
        scheduler.shutdown();
        System.out.println();

        System.out.println("4. PeriodicDataAggregator (10 sec)");
        List<String> entities = List.of("USD", "EUR", "GBP", "JPY", "CNY", "CHF", "AUD");
        PeriodicDataAggregator aggregator = new PeriodicDataAggregator(entities, 3);
        aggregator.start();

        Thread.sleep(10_000);
        aggregator.stop();
    }
}
