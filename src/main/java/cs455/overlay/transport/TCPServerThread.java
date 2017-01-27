package cs455.overlay.transport;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import cs455.overlay.node.Node;

public class TCPServerThread implements Runnable {

    private int portNum;

    private Map<String, Socket> socketMap;

    private Node node;

    private ServerSocket serverSocket;

    public TCPServerThread(Node node, int portNum) {
        this.node = node;
        socketMap = new ConcurrentHashMap<>();

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
                socketMap.put(client.getInetAddress()
                        .toString(), client);
                TCPReceiverThread tcpReceiverThread = new TCPReceiverThread(node, client);
                new Thread(tcpReceiverThread).start();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public Socket getSocket(String ip) {
        System.out.println(ip + socketMap.get(ip));
        return socketMap.get(ip);
    }
}