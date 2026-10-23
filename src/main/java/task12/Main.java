package task12;

public class Main {
    public static void main(String[] args) throws Exception {
        int producerCount = 4;
        int consumerCount = 4;
        int eventsPerProducer = 10_000;

        EventBus bus = new EventBus(consumerCount);

        bus.subscribe("USER_LOGIN", new Consumer("LoginHandler"));
        bus.subscribe("ORDER_CREATED", new Consumer("OrderHandler"));
        bus.subscribe("PAYMENT_DONE", new Consumer("PaymentHandler"));
        bus.subscribe("PAYMENT_DONE", e ->
                System.out.println("[AUDIT] payment: " + e.getPayload()));

        Thread[] producers = new Thread[producerCount];
        for (int i = 0; i < producerCount; i++) {
            producers[i] = new Thread(new Producer(bus, eventsPerProducer),
                    "producer-" + i);
        }

        long start = System.nanoTime();
        for (Thread t : producers) t.start();
        for (Thread t : producers) t.join();
        long publishTime = (System.nanoTime() - start) / 1_000_000;

        while (bus.getQueueSize() > 0) {
            Thread.sleep(50);
        }

        long totalTime = (System.nanoTime() - start) / 1_000_000;

        bus.shutdown();

        System.out.println();
        System.out.println("Producers:        " + producerCount);
        System.out.println("Consumers:        " + consumerCount);
        System.out.println("Events published: " + bus.getPublished());
        System.out.println("Events processed: " + bus.getProcessed());
        System.out.println("Events dropped:   " + bus.getDropped());
        System.out.println("Log size:         " + bus.getLogSize());
        System.out.println("Publish time:     " + publishTime + " ms");
        System.out.println("Total time:       " + totalTime + " ms");
    }
}
