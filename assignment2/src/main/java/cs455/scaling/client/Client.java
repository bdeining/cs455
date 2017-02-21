package cs455.scaling.client;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;

public class Client {

    private static String serverHost;

    private static int serverPort;

    private static int messageRate;

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
        try {
            startClient();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            return;
        }
    }

    private void startClient() throws IOException, InterruptedException {

        InetSocketAddress hostAddress = new InetSocketAddress(serverHost, serverPort);
        SocketChannel client = SocketChannel.open(hostAddress);

        System.out.println("Client... started");

        String threadName = Thread.currentThread().getName();

        // Send messages to server
        String [] messages = new String []
                {threadName + ": test1",threadName + ": test2",threadName + ": test3"};

        for (int i = 0; i < messages.length; i++) {
            byte [] message = new String(messages [i]).getBytes();
            ByteBuffer buffer = ByteBuffer.wrap(message);
            client.write(buffer);
            System.out.println(messages [i]);
            buffer.clear();
            Thread.sleep(5000);
        }
        client.close();
    }

    private ByteBuffer generateMessage() {
        byte[] bytes = new byte[8000];
        for(int i = 0; i< 8000; i++) {
            bytes[i] = (byte) i;
        }
        return ByteBuffer.wrap(bytes);
    }
}
