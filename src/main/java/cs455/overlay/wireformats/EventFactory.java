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
            default:
                event = null;
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return event;
    }

}
