package cs455.overlay.node;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.List;
import java.util.Map;
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
import cs455.overlay.wireformats.Message;

public class MessagingNode implements Node {
    private static final String PRINT_SHORTEST_PATH_COMMAND = "print-shortest-path";

    private static final String EXIT_OVERLAY_COMMAND = "exit-overlay";

    private ConcurrentHashMap<String, Socket> connections;

    private static final AtomicInteger SEND_TRACKER = new AtomicInteger(0);

    private static final AtomicInteger RECEIVE_TRACKER = new AtomicInteger(0);

    private static final AtomicInteger RELAY_TRACKER = new AtomicInteger(0);

    private static final AtomicLong SEND_SUMMATION = new AtomicLong(0);

    private static final AtomicLong RECEIVE_SUMMATION = new AtomicLong(0);

    private Socket registrySocket;

    private static String registryHost;

    private Graph graph;

    private static int registryPort;

    private int listeningPort;

    private TCPSender tcpSender;

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
            registrySocket = new Socket(registryHost, registryPort);
            TCPReceiverThread tcpReceiverThread = new TCPReceiverThread(this, registrySocket);
            new Thread(tcpReceiverThread).start();
            tcpSender = new TCPSender(registrySocket);

            byte[] wrappedData = EventFactory.createRegisterRequest(inetAddress, listeningPort)
                    .getBytes();

            tcpSender.sendData(wrappedData);

        } catch (UnknownHostException e) {
            System.out.println("Unknown host: kq6py");
        } catch (IOException e) {
            System.out.println("No I/O");

        }

    }

    public void generateMapFromLinkWeights(LinkWeights event) {
        graph = new Graph(event.getLinks());
        System.out.println("Link weights are received and processed. Ready to send messages.");
    }

    /**
     * This should print the shortest paths that have been computed to all other the messaging nodes
     * within the system. The listing should also indicate weights associated with the links.
     * e.g. carrot–-8––broccoli––4––-zucchini––-2––brussels––1––onion
     */
    public void printShortestPath() {
        StringBuilder stringBuilder = new StringBuilder("");
        for (Map.Entry<String, Socket> entry : connections.entrySet()) {
            List<String> shortestPaths = graph.getShortestPath(inetAddress + ":" + listeningPort,
                    entry.getKey());
            //            stringBuilder.append(shortestPaths.)
            System.out.println(shortestPaths.toString());
        }
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

    public void addSocket(String hostName, Socket socket) {
        connections.put(hostName, socket);
    }

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
                System.out.println(node);
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

    public synchronized void processMessage(Event event) {
        Message message = (Message) event;

        /* Recieve message */
        if (message.getDestination()
                .equals(inetAddress + ":" + listeningPort)) {
            RECEIVE_TRACKER.incrementAndGet();
            RECEIVE_SUMMATION.addAndGet(message.getPayload());
            return;
        }

        /* Otherwise Relay Message*/
        RELAY_TRACKER.incrementAndGet();
        sendTo(event);
    }

    private synchronized void sendTo(Event event) {
        Message message = (Message) event;
        String destination = message.getDestination();
        List<String> paths = graph.getShortestPath(inetAddress + ":" + listeningPort, destination);
        String sendDest = paths.get(1);
        Socket socket = connections.get(sendDest);
        sendEventToIp(socket, event);
    }

    public void startRounds(int numberOfRounds) {
        if (graph == null) {
            System.out.println("Unable to start round, overlay may not have been set up.");
        }

        String source = inetAddress + ":" + listeningPort;

        for (int i = 0; i < numberOfRounds; i++) {
            String randomDest = graph.getRandomHost(inetAddress + ":" + listeningPort);
            for (int c=0; c < 5; c++) {
                int randomInt = getRandomInt();
                Event event = EventFactory.createMessage(randomInt, source, randomDest);
                SEND_TRACKER.incrementAndGet();
                SEND_SUMMATION.addAndGet(randomInt);
                sendTo(event);
            }
        }

        Event event = EventFactory.createTaskComplete(inetAddress, listeningPort);
        sendEventToIp(registrySocket, event);
    }

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

    private synchronized static void sendEventToIp(Socket socket, Event event) {
        try {
            TCPSender tcpSender = new TCPSender(socket);
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
        return registryPort;
    }
}
