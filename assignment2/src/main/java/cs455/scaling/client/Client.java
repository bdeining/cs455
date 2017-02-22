package cs455.scaling.client;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Client {

    private static String serverHost;

    private LinkedList<String> hashCodes;

    private static int serverPort;

    private Lock lock = new ReentrantLock();

    private Selector selector;

    private static int messageRate;

    private WriteThread writeThread;

    private long messagesReceived;

    public static void main(String[] args) {
        if (args.length != 3) {
            return;
        }

        serverHost = args[0];

        try {
            serverPort = Integer.parseInt(args[1]);
            messageRate = Integer.parseInt(args[2]);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }

        new Client();
    }

    public Client() {
        hashCodes = new LinkedList<>();
        try {
            startClient();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void startClient() throws IOException, InterruptedException {

        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");


        selector = Selector.open();
        SocketChannel channel = SocketChannel.open();
        channel.configureBlocking(false);
        channel.connect(new InetSocketAddress(serverHost, serverPort));
        channel.register(selector, SelectionKey.OP_CONNECT);

        long before = System.currentTimeMillis() / 1000;

        while (true) {
            selector.select();

            Iterator i = selector.selectedKeys().iterator();

            while (i.hasNext()) {
                SelectionKey key = (SelectionKey) i.next();

                i.remove();
                if (key.isConnectable()) {
                    if (channel.finishConnect()) {
                        key.interestOps(SelectionKey.OP_READ | SelectionKey.OP_WRITE);
                        writeThread = new WriteThread(channel, messageRate, lock, hashCodes);
                        new Thread(writeThread).start();
                    }
                } else if (key.isReadable()) {
                    ByteBuffer buf = ByteBuffer.allocate(8000);
                    buf.rewind();
                    int read = channel.read(buf);
                    byte[] data = new byte[read];
                    System.arraycopy(buf.array(), 0, data, 0, read);
                    String hashCode = new String(data);
                    removeHashCode(hashCode);
                    messagesReceived++;
                } else if (key.isWritable()) {
                    writeThread.setKey(key);
                }
            }

            long after = System.currentTimeMillis() / 1000;
            if(after - before == 10) {
                Calendar cal = Calendar.getInstance();
                String output = String.format("[%s] Total Sent Count: %s, Total Received Count: %s", sdf.format(cal.getTime()), writeThread.getMessagesSent(), messagesReceived);
                System.out.println(output);
                before = System.currentTimeMillis() / 1000;
            }
        }
    }

    private void removeHashCode(String hashCode) {
        try {
            lock.lock();
            for (String comp : hashCodes) {
                if (hashCode.equals(comp)) {
                    System.out.println("remove " + hashCode);
                    hashCodes.remove(hashCode);
                }
            }
        } finally {
            lock.unlock();
        }
    }
}
