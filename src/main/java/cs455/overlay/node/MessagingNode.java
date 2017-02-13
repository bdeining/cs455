package cs455.overlay.node;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

import cs455.overlay.graph.Graph;
import cs455.overlay.transport.TCPReceiverThread;
import cs455.overlay.transport.TCPSender;
import cs455.overlay.transport.TCPServerThread;
import cs455.overlay.wireformats.Event;
import cs455.overlay.wireformats.EventFactory;
import cs455.overlay.wireformats.LinkWeights;

public class MessagingNode implements Node {
    private static final String PRINT_SHORTEST_PATH_COMMAND = "print-shortest-path";

    private static final String EXIT_OVERLAY_COMMAND = "exit-overlay";

    private ConcurrentHashMap<String, Socket> connections;

    private static final AtomicInteger SEND_TRACKER = new AtomicInteger(0);

    private static final AtomicInteger RECEIVE_TRACKER = new AtomicInteger(0);

    private static final AtomicInteger RELAY_TRACKER = new AtomicInteger(0);

    private static final AtomicLong SEND_SUMMATION = new AtomicLong(0);

    private static final AtomicLong RECEIVE_SUMMATION = new AtomicLong(0);

    private TCPSender tcpSender;

    private static String registryHost;

    private Graph graph;

    private static int registryPort;

    private int listeningPort;

    private Socket registrySocket;

    private String inetAddress;

