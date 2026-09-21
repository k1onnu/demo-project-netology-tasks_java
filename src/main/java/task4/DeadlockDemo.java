package task4;

public class DeadlockDemo {
    private static final Object resourceA = new Object();
    private static final Object resourceB = new Object();

    public static void main(String[] args) {
        Thread t1 = new Thread(() -> {
            synchronized (resourceA) {
                System.out.println("T1: захватил A");
                try { Thread.sleep(100); } catch (InterruptedException e) {}
                synchronized (resourceB) {
                    System.out.println("T1: захватил B");
                }
            }
        });

        Thread t2 = new Thread(() -> {
            synchronized (resourceB) {
                System.out.println("T2: захватил B");
                try { Thread.sleep(100); } catch (InterruptedException e) {}
                synchronized (resourceA) {
                    System.out.println("T2: захватил A");
                }
            }
        });

        t1.start();
        t2.start();
    }
}