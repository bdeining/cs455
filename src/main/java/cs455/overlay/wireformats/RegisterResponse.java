package cs455.overlay.wireformats;

public class RegisterResponse {

    private static final int registerResponse = 2;

    private String additionalInfo;

    private byte statusCode;

    public RegisterResponse(byte statusCode, String additionalInfo) {
        this.statusCode = statusCode;
        this.additionalInfo = additionalInfo;
    }
}
