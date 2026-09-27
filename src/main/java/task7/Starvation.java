package task7;

public class Starvation {
    private static final Object lock = new Object();

    public static void main(String[] args) throws InterruptedException {
        // Голодный поток с низким приоритетом
        Thread starving = new Thread(() -> {
            int count = 0;
            while (count < 3) {
                synchronized (lock) {
                    System.out.println("Starving: получил доступ (" + (++count) + ")");
                }
                Thread.yield();
            }
        }, "Starving");
        starving.setPriority(Thread.MIN_PRIORITY);

        // Жадные потоки с высоким приоритетом
        Thread[] greedy = new Thread[5];
        for (int i = 0; i < greedy.length; i++) {
            greedy[i] = new Thread(() -> {
                for (int j = 0; j < 100; j++) {
                    synchronized (lock) {
                        // захватывает и сразу отпускает, не даёт другим шанса
                    }
                }
            }, "Greedy-" + i);
            greedy[i].setPriority(Thread.MAX_PRIORITY);
        }

        starving.start();
        for (Thread g : greedy) g.start();

        starving.join();
        for (Thread g : greedy) g.join();
        System.out.println("Все завершены");
    }
}

/*
* Starving: получил доступ (1)
Starving: получил доступ (2)
Starving: получил доступ (3)
Все завершены
* */
