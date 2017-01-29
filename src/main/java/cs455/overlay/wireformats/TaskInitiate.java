package cs455.overlay.wireformats;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class TaskInitiate implements Event {
    private static final int type = 7;

    private int numberOfRounds;

    public TaskInitiate(int numberOfRounds) {
        this.numberOfRounds = numberOfRounds;
    }

    public TaskInitiate(byte[] bytes) throws IOException {
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);
        DataInputStream dataInputStream = new DataInputStream(byteArrayInputStream);

        // ignore type
        int type = dataInputStream.readInt();

        numberOfRounds = dataInputStream.readInt();

        printDetails();
        dataInputStream.close();
        byteArrayInputStream.close();
    }

    @Override
    public byte[] getBytes() throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        DataOutputStream dataOutputStream = new DataOutputStream(byteArrayOutputStream);
        dataOutputStream.writeInt(type);
        dataOutputStream.writeInt(numberOfRounds);

        dataOutputStream.flush();
        dataOutputStream.close();
        byteArrayOutputStream.close();
        return byteArrayOutputStream.toByteArray();
    }

    @Override
    public int getType() {
        return 0;
    }

    public int getNumberOfRounds() {
        return numberOfRounds;
    }

    public void printDetails() {
        System.out.println(numberOfRounds);
    }
}
