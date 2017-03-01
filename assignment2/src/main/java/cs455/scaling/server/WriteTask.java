package cs455.scaling.server;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;

public class WriteTask implements Task {
    private final SelectionKey selectionKey;

    public WriteTask(SelectionKey selectionKey) {
        this.selectionKey = selectionKey;
    }

    @Override
    public void execute() {
        State state = (State) selectionKey.attachment();
        if (!state.getOperation().equals("read")) {
            System.out.println("concurrent bug.  RETURN TO TASK QUEUE");
            return;
        }
        SocketChannel channel = (SocketChannel) selectionKey.channel();

        try {
            //System.out.println("write");
            ByteBuffer payload = ByteBuffer.wrap(state.getHashCode().getBytes());
            payload.rewind();
            channel.write(payload);
            state.setOperation("reading");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
