package cs455.scaling.server;

public class Worker implements Runnable {

    private final ThreadPoolManager threadPoolManager;

    private Task task;

    public Worker(ThreadPoolManager threadPoolManager) {
        this.threadPoolManager = threadPoolManager;
    }

    public synchronized void assignTask(Task task) {
        this.task = task;
    }

    public synchronized Task getTask() {
        return task;
    }

    @Override
    public void run() {
        while (true) {
            if(getTask() != null) {
                getTask().execute();
                assignTask(null);
                threadPoolManager.addWorkerToThreadPool(this);
            }
        }
    }
}
