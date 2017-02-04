package cs455.overlay.node;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.concurrent.ConcurrentHashMap;

import cs455.overlay.graph.Graph;
import cs455.overlay.transport.TCPSender;
import cs455.overlay.transport.TCPServerThread;
import cs455.overlay.wireformats.Event;
import cs455.overlay.wireformats.EventFactory;

public class Registry implements Node {
    private static final String LIST_MESSAGING_NODES_COMMAND = "list-messaging nodes";

    private static final String LIST_WEIGHTS_COMMAND = "list-weights";

    private static final String SETUP_OVERLAY_COMMAND = "setup-overlay";

    private static final String SEND_OVERLAY_LINK_WEIGHTS_COMMAND = "send-overlay-link-weights";

    private static final String START_COMMAND = "start";

    private static TCPServerThread tcpServerThread;

    private Graph graph;

    private static ConcurrentHashMap<String, Connection> registeredNodes;

    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Invalid number of args");
        }
        Registry registry = null;
        try {
            int portnum = Integer.parseInt(args[0]);
            registry = new Registry(portnum);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }

        if (registry == null) {
            System.out.println("failed to set up registry");
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

    public Registry(int portnum) {
        registeredNodes = new ConcurrentHashMap<>();
        tcpServerThread = new TCPServerThread(this, portnum);

        new Thread(tcpServerThread).start();
    }

    public void registerNode(Socket socket, String eventIp, int port) {
        if (registeredNodes == null) {
            return;
        }

        String ip = socket.getInetAddress()
                .getHostAddress();
        Event event;

        System.out.println(eventIp + " = " + ip);

        if (!eventIp.equals(ip)) {
            byte statusCode = 0;
            event = EventFactory.createRegisterRespone(statusCode,
                    "Registration request "
                            + "unsuccessful. The number of messaging nodes currently constituting the overlay is "
                            + registeredNodes.size());
            sendEventToIp(socket, event);

        }

        if (!registeredNodes.containsKey(ip)) {
            registeredNodes.put(ip + ":" + port, new Connection(socket, port));

            byte statusCode = 1;
            event = EventFactory.createRegisterRespone(statusCode,
                    "Registration request "
                            + "successful. The number of messaging nodes currently constituting the overlay is "
                            + registeredNodes.size());

        } else {
            byte statusCode = 0;
            event = EventFactory.createRegisterRespone(statusCode,
                    "Registration request "
                            + "unsuccessful. The number of messaging nodes currently constituting the overlay is "
                            + registeredNodes.size());

        }
        printRegisteredNodes();
        sendEventToIp(socket, event);
    }

    private static void printRegisteredNodes() {
        System.out.println(registeredNodes.toString());
    }

    private static void sendEventToIp(Socket socket, Event event) {
        try {
            TCPSender tcpSender = new TCPSender(socket);
            tcpSender.sendData(event.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void deRegisterNode(Socket socket, int port) {
        Event event;
        String ip = socket.getInetAddress()
                .getHostAddress() + ":" + port;

        if (registeredNodes.containsKey(ip)) {
            registeredNodes.remove(socket);
            byte statusCode = 1;
            event = EventFactory.createDeregisterResponse(statusCode,
                    "Deregistration request "
                            + "successful. The number of messaging nodes currently constituting the overlay is "
                            + registeredNodes.size());
        } else {
            byte statusCode = 0;
            event = EventFactory.createDeregisterResponse(statusCode,
                    "Deregistration request "
                            + "unsuccessful. The number of messaging nodes currently constituting the overlay is "
                            + registeredNodes.size());
        }

        sendEventToIp(socket, event);
    }

    /**
     * This should result in information about the messaging nodes (hostname, and port-number) being
     * listed. Information for each messaging node should be listed on a separate line.
     **/
    public void listMessagingNodes() {
        for (Map.Entry<String, Connection> entry : registeredNodes.entrySet()) {
            Socket socket = entry.getValue()
                    .getSocket();
            System.out.println(socket.getInetAddress()
                    .getHostAddress() + ":" + socket.getPort());
        }
    }

    /**
     * This should list information about links comprising the overlay. Each link’s information should be on
     * a separate line and include information about the nodes that it connects and the weight of that link.
     * For example, carrot.cs.colostate.edu:2000 broccoli.cs.colostate.edu:5001 8, indicates that
     * the link is between two messaging nodes (carrot.cs.colostate.edu:2000) and
     * (broccoli.cs.colostate.edu:5001) with a link weight of 8.
     */
    public void listWeights() {
        System.out.println("listing weights");
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
        System.out.println("setup overlay + " + numberOfConnections);
        List<String> registeredHosts = Collections.list(registeredNodes.keys());
        graph = new Graph(registeredHosts, numberOfConnections);
        graph.generateConnectedGraph();

        for(Map.Entry<String, Connection> entry : registeredNodes.entrySet()) {
            List<String> messageNode = graph.generateMessageNodeList(entry.getKey());
            System.out.println(messageNode.toString());
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
        System.out.println("sending link weights");
        if(graph == null) {
            return;
        }

        List<String> linkWeights = graph.generateLinkWeightList();

        Event event = EventFactory.createLinkWeights(linkWeights.size(), linkWeights);
        for(Map.Entry<String, Connection> entry : registeredNodes.entrySet()) {
            Socket socket = entry.getValue().getSocket();
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
        System.out.println("starting : number of rounds " + numberOfRounds);
        Event event = EventFactory.createTaskInitiate(numberOfRounds);

        for(Map.Entry<String, Connection> stringConnectionEntry : registeredNodes.entrySet()) {
            Socket socket = stringConnectionEntry.getValue().getSocket();
            sendEventToIp(socket, event);
        }

    }

    @Override
    public String getHostname() {
        return null;
    }

    @Override
    public int getPort() {
        return 0;
    }
}

