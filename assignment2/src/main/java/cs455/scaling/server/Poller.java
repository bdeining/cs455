package cs455.scaling.server;

import java.util.Calendar;

import cs455.scaling.util.Constants;

public class Poller implements Runnable {
    private ThreadPoolManager threadPoolManager;

    public Poller(ThreadPoolManager threadPoolManager) {
        this.threadPoolManager = threadPoolManager;
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
            String output = String.format(
                    "[%s] Current Server Throughput: %s messages/s, Active Client Connections: %s",
                    Constants.SIMPLE_DATE_FORMAT.format(cal.getTime()),
                    threadPoolManager.getThroughput(),
                    threadPoolManager.getConnectionCount());
            System.out.println(output);
            threadPoolManager.resetThroughput();
        }
    }
}
