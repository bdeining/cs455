package cs455.scaling.client;

import java.io.IOException;
import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Random;

public class WriteThread implements Runnable {

    private SelectionKey selectionKey;

    private SocketChannel socketChannel;

    private List<String> hashCodes;

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
        byte[] bytes = new byte[8000];
        for (int i = 0; i < 8000; i++) {
            bytes[i] = (byte) getRandomInt();
        }

        try {
            String hashCode = SHA1FromBytes(bytes);
            synchronized (hashCodes) {
                hashCodes.add(hashCode);
            }

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        return ByteBuffer.wrap(bytes);
    }

    //TODO Common Util
    private String SHA1FromBytes(byte[] data) throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance("SHA1");
        byte[] hash = digest.digest(data);
        BigInteger hashInt = new BigInteger(1, hash);

        return hashInt.toString(16);
    }

    private int getRandomInt() {
        Random random = new Random();
        return random.nextInt();
    }

    public long getMessagesSent() {
        return messagesSent;
    }
}
