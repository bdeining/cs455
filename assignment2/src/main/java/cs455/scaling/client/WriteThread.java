package cs455.scaling.client;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Random;

import cs455.scaling.util.Constants;
import cs455.scaling.util.HashCodeGenerator;

public class WriteThread implements Runnable {
    private final List<String> hashCodes;

    private SelectionKey selectionKey;

    private SocketChannel socketChannel;

    private int messageRate;

    private long messagesSent;

    public WriteThread(SocketChannel socketChannel, int messageRate, List<String> hashCodes) {
        this.messageRate = messageRate;
        this.socketChannel = socketChannel;
        this.hashCodes = hashCodes;
    }

    public void setKey(SelectionKey selectionKey) {
        this.selectionKey = selectionKey;
    }

    @Override
    public void run() {
        while (true) {
            if (selectionKey != null && selectionKey.isWritable()) {
                ByteBuffer buffer = generateMessage();
                try {
                    socketChannel.write(buffer);
                    messagesSent++;
                } catch (IOException e) {
                    e.printStackTrace();
                }
                buffer.clear();
                selectionKey = null;
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
        for (int i = 0; i < bytes.length; i++) {
            bytes[i] = (byte) getRandomInt();
        }

        try {
            String hashCode = HashCodeGenerator.generateHashCodeFromBytes(bytes);
            synchronized (hashCodes) {
                hashCodes.add(hashCode);
            }

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        return ByteBuffer.wrap(bytes);
    }

    private int getRandomInt() {
        Random random = new Random();
        return random.nextInt();
    }

    public long getMessagesSent() {
        return messagesSent;
    }
}
