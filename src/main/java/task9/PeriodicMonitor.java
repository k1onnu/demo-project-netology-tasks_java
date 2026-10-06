package task9;

import java.time.LocalTime;

public class PeriodicMonitor implements Runnable {
    private final String name;
    private int tick = 0;

    public PeriodicMonitor(String name) {
        this.name = name;
    }

    @Override
    public void run() {
        tick++;
        System.out.println("[" + LocalTime.now() + "] " + name + " tick #" + tick);
    }
}