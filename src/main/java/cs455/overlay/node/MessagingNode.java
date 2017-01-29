package cs455.overlay.node;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

import cs455.overlay.transport.TCPReceiverThread;
import cs455.overlay.transport.TCPSender;
import cs455.overlay.transport.TCPServerThread;
import cs455.overlay.wireformats.Event;
import cs455.overlay.wireformats.EventFactory;

public class MessagingNode implements Node {
    private static final String PRINT_SHORTEST_PATH_COMMAND = "print-shortest-path";

    private static final String EXIT_OVERLAY_COMMAND = "exit-overlay";

    private ConcurrentHashMap<String, Socket> connections;

    private static final AtomicInteger SEND_TRACKER = new AtomicInteger(0);

    private static final AtomicInteger RECIEVE_TRACKER = new AtomicInteger(0);

    private static final AtomicInteger RELAY_TRACKER = new AtomicInteger(0);

    private static final AtomicLong SEND_SUMMATION = new AtomicLong(0);

    private static final AtomicLong RECIEVE_SUMMATION = new AtomicLong(0);

    private static int port;

    private int listeningPort;

    private TCPSender tcpSender;

    private String inetAddress;

    public static void main(String[] args) {
        if (args.length != 2) {
            System.out.println("Not enough args");
        }

        MessagingNode messagingNode = null;

        try {
            String registryHost = args[0];
            port = Integer.parseInt(args[1]);
            messagingNode = new MessagingNode(registryHost, port);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }

        if (messagingNode == null) {
            System.out.println("Messaging node not set up");
            return;
        }

        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNext()) {
            String command = scanner.nextLine();

            if (command.equals(PRINT_SHORTEST_PATH_COMMAND)) {
                messagingNode.printShortestPath();
            } else if (command.equals(EXIT_OVERLAY_COMMAND)) {
                messagingNode.exitOverlay();
            }
        }
    }

    public MessagingNode(String registryHost, int registryPort) {
        connections = new ConcurrentHashMap<>();

        try {
            inetAddress = InetAddress.getLocalHost()
                    .getHostAddress();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }

        TCPServerThread tcpServerThread = new TCPServerThread(this, 0);
        new Thread(tcpServerThread).start();
        //TODO refactor port stuff
        listeningPort = tcpServerThread.getPort();
        System.out.println(listeningPort);

        try {
            Socket socket = new Socket(registryHost, registryPort);
            TCPReceiverThread tcpReceiverThread = new TCPReceiverThread(this, socket);
            new Thread(tcpReceiverThread).start();
            tcpSender = new TCPSender(socket);

            byte[] wrappedData = EventFactory.createRegisterRequest(inetAddress, listeningPort)
                    .getBytes();

            tcpSender.sendData(wrappedData);

        } catch (UnknownHostException e) {
            System.out.println("Unknown host: kq6py");
        } catch (IOException e) {
            System.out.println("No I/O");

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
        Event event = EventFactory.createDeregisterRequest(inetAddress, listeningPort);
        try {
            tcpSender.sendData(event.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void exit() {
        System.exit(0);
    }

    public void setupMessagingNodeLinks(List<String> nodes) {
        for (String node : nodes) {
            System.out.println("set up " + node);

            String[] strings = node.split(":");
            if (strings.length != 2) {
                continue;
            }

            int port;
            try {
                port = Integer.parseInt(strings[1]);
            } catch (NumberFormatException e) {
                continue;
            }

            try {
                Socket socket = new Socket(strings[0], port);
                connections.put(socket.getInetAddress()
                        .getHostAddress(), socket);
                TCPReceiverThread tcpReceiverThread = new TCPReceiverThread(this, socket);
                new Thread(tcpReceiverThread).start();

                TCPSender tcpSender = new TCPSender(socket);
                tcpSender.sendData(EventFactory.createMessage(12, "source", "destination")
                        .getBytes());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void processMessage(Event event) {
        System.out.println("Process message" + event);
    }

    public void startRounds(int numberOfRounds) {
        System.out.println("Start + " + numberOfRounds);
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
