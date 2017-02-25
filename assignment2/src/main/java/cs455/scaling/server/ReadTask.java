package cs455.scaling.server;

import java.io.IOException;
import java.net.Socket;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.security.NoSuchAlgorithmException;

import cs455.scaling.util.Constants;
import cs455.scaling.util.HashCodeGenerator;

public class ReadTask implements Task {

    private SelectionKey selectionKey;

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
        //System.out.println("read");
        try {
            int numRead = channel.read(buffer);

            if (numRead == -1) {

                Socket socket = channel.socket();
                SocketAddress remoteAddr = socket.getRemoteSocketAddress();
                System.out.println("Connection closed by client: " + remoteAddr);
                channel.close();
                selectionKey.cancel();
                return;
            }

            byte[] data = new byte[numRead];
            System.arraycopy(buffer.array(), 0, data, 0, numRead);
            String hashCode = HashCodeGenerator.generateHashCodeFromBytes(data);
            //System.out.println(hashCode);
            selectionKey.attach("read");
            Task writeTask = new WriteTask(selectionKey, hashCode);
            //System.out.println("add write to queue " + hashCode);
            threadPoolManager.addTaskToQueue(writeTask);
        } catch (IOException | NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }
}
