package cs455.scaling.server;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.LinkedList;
import java.util.Queue;

public class ThreadPoolManager {

    private final Queue<Worker> threadPool;

    private final Deque<Task> tasks;

    public ThreadPoolManager(int threadPoolSize) {
        threadPool = new LinkedList<>();
        for (int i = 0; i < threadPoolSize; i++) {
            Worker worker = new Worker(this);
            threadPool.add(worker);
            new Thread(worker).start();
        }
        tasks = new ArrayDeque<>();
    }

    public void addTaskToQueue(Task task) {
        synchronized (tasks) {
            tasks.add(task);
        }
    }

    public void addWorkerToThreadPool(Worker worker) {
        synchronized (threadPool) {
            threadPool.add(worker);
        }
    }

    public void assignTaskIfPossible() {
        Task task = getTask();
        if(task == null) {
            return;
        }

        if (threadPool == null || threadPool.size() == 0) {
            return;
        }

        synchronized (threadPool) {
            Worker worker = threadPool.poll();
            if (worker != null) {
                worker.assignTask(task);
            }
        }
    }

    public Task getTask() {
        if (tasks == null || tasks.size() == 0) {
            return null;
        }

        synchronized (tasks) {
            return tasks.poll();
        }
    }
}
