package cs455.scaling.server;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class ThreadPoolManager {

    private final Queue<Worker> threadPool;

    private final Queue<Task> taskQueue;

    private Integer throughPut = 0;

    private final List<String> selectionKeyList;

    public ThreadPoolManager(int threadPoolSize) {
        threadPool = new LinkedList<>();
        for (int i = 0; i < threadPoolSize; i++) {
            Worker worker = new Worker(this);
            threadPool.add(worker);
            new Thread(worker).start();
        }
        selectionKeyList = new ArrayList<>();
        taskQueue = new LinkedList<>();
    }

    public void addTaskToQueue(Task task) {
        synchronized (taskQueue) {
            taskQueue.add(task);
        }
    }

    public void addWorkerToThreadPool(Worker worker) {
        synchronized (threadPool) {
            threadPool.add(worker);
        }
    }

    public void assignTaskIfPossible() {
        Task task = getTask();
        if (task == null) {
            return;
        }

        if (threadPool == null || threadPool.size() == 0) {
            return;
        }

        Worker worker;
        synchronized (threadPool) {
            worker = threadPool.poll();
        }
        if (worker != null) {
            worker.assignTask(task);
        }
    }

    public int getTaskQueueSize() {
        return taskQueue.size();
    }

    public Task getTask() {
        if (taskQueue != null && taskQueue.size() != 0) {
            synchronized (taskQueue) {
                return taskQueue.poll();
            }
        }
        return null;
    }

    public void addConnection(String connection) {
        synchronized (selectionKeyList) {
            selectionKeyList.add(connection);
        }
    }

    public void removeConnection(String connection) {
        synchronized (selectionKeyList) {
            selectionKeyList.remove(connection);
        }
    }

    public int getConnectionCount() {
        return selectionKeyList.size();
    }

    public Integer getThroughput() {
        synchronized (throughPut) {
            return throughPut / 2;
        }
    }

    public void incrementThroughput() {
        synchronized (throughPut) {
            throughPut++;
        }
    }

    public void resetThroughput() {
        synchronized (throughPut) {
            throughPut = 0;
        }
    }
}
