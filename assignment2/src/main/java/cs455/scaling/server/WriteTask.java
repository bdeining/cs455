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
        State state = (State) selectionKey.attachment();
        SocketChannel channel = (SocketChannel) selectionKey.channel();

        try {
            ByteBuffer payload = ByteBuffer.wrap(hashCode.getBytes());
            payload.rewind();

            channel.write(payload);
            state.setOperation("reading");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
