import java.io.*;
import java.net.*;
import java.util.Scanner;

public class DNSServer {
    public static void main(String[] argv) throws IOException {


        // get a datagram socket
    	try {
    		
    	
        DatagramSocket s1 = new DatagramSocket(53);  //bind to port 53
        
        


        while (true) {

            // receive a packet 
            byte[] buf = new byte[550];
            DatagramPacket packet = new DatagramPacket(buf, buf.length);
            s1.receive(packet);

            // display response
            String received = new String(packet.getData(), 0, packet.getLength());
            System.out.println("Data received from " + packet.getAddress() + ":  " + received);
            
          //  String[] temp = received.split(" ");
          //   String[] temp = new String[550];

            byte[] token = received.getBytes();
            byte[] msg = new byte[28];
            String[] hex = new String[550];
            for(int i=0; i<token.length;i++) {
                System.out.print(token[i]+" ");
                hex[i]=String.format("%02X", token[i]);
            }
            System.out.println();
            for(int j=0; j<hex.length; j++) {
                System.out.print(hex[j]+" ");
            

            }
            for(int n=0; n<msg.length;n++) {
            	msg[n]=token[n];
            }

                InetAddress address = InetAddress.getByName("10.110.4.227");  // get the ip address of the host.

            
                DatagramPacket packet2 = new DatagramPacket(msg, msg.length,packet.getAddress(),packet.getPort());
                
                

                
                s1.send(packet2);

                //s1.close();

            

            
        }
    	}catch(Exception e) {
    		System.out.println(e);
    	}


    }
}



