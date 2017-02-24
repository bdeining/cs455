package cs455.scaling.server;

public class Worker implements Runnable {

    private final ThreadPoolManager threadPoolManager;

    public Worker(ThreadPoolManager threadPoolManager) {
        this.threadPoolManager = threadPoolManager;
    }

    @Override
    public void run() {
        while (true) {
            Task task = threadPoolManager.getTask();
            if (task == null) {
                continue;
            }

            threadPoolManager.removeWorkerFromThreadPool(this);
            task.execute();
            threadPoolManager.addWorkerToThreadPool(this);
        }
    }
}
