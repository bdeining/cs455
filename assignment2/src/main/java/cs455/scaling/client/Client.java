package cs455.scaling.client;

import java.io.IOException;
import java.math.BigInteger;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.LinkedList;

public class Client {

    private static String serverHost;

    private LinkedList<String> hashCodes;

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
        hashCodes = new LinkedList<>();
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

        while (true) {

            ByteBuffer buffer = generateMessage();
            client.write(buffer);
            System.out.println("sending message test1");
            buffer.clear();
            Thread.sleep(1000 / messageRate);
        }

    }

    private ByteBuffer generateMessage() {
        byte[] bytes = new byte[8000];
        for (int i = 0; i < 8000; i++) {
            bytes[i] = (byte) i;
        }
        try {
            String hashCode = SHA1FromBytes(bytes);
            System.out.println(hashCode);
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
