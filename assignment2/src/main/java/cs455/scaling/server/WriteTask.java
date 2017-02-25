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
        SocketChannel channel = (SocketChannel) selectionKey.channel();
        if (!selectionKey.attachment().equals("read")) {
            System.out.println("concurrent bug.  RETURN TO TASK QUEUE");
        }
        try {
            //System.out.println("write");
            ByteBuffer payload = ByteBuffer.wrap(hashCode.getBytes());
            payload.rewind();
            channel.write(payload);
            selectionKey.interestOps(SelectionKey.OP_READ);
            selectionKey.attach("reading");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
