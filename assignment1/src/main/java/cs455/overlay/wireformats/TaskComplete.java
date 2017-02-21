package cs455.overlay.wireformats;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class TaskComplete implements Event {

    private static final int type = 10;

    private int port;

    private String ip;

    public TaskComplete(String ip, int port) {
        this.ip = ip;
        this.port = port;
    }

    public TaskComplete(byte[] bytes) throws IOException {
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);
        DataInputStream dataInputStream = new DataInputStream(byteArrayInputStream);
        // ignore type
        int type = dataInputStream.readInt();
        int elementLength = dataInputStream.readInt();
        byte[] ipAddressBytes = new byte[elementLength];
        dataInputStream.read(ipAddressBytes, 0, elementLength);
        ip = new String(ipAddressBytes);
        port = dataInputStream.readInt();
        dataInputStream.close();
        byteArrayInputStream.close();

    }

    @Override
    public byte[] getBytes() throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        DataOutputStream dataOutputStream = new DataOutputStream(byteArrayOutputStream);
        dataOutputStream.writeInt(type);

        byte[] element = ip.getBytes();

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
        return 10;
    }

    public String getIp() {
        return ip;
    }

    public int getPort() {
        return port;
    }
}
