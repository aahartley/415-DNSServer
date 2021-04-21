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
            byte[] buf = new byte[150];
            DatagramPacket packet = new DatagramPacket(buf, buf.length);
            s1.receive(packet);

            // display response
            System.out.println();
            String received = new String(packet.getData(), 0, packet.getLength());
            System.out.println("Data received from " + packet.getAddress() + ":  " + received);
            
          //  String[] temp = received.split(" ");
          //   String[] temp = new String[550];

            byte[] token = received.getBytes();
            byte[] msg = new byte[132];
            String[] hex = new String[150];
            for(int i=0; i<token.length;i++) {
                System.out.print(token[i]+" ");
                hex[i]=String.format("%02X", token[i]);
            }
            System.out.println();
            for(int j=0; j<hex.length; j++) {
                System.out.print(hex[j]+" ");
            

            }
            
         String bytes="00 02 81 80 00 01 00 05 00 00 00 00 03 77 77 77 03 63 6e 6e 03 63 6f 6d 00 00 01 00 01 c0 0c 00 05 00 01 00 00 00 80 00 1b 0a 74 75 72 6e 65 72 2d 74 6c 73 03 6d 61 70 06 66 61 73 74 6c 79 03 6e 65 74 00 c0 29 00 01 00 01 00 00 00 16 00 04 97 65 01 43 c0 29 00 01 00 01 00 00 00 16 00 04 97 65 41 43 c0 29 00 01 00 01 00 00 00 16 00 04 97 65 81 43 c0 29 00 01 00 01 00 00 00 16 00 04 97 65 c1 43";        
       
           // String bytes="00 02 81 80 00 01 00 02 00 00 00 00 03 77 77 77 03 63 6e 6e 03 63 6f 6d 00 00 01 00 01 c0 0c 00 05 00 01 00 00 00 d0 00 1b 0a 74 75 72 6e 65 72 2d 74 6c 73 03 6d 61 70 06 66 61 73 74 6c 79 03 6e 65 74 00 c0 29 00 01 00 01 00 00 00 09 00 04 e8 21 43";
            String[] bytesA = bytes.split(" ");
            for(int m =2; m<12;m++) {
            	msg[m]=hexToByte(bytesA[m]);
            }
            System.out.println();
            int length =0;
            for(int l=13; l<token.length; l++) {
            	if(token[l]==99&&token[l+1]==111&&token[l+2]==109) {
            		length=l+3;
            		System.out.println("NAME LEGNTH IS "+((l+3)-13)+" l= "+length);
            		break;
            	}
            }
            msg[0]=token[0];
            msg[1]= token[1];
            msg[12]=token[12];
            for(int h=13;h<=length;h++) {
            	msg[h]=token[h];
            }
            msg[length+1]=token[length+1];
            msg[length+2]=token[length+2];
            msg[length+3]=token[length+3];
            msg[length+4]=token[length+4];

            String nby ="c0 0c 00 01 00 01 00 00 00 d1 00 04 8e fa 09 68";
            String[] nbytesA = nby.split(" ");
            for(int g=0; g<nbytesA.length;g++) {
            	msg[g+(length+5)]=hexToByte(nbytesA[g]);
            }
          
            System.out.println();
            int count =0;
            for(int d=0;d<msg.length;d++) {
            	count++;
            	System.out.print(msg[d]+" ");
            }
          
System.out.println("COUNT: "+ count);

            
                DatagramPacket packet2 = new DatagramPacket(msg, msg.length,packet.getAddress(),packet.getPort());
                
                

                
                s1.send(packet2);
System.out.println();
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



