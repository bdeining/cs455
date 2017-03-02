package cs455.scaling.server;

import java.io.IOException;
import java.net.Socket;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Formatter;

import cs455.scaling.util.Constants;

public class ReadTask implements Task {

    private final SelectionKey selectionKey;

    private ThreadPoolManager threadPoolManager;

    public ReadTask(ThreadPoolManager threadPoolManager, SelectionKey selectionKey) {
        this.selectionKey = selectionKey;
        this.threadPoolManager = threadPoolManager;
    }

    @Override
    public void execute() {
        SocketChannel channel = (SocketChannel) selectionKey.channel();

        ByteBuffer buffer = ByteBuffer.allocate(Constants.BUFFER_SIZE);
        buffer.clear();
        try {

            int read = 0;
            while (buffer.hasRemaining() && read != 1) {
                read = channel.read(buffer);
            }

            if (read == -1) {
                Socket socket = channel.socket();
                SocketAddress remoteAddr = socket.getRemoteSocketAddress();
                System.out.println("Connection closed by client: " + remoteAddr);
                channel.close();
                selectionKey.cancel();
                return;
            }

            byte[] data = new byte[Constants.BUFFER_SIZE];
            System.arraycopy(buffer.array(), 0, data, 0, Constants.BUFFER_SIZE);
            String hashcode = null;

            try {
                MessageDigest md = MessageDigest.getInstance("SHA-1");
                final byte[] hash = md.digest(data);
                Formatter formatter = new Formatter();
                for (byte b : hash) {
                    formatter.format("%02x", b);
                }
                hashcode = formatter.toString();
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }

            if (hashcode == null) {
                return;
            }

            System.out.println(hashcode);
            State state = (State) selectionKey.attachment();
            state.setOperation("read");
            state.setHashCode(hashcode);
            Task writeTask = new WriteTask(selectionKey, hashcode);
            threadPoolManager.addTaskToQueue(writeTask);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
