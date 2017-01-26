package cs455.overlay;

import cs455.overlay.node.MessagingNode;

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
