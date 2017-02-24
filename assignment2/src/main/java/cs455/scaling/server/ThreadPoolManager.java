package cs455.scaling.server;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;

public class ThreadPoolManager {

    private final List<Worker> threadPool;

    private final LinkedBlockingQueue<Task> tasks;

    public ThreadPoolManager(int threadPoolSize) {
        threadPool = new ArrayList<>();
        for (int i = 0; i < threadPoolSize; i++) {
            Worker worker = new Worker(this);
            threadPool.add(worker);
            new Thread(worker).start();
        }
        tasks = new LinkedBlockingQueue<>();
    }

    public void addTaskToQueue(Task task) {
        synchronized (tasks) {
            tasks.add(task);
        }
    }

    public void removeWorkerFromThreadPool(Worker worker) {
        synchronized (threadPool) {
            threadPool.remove(worker);
        }
    }

    public void addWorkerToThreadPool(Worker worker) {
        synchronized (threadPool) {
            threadPool.add(worker);
        }
    }

    public Task getTask() {
        if (tasks == null) {
            return null;
        }

        Task task;
        synchronized (tasks) {
            task = tasks.poll();
        }
        return task;
    }
}
