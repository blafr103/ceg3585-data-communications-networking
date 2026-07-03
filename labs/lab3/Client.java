import java.util.*;
import java.net.*;
import java.io.*;


public class Client{
   

   public static void main(String args[]){
      Scanner s =new Scanner(System.in);
      System.out.println("enter code");
      Socket cSocket=new Socket();
      //create client server relation
      try{
         String address="localhost";
         int port=4444;
         cSocket=new Socket(address,port);
         DataOutputStream data=new DataOutputStream(cSocket.getOutputStream());
         DataInputStream datain=new DataInputStream(cSocket.getInputStream());
         String t="";
         String tmp=s.nextLine();
               t=t+tmp;

            
      
      //Le programme d'encodage doit envoyer un message « demande d’envoi » au programme de
      //décodage et attendre la réponse « prêt à recevoir » du programme de décodage, avant de l'envoyer
      data.writeUTF(encodageB8ZS(t));
      data.flush();
      
      //wait for response
      while(true){
         System.out.println(datain.readUTF());
         
      }
      }
      catch(Exception e){
         System.out.println(e);
      }

      //close streams and sockets
      
      s.close();
   }      
   
   
   public static String encodageB8ZS(String chaine){
      //new empty string to hold encoded message
      StringBuilder encoded = new StringBuilder();
	//flag to let us know when we get to 8 consecutive 0s
      boolean zero8 = false; 
	   //variables to  keep track on consecutive zeros and ones, and placeholder in B8ZS sequence
      int zeros = 0;
      int spot = 0;
      int ones = 0;
	   //B8ZS sequences
      StringBuilder OOOBpos = new StringBuilder("000+-0-+000+-0-+000+-");
      StringBuilder OOOBneg = new StringBuilder("000-+0+-000-+0+-000-+");
	   //keep track of polarity, initiated to positive
      StringBuilder polarity = new StringBuilder("+");
      
      encoded.append('+');
      
      
      for (int i = 0 ; i<chaine.length() ; i++){
         
         if (chaine.charAt(i) == '0'){
            zeros++;
            if(zeros>=8){zero8=true;}
            if (i==chaine.length()-1 && zero8){
	               for (int z = 0; z<zeros; z++){
                     if (polarity.charAt(0)=='+'){
	                     encoded.append(OOOBpos.charAt(z+spot));
	                  }else{
	                     encoded.append(OOOBneg.charAt(z+spot));
	                  }
	               }
	               zeros=0;
	            }
         }else if (i!=0){
            //when 0s finish, check to see if at least 8 in sequence
            if (zero8){
               for (int j = 0; j<zeros; j++){ //if so, append the appropriate (polariy) sequence until the amount of 0s is reached
                  if (polarity.charAt(0)=='+'){
                     encoded.append(OOOBpos.charAt(j+spot));
                  }else{
                     encoded.append(OOOBneg.charAt(j+spot));
                  }
               }
               if (zeros!=0){spot=zeros-1;}
		//swap the polarity when a 1 comes up
               if (OOOBpos.charAt(spot)=='+'){
                  polarity.replace(0, 1, "-");   
               }else{
                  polarity.replace(0, 1, "+");
               }
            }else{
               for (int x = 0; x< zeros; x++){
                  encoded.append("0");
               }              
            } 
		 //swap the polarity when a 1 comes up
            if (polarity.charAt(0)=='+'){
               encoded.append("-");
               polarity.replace(0, 1, "-");
            }else{
               encoded.append("+");
               polarity.replace(0, 1, "+");
            }
            zeros = 0;
            ones++;
            zero8 = false;
         }         
      }
      if (zeros!=0){
         for (int y = 0; y< zeros; y++){
            encoded.append("0");
         }   
      }
      return encoded.toString();
   }
}

