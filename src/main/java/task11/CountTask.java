package task11;

import java.util.List;
import java.util.concurrent.RecursiveTask;

public class CountTask extends RecursiveTask<Long> {

    private static final int THRESHOLD = 1000;

    private final List<?> items;
    private final int from;
    private final int to;

    public CountTask(List<?> items, int from, int to) {
        this.items = items;
        this.from = from;
        this.to = to;
    }

    @Override
    protected Long compute() {
        int size = to - from;

        if (size <= THRESHOLD) {
            long count = 0;
            for (int i = from; i < to; i++) {
                if (items.get(i) != null) count++;
            }
            return count;
        }

        int mid = from + size / 2;
        CountTask left = new CountTask(items, from, mid);
        CountTask right = new CountTask(items, mid, to);

        left.fork();
        long rightResult = right.compute();
        long leftResult = left.join();

        return leftResult + rightResult;
    }
}
