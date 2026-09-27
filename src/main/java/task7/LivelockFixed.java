package task7;

import java.util.Random;

public class LivelockFixed {
    private static final Random random = new Random();

    static class Worker {
        private final String name;
        private boolean busy = false;

        Worker(String name) { this.name = name; }

        boolean isBusy() { return busy; }

        void work(Worker other) {
            int steps = 0;
            while (steps < 5) {
                if (!other.isBusy()) {
                    busy = false;
                    System.out.println(name + ": уступаю " + other.name);
                    // случайная задержка — ломает синхронизацию уступок
                    try {
                        Thread.sleep(random.nextInt(50));
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                    continue;
                }
                busy = true;
                System.out.println(name + ": работаю");
                steps++;
            }
            System.out.println(name + ": закончил");
        }

        void setBusy(boolean b) { busy = b; }
    }

    public static void main(String[] args) throws InterruptedException {
        Worker w1 = new Worker("Worker-1");
        Worker w2 = new Worker("Worker-2");
        w1.setBusy(true);
        w2.setBusy(true);

        Thread t1 = new Thread(() -> w1.work(w2), "T1");
        Thread t2 = new Thread(() -> w2.work(w1), "T2");

        t1.start();
        t2.start();
        t1.join();
        t2.join();
        System.out.println("Завершено без livelock");
    }
}


/*
Worker-2: работаю
Worker-2: работаю
Worker-2: работаю
Worker-2: работаю
Worker-2: работаю
Worker-1: работаю
Worker-1: работаю
Worker-1: работаю
Worker-1: работаю
Worker-2: закончил
Worker-1: работаю
Worker-1: закончил
Завершено без livelock
*/