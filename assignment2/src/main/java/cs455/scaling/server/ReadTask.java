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


        if (!selectionKey.isValid()) {
            System.out.println("channel closed");
            return;
        }

        SocketChannel channel = (SocketChannel) selectionKey.channel();

        if (!channel.isConnected()) {
            closeChannel(channel);
            return;
        }

        ByteBuffer buffer = ByteBuffer.allocate(Constants.BUFFER_SIZE);
        buffer.clear();

        int read = readSocketFully(buffer, channel);

        if (read == -1) {
            closeChannel(channel);
            return;
        }

        byte[] data = createCopyOfData(buffer);
        String hashcode = generateHashCode(data);

        if (hashcode == null) {
            return;
        }
        assignWriteTask(hashcode);
    }

    private int readSocketFully(ByteBuffer byteBuffer, SocketChannel socketChannel) {
        int read = 0;
        try {
            read = socketChannel.read(byteBuffer);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return read;
    }

    private String generateHashCode(byte[] data) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-1");
            final byte[] hash = md.digest(data);
            Formatter formatter = new Formatter();
            for (byte b : hash) {
                formatter.format("%02x", b);
            }
            return formatter.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }

    private void assignWriteTask(String hashcode) {
        Task writeTask = new WriteTask(threadPoolManager, selectionKey, hashcode);
        threadPoolManager.addTaskToQueue(writeTask);
    }

    private byte[] createCopyOfData(ByteBuffer byteBuffer) {
        byte[] data = new byte[Constants.BUFFER_SIZE];
        System.arraycopy(byteBuffer.array(), 0, data, 0, Constants.BUFFER_SIZE);
        return data;
    }

    private void closeChannel(SocketChannel channel) {
        Socket socket = channel.socket();
        SocketAddress remoteAddr = socket.getRemoteSocketAddress();
        System.out.println("Connection closed by client: " + remoteAddr);
        try {
            channel.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        selectionKey.cancel();
        threadPoolManager.removeConnection(remoteAddr.toString());
    }
}
