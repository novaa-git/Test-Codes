import java.io.InputStream;
import java.io.*;
import java.net.*; 
import java.net.Socket;
import java.security.cert.X509Certificate;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.security.cert.CertificateException;

import java.io.OutputStream;

 
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.*;

import org.apache.tika.Tika;
import org.apache.tika.parser.microsoft.rtf.*;
import org.apache.tika.metadata.*;
import javax.xml.transform.sax.SAXTransformerFactory;
import javax.xml.transform.sax.TransformerHandler;
import org.apache.tika.sax.*;
import org.apache.tika.parser.ParseContext;
import javax.xml.transform.stream.StreamResult;
import com.tutego.jrtf.Rtf;

import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.rtf.RTFEditorKit;
import javax.swing.text.Element;
import javax.swing.text.StyledDocument;

public class App {

 
    private DatagramSocket socket;
    private List<String> listQuotes = new ArrayList<String>();
    private Random random;
   
   public static Socket getSocketxx(String host, int port) throws Exception {
        
    TrustManager tm = new X509TrustManager() {
        public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
        }

        public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
        }

        public X509Certificate[] getAcceptedIssuers() {
            return null;
        }
    };

     SSLContext sslContext = SSLContext.getInstance("TLS");
     sslContext.init(null, new TrustManager[] { tm }, null);

     return sslContext.getSocketFactory().createSocket( host, port);
    //return sslContext.getSocketFactory().createSocket();
   }
    
    public static String  hl7 = "MSH|^~\\&|C740D0288EDFC45FE0407C0A04162BDD|Sisoft|teletipapp|teletip|20230321183432||ORM^O01|20230321183432|P|2.3.1||||||UTF8|" + ""+(char)13+
    "PID|1|900037-37243^^^^^sisoftapp|37243^^^TC|19561721002^^^TC|TEKTEN^SELDA^||19770310|F|||No:||5057216414^^^|||||37243|19561721002||||||||||||||||||||" + ""+(char)13 + 
    "PV1|1|O||||||33373047044^KURT^Mehmet^Hakan|33373047044^KURT^Mehmet^Hakan|||||||||R|2023-900037-P10754110|||||||||||||||||||||||||20230320090606|||||||V" + ""+(char)13 + 
    "ORC|NW|7255305^Sisoft|7255305^Sisoft||SC||1^once^^20230320090626||20230320090626|762^KURT^Mehmet^Hakan|762^KURT^Mehmet^Hakan|33373047044^KURT^Mehmet^Hakan|||||198^DIŞ HEKIMLIĞI (GENEL DIŞ)^ANKARA ÜNİVERSİTESİ||||ANKARA ÜNİVERSİTESİ DİŞ HEKİMLİĞİ FAKÜLTESİ^^900037\\S\\1\\S\\11060014" + ""+(char)13 +
    "OBR|1|7255305^Sisoft|7255305^Sisoft|R100250^BT, Mandibula(ALT ÇENE)^SUT^36938-9^Facial bones and Maxilla CT limited WO contrast^LNC||20230320090626|||||||||Radiology^^^^^R|33373047044^KURT^Mehmet^Hakan||7255305||7SGJCIV4UXAA418APX6N|2023-900037-P10754110|||CT|||1^once^^20230320090626|||||||||20230320090626|||||||" + ""+(char)13 +
    "DG1|3||Z96.5^DIS-KOKU VE MANDIBULER IMPLANT^I10|||F\n" + ""+(char)13+(char)28+(char)13;
    
    public static String dateToDateTimeStr1(Date paramDate)
    {
      String str = "0001-01-01T00:00:00";
      SimpleDateFormat localSimpleDateFormat = new SimpleDateFormat();
      localSimpleDateFormat.applyPattern("yyyy-MM-dd'T'HH:mm:ss");
      if (paramDate != null) {
        str = localSimpleDateFormat.format(paramDate);
      }
      return str;
    }

   public static void main3(String[] args) throws Exception {
    System.out.println(dateToDateTimeStr1(new Date()));

    System.out.println("işlemler başlatıldı");
    InputStream sockInput = null;
    OutputStream sockOutput = null;

// sisofttrustmanager 
    
    try {
        Socket sc = getSocketxx("hl7app.teletip.saglik.gov.tr", 443);
        sc.setSoTimeout(10000);
        sockInput = sc.getInputStream();
        sockOutput = sc.getOutputStream();
        
        try {
            byte[] data = hl7.getBytes();
            sockOutput.write(data, 0, data.length);
            sockOutput.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }

        final int readBufferSize = 2000;
        while (true) {
            byte[] bufferReader = new byte[readBufferSize];
            int bytesRead = sockInput.read(bufferReader, 0, readBufferSize);
            if (bytesRead > 0) {
                System.out.println( new String(bufferReader, 0, bytesRead) );
                    
                System.out.println(bytesRead);
                System.out.println(bufferReader.toString());
                break;
            }
        }
        
        System.out.println("işlemler tamamlandı");        

        

    } catch (Exception ex) {
        ex.printStackTrace();
    }
    



   }


   public static void main(String[] args) {

    String hostname = "10.6.240.201";
    int port = 2862;

  
        Socket clientSocket = null;  
        DataOutputStream os = null;
        BufferedReader is = null;

 
        try {
            clientSocket = new Socket(hostname, port);
            os = new DataOutputStream(clientSocket.getOutputStream());
            is = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            clientSocket.getOutputStream().write((byte) '\n');
            int ch = clientSocket.getInputStream().read();
            System.out.print("yaziyor");
        

        } catch (UnknownHostException e) {
            System.err.println("Don't know about host: " + hostname);
        } catch (IOException e) {
            System.err.println("Couldn't get I/O for the connection to: " + hostname);
        }

    // If everything has been initialized then we want to write some data
    // to the socket we have opened a connection to on the given port

    if (clientSocket == null || os == null || is == null) {
        System.err.println( "Something is wrong. One variable is null." );
        return;
    }

    try {
        while ( true ) {
        System.out.print( "Enter an integer (0 to stop connection, -1 to stop server): " );
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String keyboardInput = br.readLine();
        os.writeBytes( keyboardInput + "\n" );

        int n = Integer.parseInt( keyboardInput );
        if ( n == 0 || n == -1 ) {
            break;
        }

        String responseLine = is.readLine();
        System.out.println("Server returns its square as: " + responseLine);
        }

        // clean up:
        // close the output stream
        // close the input stream
        // close the socket

        os.close();
        is.close();
        clientSocket.close();   
    } catch (UnknownHostException e) {
        System.err.println("Trying to connect to unknown host: " + e);
    } catch (IOException e) {
        System.err.println("IOException:  " + e);
    }
    } 
    
     //---------------------------------------------------------------------------/

    public App(int port) throws SocketException {
        socket = new DatagramSocket(port);
        random = new Random();
    }
 
    private void service() throws IOException {
        while (true) {
            DatagramPacket request = new DatagramPacket(new byte[1], 1);
            socket.receive(request);
 
            String quote = getRandomQuote();
            byte[] buffer = quote.getBytes();
 
            InetAddress clientAddress = request.getAddress();
            int clientPort = request.getPort();
 
            DatagramPacket response = new DatagramPacket(buffer, buffer.length, clientAddress, clientPort);
            socket.send(response);
        }
    }
 
    private void loadQuotesFromFile(String quoteFile) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(quoteFile));
        String aQuote;
 
        while ((aQuote = reader.readLine()) != null) {
            listQuotes.add(aQuote);
        }
 
        reader.close();
    }
 
    private String getRandomQuote() {
        int randomIndex = random.nextInt(listQuotes.size());
        String randomQuote = listQuotes.get(randomIndex);
        return randomQuote;
    }

    public static void mainEx(String[] args) {
        String quoteFile = "C:\\cozum\\pacs\\PKDS_BARCODES\\Release\\log.log";
        int port = 1311;
 
        try {
            App server = new App(port);
            server.loadQuotesFromFile(quoteFile);
            server.service();
        } catch (SocketException ex) {
            System.out.println("Socket error: " + ex.getMessage());
        } catch (IOException ex) {
            System.out.println("I/O error: " + ex.getMessage());
        }
    }
 
    //---------------------------------------------------------------------------/

    public static String convertRTFtoHTML(String rtfFilePath) throws Exception {
        RTFParser parser1 = new RTFParser();
        
        File fl = new File("E:\\PROJECT\\rtf\\a1.rtf");
        

        FileInputStream fis = new FileInputStream(rtfFilePath);
        Tika tika = new Tika();

        String rtfDetect = tika.detect(fl); // detect as 'application/rtf'
        Metadata metadata = new Metadata();
        metadata.set(Metadata.CONTENT_TYPE, rtfDetect);


        SAXTransformerFactory factory = (SAXTransformerFactory)
        SAXTransformerFactory.newInstance();

        TransformerHandler handler = factory.newTransformerHandler();
        //handler.getTransformer().setOutputProperty("omit-xml-declaration", "xml");
        handler.getTransformer().setOutputProperty("indent", "yes");
        //handler.getTransformer().setOutputProperty("encoding", cpOut.name());
        ExpandedTitleContentHandler handler1 = new ExpandedTitleContentHandler(handler);
        
        ParseContext context = new ParseContext();
        
        
        OutputStream htmlStream = new FileOutputStream("E:\\PROJECT\\rtf\\a1.html");
        ToHTMLContentHandler toHTMLContentHandler = new ToHTMLContentHandler(htmlStream, "UTF-8");
  
        handler.setResult(new StreamResult(htmlStream));
        parser1.parse(fis, handler1, metadata, context);

        htmlStream.flush();
        //rtfStream.close();
        htmlStream.close();

        String htmlContent = "";
        /*  
        try {
            //htmlContent = tika.parseToString(fis);

        } catch (Exception ex) {
            ex.printStackTrace();
            
        } 
        */
        fis.close();
        return htmlContent;
    }

    public static String convertRTFtoHTML2(String rtfFilePath) throws Exception {
        InputStream inputStream = new FileInputStream(rtfFilePath);
        try {
        
            return Rtf.template(inputStream).out();
            
            /*
            StringBuilder htmlBuilder = new StringBuilder();
            htmlBuilder.append("<html><body>");
            htmlBuilder.append(rtf.getAsHTML());
            htmlBuilder.append("</body></html>");

            inputStream.close();
            */
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return "";
    }

    public static void main222(String[] args) {
        try {
            String rtfFilePath = "E:\\PROJECT\\rtf\\a1.rtf";
            String htmlContent = convertRTFtoHTML2(rtfFilePath);
            System.out.println(htmlContent);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /* -------------------------------------------------------- */

    
    public static String rtf2html(String paramString)
    throws Exception
  {
    StringBuffer localStringBuffer = new StringBuffer();
    RTFEditorKit localRTFEditorKit = new RTFEditorKit();
    DefaultStyledDocument localDefaultStyledDocument = new DefaultStyledDocument();
    int i = 0;
    try
    {
      
        i = 1;
        StringReader localStringReader = null;
        localStringReader = new StringReader(paramString);
        localRTFEditorKit.read(localStringReader, localDefaultStyledDocument, 0);
   
    }
    catch (Exception localException)
    {
      return paramString;
    }
    if (i != 0)
    {
      int j = 0;
      if (j < localDefaultStyledDocument.getRootElements().length)
      {
        Element localElement = localDefaultStyledDocument.getRootElements()[j];
        int k = 0;
        if (k < localElement.getElementCount()) {
         
        }
      }
    }
    String str = localStringBuffer.toString();
    str = str.replaceAll("?", "�").replaceAll("�", "&uuml;").replace("?", "�").replaceAll("�", "&ccedil;").replaceAll("?", "�").replaceAll("?", "�").replace("?", "�").replaceAll("?", "�");
    str = str.replaceAll("�", "&Uuml;");
    str = str.replaceAll("�", "&Ccedil;");
    str = str.replaceAll("�", "&Ouml;");
    return str;
  }    
/*
  public static void main(String[] args) {
    try {
        String rtfFilePath = "E:\\PROJECT\\rtf\\a1.rtf";
        
        String rtfCode =  "{\\rtf1\\ansi\\deff0\n" + 
        "\\trowd\n" + 
        "\\cellx1000\n" + 
        "\\cellx2000\n" + 
        "\\cellx3000\n" + 
        "cell 1\\intbl\\cell\n" + 
        "cell 2\\intbl\\cell\n" + 
        "cell 3\\intbl\\cell\n" + 
        "\\row\n" + 
        "}";
        
        String htmlContent = rtf2html(rtfCode);
        System.out.println(htmlContent);
    } catch (Exception e) {
        e.printStackTrace();
    }

}
*/

}
