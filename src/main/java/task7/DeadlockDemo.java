package task7;

public class DeadlockDemo {
    private static final Object resourceA = new Object();
    private static final Object resourceB = new Object();

    public static void main(String[] args) {
        Thread t1 = new Thread(() -> {
            synchronized (resourceA) {
                System.out.println("T1: захватил A");
                sleep(100);
                synchronized (resourceB) {
                    System.out.println("T1: захватил B");
                }
            }
        }, "Thread-1");

        Thread t2 = new Thread(() -> {
            synchronized (resourceB) {
                System.out.println("T2: захватил B");
                sleep(100);
                synchronized (resourceA) {
                    System.out.println("T2: захватил A");
                }
            }
        }, "Thread-2");

        t1.start();
        t2.start();

        System.out.println("Программа запущена. Ждём deadlock...");
        System.out.println("PID: " + ProcessHandle.current().pid());
    }

    private static void sleep(long ms) {
        try { Thread.sleep(ms); } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}

/*
Found one Java-level deadlock:
=============================
"Thread-1":
  waiting to lock monitor 0x00000205b5974120 (object 0x000000044ad46790, a java.lang.Object),
  which is held by "Thread-2"

"Thread-2":
  waiting to lock monitor 0x00000205b533f380 (object 0x000000044ad46780, a java.lang.Object),
  which is held by "Thread-1"

Java stack information for the threads listed above:
===================================================
"Thread-1":
	at task7.DeadlockDemo.lambda$main$0(DeadlockDemo.java:13)
	- waiting to lock <0x000000044ad46790> (a java.lang.Object)
	- locked <0x000000044ad46780> (a java.lang.Object)
	at task7.DeadlockDemo$$Lambda/0x00000205b7003520.run(Unknown Source)
	at java.lang.Thread.runWith(java.base@21.0.12.1/Thread.java:1596)
	at java.lang.Thread.run(java.base@21.0.12.1/Thread.java:1583)
"Thread-2":
	at task7.DeadlockDemo.lambda$main$1(DeadlockDemo.java:23)
	- waiting to lock <0x000000044ad46780> (a java.lang.Object)
	- locked <0x000000044ad46790> (a java.lang.Object)
	at task7.DeadlockDemo$$Lambda/0x00000205b7003740.run(Unknown Source)
	at java.lang.Thread.runWith(java.base@21.0.12.1/Thread.java:1596)
	at java.lang.Thread.run(java.base@21.0.12.1/Thread.java:1583)

Found 1 deadlock.

* */
