import java.net.*;
import java.io.*;
import org.apache.commons.codec.binary.*;
 
public class testServer
{
    

    
    private Socket socket = null;
    private ServerSocket  server  = null;
    private InputStream in =  null;
    private OutputStream ot =  null;

        public static int[] table = {
         0x0000, 0xC0C1, 0xC181, 0x0140, 0xC301, 0x03C0, 0x0280, 0xC241,
         0xC601, 0x06C0, 0x0780, 0xC741, 0x0500, 0xC5C1, 0xC481, 0x0440,
         0xCC01, 0x0CC0, 0x0D80, 0xCD41, 0x0F00, 0xCFC1, 0xCE81, 0x0E40,
         0x0A00, 0xCAC1, 0xCB81, 0x0B40, 0xC901, 0x09C0, 0x0880, 0xC841,
         0xD801, 0x18C0, 0x1980, 0xD941, 0x1B00, 0xDBC1, 0xDA81, 0x1A40,
         0x1E00, 0xDEC1, 0xDF81, 0x1F40, 0xDD01, 0x1DC0, 0x1C80, 0xDC41,
         0x1400, 0xD4C1, 0xD581, 0x1540, 0xD701, 0x17C0, 0x1680, 0xD641,
         0xD201, 0x12C0, 0x1380, 0xD341, 0x1100, 0xD1C1, 0xD081, 0x1040,
         0xF001, 0x30C0, 0x3180, 0xF141, 0x3300, 0xF3C1, 0xF281, 0x3240,
         0x3600, 0xF6C1, 0xF781, 0x3740, 0xF501, 0x35C0, 0x3480, 0xF441,
         0x3C00, 0xFCC1, 0xFD81, 0x3D40, 0xFF01, 0x3FC0, 0x3E80, 0xFE41,
         0xFA01, 0x3AC0, 0x3B80, 0xFB41, 0x3900, 0xF9C1, 0xF881, 0x3840,
         0x2800, 0xE8C1, 0xE981, 0x2940, 0xEB01, 0x2BC0, 0x2A80, 0xEA41,
         0xEE01, 0x2EC0, 0x2F80, 0xEF41, 0x2D00, 0xEDC1, 0xEC81, 0x2C40,
         0xE401, 0x24C0, 0x2580, 0xE541, 0x2700, 0xE7C1, 0xE681, 0x2640,
         0x2200, 0xE2C1, 0xE381, 0x2340, 0xE101, 0x21C0, 0x2080, 0xE041,
         0xA001, 0x60C0, 0x6180, 0xA141, 0x6300, 0xA3C1, 0xA281, 0x6240,
         0x6600, 0xA6C1, 0xA781, 0x6740, 0xA501, 0x65C0, 0x6480, 0xA441,
         0x6C00, 0xACC1, 0xAD81, 0x6D40, 0xAF01, 0x6FC0, 0x6E80, 0xAE41,
         0xAA01, 0x6AC0, 0x6B80, 0xAB41, 0x6900, 0xA9C1, 0xA881, 0x6840,
         0x7800, 0xB8C1, 0xB981, 0x7940, 0xBB01, 0x7BC0, 0x7A80, 0xBA41,
         0xBE01, 0x7EC0, 0x7F80, 0xBF41, 0x7D00, 0xBDC1, 0xBC81, 0x7C40,
         0xB401, 0x74C0, 0x7580, 0xB541, 0x7700, 0xB7C1, 0xB681, 0x7640,
         0x7200, 0xB2C1, 0xB381, 0x7340, 0xB101, 0x71C0, 0x7080, 0xB041,
         0x5000, 0x90C1, 0x9181, 0x5140, 0x9301, 0x53C0, 0x5280, 0x9241,
         0x9601, 0x56C0, 0x5780, 0x9741, 0x5500, 0x95C1, 0x9481, 0x5440,
         0x9C01, 0x5CC0, 0x5D80, 0x9D41, 0x5F00, 0x9FC1, 0x9E81, 0x5E40,
         0x5A00, 0x9AC1, 0x9B81, 0x5B40, 0x9901, 0x59C0, 0x5880, 0x9841,
         0x8801, 0x48C0, 0x4980, 0x8941, 0x4B00, 0x8BC1, 0x8A81, 0x4A40,
         0x4E00, 0x8EC1, 0x8F81, 0x4F40, 0x8D01, 0x4DC0, 0x4C80, 0x8C41,
         0x4400, 0x84C1, 0x8581, 0x4540, 0x8701, 0x47C0, 0x4680, 0x8641,
         0x8201, 0x42C0, 0x4380, 0x8341, 0x4100, 0x81C1, 0x8081, 0x4040,
    };  

