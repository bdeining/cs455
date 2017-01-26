package cs455.overlay.transport;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import cs455.overlay.node.Node;
import cs455.overlay.node.Registry;

public class TCPServerThread implements Runnable {

    private int portNum;

    private ServerSocket serverSocket;

    public TCPServerThread(int portNum) {
        this.portNum = portNum;
        try {
            serverSocket = new ServerSocket(portNum);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        while (true) {
            try {
                Socket client = serverSocket.accept();
                TCPReceiverThread tcpReceiverThread = new TCPReceiverThread(client);
                new Thread(tcpReceiverThread).start();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}