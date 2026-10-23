package task12;

public class Consumer implements java.util.function.Consumer<Event> {
    private final String name;

    public Consumer(String name) {
        this.name = name;
    }

    @Override
    public void accept(Event event) {
        System.out.printf("[%s] %s → %s%n",
                Thread.currentThread().getName(), name, event);
    }
}
