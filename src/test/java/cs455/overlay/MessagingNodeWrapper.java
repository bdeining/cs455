package cs455.overlay;

import cs455.overlay.node.MessagingNode;

/**
 * Created by bdeininger on 1/25/17.
 */
public class MessagingNodeWrapper implements Runnable {

    private int port;

    private String ip;

    public MessagingNodeWrapper(String ip, int port) {
        this.ip = ip;
        this.port = port;
    }

    @Override
    public void run() {
        MessagingNode messagingNode = new MessagingNode(ip, port);
    }
}
