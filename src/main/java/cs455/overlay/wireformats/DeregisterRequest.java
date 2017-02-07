package cs455.overlay.wireformats;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class DeregisterRequest implements Event {
    private static final int type = 2;

    private String ipAddress;

    private int port;

    public DeregisterRequest(String ipAddress, int port) {
        this.ipAddress = ipAddress;
        this.port = port;
    }

    public DeregisterRequest(byte[] bytes) throws IOException {
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);
        DataInputStream dataInputStream = new DataInputStream(byteArrayInputStream);
        // ignore type
        int type = dataInputStream.readInt();
        int elementLength = dataInputStream.readInt();
        byte[] ipAddressBytes = new byte[elementLength];
        dataInputStream.read(ipAddressBytes, 0, elementLength);
        ipAddress = new String(ipAddressBytes);
        port = dataInputStream.readInt();
        dataInputStream.close();
        byteArrayInputStream.close();

    }

    @Override
    public byte[] getBytes() throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        DataOutputStream dataOutputStream = new DataOutputStream(byteArrayOutputStream);
        dataOutputStream.writeInt(type);

        byte[] element = ipAddress.getBytes();

        dataOutputStream.writeInt(element.length);
        dataOutputStream.write(element);
        dataOutputStream.writeInt(port);

        dataOutputStream.flush();
        dataOutputStream.close();
        byteArrayOutputStream.close();
        return byteArrayOutputStream.toByteArray();
    }

    @Override
    public int getType() {
        return type;
    }

    public int getPort() {
        return port;
    }

    public String getIpAddress() {
        return ipAddress;
    }
}
