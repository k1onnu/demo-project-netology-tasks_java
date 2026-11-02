package task14;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.StructuredTaskScope;

public class Main {
    static final int N = 10_000;

    static void task(int id) {
        try { Thread.sleep(100); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
        if (id < 10) {
            System.out.println("Task " + id + " in " + Thread.currentThread() + " virtual=" + Thread.currentThread().isVirtual());
        }
    }

    public static void main(String[] args) throws Exception {
        long start = System.nanoTime();
        List<Thread> vt = new ArrayList<>();
        for (int i = 0; i < N; i++) {
            int id = i;
            vt.add(Thread.ofVirtual().name("v-" + i).start(() -> task(id)));
        }
        for (Thread t : vt) t.join();
        System.out.println("Virtual: " + (System.nanoTime() - start) / 1_000_000 + " ms\n");

        start = System.nanoTime();
        List<Thread> pt = new ArrayList<>();
        for (int i = 0; i < N; i++) {
            int id = i;
            pt.add(new Thread(() -> task(id), "p-" + i));
        }
        for (Thread t : pt) t.start();
        for (Thread t : pt) t.join();
        System.out.println("Platform: " + (System.nanoTime() - start) / 1_000_000 + " ms\n");

        start = System.nanoTime();
        try (var scope = new StructuredTaskScope.ShutdownOnFailure()) {
            for (int i = 0; i < N; i++) {
                int id = i;
                scope.fork(() -> { task(id); return null; });
            }
            scope.join().throwIfFailed();
        }
        System.out.println("StructuredTaskScope: " + (System.nanoTime() - start) / 1_000_000 + " ms");
    }
}