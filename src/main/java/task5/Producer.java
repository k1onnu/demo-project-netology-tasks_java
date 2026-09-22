package task5;

public class Producer  implements Runnable {
    private final BoundedBuffer<Integer> buffer;
    private final int itemsToProduce;

    public Producer(BoundedBuffer<Integer> buffer, int itemsToProduce) {
        this.buffer = buffer;
        this.itemsToProduce = itemsToProduce;
    }

    @Override
    public void run() {
        try {
            for (int i = 0; i < itemsToProduce; i++) {
                buffer.put(i);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
