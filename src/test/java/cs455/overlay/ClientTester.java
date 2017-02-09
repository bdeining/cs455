package cs455.overlay;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

import org.junit.Test;

import cs455.overlay.transport.TCPSender;
import cs455.overlay.wireformats.RegisterRequest;

public class ClientTester {

    @Test
    public void testClient() throws Exception {
        try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
             DataOutputStream dataOutputStream = new DataOutputStream(byteArrayOutputStream)){
            Socket socket = new Socket("localhost", 50000);
            TCPSender tcpSender = new TCPSender(socket);

            dataOutputStream.writeInt(1);
            byte[] ip = "127.0.0.1".getBytes();
            dataOutputStream.writeInt(ip.length);
            dataOutputStream.write(ip);
            dataOutputStream.writeInt(50000);
            dataOutputStream.flush();

            byte[] data = byteArrayOutputStream.toByteArray();
            dataOutputStream.close();
            byteArrayOutputStream.close();

            byte[] wrappedData = new RegisterRequest(data).getBytes();


            tcpSender.sendData(wrappedData);
            tcpSender.closeSocket();
        } catch (UnknownHostException e) {
            System.out.println("Unknown host: kq6py");
            System.exit(1);
        } catch  (IOException e) {
            System.out.println("No I/O");
            System.exit(1);
        }

    }
}
