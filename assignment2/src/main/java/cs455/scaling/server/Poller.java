package cs455.scaling.server;

import java.nio.channels.SelectionKey;
import java.util.Calendar;
import java.util.List;

import cs455.scaling.util.Constants;

public class Poller implements Runnable {
    private ThreadPoolManager threadPoolManager;

    private List<SelectionKey> selectionKeyList;

    public Poller(ThreadPoolManager threadPoolManager, List<SelectionKey> selectionKeyList) {
        this.threadPoolManager = threadPoolManager;
        this.selectionKeyList = selectionKeyList;
    }

    @Override
    public void run() {
        while (true) {
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            Calendar cal = Calendar.getInstance();
            String output = String.format("[%s] Current Server Throughput: %s messages/s, Active Client Connections: %s",
                    Constants.SIMPLE_DATE_FORMAT.format(cal.getTime()),
                    threadPoolManager.getTaskQueueSize(),
                    selectionKeyList.size());
            System.out.println(output);


        }
    }
}
