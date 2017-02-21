package cs455.overlay.wireformats;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class PullTrafficSummary implements Event {

    private static final int type = 11;

    public PullTrafficSummary() {

    }

    public PullTrafficSummary(byte[] bytes) throws IOException {
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);
        DataInputStream dataInputStream = new DataInputStream(byteArrayInputStream);

        // ignore type
        int type = dataInputStream.readInt();
        dataInputStream.close();
        byteArrayInputStream.close();

    }

    @Override
    public byte[] getBytes() throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        DataOutputStream dataOutputStream = new DataOutputStream(byteArrayOutputStream);
        dataOutputStream.writeInt(type);

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