    public static void main(String[] args) {
        if (args.length != 2) {
            System.out.println("Not enough args");
        }

        MessagingNode messagingNode = null;

        try {
            registryHost = args[0];
            registryPort = Integer.parseInt(args[1]);
            messagingNode = new MessagingNode();
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

    public MessagingNode() {
        connections = new ConcurrentHashMap<>();

        try {
            inetAddress = InetAddress.getLocalHost()
                    .getHostAddress();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }

        TCPServerThread tcpServerThread = new TCPServerThread(this, 0);
        new Thread(tcpServerThread).start();
        listeningPort = tcpServerThread.getPort();

        try {
            tcpSender = new TCPSender();
            new Thread(tcpSender).start();
            registrySocket = new Socket(registryHost, registryPort);
            TCPReceiverThread tcpReceiverThread = new TCPReceiverThread(this, registrySocket);
            new Thread(tcpReceiverThread).start();

            byte[] wrappedData = EventFactory.createRegisterRequest(inetAddress, listeningPort)
                    .getBytes();

            tcpSender.sendData(wrappedData, registrySocket);

        } catch (IOException e) {
            System.out.println("Could not communicate with registry.");
        }
    }

    public void printRegisterResponse(String additionalInfo) {
        System.out.println(additionalInfo);
    }

    /**
     * This should print the shortest paths that have been computed to all other the messaging nodes
     * within the system. The listing should also indicate weights associated with the links.
     * e.g. carrot–-8––broccoli––4––-zucchini––-2––brussels––1––onion
     */
    public void printShortestPath() {
        if (graph == null) {
            System.out.println(
                    "Could not print shortest paths, graph may not have been initialized.");
            return;
        }
        System.out.println(graph.getShortestPathList(inetAddress + ":" + listeningPort));
    }

    /**
     * This allows a messaging node to exit the overlay. The messaging node should first send a
     * deregistration message (see Section 2.2) to the registry and await a response before exiting and
     * terminating the process.
     */
    public void exitOverlay() {
        Event event = EventFactory.createDeregisterRequest(inetAddress, listeningPort);
        try {
            tcpSender.sendData(event.getBytes(), registrySocket);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void generateMapFromLinkWeights(LinkWeights event) {
        graph = new Graph(event.getLinks());
        System.out.println("Link weights are received and processed. Ready to send messages.");
    }

    /**
     * Exits the program when a DeregisterResponse is received from the registry
     */
    public void exit() {
        System.exit(0);
    }

    /**
     * When a socket is established from a calling messaging node, add the socket to
     * the connections map
     *
     * @param hostName - the hostname of the messaging node establishing the connection
     * @param socket   - the socket between the remote messaging node and this messaging node
     */
    public void addSocket(String hostName, Socket socket) {
        connections.put(hostName, socket);
    }

    /**
     * Sets up the messaging node links from a MessagingNodesList Event.  This will establish
     * connections with each node listed in the nodes list.  If there are no connections to establish
     * the method does nothing.
     *
     * @param nodes         - the list of nodes to connect to
     * @param numberOfPeers - the number of peers in the nodes list
     */
    public void setupMessagingNodeLinks(List<String> nodes, int numberOfPeers) {
        if (numberOfPeers == 0) {
            System.out.println(
                    "All connections are established. Number of connections: " + numberOfPeers);
            return;
        }

        for (String node : nodes) {
            String[] split = node.split(" ");
            String[] strings = split[1].split(":");
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
                        .getHostAddress() + ":" + port, socket);
                TCPReceiverThread tcpReceiverThread = new TCPReceiverThread(this, socket);
                new Thread(tcpReceiverThread).start();

                Event event = EventFactory.createIdentification(inetAddress + ":" + listeningPort);
                sendEventToIp(socket, event);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        System.out.println(
                "All connections are established. Number of connections: " + numberOfPeers);
    }

    /**
     * Processes the received message from the round.  If the destination is this messaging node,
     * increment the counters appropriately, otherwise use the graph to relay the message along the
     * shortest path.
     *
     * @param event       - the message received
     * @param destination - the destination of the message
     * @param payload     - the payload of the message
     */
    public void processMessage(Event event, String destination, int payload) {
        /* Receive message */
        if (destination.equals(inetAddress + ":" + listeningPort)) {
            RECEIVE_TRACKER.incrementAndGet();
            RECEIVE_SUMMATION.addAndGet(payload);
            return;
        }

        /* Otherwise Relay Message*/
        RELAY_TRACKER.incrementAndGet();
        sendEventToDesination(event, destination);
    }

    /**
     * Sends the event to the destination along the shortest path.
     *
     * @param event       - the event to send
     * @param destination - the destination of the event, used for SSP
     */
    private void sendEventToDesination(Event event, String destination) {
        List<String> paths = graph.getShortestPath(inetAddress + ":" + listeningPort, destination);
        String sendDest = paths.get(1);
        Socket socket = connections.get(sendDest);
        sendEventToIp(socket, event);
    }

    /**
     * Sends messages based on the number of rounds.  Each round, 5 messages are sent to a random
     * node in the overlay
     *
     * @param numberOfRounds - the number of rounds to send messages
     */
    public void startRounds(int numberOfRounds) {
        if (graph == null) {
            System.out.println("Unable to start round, overlay may not have been set up.");
        }

        String source = inetAddress + ":" + listeningPort;

        for (int i = 0; i < numberOfRounds; i++) {
            String randomDest = graph.getRandomHost(inetAddress + ":" + listeningPort);
            for (int c = 0; c < 5; c++) {
                int randomInt = getRandomInt();
                Event event = EventFactory.createMessage(randomInt, source, randomDest);
                SEND_TRACKER.incrementAndGet();
                SEND_SUMMATION.addAndGet(randomInt);
                sendEventToDesination(event, randomDest);
            }
        }

        Event event = EventFactory.createTaskComplete(inetAddress, listeningPort);
        sendEventToIp(registrySocket, event);
    }

    /**
     * Generates a TrafficSummary message and sends it to the registry.  This happens at the end of
     * all rounds.  All counters are then reset.
     */
    public void pullTrafficSummary() {
        Event event = EventFactory.createTrafficSummary(inetAddress,
                listeningPort,
                SEND_TRACKER.get(),
                RECEIVE_TRACKER.get(),
                RELAY_TRACKER.get(),
                SEND_SUMMATION.get(),
                RECEIVE_SUMMATION.get());
        sendEventToIp(registrySocket, event);

        SEND_TRACKER.set(0);
        RECEIVE_TRACKER.set(0);
        RELAY_TRACKER.set(0);
        SEND_SUMMATION.set(0);
        RECEIVE_SUMMATION.set(0);
    }

    private int getRandomInt() {
        Random random = new Random();
        return random.nextInt();
    }

    /**
     * Sends the event to the socket.
     *
     * @param socket - the socket to send the event to
     * @param event  - the event to send
     */
    private void sendEventToIp(Socket socket, Event event) {
        try {
            tcpSender.sendData(event.getBytes(), socket);
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
        return registryPort;
    }
}
