package task3;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class Main {
    public static void main(String[] args) {
        List<Integer> numbers = new Random()
                .ints(1_000_000, 0, 1000)
                .boxed()
                .collect(Collectors.toList());

        //testing
        long test = 0;
        for (int i = 0; i < 3; i++) {
            test += processStream(numbers);
            test += processParallel(numbers);
        }
        System.out.printf("Test: %d%n",test);

        int i = 0;
        while (i < 5) {

            //stream
            long start1 = System.nanoTime();
            long sum1 = processStream(numbers);
            long end1 = System.nanoTime();
            System.out.printf("streamTime: sum = %d; time = %.2f ms%n", sum1, (end1 - start1) / 1_000_000.0);

            //Parallel
            long start2 = System.nanoTime();
            long sum2 = processParallel(numbers);
            long end2 = System.nanoTime();
            System.out.printf("ParallelStreamTime: sum = %d; time = %.2f ms%n", sum2, (end2 - start2) / 1_000_000.0);

            //speedup + sanity check
            System.out.printf("Speedup: %.2fx%n", (double)(end1 - start1) / (end2 - start2));
            System.out.println("Sums equal: " + (sum1 == sum2));
            System.out.println("------");
            i++;
        }
        System.out.println("Cores: " + Runtime.getRuntime().availableProcessors());
    }

    private static long processStream(List<Integer> list) {
        return list
                .stream()
                .filter(n -> n % 2 == 0)
                .map(n -> n * 2)
                .mapToLong(Integer::longValue)
                .sum();
    }

    private static long processParallel(List<Integer> list) {
        return list
                .parallelStream()
                .filter(n -> n % 2 == 0)
                .map(n -> n * 2)
                .mapToLong(Integer::longValue)
                .sum();
    }
}
