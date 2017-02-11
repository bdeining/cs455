package cs455.overlay.transport;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.SocketException;
import java.util.zip.CRC32;

import cs455.overlay.node.MessagingNode;
import cs455.overlay.node.Node;
import cs455.overlay.node.Registry;
import cs455.overlay.wireformats.DeregisterRequest;
import cs455.overlay.wireformats.Event;
import cs455.overlay.wireformats.EventFactory;
import cs455.overlay.wireformats.Identity;
import cs455.overlay.wireformats.LinkWeights;
import cs455.overlay.wireformats.Message;
import cs455.overlay.wireformats.MessagingNodesList;
import cs455.overlay.wireformats.RegisterRequest;
import cs455.overlay.wireformats.TaskComplete;
import cs455.overlay.wireformats.TaskInitiate;
import cs455.overlay.wireformats.TrafficSummary;

public class TCPReceiverThread implements Runnable {

    private Socket socket;

    private DataInputStream dataInputStream;

    private Node node;

    public TCPReceiverThread(Node node, Socket socket) throws IOException {
        this.node = node;
        this.socket = socket;
        dataInputStream = new DataInputStream(socket.getInputStream());
    }

    @Override
    public void run() {

        while (socket != null) {
            try {
                long receivedChecksum = dataInputStream.readLong();

                int dataLength = dataInputStream.readInt();

                byte[] data = new byte[dataLength];
                dataInputStream.readFully(data);

                long checksum = generateChecksum(data);
                if (checksum != receivedChecksum) {
                    System.out.println("Corrupted message found.  Skipping.");
                    continue;
                }

                int type = readIntFromByteArray(data);

                Event event = EventFactory.createEvent(type, data);

                switch (type) {
                case 1:
                    if (node instanceof Registry) {
                        RegisterRequest registerRequest = (RegisterRequest) event;
                        ((Registry) node).registerNode(socket,
                                registerRequest.getIpAddress(),
                                registerRequest.getPort());
                    }
                    break;
                case 2:
                    if (node instanceof Registry) {
                        DeregisterRequest deregisterRequest = (DeregisterRequest) event;
                        ((Registry) node).deRegisterNode(socket, deregisterRequest.getIpAddress(), deregisterRequest.getPort());
                    }
                    break;
                case 3:
                    // Register Response Print message
                    break;

                case 4:
                    if (node instanceof MessagingNode) {
                        ((MessagingNode) node).exit();
                    }
                    break;
                case 5:
                    if (node instanceof MessagingNode) {
                        MessagingNodesList messagingNodesList = (MessagingNodesList) event;
                        ((MessagingNode) node).setupMessagingNodeLinks(messagingNodesList.getMessagingNodes(),
                                messagingNodesList.getNumberOfPeers());
                    }
                    break;
                case 6:
                    if (node instanceof MessagingNode) {
                        String destination = ((Message)event).getDestination();
                        int payload = ((Message)event).getPayload();
                        ((MessagingNode) node).processMessage(event, destination, payload);
                    }
                    break;
                case 7:
                    if (node instanceof MessagingNode) {
                        TaskInitiate taskInitiate = (TaskInitiate) event;
                        ((MessagingNode) node).startRounds(taskInitiate.getNumberOfRounds());
                    }
                    break;
                case 8:
                    if (node instanceof MessagingNode) {
                        // link weights processing
                        ((MessagingNode) node).generateMapFromLinkWeights((LinkWeights) event);
                    }
                    break;
                case 9:
                    if (node instanceof MessagingNode) {
                        Identity identity = (Identity) event;
                        ((MessagingNode) node).addSocket(identity.getIdentification(), socket);
                    }
                    break;

                case 10:
                    if (node instanceof Registry) {
                        TaskComplete taskComplete = (TaskComplete) event;
                        ((Registry) node).taskComplete(taskComplete);
                    }
                    break;

                case 11:
                    if (node instanceof MessagingNode) {
                        ((MessagingNode)node).pullTrafficSummary();
                    }
                    break;

                case 12:
                    if (node instanceof Registry) {
                        TrafficSummary trafficSummary = (TrafficSummary)event;
                        ((Registry) node).trafficSummary(trafficSummary);
                    }
                }
            } catch (SocketException e) {
                e.printStackTrace();
                break;
            } catch (IOException e) {
                e.printStackTrace();
                break;
            }
        }
    }

    private int readIntFromByteArray(byte[] data) {
        int value =
                ((data[0] & 0xFF) << 24) | ((data[1] & 0xFF) << 16) | ((data[2] & 0xFF) << 8) | (
                        data[3] & 0xFF);
        return value;
    }

    public long generateChecksum(byte[] dataToSend) {
        CRC32 crc32 = new CRC32();
        crc32.update(dataToSend);
        return crc32.getValue();
    }

}
