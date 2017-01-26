package cs455.overlay.node;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

import cs455.overlay.transport.TCPSender;
import cs455.overlay.wireformats.EventFactory;

public class MessagingNode {

    /*
    java cs455.overlay.node.MessagingNode registry-host registry-port
     */

    private String inetAddress;

    public static void main(String[] args) {
        if(args.length != 2) {
            System.out.println("Not enough args");
        }

        try {
            String registryHost = args[0];
            int registryPort = Integer.parseInt(args[1]);
            new MessagingNode(registryHost, registryPort);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }

        Scanner sc = new Scanner(System.in);
        int i = sc.nextInt();

        System.out.println(i);

    }

    public MessagingNode(String registryHost, int registryPort) {
        try {
            inetAddress = InetAddress.getLocalHost().toString();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }

        System.out.println(inetAddress.toString());

        try {
            Socket socket = new Socket(registryHost, registryPort);
            TCPSender tcpSender = new TCPSender(socket);

            byte[] wrappedData = EventFactory.createRegisterRequest(inetAddress, registryPort).getBytes();

            tcpSender.sendData(wrappedData);
            tcpSender.closeSocket();
        } catch (UnknownHostException e) {
            System.out.println("Unknown host: kq6py");
            System.exit(1);
        } catch  (IOException e) {
            System.out.println("No I/O");
            System.exit(1);
        }



    }

    /**
     *  This should print the shortest paths that have been computed to all other the messaging nodes
     *  within the system. The listing should also indicate weights associated with the links.
     *  e.g. carrot–-8––broccoli––4––-zucchini––-2––brussels––1––onion
     */
    public void printShortestPath() {

    }

    /**
     *  This allows a messaging node to exit the overlay. The messaging node should first send a
     *  deregistration message (see Section 2.2) to the registry and await a response before exiting and
     *  terminating the process.
     */
    public void exitOverlay() {

    }
}
