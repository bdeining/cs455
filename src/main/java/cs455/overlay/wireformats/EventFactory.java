package cs455.overlay.wireformats;

import java.io.IOException;

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

    public static Event createDeregisterRequest(String address, int port) {
        return new Deregister(address, port);
    }


    public static Event createEvent(int type, byte[] bytes) {

        Event event = null;
        try {

            switch (type) {

            case 1:
                event = new RegisterRequest(bytes);
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
