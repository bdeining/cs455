package cs455.overlay.wireformats;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MessagingNodesList implements Event {
    private static final int type = 5;

    private int numberOfPeers;

    private List<String> messagingNodes;

    public MessagingNodesList(int numberOfPeers, List<String> messagingNodes) {
        this.numberOfPeers = numberOfPeers;
        this.messagingNodes = messagingNodes;
    }

    public MessagingNodesList(byte[] bytes) throws IOException {
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);
        DataInputStream dataInputStream = new DataInputStream(byteArrayInputStream);

        /* skip type */
        int type = dataInputStream.readInt();
        numberOfPeers = dataInputStream.readInt();

        int elementLength = dataInputStream.readInt();
        byte[] messagingNodeList = new byte[elementLength];
        dataInputStream.read(messagingNodeList, 0, elementLength);

        String messagingNodeString = new String(messagingNodeList);
        String[] messagingList = messagingNodeString.split("\n");
        messagingNodes = new ArrayList<>(Arrays.asList(messagingList));

        dataInputStream.close();
        byteArrayInputStream.close();
    }

    @Override
    public byte[] getBytes() throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        DataOutputStream dataOutputStream = new DataOutputStream(byteArrayOutputStream);
        dataOutputStream.writeInt(type);
        dataOutputStream.writeInt(numberOfPeers);

        String messagingNodesString = String.join("\n", messagingNodes);
        byte[] element = messagingNodesString.getBytes();

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

    private void printDetails() {
        System.out.println(type);
        System.out.println(numberOfPeers);
        System.out.println(messagingNodes);
    }

    public List<String> getMessagingNodes() {
        return messagingNodes;
    }

    public int getNumberOfPeers() {
        return numberOfPeers;
    }
}
