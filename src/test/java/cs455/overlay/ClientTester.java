package cs455.overlay;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

import org.junit.Before;
import org.junit.Test;

import cs455.overlay.node.Registry;
import cs455.overlay.transport.TCPSender;

/**
 * Created by bdeininger on 1/24/17.
 */
public class ClientTester {



    @Before
    public void setUp() {
        //Registry registry = new Registry(50000);
    }

    @Test
    public void testClient() throws Exception {
        try{
            Socket socket = new Socket("localhost", 50000);
            TCPSender tcpSender = new TCPSender(socket);
            tcpSender.sendData("butt".getBytes());
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
