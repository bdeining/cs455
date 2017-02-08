package cs455.overlay.node;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

import cs455.overlay.graph.Graph;
import cs455.overlay.transport.TCPSender;
import cs455.overlay.transport.TCPServerThread;
import cs455.overlay.wireformats.Event;
import cs455.overlay.wireformats.EventFactory;
import cs455.overlay.wireformats.PullTrafficSummary;
import cs455.overlay.wireformats.TaskComplete;
import cs455.overlay.wireformats.TrafficSummary;

public class Registry implements Node {
    private static final String LIST_MESSAGING_NODES_COMMAND = "list-messaging nodes";

    private static final String LIST_WEIGHTS_COMMAND = "list-weights";

    private static final String SETUP_OVERLAY_COMMAND = "setup-overlay";

    private static final String SEND_OVERLAY_LINK_WEIGHTS_COMMAND = "send-overlay-link-weights";

    private static final String START_COMMAND = "start";

    private static final AtomicLong SENT_SUM = new AtomicLong();

    private static final AtomicInteger TOTAL_TRAFFIC_SUMMARY = new AtomicInteger();

    private static final AtomicLong RECIEVED_SUM = new AtomicLong();

    private static final AtomicLong SENT_SUMMATION_SUM = new AtomicLong();

    private static final AtomicLong RECIEVED_SUMMATION_SUM = new AtomicLong();

    private static TCPServerThread tcpServerThread;

    private static ConcurrentHashMap<String, Boolean> taskCompleteNodes;

    private static ConcurrentHashMap<String, Connection> registeredNodes;

    private static int portnum;

    private static String inetAddress;

    private Graph graph;

