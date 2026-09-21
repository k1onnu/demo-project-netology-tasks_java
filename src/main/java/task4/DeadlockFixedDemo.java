package task4;

public class DeadlockFixedDemo {
    private static final Object resourceA = new Object();
    private static final Object resourceB = new Object();

    public static void main(String[] args) throws InterruptedException {
        Thread t1 = new Thread(() -> {
            synchronized (resourceA) {           // всегда A первым
                System.out.println("T1: захватил A");
                try { Thread.sleep(100); } catch (InterruptedException e) {}
                synchronized (resourceB) {
                    System.out.println("T1: захватил B");
                }
            }
        });

        Thread t2 = new Thread(() -> {
            synchronized (resourceA) {           // ← тоже A первым!
                System.out.println("T2: захватил A");
                try { Thread.sleep(100); } catch (InterruptedException e) {}
                synchronized (resourceB) {
                    System.out.println("T2: захватил B");
                }
            }
        });

        t1.start();
        t2.start();
        t1.join();
        t2.join();
        System.out.println("Завершено без deadlock");
    }
}

/*
T1: захватил A
T1: захватил B
T2: захватил A
T2: захватил B
Завершено без deadlock
*/
