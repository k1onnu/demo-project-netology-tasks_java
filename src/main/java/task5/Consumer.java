package task5;

public class Consumer implements Runnable {
    private final BoundedBuffer<Integer> buffer;
    private final int itemsToConsume;

    public Consumer(BoundedBuffer<Integer> buffer, int itemsToConsume) {
        this.buffer = buffer;
        this.itemsToConsume = itemsToConsume;
    }

    @Override
    public void run() {
        try {
            for (int i = 0; i < itemsToConsume; i++) {
                buffer.take();
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
