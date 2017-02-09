package cs455.overlay.wireformats;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class TrafficSummary implements Event {
    private static final int type = 12;

    private int port;

    private String ip;

    private int messagesSent;

    private int messagesReceived;

    private int messagesRelayed;

    private long messagesSentSummation;

    private long messagesReceivedSummation;

    public TrafficSummary(String ip, int port, int messagesSent, int messagesReceived,
            int messagesRelayed, long messagesSentSummation, long messagesReceivedSummation) {
        this.ip = ip;
        this.port = port;
        this.messagesSent = messagesSent;
        this.messagesReceived = messagesReceived;
        this.messagesRelayed = messagesRelayed;
        this.messagesSentSummation = messagesSentSummation;
        this.messagesReceivedSummation = messagesReceivedSummation;
    }

    public TrafficSummary(byte[] bytes) throws IOException {
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);
        DataInputStream dataInputStream = new DataInputStream(byteArrayInputStream);

        int type = dataInputStream.readInt();

        int elementLength = dataInputStream.readInt();
        byte[] ipAddressBytes = new byte[elementLength];
        dataInputStream.read(ipAddressBytes, 0, elementLength);
        ip = new String(ipAddressBytes);
        port = dataInputStream.readInt();

        messagesSent = dataInputStream.readInt();
        messagesReceived = dataInputStream.readInt();
        messagesRelayed = dataInputStream.readInt();
        messagesReceivedSummation = dataInputStream.readLong();
        messagesSentSummation = dataInputStream.readLong();

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
        dataOutputStream.writeInt(messagesSent);
        dataOutputStream.writeInt(messagesReceived);
        dataOutputStream.writeInt(messagesRelayed);
        dataOutputStream.writeLong(messagesReceivedSummation);
        dataOutputStream.writeLong(messagesSentSummation);

        dataOutputStream.flush();
        dataOutputStream.close();
        byteArrayOutputStream.close();
        return byteArrayOutputStream.toByteArray();
    }

    @Override
    public int getType() {
        return type;
    }

    public int getMessagesSent() {
        return messagesSent;
    }

    public int getMessagesReceived() {
        return messagesReceived;
    }

    public int getMessagesRelayed() {
        return messagesRelayed;
    }

    public long getMessagesSentSummation() {
        return messagesSentSummation;
    }

    public long getMessagesReceivedSummation() {
        return messagesReceivedSummation;
    }

    public int getPort() {
        return port;
    }

    public String getIp() {
        return ip;
    }
}
