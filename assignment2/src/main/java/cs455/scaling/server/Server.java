package cs455.scaling.server;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.UnknownHostException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Calendar;
import java.util.Iterator;

import cs455.scaling.util.Constants;

public class Server {

    private static int portNum;

    private static int threadPoolSize;

    private Selector selector;

    private ThreadPoolManager threadPoolManager;

    private static String inetAddress;

    public static void main(String[] args) {
        if (args.length != 2) {
            return;
        }

        try {
            portNum = Integer.parseInt(args[0]);
            threadPoolSize = Integer.parseInt(args[1]);
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return;
        }

        try {
            inetAddress = InetAddress.getLocalHost()
                    .getHostAddress();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }

        try {
            new Server();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Server() throws IOException {
        startServer();
    }

    public void startServer() throws IOException {
        threadPoolManager = new ThreadPoolManager(threadPoolSize);

        this.selector = Selector.open();
        ServerSocketChannel serverChannel = ServerSocketChannel.open();
        serverChannel.configureBlocking(false);

        serverChannel.socket()
                .bind(new InetSocketAddress(inetAddress, portNum));
        serverChannel.register(this.selector, SelectionKey.OP_ACCEPT);

        System.out.println("Server started...");
        long before = System.currentTimeMillis() / 1000;

        while (true) {
            this.selector.select();

            Iterator keys = this.selector.selectedKeys()
                    .iterator();
            while (keys.hasNext()) {
                SelectionKey key = (SelectionKey) keys.next();
                keys.remove();

                if (!key.isValid()) {
                    continue;
                }

                if (key.isAcceptable()) {
                    this.accept(key);
                } else if (key.isReadable()) {
                    this.read(key);
                    threadPoolManager.assignTaskIfPossible();
                }
            }



            long after = System.currentTimeMillis() / 1000;
            if (after - before == 10) {
                Calendar cal = Calendar.getInstance();
                String output = String.format("[%s] Total Sent Count: %s, Total Received Count: %s",
                        Constants.SIMPLE_DATE_FORMAT.format(cal.getTime()),
                        0,
                        0);
                System.out.println(output);
                before = System.currentTimeMillis() / 1000;
            }
        }
    }

    private void accept(SelectionKey key) throws IOException {
        ServerSocketChannel serverChannel = (ServerSocketChannel) key.channel();
        SocketChannel channel = serverChannel.accept();
        channel.configureBlocking(false);
        Socket socket = channel.socket();
        SocketAddress remoteAddr = socket.getRemoteSocketAddress();
        System.out.println("Connected to: " + remoteAddr);

        channel.register(this.selector, SelectionKey.OP_READ, "reading");
    }

    private void read(SelectionKey key) throws IOException {
//        key.interestOps(SelectionKey.OP_WRITE);
        Task task = new ReadTask(threadPoolManager, key);
        threadPoolManager.addTaskToQueue(task);
    }
}