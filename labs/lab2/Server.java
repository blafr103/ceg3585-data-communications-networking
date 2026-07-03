import java.net.*;
import java.io.*;

public class Server {
    public static void main(String[] args) {
        int port = 4444;
        int functionCount = 6;   // fixed set of six waveforms

        try (ServerSocket socket = new ServerSocket(port);
             Socket sSocket = socket.accept();
             DataInputStream data = new DataInputStream(sSocket.getInputStream())) {

            // this machine is the remote display: receive each curve and print it
            for (int i = 0; i < functionCount; i++) {
                System.out.println(data.readUTF());
            }

        } catch (IOException e) {
            System.out.println(e);
        }
    }
}
