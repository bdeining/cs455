package cs455.overlay.transport;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cs455.overlay.node.Node;
import cs455.overlay.node.Registry;

public class TCPServerThread implements Runnable {

    private int portNum;

    private List<Socket> socketList;

    private Node node;

    private ServerSocket serverSocket;

    public TCPServerThread(Node node, int portNum) {
        this.node = node;
        socketList = new ArrayList<>();

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
                socketList.add(client);
                TCPReceiverThread tcpReceiverThread = new TCPReceiverThread(node, client);
                new Thread(tcpReceiverThread).start();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}