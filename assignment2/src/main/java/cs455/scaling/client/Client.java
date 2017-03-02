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

import cs455.scaling.util.Constants;
import cs455.scaling.util.HashCodeGenerator;

public class Client {

    private static String serverHost;

    private static int serverPort;

    private Selector selector;

    private List<String> hashCodes;

    private static int messageRate;

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
        selector = Selector.open();
        SocketChannel channel = SocketChannel.open();
        channel.configureBlocking(false);
        channel.connect(new InetSocketAddress(serverHost, serverPort));
        channel.register(selector, SelectionKey.OP_CONNECT);

        ByteBuffer buffer = ByteBuffer.allocate(40);

        while (true) {

            selector.select();

            Iterator i = selector.selectedKeys()
                    .iterator();

            while (i.hasNext()) {
                final SelectionKey key = (SelectionKey) i.next();
                i.remove();

                synchronized (key) {

                    if (key.isConnectable()) {
                        if (channel.finishConnect()) {
                            key.interestOps(SelectionKey.OP_READ);
                            writeThread = new WriteThread(key, messageRate, hashCodes);
                            new Thread(writeThread).start();
                        }
                    } else if (key.isReadable()) {
                        buffer.clear();
                        buffer.rewind();

//                        int read = channel.read(buffer);

                        int read = 0;
                        while (buffer.hasRemaining() && read != 1) {
                            read = channel.read(buffer);
                        }

                        if (read == -1) {
                            System.out.println("connection closed");
                        }

                        String hashCode = new String(buffer.array());
                        //System.out.println(hashCode);

                        //byte[] data = new byte[Constants.BUFFER_SIZE];
                        //System.arraycopy(buffer.array(), 0, data, 0, Constants.BUFFER_SIZE);
                        //String hashCode = new String(data);

                        writeThread.incrementMessagesReceived();
                    }
                }
            }
        }
    }
}
