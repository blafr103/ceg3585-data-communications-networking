import java.net.*;
import java.io.*;

public class Client {
    public static void main(String[] args) {
        String address = "localhost";
        int port = 4444;

        // try-with-resources closes the socket and stream on exit (the old code never did)
        try (Socket cSocket = new Socket(address, port);
             DataOutputStream data = new DataOutputStream(cSocket.getOutputStream())) {

            FourierSeries f = new FourierSeries();

            // send each function's curve as one UTF message; the server displays them
            data.writeUTF(f.function1());
            data.writeUTF(f.function2());
            data.writeUTF(f.function3());
            data.writeUTF(f.function4());
            data.writeUTF(f.function5());
            data.writeUTF(f.function6());
            data.flush();

        } catch (IOException e) {
            System.out.println(e);
        }
    }
}
