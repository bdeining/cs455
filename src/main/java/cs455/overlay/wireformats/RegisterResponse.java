package cs455.overlay.wireformats;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class RegisterResponse implements Event {

    private static final int type = 3;

    private String additionalInfo;

    private byte statusCode;

    public RegisterResponse(byte statusCode, String additionalInfo) {
        this.statusCode = statusCode;
        this.additionalInfo = additionalInfo;
    }

    public RegisterResponse(byte[] bytes) throws IOException {
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);
        DataInputStream dataInputStream = new DataInputStream(byteArrayInputStream);

        // ignore type
        int type = dataInputStream.readInt();

        statusCode = dataInputStream.readByte();

        int elementLength = dataInputStream.readInt();
        byte[] additionalInformationBytes = new byte[elementLength];
        dataInputStream.read(additionalInformationBytes, 0, elementLength);
        additionalInfo = new String(additionalInformationBytes);

        dataInputStream.close();
        byteArrayInputStream.close();
    }

    @Override
    public byte[] getBytes() throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        DataOutputStream dataOutputStream = new DataOutputStream(byteArrayOutputStream);
        dataOutputStream.writeInt(type);
        dataOutputStream.writeByte(statusCode);

        byte[] element = additionalInfo.getBytes();
        dataOutputStream.writeInt(element.length);
        dataOutputStream.write(element);

        dataOutputStream.flush();
        dataOutputStream.close();
        byteArrayOutputStream.close();
        return byteArrayOutputStream.toByteArray();
    }

    @Override
    public int getType() {
        return type;
    }
}
