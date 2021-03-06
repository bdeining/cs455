package cs455.overlay.wireformats;

import java.io.IOException;
import java.util.List;

public class EventFactory {

    private static EventFactory singleton = new EventFactory();

    private EventFactory() {

    }

    public static EventFactory getInstance() {
        return singleton;
    }

    public static Event createRegisterRequest(String address, int port) {
        return new RegisterRequest(address, port);
    }

    public static Event createRegisterRespone(byte statusCode, String additionalInformation) {
        return new RegisterResponse(statusCode, additionalInformation);
    }

    public static Event createDeregisterResponse(byte statusCode, String additionalInformation) {
        return new DeregisterResponse(statusCode, additionalInformation);
    }

    public static Event createDeregisterRequest(String address, int port) {
        return new DeregisterRequest(address, port);
    }

    public static Event createMessagingNodeList(int numberOfPeers, List<String> messagingList) {
        return new MessagingNodesList(numberOfPeers, messagingList);
    }

    public static Event createMessage(int payload, String source, String destination) {
        return new Message(payload, source, destination);
    }

    public static Event createTaskInitiate(int numberOfRounds) {
        return new TaskInitiate(numberOfRounds);
    }

    public static Event createLinkWeights(int numberOfConnections, List<String> links) {
        return new LinkWeights(numberOfConnections, links);
    }

    public static Event createIdentification(String identity) {
        return new Identity(identity);
    }

    public static Event createTaskComplete(String ip, int port) {
        return new TaskComplete(ip, port);
    }

    public static Event createTrafficSummary(String ip, int port, int messagesSent,
            int messagesReceive, int messagesRelayed, long messagesSentSummation,
            long messagesReceivedSummation) {
        return new TrafficSummary(ip,
                port,
                messagesSent,
                messagesReceive,
                messagesRelayed,
                messagesSentSummation,
                messagesReceivedSummation);
    }

    public static Event createEvent(int type, byte[] bytes) {

        Event event = null;
        try {

            switch (type) {

            case 1:
                event = new RegisterRequest(bytes);
                break;
            case 2:
                event = new DeregisterRequest(bytes);
                break;
            case 3:
                event = new RegisterResponse(bytes);
                break;
            case 4:
                event = new DeregisterResponse(bytes);
                break;
            case 5:
                event = new MessagingNodesList(bytes);
                break;
            case 6:
                event = new Message(bytes);
                break;
            case 7:
                event = new TaskInitiate(bytes);
                break;
            case 8:
                event = new LinkWeights(bytes);
                break;
            case 9:
                event = new Identity(bytes);
                break;
            case 10:
                event = new TaskComplete(bytes);
                break;
            case 11:
                event = new PullTrafficSummary(bytes);
                break;
            case 12:
                event = new TrafficSummary(bytes);
                break;
            default:
                event = null;
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return event;
    }

}
