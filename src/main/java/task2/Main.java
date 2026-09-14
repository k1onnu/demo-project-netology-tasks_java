package task2;

public class Main {
    public static void main(String[] args) throws InterruptedException {

        Object lock = new Object();

        Thread thread1 = new Thread(() -> {
            synchronized (lock) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        }, "first");

        Thread thread2 = new Thread(() -> {
            synchronized (lock) {
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        },"second");

        Thread thread3 = new Thread(() -> {
            try {
                thread1.join();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }, "third");

        printStatistic(thread1, thread2, thread3);

        thread1.start();
        thread2.start();
        thread3.start();

        while (thread1.isAlive() || thread2.isAlive() || thread3.isAlive()) {
            printStatistic(thread1, thread2, thread3);
            Thread.sleep(300);
        }

        System.out.println("Final state:");
        printStatistic(thread1, thread2, thread3);
    }

    private static void printStatistic(Thread thread1, Thread thread2, Thread thread3) {
        System.out.printf("%s: %s%n", thread1.getName(), thread1.getState());
        System.out.printf("%s: %s%n", thread2.getName(), thread2.getState());
        System.out.printf("%s: %s%n", thread3.getName(), thread3.getState());
        System.out.println("-----------");
    }
}
