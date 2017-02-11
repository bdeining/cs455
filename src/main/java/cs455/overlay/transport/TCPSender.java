package cs455.overlay.transport;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.zip.CRC32;

public class TCPSender implements Runnable {

    private ConcurrentLinkedQueue<SocketMessage> concurrentLinkedQueue;

    private DataOutputStream dataOutputStream;

    public TCPSender() {
        concurrentLinkedQueue = new ConcurrentLinkedQueue<>();
    }

    public synchronized void sendData(byte[] dataToSend, Socket socket) {
        concurrentLinkedQueue.add(new SocketMessage(socket, dataToSend));
    }

    private long generateChecksum(byte[] dataToSend) {
        CRC32 crc32 = new CRC32();
        crc32.update(dataToSend);
        return crc32.getValue();
    }

    private class SocketMessage {
        private Socket socket;
        private byte[] bytes;
        public SocketMessage(Socket socket, byte[] bytes) {
            this.socket = socket;
            this.bytes = bytes;
        }

        public Socket getSocket() {
            return socket;
        }

        public byte[] getBytes() {
            return bytes;
        }
    }

    @Override
    public void run() {
        while(true) {
            if (!concurrentLinkedQueue.isEmpty()) {
                SocketMessage socketMessage = concurrentLinkedQueue.poll();
                Socket socket = socketMessage.getSocket();
                byte[] dataToSend = socketMessage.getBytes();
                try {
                    dataOutputStream = new DataOutputStream(socket.getOutputStream());

                    long checksum = generateChecksum(dataToSend);
                    dataOutputStream.writeLong(checksum);

                    int dataLength = dataToSend.length;
                    dataOutputStream.writeInt(dataLength);

                    dataOutputStream.write(dataToSend, 0, dataLength);
                    dataOutputStream.flush();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
