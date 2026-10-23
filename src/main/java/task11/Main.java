package task11;

import java.util.List;
import java.util.concurrent.ForkJoinPool;

public class Main {
    public static void main(String[] args) {
        int totalItems = 1_000_000;
        List<String> items = DataProvider.generateAggregatedItems(totalItems);

        System.out.println("ForkJoin recursive count");
        System.out.println("Total items: " + items.size());
        System.out.println("Threshold (batch size): " + 1000);

        int parallelism = Runtime.getRuntime().availableProcessors();
        ForkJoinPool pool = new ForkJoinPool(parallelism);

        try {
            long start = System.nanoTime();
            CountTask task = new CountTask(items, 0, items.size());
            long count = pool.invoke(task); // запуск и ожидание
            long elapsedMs = (System.nanoTime() - start) / 1_000_000;

            System.out.println("Aggregated count: " + count);
            System.out.println("ForkJoin time:    " + elapsedMs + " ms");
            System.out.println("Parallelism:      " + parallelism);

            long startSeq = System.nanoTime();
            long seqCount = 0;
            for (String s : items) {
                if (s != null) seqCount++;
            }
            long seqElapsed = (System.nanoTime() - startSeq) / 1_000_000;

            System.out.println();
            System.out.println("Sequential count: " + seqCount);
            System.out.println("Sequential time:  " + seqElapsed + " ms");

            System.out.printf("Speedup: %.2fx%n",
                    (double) seqElapsed / elapsedMs);
        } finally {
            pool.shutdown();
            System.out.println("ForkJoinPool stopped.");
        }
    }
}
