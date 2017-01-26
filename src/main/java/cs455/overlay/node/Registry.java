package cs455.overlay.node;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.ConcurrentHashMap;

import cs455.overlay.transport.TCPServerThread;

public class Registry {

    private TCPServerThread tcpServerThread;

    private static ConcurrentHashMap<String, Integer> registeredNodes;

    private static final String LIST_MESSAGING_NODES_COMMAND = "list-messaging nodes";

    private static final String LIST_WEIGHTS_COMMAND = "list-weights";

    private static final String SETUP_OVERLAY_COMMAND = "setup-overlay";

    private static final String SEND_OVERLAY_LINK_WEIGHTS_COMMAND = "send-overlay-link-weights";

    private static final String START_COMMAND = "start";

    public static void main(String[] args) {
        if(args.length != 1) {
            System.out.println("Invalid number of args");
        }
        Registry registry = null;
        try {
            int portnum = Integer.parseInt(args[0]);
            registry = new Registry(portnum);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }

        if(registry == null) {
            System.out.println("failed to set up registry");
            return;
        }

        Scanner scanner = new Scanner(System.in);
        while(scanner.hasNext()) {
            String command = scanner.nextLine();

            if (command.contains(LIST_MESSAGING_NODES_COMMAND)) {
                String[] strings = command.substring(LIST_MESSAGING_NODES_COMMAND.length()).split(
                        " ");
                if(strings.length == 0) {
                    continue;
                }

                registry.listMessagingNodes(new ArrayList<>(Arrays.asList(strings)));
            } else if(command.contains(LIST_WEIGHTS_COMMAND)) {
                registry.listWeights();
            } else if(command.contains(SETUP_OVERLAY_COMMAND)) {
                String[] strings = command.split(" ");
                if(strings.length != 2) {
                    continue;
                }
                // TODO check number
                registry.setupOverlay(Integer.parseInt(strings[1]));
            } else if(command.contains(SEND_OVERLAY_LINK_WEIGHTS_COMMAND)) {
                registry.sendOverlayLinkWeights();
            } else if(command.contains(START_COMMAND)) {
                String[] strings = command.split(" ");
                System.out.println(command);
                if(strings.length != 2) {
                    continue;
                }
                // TODO check number
                registry.start(Integer.parseInt(strings[1]));
            }
        }
    }

    public Registry(int portnum) {
        registeredNodes = new ConcurrentHashMap<>();
        tcpServerThread = new TCPServerThread(portnum);

        new Thread(tcpServerThread).start();
//        tcpServerThread.run();
        //tcpServerThread.run();
    }

    public static void registerNode(Node node) {
        if(registeredNodes == null) {
            return;
        }

        if(!registeredNodes.containsKey(node.getHostname())) {
            System.out.println("Registering : " + node.getHostname() + " " + node.getPort());
            registeredNodes.put(node.getHostname(), node.getPort());

        } else {
            System.out.println("Node already registered : " + node.getHostname() + " " + node.getPort());
        }
    }

    /**
     * This should result in information about the messaging nodes (hostname, and port-number) being
     * listed. Information for each messaging node should be listed on a separate line.
     **/
    public void listMessagingNodes(List<String> nodes) {
        System.out.println("list nodes : " + nodes.toString());
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
     *  This should result in the registry setting up the overlay. It does so by sending messaging nodes
     *  messages containing information about the messaging nodes that it should connect to. The registry
     *  tracks the connection counts for each messaging node and will send the MESSAGING_NODES_LIST
     *  message (see Section 2.3) to every messaging node. A sample specification of this command is
     *  setup-overlay 4 that will result in the creation of an overlay where each messaging node is
     *  connected to exactly 4 other messaging nodes in the overlay. You should handle the error condition
     *  where the number of messaging nodes is less than the connection limit that is specified.
     *  NOTE: You are not required to deal with the case where a messaging node is added or removed after
     *  the overlay has been set up. You must however deal with the case where a messaging node registers
     *  and deregisters from the registry before the overlay is set up.
     *
     * @param numberOfConnections
     */
    public void setupOverlay(int numberOfConnections) {
        System.out.println("setup overlay + " + numberOfConnections);
    }

    /**
     * This should result in a Link_Weights message being sent to all registered nodes in the overlay. This
     * command is issued once after the setup-overlay command has been issued. This also allows all
     * nodes in the system to be aware of not just all the nodes in the system, but also the complete set of
     * links in the system.
     *
     */
    public void sendOverlayLinkWeights() {
        System.out.println("sending link weights");

    }

    /**
     * The start command results in nodes exchanging messages within the overlay. Each node in the
     * overlay will be responding for sending number-of-rounds messages. An advantage of this is that you
     * are able to debug your system with a smaller set of messages and verify correctness of your programs
     * across a wide range of values. A detailed description is provided in section 4 below.
     * @param numberOfRounds
     */
    public void start(int numberOfRounds) {
        System.out.println("starting : number of rounds " + numberOfRounds);
    }
}

