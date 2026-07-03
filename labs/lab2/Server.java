import java.net.*;
import java.io.*;
public class Server {
    public static void main(String[] args){
        try{
            int port=4444;
            ServerSocket socket=new ServerSocket(port);
            Socket sSocket=socket.accept();
            DataInputStream data=new DataInputStream(sSocket.getInputStream());
            DataOutputStream dataout=new DataOutputStream(sSocket.getOutputStream());
            while(true){
                    String tmp= (String)data.readUTF();
                     dataout.writeUTF(tmp);
            }
        }
        catch(Exception e){
            System.out.println(e);
        }
    }
}
