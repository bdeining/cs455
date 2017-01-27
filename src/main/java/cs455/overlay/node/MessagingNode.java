package cs455.overlay.node;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

import cs455.overlay.transport.TCPSender;
import cs455.overlay.wireformats.Event;
import cs455.overlay.wireformats.EventFactory;

public class MessagingNode implements Node {
    private static final String PRINT_SHORTEST_PATH_COMMAND = "print-shortest-path";

    private static final String EXIT_OVERLAY_COMMAND = "exit-overlay";

    private static int port;

    private TCPSender tcpSender;

    private String inetAddress;

    public static void main(String[] args) {
        if (args.length != 2) {
            System.out.println("Not enough args");
        }

        System.out.println(args[0]);
        System.out.println(args[1]);
        try {
            String registryHost = args[0];
            port = Integer.parseInt(args[1]);
            new MessagingNode(registryHost, port);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
    }

    public MessagingNode(String registryHost, int registryPort) {
        try {
            inetAddress = InetAddress.getLocalHost()
                    .toString();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }

        System.out.println(inetAddress.toString());

        try {
            Socket socket = new Socket(registryHost, registryPort);
            tcpSender = new TCPSender(socket);

            byte[] wrappedData = EventFactory.createRegisterRequest(inetAddress, registryPort)
                    .getBytes();

            tcpSender.sendData(wrappedData);
            tcpSender.closeSocket();
        } catch (UnknownHostException e) {
            System.out.println("Unknown host: kq6py");
            System.exit(1);
        } catch (IOException e) {
            System.out.println("No I/O");
            System.exit(1);
        }

    }

    /**
     * This should print the shortest paths that have been computed to all other the messaging nodes
     * within the system. The listing should also indicate weights associated with the links.
     * e.g. carrot–-8––broccoli––4––-zucchini––-2––brussels––1––onion
     */
    public void printShortestPath() {

    }

    /**
     * This allows a messaging node to exit the overlay. The messaging node should first send a
     * deregistration message (see Section 2.2) to the registry and await a response before exiting and
     * terminating the process.
     */
    public void exitOverlay() {
        Event event = EventFactory.createDeregisterRequest(inetAddress, port);
        try {
            tcpSender.sendData(event.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getHostname() {
        return inetAddress;
    }

    @Override
    public int getPort() {
        return port;
    }
}
