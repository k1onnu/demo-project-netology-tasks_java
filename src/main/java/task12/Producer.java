package task12;

import java.util.Random;

public class Producer implements Runnable {
    private static final String[] TYPES = {"USER_LOGIN", "ORDER_CREATED", "PAYMENT_DONE"};
    private final EventBus bus;
    private final int count;
    private final Random random = new Random();

    public Producer(EventBus bus, int count) {
        this.bus = bus;
        this.count = count;
    }

    @Override
    public void run() {
        for (int i = 0; i < count; i++) {
            String type = TYPES[random.nextInt(TYPES.length)];
            bus.publish(new Event(type, "payload-" + i));
        }
    }
}
