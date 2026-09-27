package task7;

import java.util.concurrent.locks.ReentrantLock;

public class StarvationFixed {
    private static final ReentrantLock lock = new ReentrantLock(true); // fair!

    public static void main(String[] args) throws InterruptedException {
        Runnable task = () -> {
            for (int j = 0; j < 5; j++) {
                lock.lock();
                try {
                    System.out.println(Thread.currentThread().getName() + " работает (" + (j + 1) + ")");
                } finally {
                    lock.unlock();
                }
            }
        };

        Thread[] threads = new Thread[6];
        for (int i = 0; i < threads.length; i++) {
            threads[i] = new Thread(task, "Thread-" + i);
            threads[i].setPriority(i == 0 ? Thread.MIN_PRIORITY : Thread.MAX_PRIORITY);
        }

        for (Thread t : threads) t.start();
        for (Thread t : threads) t.join();
        System.out.println("Все получили доступ — starvation предотвращён");
    }
}

/*
* Thread-0 работает (1)
Thread-1 работает (1)
Thread-2 работает (1)
Thread-3 работает (1)
Thread-5 работает (1)
Thread-4 работает (1)
Thread-0 работает (2)
Thread-1 работает (2)
Thread-2 работает (2)
Thread-3 работает (2)
Thread-5 работает (2)
Thread-4 работает (2)
Thread-0 работает (3)
Thread-1 работает (3)
Thread-2 работает (3)
Thread-3 работает (3)
Thread-5 работает (3)
Thread-4 работает (3)
Thread-0 работает (4)
Thread-1 работает (4)
Thread-2 работает (4)
Thread-3 работает (4)
Thread-5 работает (4)
Thread-4 работает (4)
Thread-0 работает (5)
Thread-1 работает (5)
Thread-2 работает (5)
Thread-3 работает (5)
Thread-5 работает (5)
Thread-4 работает (5)
Все получили доступ — starvation предотвращён
* */
