package cs455.scaling.client;

import java.io.IOException;
import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Formatter;
import java.util.List;
import java.util.Random;

import cs455.scaling.util.Constants;

public class WriteThread implements Runnable {
    private final List<String> hashCodes;

    private final SelectionKey selectionKey;

    private int messageRate;

    private long messagesSent;

    private long messagesReceived = 0;

    public WriteThread(SelectionKey selectionKey, int messageRate, List<String> hashCodes) {
        this.messageRate = messageRate;
        this.selectionKey = selectionKey;
        this.hashCodes = hashCodes;
    }

    @Override
    public void run() {
        int count = 0;
        while (true) {

            ByteBuffer buffer = generateMessage();
            String hashCode = generateHashCodeFromBytes(buffer.array());
            synchronized (hashCodes) {
                hashCodes.add(hashCode);
            }

            synchronized (selectionKey) {
                SocketChannel socketChannel = (SocketChannel) selectionKey.channel();
                try {
                    socketChannel.write(buffer);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            count++;
            messagesSent++;

            if (count == 10) {
                Calendar cal = Calendar.getInstance();
                String output = String.format("[%s] Total Sent Count: %s, Total Received Count: %s",
                        Constants.SIMPLE_DATE_FORMAT.format(cal.getTime()),
                        messagesSent,
                        messagesReceived);
                System.out.println(output);
                count = 0;
            }

            try {
                Thread.sleep(1000 / messageRate);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private ByteBuffer generateMessage() {
        byte[] bytes = new byte[Constants.BUFFER_SIZE];
        new Random().nextBytes(bytes);
        return ByteBuffer.wrap(bytes);
    }

    private String generateHashCodeFromBytes(byte[] data) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-1");
            final byte[] hash = md.digest(data);
            Formatter formatter = new Formatter();
            for (byte b : hash) {
                formatter.format("%02x", b);
            }
            return formatter.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        // Will not happen
        return null;

    }

    public synchronized void incrementMessagesReceived() {
        messagesReceived++;
    }

    public long getMessagesSent() {
        return messagesSent;
    }
}
