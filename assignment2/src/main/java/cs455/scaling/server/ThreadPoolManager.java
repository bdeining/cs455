package cs455.scaling.server;

import java.nio.channels.SelectionKey;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.LinkedBlockingQueue;

public class ThreadPoolManager {

    private final List<Worker> threadPool;

    private final LinkedBlockingQueue<Task> tasks;

    private final Map<SelectionKey, List<String>> dataMapper;


    public ThreadPoolManager(int threadPoolSize) {
        threadPool = new ArrayList<>();
        for (int i = 0; i < threadPoolSize; i++) {
            Worker worker = new Worker(this);
            threadPool.add(worker);
            new Thread(worker).start();
        }

        tasks = new LinkedBlockingQueue<>();
        dataMapper = new HashMap<>();
    }

    public void addTaskToQueue(Task task) {
        synchronized (tasks) {
            System.out.println("add task " + task);
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

/*    public void addHashCodeToKey(SelectionKey selectionKey, String hashCode) {
        synchronized (dataMapper) {
            if (dataMapper.containsKey(selectionKey)) {
                List<String> hashCodes = new ArrayList<>(dataMapper.get(selectionKey));
                hashCodes.add(hashCode);
                dataMapper.put(selectionKey, hashCodes);
            } else {
                dataMapper.put(selectionKey, Arrays.asList(hashCode));
            }
        }
    }

    public String getHashCodeFromKey(SelectionKey selectionKey) {
        synchronized (dataMapper) {
            if(dataMapper.containsKey(selectionKey)) {
                List<String> hashCodes = dataMapper.get(selectionKey);
                String hashCode = hashCodes.remove(0);
                dataMapper.put(selectionKey, hashCodes);
                return hashCode;
            }
        }
        return null;
    }*/

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
