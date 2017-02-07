package cs455.overlay.wireformats;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class LinkWeights implements Event {
    private static final int type = 8;

    private int numberOfLinks;

    private List<String> links;

    public LinkWeights(int numberOfLinks, List<String> links) {
        this.numberOfLinks = numberOfLinks;
        this.links = links;
    }

    public LinkWeights(byte[] bytes) throws IOException {
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);
        DataInputStream dataInputStream = new DataInputStream(byteArrayInputStream);

        /* skip type */
        int type = dataInputStream.readInt();
        numberOfLinks = dataInputStream.readInt();

        int elementLength = dataInputStream.readInt();
        byte[] element = new byte[elementLength];
        dataInputStream.read(element, 0, elementLength);
        String messagingNodeString = new String(element);
        String[] messagingList = messagingNodeString.split("\n");
        links = new ArrayList<>(Arrays.asList(messagingList));

        dataInputStream.close();
        byteArrayInputStream.close();
    }

    @Override
    public byte[] getBytes() throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        DataOutputStream dataOutputStream = new DataOutputStream(byteArrayOutputStream);
        dataOutputStream.writeInt(type);
        dataOutputStream.writeInt(numberOfLinks);

        String messagingNodesString = String.join("\n", links);
        byte[] element = messagingNodesString.getBytes();

        dataOutputStream.writeInt(element.length);
        dataOutputStream.write(element);

        dataOutputStream.flush();
        dataOutputStream.close();
        byteArrayOutputStream.close();
        return byteArrayOutputStream.toByteArray();
    }

    public List<String> getLinks() {
        return links;
    }

    @Override
    public int getType() {
        return type;
    }
}
