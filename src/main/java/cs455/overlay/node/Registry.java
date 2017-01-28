package cs455.overlay.node;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.concurrent.ConcurrentHashMap;

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

    private static ConcurrentHashMap<String, List<Socket>> registeredNodes;

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

            if (command.equals(LIST_MESSAGING_NODES_COMMAND)) {
                registry.listMessagingNodes();
            } else if(command.equals(LIST_WEIGHTS_COMMAND)) {
                registry.listWeights();
            } else if(command.contains(SETUP_OVERLAY_COMMAND)) {
                Integer integer = parseCommandInteger(command);
                if (integer != null) {
                    registry.setupOverlay(integer);
                }
            } else if(command.equals(SEND_OVERLAY_LINK_WEIGHTS_COMMAND)) {
                registry.sendOverlayLinkWeights();
            } else if(command.contains(START_COMMAND)) {
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
        if(strings.length == 2) {
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

    public void registerNode(Socket socket, String eventIp) {
        if(registeredNodes == null) {
            return;
        }

        String ip = socket.getInetAddress().toString();
        Event event;

        if(!eventIp.equals(ip)) {
            byte statusCode = 0;
            event = EventFactory.createRegisterRespone(statusCode, "Registration request "
                    + "unsuccessful. The number of messaging nodes currently constituting the overlay is "
                    + registeredNodes.size());
            sendEventToIp(socket, event);
        }

        if(!registeredNodes.containsKey(ip)) {
            List<Socket> sockets = new ArrayList<>();
            sockets.add(socket);
            registeredNodes.put(ip, sockets);

            byte statusCode = 1;
             event = EventFactory.createRegisterRespone(statusCode, "Registration request "
                    + "successful. The number of messaging nodes currently constituting the overlay is "
                    + registeredNodes.size());


        } else {
            List<Socket> sockets = registeredNodes.get(ip);
            if (sockets.contains(socket)) {
                byte statusCode = 0;
                event = EventFactory.createRegisterRespone(statusCode, "Registration request "
                        + "unsuccessful. The number of messaging nodes currently constituting the overlay is "
                        + registeredNodes.size());
            } else {
                sockets.add(socket);
                registeredNodes.put(ip, sockets);
                byte statusCode = 1;
                event = EventFactory.createRegisterRespone(statusCode, "Registration request "
                        + "successful. The number of messaging nodes currently constituting the overlay is "
                        + registeredNodes.size());
            }

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

    public void deRegisterNode(Socket socket) {
        Event event;
        String ip = socket.getInetAddress().toString();

        if(registeredNodes.containsKey(ip)) {
            List<Socket> sockets = registeredNodes.get(ip);
            if(sockets.contains(socket)) {
                System.out.println("deregistering " + ip);
                if(sockets.size() == 1) {
                    registeredNodes.remove(ip);
                } else {
                    sockets.remove(socket);
                    registeredNodes.put(ip, sockets);
                }
                byte statusCode = 1;
                event = EventFactory.createDeregisterResponse(statusCode,  "Deregistration request "
                        + "successful. The number of messaging nodes currently constituting the overlay is "
                        + registeredNodes.size());
            } else {
                byte statusCode = 0;
                event = EventFactory.createDeregisterResponse(statusCode,  "Deregistration request "
                        + "unsuccessful. The number of messaging nodes currently constituting the overlay is "
                        + registeredNodes.size());
            }
        } else {
            byte statusCode = 0;
            event = EventFactory.createDeregisterResponse(statusCode,  "Deregistration request "
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
        for (Map.Entry<String, List<Socket>> entry : registeredNodes.entrySet()) {
            for(Socket socket: entry.getValue()) {
                System.out.println(socket.getInetAddress().toString() + ":" + socket.getPort());
            }

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

/*
 Flatten map first
 if(registeredNodes.size() < numberOfConnections) {
            // Error message
            System.out.println("Not enough nodes");
            return;
        }*/

        Map<Socket, List<String>> sockets = new HashMap<>();
        /* Map Sockets and List of Messaging Node Strings */
        for (Map.Entry<String, List<Socket>> entry : registeredNodes.entrySet()) {
            for(Socket socket : entry.getValue()) {
                if (!socket.isClosed() && socket.isConnected()) {
                    sockets.put(socket, new ArrayList<>());
                }
            }
        }

        List<Map.Entry<Socket, List<String>>> entryList = new ArrayList<>(sockets.entrySet());

        /* Linear Ring first.  Connection Pattern   */
        /*
                    A          B          C        D        E
                             A - B      B - C    C - D    D - E
                  A - E

                            A     -    B
                            |          |
                            E  -  D  - C

         */

        /* Connect first and last */
        Map.Entry<Socket, List<String>> entry = entryList.get(0);
        List<String> list = entry.getValue();
        Socket connectingSocket = entryList.get(entryList.size() -1 ).getKey();
        list.add(connectingSocket.getInetAddress().getHostAddress() + ":" + connectingSocket.getPort());

        for (int i=1; i< entryList.size()-1; i++) {
            entry = entryList.get(i);
            list = entry.getValue();
            connectingSocket = entryList.get(i-1).getKey();
            list.add(connectingSocket.getInetAddress().getHostName() + ":" + connectingSocket.getPort());
        }

        /*   Remaining connections  :  Loop through and connect

                    A          B          C        D        E
                             A - B      B - C    C - D    D - E
                  A - E

                             B - D      C - A    D - A    E - B

                                                          E - C

                  Result :
                    A          B          C        D        E
                  A - E      B - A      C - B    D - C    E - D
                  A - B      B - C      C - D    D - E    E - A
                  A - C      B - D      C - A    D - B    E - B
                  A - D      B - E      C - E    D - A    E - C


                    A          B          C        D       E
                  A - B     B - A       C - D    D - C   E - A
                  A - C     B - C       C - A    D - A   E - B
                  A - D     B - D       C - B    D - B   E - C
                  A - E     B - E       C - E    D - E   E - D

         */



        /* Print */
        for(Map.Entry<Socket, List<String>> printlist : entryList) {

            List<String> stringList = new ArrayList<>();
            for(String string : printlist.getValue()) {
                stringList.add(string);
            }
            Socket socket = printlist.getKey();
            Event event = EventFactory.createMessagingNodeList(4, stringList);
            sendEventToIp(socket, event);

            System.out.println(printlist.getKey() + " : " + printlist.getValue().toString());
        }


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

    @Override
    public String getHostname() {
        return null;
    }

    @Override
    public int getPort() {
        return 0;
    }
}

