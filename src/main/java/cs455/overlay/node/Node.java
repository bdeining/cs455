package cs455.overlay.node;

public class Node {

    private String hostname;

    private int port;

    public Node(String hostname, int port) {
        this.hostname = hostname;
        this.port = port;
    }

    public String getHostname() {
        return hostname;
    }

    public int getPort() {
        return port;
    }
}
