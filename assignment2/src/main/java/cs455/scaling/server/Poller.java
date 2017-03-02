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
            String output = String.format("[%s] Total Sent Count: %s, Total Received Count: %s",
                    Constants.SIMPLE_DATE_FORMAT.format(cal.getTime()),
                    threadPoolManager.getTaskQueueSize(),
                    0);
            System.out.println(output);


        }
    }
}
