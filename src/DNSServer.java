import java.io.*;
import java.net.*;
import java.util.Scanner;

public class DNSServer {
    public static void main(String[] argv) throws IOException {


    	try {
    		
    	// get a datagram socket

        DatagramSocket s1 = new DatagramSocket(53);  //bind to port 53
        
        


        while (true) {

            // receive a packet 
            byte[] buf = new byte[150];
            DatagramPacket packet = new DatagramPacket(buf, buf.length);
            s1.receive(packet);

            // display request
            System.out.println();
            String received = new String(packet.getData(), 0, packet.getLength());
            System.out.println("Data received from " + packet.getAddress() + ":  " + received);
            
            //convert string of the request to bytes
            byte[] token = received.getBytes();
            // create byte array to send back as response
            byte[] msg = new byte[132];
            //convert request bytes into hexadeciaml
            String[] hex = new String[150];
            for(int i=0; i<token.length;i++) {
                System.out.print(token[i]+" ");
                hex[i]=String.format("%02X", token[i]);
            }
            System.out.println();
            //show the hexadeciaml of the request, which will be used to forge the response
            for(int j=0; j<hex.length; j++) {
                System.out.print(hex[j]+" ");
            

            }
            //all the bytes of a type A google.com response
         String bytes="00 02 81 80 00 01 00 05 00 00 00 00 03 77 77 77 03 63 6e 6e 03 63 6f 6d 00 00 01 00 01 c0 0c 00 05 00 01 00 00 00 80 00 1b 0a 74 75 72 6e 65 72 2d 74 6c 73 03 6d 61 70 06 66 61 73 74 6c 79 03 6e 65 74 00 c0 29 00 01 00 01 00 00 00 16 00 04 97 65 01 43 c0 29 00 01 00 01 00 00 00 16 00 04 97 65 41 43 c0 29 00 01 00 01 00 00 00 16 00 04 97 65 81 43 c0 29 00 01 00 01 00 00 00 16 00 04 97 65 c1 43";        
            //split the bytes into an array by space
            String[] bytesA = bytes.split(" ");
            //first 12 bytes are used for the DNS response query message part of the packet
            for(int m =2; m<12;m++) {
            	msg[m]=hexToByte(bytesA[m]);
            }
            System.out.println();
            int length =0;
            //Loop to figure out how many bytes make up the name in the request, if the domain name ends in com, the name is done
            for(int l=13; l<token.length; l++) {
            	if(token[l]==99&&token[l+1]==111&&token[l+2]==109) {
            		length=l+3;
            		System.out.println("NAME LEGNTH IS "+((l+3)-13)+" l= "+length);
            		break;
            	}
            }
            //copy certain parts of the request to match for the response ("id")
            msg[0]=token[0];
            msg[1]= token[1];
            msg[12]=token[12];
            //put name of the request into the response at the correct spot
            for(int h=13;h<=length;h++) {
            	msg[h]=token[h];
            }
            //extra request info that needs to be in response
            msg[length+1]=token[length+1];
            msg[length+2]=token[length+2];
            msg[length+3]=token[length+3];
            msg[length+4]=token[length+4];
            // String of hexadeicaml which is the IP address for google, at the correct spot ( which is based upon name length)
            String nby ="c0 0c 00 01 00 01 00 00 00 d1 00 04 8e fa 09 68";
            String[] nbytesA = nby.split(" ");
            for(int g=0; g<nbytesA.length;g++) {
            	msg[g+(length+5)]=hexToByte(nbytesA[g]);
            }
          //code for testing purposes to see how long response is
            System.out.println();
            int count =0;
            for(int d=0;d<msg.length;d++) {
            	count++;
            	System.out.print(msg[d]+" ");
            }
          
System.out.println("COUNT: "+ count);

            // construct the response into a packet to send back to client
                DatagramPacket packet2 = new DatagramPacket(msg, msg.length,packet.getAddress(),packet.getPort());
                
                

                //send packet
                s1.send(packet2);
System.out.println();
            //dont need to close
                //s1.close();

            

            
        }
    	}catch(Exception e) {
    		System.out.println(e);
    	}


    }
    //methods from https://www.baeldung.com/java-byte-arrays-hex-strings to convert hex to byte
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



