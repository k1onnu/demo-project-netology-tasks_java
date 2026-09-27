package task7;

public class Livelock {
    static class Worker {
        private final String name;
        private boolean busy = false;

        Worker(String name) { this.name = name; }

        boolean isBusy() { return busy; }

        void work(Worker other) {
            while (true) {
                if (!other.isBusy()) {
                    // другой свободен
                    busy = false;
                    System.out.println(name + ": уступаю " + other.name);
                    continue; // крутимся
                }
                // оба заняты, значит ничего не делаем
                busy = true;
            }
        }

        void setBusy(boolean b) { busy = b; }
    }

    public static void main(String[] args) {
        Worker w1 = new Worker("Worker-1");
        Worker w2 = new Worker("Worker-2");
        w1.setBusy(true);
        w2.setBusy(true);

        Thread t1 = new Thread(() -> w1.work(w2), "T1");
        Thread t2 = new Thread(() -> w2.work(w1), "T2");

        t1.start();
        t2.start();

        // через 2 секунды убиваем иначе вечный цикл
        try { Thread.sleep(2000); } catch (InterruptedException e) {}
        System.out.println("Программа не продвигается — livelock");
        System.exit(0);
    }
}

//Программа не продвигается — livelock
