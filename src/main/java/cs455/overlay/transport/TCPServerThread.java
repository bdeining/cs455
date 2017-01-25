package cs455.overlay.transport;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class TCPServerThread {

    private int portNum;

    private ServerSocket serverSocket;

    public TCPServerThread(int portNum) {
        this.portNum = portNum;
        try {
            serverSocket = new ServerSocket(portNum);
        } catch (IOException e) {
            e.printStackTrace();
        }

        listenOnServerSocket();
    }

    private void listenOnServerSocket() {
        while (true) {
            try {
                Socket client = serverSocket.accept();
                TCPReceiverThread tcpReceiverThread = new TCPReceiverThread(client);
                tcpReceiverThread.run();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}