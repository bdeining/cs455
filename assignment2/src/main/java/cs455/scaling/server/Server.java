package cs455.scaling.server;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;

public class Server {

    /*
    bytebuffer
    socketchannel
     */

    private static int portNum;

    private static int threadPoolSize;

    private ServerSocketChannel serverSocketChannel;

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
        serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.configureBlocking(false);
        serverSocketChannel.socket().bind(new InetSocketAddress(inetAddress,portNum));

        Selector selector = Selector.open();
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);

        while (true) {
            selector.select();
            Iterator it = selector.selectedKeys().iterator();
            while (it.hasNext()) {
                SelectionKey selKey = (SelectionKey) it.next();

                if (selKey.isAcceptable()) {
                    ServerSocketChannel ssChannel = (ServerSocketChannel) selKey.channel();
                    SocketChannel sc = ssChannel.accept();
                }
                it.remove();
            }
        }
    }
}
