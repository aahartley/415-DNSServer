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
            byte[] buf = new byte[28];
            DatagramPacket packet = new DatagramPacket(buf, buf.length);
            s1.receive(packet);

            // display response
            String received = new String(packet.getData(), 0, packet.getLength());
            System.out.println("Data received from " + packet.getAddress() + ":  " + received);
            
          //  String[] temp = received.split(" ");
          //   String[] temp = new String[550];

            byte[] token = received.getBytes();
            byte[] msg = new byte[83];
            String[] hex = new String[28];
            for(int i=0; i<hex.length;i++) {
                System.out.print(token[i]+" ");
                hex[i]=String.format("%02X", token[i]);
            }
            System.out.println();
            for(int j=0; j<hex.length; j++) {
                System.out.print(hex[j]+" ");
            

            }
       
            String bytes="00 02 81 80 00 01 00 02 00 00 00 00 03 77 77 77 03 63 6e 6e 03 63 6f 6d 00 00 01 00 01 c0 0c 00 05 00 01 00 00 00 d0 00 1b 0a 74 75 72 6e 65 72 2d 74 6c 73 03 6d 61 70 06 66 61 73 74 6c 79 03 6e 65 74 00 c0 29 00 01 00 01 00 00 00 09 00 04 e8 21 43";
            String[] bytesA = bytes.split(" ");
            for(int m =2; m<bytesA.length;m++) {
            	msg[m]=hexToByte(bytesA[m]);
            }
            msg[0]=token[0];
            msg[1]= token[1];
          


            
                DatagramPacket packet2 = new DatagramPacket(msg, msg.length,packet.getAddress(),packet.getPort());
                
                

                
                s1.send(packet2);

                //s1.close();

            

            
        }
    	}catch(Exception e) {
    		System.out.println(e);
    	}


    }
    public static byte hexToByte(String hexString) {
        int firstDigit = toDigit(hexString.charAt(0));
        int secondDigit = toDigit(hexString.charAt(1));
        return (byte) ((firstDigit << 4) + secondDigit);
    }
    private static int toDigit(char hexChar) {
        int digit = Character.digit(hexChar, 16);
        if(digit == -1) {
            throw new IllegalArgumentException(
              "Invalid Hexadecimal Character: "+ hexChar);
        }
        return digit;
}
}



