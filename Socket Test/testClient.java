// A Java program for a Client
import java.io.*;
import java.net.*;
 
public class testClient {
    // initialize socket and input output streams
    private Socket socket = null;
    private InputStream input = null;
    private OutputStream out = null;
 
    private String getMesajLine(byte[] buf) {
        String messageLine = null;
        try {
            messageLine = new String(buf, 0, buf.length, "windows-1254");

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return messageLine;
    }

    // constructor to put ip address and port
    public testClient(String address, int port)
    {
        
        int aa = 0;

        try {
            socket = new Socket(address, port);
            System.out.println("Connected");
            input = socket.getInputStream();

            
 
            // sends output to the socket
            out = socket.getOutputStream();
            byte[] pingu = hexStringToByteArray("fffa2c3216fff0");
            out.write(pingu); 
            out.flush();
            aa = 1;
        }


        catch (Exception u) {
            System.out.println(u);
            return;
        }
    
            if (aa == 1)
                return;
        

        // string to read message from input
        String line = "";
 
        // keep reading until "Over" is input
        while (true) {
            System.out.println("while-1");
            try {
                byte[] bufferReader = new byte[2000];
                int bytesRead = input.read(bufferReader, 0, 2000);
                
                if (bytesRead < 0) {
                    break;  
                }
                System.out.println( "client-e mesaj geldi" );    
                System.out.println(bufferReader );    
                System.out.println( getMesajLine(bufferReader) );

               // byte[] pingu = hexStringToByteArray("fffa2c3216fff0");
                //out.write(pingu); 
                //out.flush();
            }

            catch (IOException i) {
                System.out.println(i);
                break;
            }
        }
 
        // close the connection
        try {
            input.close();
            out.close();
            socket.close();
        }
        catch (IOException i) {
            System.out.println(i);
        }
    }
 

    public static byte[] hexStringToByteArray(String s) {
        int len = s.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
                                 + Character.digit(s.charAt(i+1), 16));
        }
        return data;
    }

    public static void main(String args[])
    {
        testClient client = new testClient("10.6.240.201", 2862);
    }
}