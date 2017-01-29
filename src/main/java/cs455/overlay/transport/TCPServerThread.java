package cs455.overlay.transport;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import cs455.overlay.node.Node;

public class TCPServerThread implements Runnable {

    private int portNum;

    private Node node;

    private ServerSocket serverSocket;

    public TCPServerThread(Node node, int portNum) {
        this.node = node;
        this.portNum = portNum;
        try {
            serverSocket = new ServerSocket(portNum);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public int getPort() {
        return serverSocket.getLocalPort();
    }

    @Override
    public void run() {
        while (true) {
            try {
                Socket client = serverSocket.accept();
                TCPReceiverThread tcpReceiverThread = new TCPReceiverThread(node, client);
                new Thread(tcpReceiverThread).start();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}