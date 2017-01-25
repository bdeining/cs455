package cs455.overlay.node;

public class MessagingNode {

    /*
    java cs455.overlay.node.MessagingNode registry-host registry-port
     */

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

    }

    public MessagingNode(String registryHost, int registryPort) {

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
