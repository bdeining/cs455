package cs455.overlay.node;

import java.net.Socket;

public class Connection {

    private Socket socket;

    private int port;

    public Connection(Socket socket, int port) {
        this.socket = socket;
        this.port = port;
    }

    public Socket getSocket() {
        return socket;
    }

    public int getPort() {
        return port;
    }
}
