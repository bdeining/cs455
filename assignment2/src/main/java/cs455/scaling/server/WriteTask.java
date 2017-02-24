package cs455.scaling.server;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;

public class WriteTask implements Task {
    private SelectionKey selectionKey;

    private String hashCode;

    public WriteTask(SelectionKey selectionKey, String hashCode) {
        this.selectionKey = selectionKey;
        this.hashCode = hashCode;
    }

    @Override
    public void execute() {
        SocketChannel channel = (SocketChannel) selectionKey.channel();
        try {

            System.out.println("write " + hashCode);
            ByteBuffer payload = ByteBuffer.wrap(hashCode.getBytes());
            channel.write(payload);

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
