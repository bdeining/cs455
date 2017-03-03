package cs455.scaling.server;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class ThreadPoolManager {

    private final Queue<Worker> threadPool;

    private final Queue<Task> readTasks;

    private final Queue<Task> writeTasks;

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
        readTasks = new LinkedList<>();
        writeTasks = new LinkedList<>();
    }

    public void addTaskToQueue(Task task) {

        if (task instanceof WriteTask) {
            synchronized (writeTasks) {
                writeTasks.add(task);
            }
        } else {
            synchronized (readTasks) {
                readTasks.add(task);
            }
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
        return readTasks.size() + writeTasks.size();
    }

    public Task getTask() {
        if (writeTasks != null && writeTasks.size() != 0) {
            synchronized (writeTasks) {
                return writeTasks.poll();
            }
        }

        if (readTasks != null && readTasks.size() != 0) {
            synchronized (readTasks) {
                return readTasks.poll();
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
        return throughPut;
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
