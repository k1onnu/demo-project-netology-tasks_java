package task10;

import java.util.List;
import java.util.concurrent.*;

public class Main {

    private static final List<String> IDS = List.of(
            "USD", "EUR", "GBP", "JPY", "CNY",
            "CHF", "AUD", "CAD", "SEK", "NOK"
    );

    public static void main(String[] args) throws Exception {
        int cores = Runtime.getRuntime().availableProcessors();
        int poolSize = Math.max(4, cores * 2);

        ThreadPoolExecutor executor = new ThreadPoolExecutor(
                poolSize, poolSize,
                30, TimeUnit.SECONDS,
                new LinkedBlockingQueue<>(),
                new ThreadFactory() {
                    private int n = 0;
                    @Override
                    public Thread newThread(Runnable r) {
                        Thread t = new Thread(r, "agg-pool-" + (n++));
                        t.setDaemon(true);
                        return t;
                    }
                },
                new ThreadPoolExecutor.CallerRunsPolicy()
        );

        try {
            AsyncAggregator async = new AsyncAggregator(executor);
            SynchronousAggregator sync = new SynchronousAggregator();

            System.out.println("ASYNC");
            long startAsync = System.nanoTime();
            List<CurrencyInfo> asyncResult = async.fetchAllDataAsync(IDS);
            long asyncMs = (System.nanoTime() - startAsync) / 1_000_000;

            System.out.println("\nAsync result (" + asyncResult.size() + " items):");
            asyncResult.forEach(System.out::println);
            System.out.println("Async time: " + asyncMs + " ms");

            System.out.println("\nSYNC");
            long startSync = System.nanoTime();
            List<CurrencyInfo> syncResult = sync.fetchAllDataSync(IDS);
            long syncMs = (System.nanoTime() - startSync) / 1_000_000;

            System.out.println("\nSync result (" + syncResult.size() + " items):");
            syncResult.forEach(System.out::println);
            System.out.println("Sync time: " + syncMs + " ms");

            System.out.println("\nSummary");
            System.out.println("Items requested:  " + IDS.size());
            System.out.println("Async items:      " + asyncResult.size());
            System.out.println("Sync items:       " + syncResult.size());
            System.out.printf("Speedup:          %.2fx%n",
                    (double) syncMs / asyncMs);
        } finally {
            executor.shutdown();
            if (!executor.awaitTermination(5, TimeUnit.SECONDS)) {
                executor.shutdownNow();
            }
            System.out.println("Executor stopped.");
        }
    }
}