    public static String CheckSumHesaplaCrc16Arc(byte[]  bytes1) {   
        int crc = 0x0000;
        for (byte b : bytes1) {
            crc = (crc >>> 8) ^ table[(crc ^ b) & 0xff];
        }
        return Integer.toHexString(crc);
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

    public static String hexCevir(byte[] bytes) {
        StringBuilder result = new StringBuilder();
        for (byte aByte : bytes) {
            result.append(String.format("%02x", aByte));
        }
        return result.toString();
    }
    
    private String isQueryMsgId(String hexMsg) {
        String cardId = "";
        if ((hexMsg != null) && (hexMsg.length() > 50)) 
            cardId = hexMsg.substring(27,35);
        return cardId;            
    }
    
    private boolean isHelloMsg(String hexMsg) {
        if (hexMsg.equals("7b50494e477d")) 
            return true;
        return false;            
    }

 

    private void sendMessage(OutputStream outs, String hexmsg) throws IOException {
        byte[] pingu = hexStringToByteArray(hexmsg);
        outs.write(pingu); 
        outs.flush();
    }
    
    public testServer(int port)
    {
        try
        {
            InetAddress addr = InetAddress.getByName("10.6.240.153");
            server = new ServerSocket(port, 0, addr);
            System.out.println("Server started");
            socket = server.accept();
           
            System.out.println("yeni bağlantı");
 
            in = socket.getInputStream();
            ot = socket.getOutputStream();
 
            while (true) {
                try {
                    byte[] bufferReader = new byte[2000];
                    int bytesRead = in.read(bufferReader);
                    
                    if (bytesRead < 0) {
                        continue;  
                    }
                    
                    byte[] result = new byte[bytesRead];
                    System.arraycopy(bufferReader, 0, result, 0, bytesRead);

                    String hexMsg = hexCevir(result);
                    System.out.println("Okunan: " + hexMsg + "\n");
                    
                    if (isHelloMsg(hexMsg)) {
                        sendMessage(ot, "7B50494E477D");
               
                        
                 
                        

                    } else {
                        String id = isQueryMsgId(hexMsg);
                        
                        System.out.println("Okunan: " + id);

                        if (!id.equals("") && id.length() == 8) {
                            // burada id kontrol 
                            String accept = "534322ffc2c8b0641f8cc32817bc074422101500029f0f4765c3a7657273697a204b617274200a0a0a0afb46";

                            //String denied = "534322ffc2c8b0641f8cc32817bc07442210030002e803cd41";

                        String startId = "534322ff";
                        String centerId = "c32817bc074422100b0001";
                        String macId = "c2c8b0641f8c";
                        String sure = "9f0f"; //3000
                        String linespace = "0a0a0a0a";
                        String mesaj = Hex.encodeHexString("GECIS YOK".getBytes());
                        
                        String response = startId + macId + centerId + sure + mesaj + linespace;
                        String checkSum = CheckSumHesaplaCrc16Arc(response.getBytes());
                        response = response + checkSum;
                        
                        System.out.println("gonderilen: " + response);
                                

                            sendMessage(ot,accept);
                        } else 
                            sendMessage(ot, "7B50494E477D");
                    }

                 
                }
                catch (IOException i) {
                    System.out.println(i);
                    break;
                }
            }

            System.out.println("Closing connection");
            socket.close();
            in.close();
        }
        catch(IOException i)
        {
            System.out.println(i);
            
        }
    }
 
}