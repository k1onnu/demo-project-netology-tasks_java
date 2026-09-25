package task6;

public class StoppableTask implements Runnable {
    private volatile boolean running = true;
    private long iterations = 0;

    public void stop() {
        running = false;
    }

    public long getIterations() {
        return iterations;
    }

    @Override
    public void run() {
        while (running) {
            iterations++;
            if(iterations % 1_000_000 == 0) {
                Thread.yield();
            }
        }
        System.out.printf("%s stopped by iteration: %d",Thread.currentThread().getName(), iterations);
    }
}