    private ConcurrentHashMap<String, String> trafficSummary;

    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Invalid number of args");
        }
        Registry registry = null;
        try {
            portnum = Integer.parseInt(args[0]);
            registry = new Registry();
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }

        if (registry == null) {
            System.out.println("Failed to set up registry");
            return;
        }

        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNext()) {
            String command = scanner.nextLine();

            if (command.equals(LIST_MESSAGING_NODES_COMMAND)) {
                registry.listMessagingNodes();
            } else if (command.equals(LIST_WEIGHTS_COMMAND)) {
                registry.listWeights();
            } else if (command.contains(SETUP_OVERLAY_COMMAND)) {
                Integer integer = parseCommandInteger(command);
                if (integer != null) {
                    registry.setupOverlay(integer);
                }
            } else if (command.equals(SEND_OVERLAY_LINK_WEIGHTS_COMMAND)) {
                registry.sendOverlayLinkWeights();
            } else if (command.contains(START_COMMAND)) {
                Integer integer = parseCommandInteger(command);
                if (integer != null) {
                    registry.start(integer);
                }

            }
        }
    }

    private static Integer parseCommandInteger(String command) {
        Integer integer = null;
        String[] strings = command.split(" ");
        if (strings.length == 2) {
            try {
                integer = Integer.parseInt(strings[1]);
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }
        return integer;
    }

    public Registry() {
        try {
            inetAddress = InetAddress.getLocalHost()
                    .getHostAddress();
        } catch (UnknownHostException e) {
            System.out.println("Unable to get host information.");
        }

        registeredNodes = new ConcurrentHashMap<>();
        tcpServerThread = new TCPServerThread(this, portnum);
        new Thread(tcpServerThread).start();
    }

    @Override
    public String getHostname() {
        return inetAddress;
    }

    @Override
    public int getPort() {
        return portnum;
    }

    public void registerNode(Socket socket, String eventIp, int port) {
        String ip = socket.getInetAddress().getHostAddress();
        Event event;
        byte statusCode = 0;

        /* Check if there is a mismatch in the address that is specified in the registration
           request and the IP address of the request (the socket’s input stream). */
        if (!eventIp.equals(ip)) {
            statusCode = 0;
            event = EventFactory.createRegisterRespone(statusCode,
                    "Registration request unsuccessful.  There is a mismatch between the address specified and the originating ip.");
            sendEventToIp(socket, event);
        }

        /* Check if the node had previously registered and has a valid entry in its registry. */
        if (!registeredNodes.containsKey(ip)) {
            registeredNodes.put(ip + ":" + port, new Connection(socket, port));
            statusCode = 1;
            event = EventFactory.createRegisterRespone(statusCode,
                    "Registration request "
                            + "successful. The number of messaging nodes currently constituting the overlay is "
                            + registeredNodes.size());

        } else {
            event = EventFactory.createRegisterRespone(statusCode,
                    "Registration request unsuccessful. This node is already registered");
        }

        try {
            sendEventToIpWithoutCatch(socket, event);
        } catch (IOException e) {
            if(registeredNodes.containsKey(ip)) {
                registeredNodes.remove(ip);
                System.out.println("Unable to send Registry Response to " + ip + ".  De-registering node.");
            }
        }
    }

    public void deRegisterNode(Socket socket, String eventIp, int port) {
        Event event;
        String ip = socket.getInetAddress().getHostAddress() + ":" + port;
        byte statusCode = 0;

        /* Check if there is a mismatch in the address that is specified in the deregistration
           request and the IP address of the request (the socket’s input stream). */
        if (!eventIp.equals(ip)) {
            event = EventFactory.createDeregisterResponse(statusCode,
                    "Deregistration request unsuccessful.  There is a mismatch between the address specified and the originating ip.");
            sendEventToIp(socket, event);
        }


        if (registeredNodes.containsKey(ip)) {
            registeredNodes.remove(socket);
            statusCode = 1;
            event = EventFactory.createDeregisterResponse(statusCode,
                    "Deregistration request "
                            + "successful. The number of messaging nodes currently constituting the overlay is "
                            + registeredNodes.size());
        } else {
            event = EventFactory.createDeregisterResponse(statusCode,
                    "Deregistration request unsuccessful. This node is not registed with the registry.");
        }

        sendEventToIp(socket, event);
    }

    /**
     * This should result in information about the messaging nodes (hostname, and port-number) being
     * listed. Information for each messaging node should be listed on a separate line.
     **/
    public void listMessagingNodes() {
        registeredNodes.keySet().forEach(System.out::println);

/*        for (Map.Entry<String, Connection> entry : registeredNodes.entrySet()) {
            Socket socket = entry.getValue()
                    .getSocket();
            System.out.println(socket.getInetAddress()
                    .getHostAddress() + ":" + socket.getPort());
        }*/
    }

    /**
     * This should list information about links comprising the overlay. Each link’s information should be on
     * a separate line and include information about the nodes that it connects and the weight of that link.
     * For example, carrot.cs.colostate.edu:2000 broccoli.cs.colostate.edu:5001 8, indicates that
     * the link is between two messaging nodes (carrot.cs.colostate.edu:2000) and
     * (broccoli.cs.colostate.edu:5001) with a link weight of 8.
     */
    public void listWeights() {
        if(graph == null) {
            System.out.println("No links have been weighted yet");
            return;
        }
        List<String> linkWeights = graph.generateLinkWeightList();
        linkWeights.forEach(System.out::println);
    }

    /**
     * This should result in the registry setting up the overlay. It does so by sending messaging nodes
     * messages containing information about the messaging nodes that it should connect to. The registry
     * tracks the connection counts for each messaging node and will send the MESSAGING_NODES_LIST
     * message (see Section 2.3) to every messaging node. A sample specification of this command is
     * setup-overlay 4 that will result in the creation of an overlay where each messaging node is
     * connected to exactly 4 other messaging nodes in the overlay. You should handle the error condition
     * where the number of messaging nodes is less than the connection limit that is specified.
     * NOTE: You are not required to deal with the case where a messaging node is added or removed after
     * the overlay has been set up. You must however deal with the case where a messaging node registers
     * and deregisters from the registry before the overlay is set up.
     *
     * @param numberOfConnections
     */
    public void setupOverlay(int numberOfConnections) {
        if(registeredNodes.size() < numberOfConnections ) {
            System.out.println("Unable to set-up overlay.  The number of connections is greater than the number of registered nodes.");
            return;
        }

        List<String> registeredHosts = Collections.list(registeredNodes.keys());
        graph = new Graph(registeredHosts, numberOfConnections);
        graph.generateConnectedGraph();

        for (Map.Entry<String, Connection> entry : registeredNodes.entrySet()) {
            List<String> messageNode = graph.generateMessageNodeList(entry.getKey());
            Connection connection = entry.getValue();
            Socket socket = connection.getSocket();
            Event event = EventFactory.createMessagingNodeList(messageNode.size(), messageNode);
            sendEventToIp(socket, event);
        }
    }

    /**
     * This should result in a Link_Weights message being sent to all registered nodes in the overlay. This
     * command is issued once after the setup-overlay command has been issued. This also allows all
     * nodes in the system to be aware of not just all the nodes in the system, but also the complete set of
     * links in the system.
     */
    public void sendOverlayLinkWeights() {
        if (graph == null) {
            return;
        }

        List<String> linkWeights = graph.generateLinkWeightList();

        Event event = EventFactory.createLinkWeights(linkWeights.size(), linkWeights);
        for (Connection connection : registeredNodes.values()) {
            Socket socket = connection.getSocket();
            sendEventToIp(socket, event);
        }
    }

    /**
     * The start command results in nodes exchanging messages within the overlay. Each node in the
     * overlay will be responding for sending number-of-rounds messages. An advantage of this is that you
     * are able to debug your system with a smaller set of messages and verify correctness of your programs
     * across a wide range of values. A detailed description is provided in section 4 below.
     *
     * @param numberOfRounds
     */
    public void start(int numberOfRounds) {
        taskCompleteNodes = new ConcurrentHashMap<>();
        trafficSummary = new ConcurrentHashMap<>();
        Event event = EventFactory.createTaskInitiate(numberOfRounds);

        for (Connection connection : registeredNodes.values()) {
            Socket socket = connection.getSocket();
            sendEventToIp(socket, event);
        }
    }

    /*                              Message Handling                                       */
    /* ----------------------------------------------------------------------------------- */

    public void taskComplete(TaskComplete event) {
        String host = event.getIp() + ":" + event.getPort();
        taskCompleteNodes.put(host, true);
        if (allNodesComplete(taskCompleteNodes)) {
            try {
                Thread.sleep(15000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            sendPullTrafficSummary();
        }
    }

    public void trafficSummary(TrafficSummary trafficSummary) {
        String source = trafficSummary.getIp() + ":" + trafficSummary.getPort();
        TOTAL_TRAFFIC_SUMMARY.incrementAndGet();
        SENT_SUM.addAndGet(trafficSummary.getMessagesSent());
        RECIEVED_SUM.addAndGet(trafficSummary.getMessagesRecieved());
        SENT_SUMMATION_SUM.addAndGet(trafficSummary.getMessagesSentSummation());
        RECIEVED_SUMMATION_SUM.addAndGet(trafficSummary.getMessagesRecievedSummation());

        String output = String.format("%10s%20s%20s%20s%20s%20s",
                source,
                trafficSummary.getMessagesSent(),
                trafficSummary.getMessagesRecieved(),
                trafficSummary.getMessagesSentSummation(),
                trafficSummary.getMessagesRecievedSummation(),
                trafficSummary.getMessagesRelayed());

        this.trafficSummary.put(source, output);

        if (allNodesComplete(this.trafficSummary) && TOTAL_TRAFFIC_SUMMARY.get() == this.trafficSummary.size()) {
            System.out.println(String.format("%10s%20s%20s%20s%20s%20s",
                    "",
                    "Sent",
                    "Received",
                    "Sent Sum",
                    "Received Sum",
                    "Relayed"));
            this.trafficSummary.values().forEach(System.out::println);

            System.out.println(String.format("%10s%20s%20s%20s%20s%20s",
                    "Sum",
                    SENT_SUM.get(),
                    RECIEVED_SUM.get(),
                    SENT_SUMMATION_SUM.get(),
                    RECIEVED_SUMMATION_SUM.get(),
                    ""));
            SENT_SUM.set(0);
            RECIEVED_SUM.set(0);
            SENT_SUMMATION_SUM.set(0);
            RECIEVED_SUMMATION_SUM.set(0);
            TOTAL_TRAFFIC_SUMMARY.set(0);
            taskCompleteNodes = new ConcurrentHashMap<>();
            this.trafficSummary = new ConcurrentHashMap<>();
        }
    }

    private void sendPullTrafficSummary() {
        for (Map.Entry<String, Connection> stringConnectionMap : registeredNodes.entrySet()) {
            Event event = new PullTrafficSummary();
            Socket socket = stringConnectionMap.getValue()
                    .getSocket();
            sendEventToIp(socket, event);
        }
    }

    private boolean allNodesComplete(ConcurrentHashMap concurrentHashMap) {
        for (Map.Entry<String, Connection> stringConnectionMap : registeredNodes.entrySet()) {
            if (!concurrentHashMap.containsKey(stringConnectionMap.getKey())) {
                return false;
            }
        }

        return true;
    }

    private static void sendEventToIpWithoutCatch(Socket socket, Event event) throws IOException {
        TCPSender tcpSender = new TCPSender(socket);
        tcpSender.sendData(event.getBytes());
    }

    private static void sendEventToIp(Socket socket, Event event) {
        try {
            TCPSender tcpSender = new TCPSender(socket);
            tcpSender.sendData(event.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

