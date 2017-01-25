package cs455.overlay.wireformats;

public class RegisterRequest {

    private static final int registerRequest = 1;

    private String ipAddress;

    private int port;

    public RegisterRequest(String ipAddress, int port) {
        this.ipAddress = ipAddress;
        this.port = port;
    }
}
