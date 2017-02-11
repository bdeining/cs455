package cs455.overlay.transport;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.zip.CRC32;

public class TCPSender {

    private DataOutputStream dataOutputStream;

    public TCPSender() {

    }

    public synchronized void sendData(byte[] dataToSend, Socket socket) throws IOException {
        dataOutputStream = new DataOutputStream(socket.getOutputStream());

        long checksum = generateChecksum(dataToSend);
        dataOutputStream.writeLong(checksum);

        int dataLength = dataToSend.length;
        dataOutputStream.writeInt(dataLength);

        dataOutputStream.write(dataToSend, 0, dataLength);
        dataOutputStream.flush();
    }

    private long generateChecksum(byte[] dataToSend) {
        CRC32 crc32 = new CRC32();
        crc32.update(dataToSend);
        return crc32.getValue();
    }
}
