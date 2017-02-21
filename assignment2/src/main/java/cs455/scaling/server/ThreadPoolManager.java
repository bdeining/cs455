package cs455.scaling.server;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;

public class ThreadPoolManager {

    private List<Worker> threadPool;

    private LinkedBlockingQueue<Task> tasks;

    public ThreadPoolManager(int threadPoolSize) {
        threadPool = createWorkers(threadPoolSize);
        tasks = new LinkedBlockingQueue<>();
    }

    public List<Worker> createWorkers(int threadPoolSize) {
        List<Worker> workers = new ArrayList<>(threadPoolSize);
        for(int i=0; i<threadPoolSize; i++) {
            Worker worker = new Worker();
            workers.add(worker);
            new Thread(worker).start();
        }
        return workers;
    }

    public void addTaskToQueue(Task task) {
        tasks.add(task);
    }
    
}
