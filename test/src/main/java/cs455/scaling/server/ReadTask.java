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
        synchronized (this.selectionKey) {
            selectionKey.interestOps(SelectionKey.OP_WRITE);
            selectionKey.selector().wakeup();
        }
    }

    @Override
    public void execute() {

        synchronized (selectionKey) {
            if (!selectionKey.isValid()) {
                System.out.println("channel closed");
                return;
            }

            SocketChannel channel = (SocketChannel) selectionKey.channel();
            if (!channel.isConnected()) {
                closeChannel(channel);
                return;
            }

            State state = (State) selectionKey.attachment();
            if (!state.getOperation().equals("reading")) {
                threadPoolManager.addTaskToQueue(this);
                return;
            }

            ByteBuffer byteBuffer = state.getBytes();
            if (byteBuffer == null) {
                byteBuffer = ByteBuffer.allocate(Constants.BUFFER_SIZE);
                byteBuffer.clear();
                state.setBytes(byteBuffer);
            }

            int read = readChannel(byteBuffer, channel);

            if (read == -1) {
                closeChannel(channel);
                return;
            }

            if (byteBuffer.hasRemaining()) {
                threadPoolManager.addTaskToQueue(this);
                return;
            }

            state.setBytes(null);
            state.setOperation("read");
            byteBuffer.rewind();
            byte[] data = createCopyOfData(byteBuffer);
            String hashcode = generateHashCode(data);
            if (hashcode == null) {
                System.out.println("null hashcode");
                return;
            }
            threadPoolManager.incrementThroughput();
            assignWriteTask(hashcode);
        }
    }

    private int readChannel(ByteBuffer byteBuffer, SocketChannel socketChannel) {
        int read = 0;
        String connection = null;
        try {
            connection = socketChannel.getRemoteAddress().toString();
            read = socketChannel.read(byteBuffer);
        } catch (IOException e) {
            e.printStackTrace();
            threadPoolManager.removeConnection(connection);
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
