package cs455.scaling.client;

import java.io.IOException;
import java.math.BigInteger;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Iterator;
import java.util.LinkedList;

public class Client {

    private static String serverHost;

    private LinkedList<String> hashCodes;

    private static int serverPort;

    private Selector selector;

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
        hashCodes = new LinkedList<>();
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

        while (true) {
            selector.select();

            Iterator i = selector.selectedKeys().iterator();
            System.out.println(selector.selectedKeys().size());
            while (i.hasNext()) {
                SelectionKey key = (SelectionKey) i.next();

                i.remove();
                if (key.isConnectable()) {
                    System.out.println("connectible");
                    if (channel.finishConnect()) {
                        key.interestOps(SelectionKey.OP_READ | SelectionKey.OP_WRITE);
                    }
                } else if (key.isReadable()) {
                    System.out.println("read");
                    ByteBuffer buf = ByteBuffer.allocate(8000);
                    buf.rewind();
                    int read = channel.read(buf);
                    System.out.println("bytes read " + read);
                    byte[] data = new byte[read];
                    System.arraycopy(buf.array(), 0, data, 0, read);
                    System.out.println(new String(data));
                } else if (key.isWritable()) {
                    System.out.println("write");
                    ByteBuffer buffer = generateMessage();
                    channel.write(buffer);
                    buffer.clear();
                    Thread.sleep(1000 / messageRate);
                }
            }
        }
    }

    private ByteBuffer generateMessage() {
        byte[] bytes = new byte[8000];
        for (int i = 0; i < 8000; i++) {
            bytes[i] = (byte) i;
        }
        try {
            String hashCode = SHA1FromBytes(bytes);
            System.out.println("sending " + hashCode);
            hashCodes.add(hashCode);
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
}
