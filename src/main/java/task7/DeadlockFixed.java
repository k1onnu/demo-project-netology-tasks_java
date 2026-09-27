package task7;

public class DeadlockFixed {
    private static final Object A = new Object();
    private static final Object B = new Object();

    public static void main(String[] args) throws InterruptedException {
        Thread t1 = new Thread(() -> {
            synchronized (A) {
                System.out.println("T1: захватил A");
                sleep(100);
                synchronized (B) {
                    System.out.println("T1: захватил B");
                }
            }
        }, "Thread-1");

        Thread t2 = new Thread(() -> {
            synchronized (A) {
                System.out.println("T2: захватил A");
                sleep(100);
                synchronized (B) {
                    System.out.println("T2: захватил B");
                }
            }
        }, "Thread-2");

        t1.start();
        t2.start();
        t1.join();
        t2.join();
        System.out.println("Завершено без deadlock");
    }

    private static void sleep(long ms) {
        try { Thread.sleep(ms); } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}

/*
* T1: захватил A
T1: захватил B
T2: захватил A
T2: захватил B
Завершено без deadlock
* */
