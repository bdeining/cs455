package cs455.scaling.server;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;

public class WriteTask implements Task {
    private final SelectionKey selectionKey;

    private String hashCode;

    private ThreadPoolManager threadPoolManager;

    public WriteTask(ThreadPoolManager threadPoolManager, SelectionKey selectionKey, String hashCode) {
        this.selectionKey = selectionKey;
        this.hashCode = hashCode;
        this.threadPoolManager = threadPoolManager;
    }

    @Override
    public void execute() {
        synchronized (selectionKey) {
            if (!selectionKey.isValid()) {
                System.out.println("channel closed");
                return;
            }

            State state = (State) selectionKey.attachment();

            if (state == null || !state.getOperation().equals("read")) {
                threadPoolManager.addTaskToQueue(this);
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
            threadPoolManager.incrementThroughput();

            state.setOperation("reading");
            selectionKey.interestOps(SelectionKey.OP_READ);
            selectionKey.selector().wakeup();
        }
    }
}
