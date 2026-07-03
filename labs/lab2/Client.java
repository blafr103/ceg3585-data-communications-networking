import java.net.*;
import java.io.*;
public class Client {
    public static void main(String[] args){
        try{
            String address="localhost";
            int port=4444;
            Socket cSocket=new Socket(address,port);
            DataOutputStream data=new DataOutputStream(cSocket.getOutputStream());
            DataInputStream datain=new DataInputStream(cSocket.getInputStream());
            FourierSeries f= new FourierSeries();
            data.writeUTF(f.function1());
            data.writeUTF(f.function2());
            data.writeUTF(f.function3());
            data.writeUTF(f.function4());
            data.writeUTF(f.function5());
            data.writeUTF(f.function6());
            data.flush();
            
            //datatype: double

            while(true){
                System.out.println(datain.readUTF());
            }
            
            
        }
        catch(Exception e){
            System.out.println(e);
        }
    }
}
