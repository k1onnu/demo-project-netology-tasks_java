package task9;

import java.util.Random;
import java.util.concurrent.Callable;

public class RandomTask implements Callable<Integer> {
    private static final Random RANDOM = new Random();
    private final int id;

    public RandomTask(int id) {
        this.id = id;
    }

    @Override
    public Integer call() throws Exception {
        int sleepMs = 1000 + RANDOM.nextInt(2000); // 1–3 сек
        System.out.println("Task-" + id + " started (will take " + sleepMs + " ms)");
        Thread.sleep(sleepMs);
        int result = id * 10;
        System.out.println("Task-" + id + " finished → result=" + result);
        return result;
    }
}
