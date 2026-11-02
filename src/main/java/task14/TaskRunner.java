package task14;

public class TaskRunner implements Runnable {
    private final int id;

    public TaskRunner(int id) {
        this.id = id;
    }

    @Override
    public void run() {
        String threadName = Thread.currentThread().getName();
        boolean virtual = Thread.currentThread().isVirtual();

        try {
            Thread.sleep(100); // 100 мс на задачу
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return;
        }

        if (id < 10) {
            System.out.printf("Task#%d in thread '%s' (virtual=%b)%n",
                    id, threadName, virtual);
        }
    }
}
