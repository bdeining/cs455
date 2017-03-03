package cs455.scaling.client;

import java.util.Calendar;

import cs455.scaling.util.Constants;

public class Poller implements Runnable {
    private WriteThread writeThread;

    public Poller(WriteThread writeThread) {
        this.writeThread = writeThread;
    }

    @Override
    public void run() {
        while (true) {
            try {
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            Calendar cal = Calendar.getInstance();
            String output = String.format("[%s] Total Sent Count: %s, Total Received Count: %s",
                    Constants.SIMPLE_DATE_FORMAT.format(cal.getTime()),
                    writeThread.getMessagesSent(),
                    writeThread.getMessagesReceived());
            System.out.println(output);

        }
    }
}
