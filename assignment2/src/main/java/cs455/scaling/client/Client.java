package cs455.scaling.client;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class Client {

    private static String serverHost;

    private static int serverPort;

    private static int messageRate;

    private final List<String> hashCodes;

    private WriteThread writeThread;

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
        hashCodes = Collections.synchronizedList(new ArrayList<>());
        try {
            startClient();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void startClient() throws IOException, InterruptedException {
        Selector selector = Selector.open();
        SocketChannel channel = SocketChannel.open();
        channel.configureBlocking(false);
        channel.connect(new InetSocketAddress(serverHost, serverPort));
        channel.register(selector, SelectionKey.OP_CONNECT);

        while (true) {

            selector.select();

            Iterator i = selector.selectedKeys()
                    .iterator();

            while (i.hasNext()) {
                final SelectionKey key = (SelectionKey) i.next();
                i.remove();

                if (key.isConnectable()) {
                    if (channel.finishConnect()) {
                        ByteBuffer buffer = ByteBuffer.allocate(40);
                        channel.register(selector, SelectionKey.OP_READ, buffer);
                        writeThread = new WriteThread(key, messageRate, hashCodes);
                        new Thread(writeThread).start();
                    }
                } else if (key.isReadable()) {
                    synchronized (key) {
                        ByteBuffer buffer = (ByteBuffer) key.attachment();
                        int read = channel.read(buffer);

                        if (read == -1) {
                            key.cancel();
                            break;
                        }

                        if (!buffer.hasRemaining()) {
                            String hashCode = new String(buffer.array());
                            removeHashCode(hashCode);
                            buffer.clear();
                            buffer.rewind();
                        }
                    }
                }
            }
        }
    }

    private void removeHashCode(String hashCode) {
        synchronized (hashCodes) {
            if (hashCodes.contains(hashCode)) {
                hashCodes.remove(hashCode);
                writeThread.incrementMessagesReceived();
            } else {
                System.out.println("bad hashcode " + hashCode);
            }
        }
    }
}
