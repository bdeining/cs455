package cs455.overlay.transport;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.SocketException;

import cs455.overlay.wireformats.RegisterRequest;

public class TCPReceiverThread implements Runnable {

    private Socket socket;

    private DataInputStream dataInputStream;

    public TCPReceiverThread(Socket socket) throws IOException {
        this.socket = socket;
        dataInputStream = new DataInputStream(socket.getInputStream());
    }

    @Override
    public void run() {
        int dataLength;
        while(socket != null) {
            try {

                dataLength = dataInputStream.readInt();
                byte[] data = new byte[dataLength];
                dataInputStream.readFully(data);

                //RegisterRequest registerRequest = new RegisterRequest(data);
            } catch (SocketException e) {
                e.printStackTrace();
                break;
            } catch (IOException e) {
                e.printStackTrace();
                break;
            }
        }
    }

}
