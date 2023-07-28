
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.OutputStream;
import javax.imageio.ImageIO;
import javax.imageio.stream.ImageOutputStream;

public class EcgTools {
    
    private int width = 0;
    private int height = 0;
    
    private String ecgFileName = null;
    private String imageFileName = null;
    private String format = EcgParams.DEFAULTFORMAT;
    private Boolean isPatientInfo = true;
    private ImageOutputStream imageOs = null;
    private OutputStream output = null;
    private ByteArrayOutputStream byteStream = null;
    private String exportImageFormat = "png";
    
    public BufferedImage getEcgToBitmap() {
        return null;
    }
    
    private final int TYPE_IMAGE_OUTPUT = 0;
    public ImageOutputStream getEcgToImageOutputStream() {
        execute(TYPE_IMAGE_OUTPUT);
        return imageOs;
    }

    private final int TYPE_OUTPUT = 1;
    public OutputStream getEcgToOutputStream() {
        execute(TYPE_OUTPUT);
        return output;
    }

    private final int TYPE_FILE_OUTPUT = 2;
    public Boolean getEcgToFile() {
        this.execute(TYPE_FILE_OUTPUT);
        return new File(imageFileName).exists();
    }

    private final int TYPE_BYTEARRAY = 3;
    public ByteArrayOutputStream getEcgToByteArray() {
        this.execute(TYPE_BYTEARRAY);
        return byteStream;
    }
    
    private void execute(int type) {
        BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D backImage = bufferedImage.createGraphics();
            backImage.setBackground(Color.WHITE);
            backImage.clearRect(0, 0, width, height);
        
        EcgParams param = new EcgParams();
            param.setCanvasW(width);
            param.setCanvasH(height);
            
        try {
            param.readEcgDicomData(EcgParams.GetFileToDicomObject(ecgFileName), backImage, format);
            if (isPatientInfo) {
                backImage.setColor(Color.WHITE);
                backImage.setBackground(Color.DARK_GRAY);
                backImage.clearRect(100, 0, 425, 20);
                backImage.setFont(new Font("Courier New", Font.BOLD, 16));
                backImage.drawString(param.patientInfo, 107, 15);        
            }
            backImage.setColor(new Color(222, 173, 169));
            backImage.drawLine(0, height-1, width, height-1);
            backImage.drawLine(width-1, 0, width-1, height);
            if (type == TYPE_IMAGE_OUTPUT) {
                System.out.println("Bilgi:Henüz aktif değil");
                //ImageIO.write(bufferedImage, "png", imageOs );
            } else 
            if (type == TYPE_OUTPUT) {
                output = new ByteArrayOutputStream();
                ImageIO.write(bufferedImage, exportImageFormat, output );
            } else 
            if (type == TYPE_BYTEARRAY) {
                this.byteStream = new ByteArrayOutputStream();
                ImageIO.write(bufferedImage, exportImageFormat, byteStream );
            } else 
                ImageIO.write(bufferedImage, exportImageFormat, new File(imageFileName));
            
        } catch (Exception e) {
            e.printStackTrace();
        }

        backImage.dispose();
    }

    public void setFileName(String fileName) {
        this.ecgFileName = fileName;
    }

    public String getFileName() {
        return ecgFileName;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getWidth() {
        return width;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getHeight() {
        return height;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public String getFormat() {
        return format;
    }

    public void setIsPatientInfo(Boolean isPatientInfo) {
        this.isPatientInfo = isPatientInfo;
    }

    public Boolean getIsPatientInfo() {
        return isPatientInfo;
    }

    public void setImageFileName(String imageFileName) {
        this.imageFileName = imageFileName;
    }

    public String getImageFileName() {
        return imageFileName;
    }
    
    public void setExportImageFormat(String exportImageFormat) {
        this.exportImageFormat = exportImageFormat;
    }

    public static Boolean setDicomToEcgImage(String dicomFileName, String imageFileName, String ecgFormat, 
                                            Boolean isPatientInfo, int width, int height, String exportExtention) {
        EcgTools tl = new EcgTools();
        tl.setFileName(dicomFileName);
        tl.setIsPatientInfo(isPatientInfo);
        if (width == 0) {
            width = 1200;
        }
        if (height == 0) {
            height = 900;
        }
        tl.setWidth(width);
        tl.setHeight(height);

        if (exportExtention != null) {
            tl.setExportImageFormat(exportExtention);    
        }
        if (ecgFormat == null)
            ecgFormat = EcgParams.DEFAULTFORMAT;
        else
            if (ecgFormat.equals("1"))
                ecgFormat = EcgParams.DEFAULTFORMAT;
                else 
                if (ecgFormat.equals("2"))
                    ecgFormat = EcgParams.TWOPARTS;
        tl.setFormat(ecgFormat);
        tl.setImageFileName(imageFileName);
        return tl.getEcgToFile();
    }

    public String getExportImageFormat() {
        return exportImageFormat;
    }
 
}
