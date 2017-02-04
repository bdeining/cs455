package cs455.overlay.wireformats;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class Message implements Event {
    private static final int type = 6;

    private int payload;

    private String source;

    private String destination;

    public Message(int payload, String source, String destination) {
        this.payload = payload;
        this.source = source;
        this.destination = destination;
    }

    public Message(byte[] bytes) throws IOException {
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);
        DataInputStream dataInputStream = new DataInputStream(byteArrayInputStream);

        /* skip type */
        int type = dataInputStream.readInt();
        payload = dataInputStream.readInt();

        int elementLength = dataInputStream.readInt();
        byte[] element = new byte[elementLength];
        dataInputStream.read(element, 0, elementLength);
        source = new String(element);

        elementLength = dataInputStream.readInt();
        element = new byte[elementLength];
        dataInputStream.read(element, 0, elementLength);
        destination = new String(element);

        dataInputStream.close();
        byteArrayInputStream.close();
    }

    @Override
    public byte[] getBytes() throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        DataOutputStream dataOutputStream = new DataOutputStream(byteArrayOutputStream);
        dataOutputStream.writeInt(type);
        dataOutputStream.writeInt(payload);

        byte[] element = source.getBytes();
        dataOutputStream.writeInt(element.length);
        dataOutputStream.write(element);

        element = destination.getBytes();
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

    public void printDetails() {
        System.out.println(payload);
        System.out.println(source);
        System.out.println(destination);
    }

    public String getSource() {
        return source;
    }

    public int getPayload() {
        return payload;
    }
}
