package cs455.overlay.transport;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.zip.CRC32;

public class TCPSender {

    private Socket socket;

    private DataOutputStream dataOutputStream;

    public TCPSender(Socket socket) throws IOException {
        this.socket = socket;
        dataOutputStream = new DataOutputStream(socket.getOutputStream());
    }

    public void sendData(byte[] dataToSend) throws IOException {
        long checksum = generateChecksum(dataToSend);
        dataOutputStream.writeLong(checksum);

        int dataLength = dataToSend.length;
        dataOutputStream.writeInt(dataLength);

        dataOutputStream.write(dataToSend, 0, dataLength);
        dataOutputStream.flush();
    }

    public void closeSocket() throws IOException {
        dataOutputStream.close();
        socket.close();
        socket = null;
    }

    public long generateChecksum(byte[] dataToSend) {
        CRC32 crc32 = new CRC32();
        crc32.update(dataToSend);
        return crc32.getValue();
    }
}
