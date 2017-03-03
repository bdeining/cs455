package cs455.scaling.server;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;

public class WriteTask implements Task {
    private final SelectionKey selectionKey;

    private String hashCode;

    public WriteTask(SelectionKey selectionKey, String hashCode) {
        this.selectionKey = selectionKey;
        this.hashCode = hashCode;
    }

    @Override
    public void execute() {

        if (!selectionKey.isValid()) {
            System.out.println("channel closed");
            return;
        }

        SocketChannel channel = (SocketChannel) selectionKey.channel();

        if (!channel.isConnected()) {
            System.out.println("channel closed");
            return;
        }

        try {
            ByteBuffer payload = ByteBuffer.wrap(hashCode.getBytes());
            payload.rewind();
            channel.write(payload);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
