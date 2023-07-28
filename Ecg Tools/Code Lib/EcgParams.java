
import org.dcm4che2.data.DicomObject;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import org.dcm4che2.data.DicomElement;
import org.dcm4che2.data.SpecificCharacterSet;
import org.dcm4che2.data.Tag;
import org.dcm4che2.io.DicomInputStream;
import org.dcm4che2.util.CloseUtils;

public class EcgParams {
    
    public String patientInfo = "" ;
    private int canvasH = 0;
    private int canvasW = 0;
    
    private int ecgH = 70;
    private int ecgW = 750;
    
    private double zoomLevel;
    public int mv_cells;
    public int seconds;
    private boolean fitToPage;
    private int numberOfChannels;
    public String displayFormat;
    public int samples_per_second;
    public int data[][];
    private EcgChannel[] channelDefinitions;
    
    public final double MAX_ZOOM_OUT = 1.0f;
    public final double MAX_ZOOM_IN = 10.0f;
    public final double ZOOM_UNIT = 0.5f;
    public final double NO_ZOOM = 1.0f;
    
    public static final String DEFAULTFORMAT = "1x10s";
    public static final String TWOPARTS = "2x5s";
    
    // Henüz aktif olmayanlar.
    public static final String FOURPARTS = "4x2.5s";
    public static final String FOURPARTSPLUS = "4x2.5s & RS";
    
    public static DicomObject GetFileToDicomObject(File localFile) {
      DicomInputStream localDicomInputStream = null;
      DicomObject localDicomObject = null;
      try {
        localDicomInputStream = new DicomInputStream(localFile);
        localDicomObject = localDicomInputStream.readDicomObject();
      } catch (IOException localIOException) {
        localIOException.printStackTrace();
      }
      finally {
        CloseUtils.safeClose(localDicomInputStream);
      }
      return localDicomObject;
    }
    
    public static DicomObject GetFileToDicomObject(String paramString) {
      File localFile = new File(paramString);
      return GetFileToDicomObject(localFile);
    }
    
    public void setCanvasW(int canvasW) {
        this.canvasW = canvasW;
    }

    public int getCanvasW() {
        return canvasW;
    }

    public void setCanvasH(int canvasH) {
        this.canvasH = canvasH;
    }

    public int getCanvasH() {
        return canvasH;
    }

    public void setEcgH(int ecgH) {
        this.ecgH = ecgH;
    }

    public int getEcgH() {
        return ecgH;
    }

    public void setEcgW(int ecgW) {
        this.ecgW = ecgW;
    }

    public int getEcgW() {
        return ecgW;
    }    
 
    public void readEcgDicomData(DicomObject dcm, Graphics2D backImage, String format) throws Exception {
        this.zoomLevel = NO_ZOOM;
        this.fitToPage = true;
        this.displayFormat = format;
        
        patientInfo = dcm.getString(Tag.PatientName)  + " - "  + 
                      dcm.getString(Tag.PatientID) + " - " + 
                      dcm.getString(Tag.PatientSex) +" - " +
        dcm.getString(Tag.PatientBirthDate);
        
        DicomElement temp = dcm.get(Tag.WaveformSequence);
        if(temp == null) 
            throw new Exception("Tag.WaveformSequence");
            
        dcm = temp.getDicomObject();
            
        DicomElement bitsAllocated = dcm.get(Tag.WaveformBitsAllocated);
        if(bitsAllocated == null) 
            throw new Exception("Tag.WaveformBitsAllocated");
            
        DicomElement waveformData = dcm.get(Tag.WaveformData);
        if(waveformData == null) 
            throw new Exception("Tag.WaveformData");
            
        DicomElement samplingFrequency = dcm.get(Tag.SamplingFrequency);
        if(samplingFrequency == null) 
            throw new Exception("Tag.SamplingFrequency");
            
        double frequency = samplingFrequency.getDouble(true);
            
        DicomElement samples = dcm.get(Tag.NumberOfWaveformSamples);
        if(samples == null) 
            throw new Exception("Tag.NumberOfWaveformSamples");
                    
        int numberOfSamples = samples.getInt(true);
            
        this.seconds = (int) (numberOfSamples / frequency);
        this.samples_per_second = numberOfSamples / seconds;
            
        DicomElement channels = dcm.get(Tag.NumberOfWaveformChannels);
        if(channels == null) 
            throw new Exception("Tag.NumberOfWaveformChannels");
                    
        this.numberOfChannels = channels.getInt(true);
            
        this.data = new int[numberOfChannels][numberOfSamples];
        
        if(bitsAllocated.getInt(true) == 16) {
            boolean order = dcm.bigEndian();
            byte[] tmp_bytes = waveformData.getBytes();
            short[] tmp = toShort(tmp_bytes, order);
            
            for (int i = 0; i < tmp.length; i++ ) {
                data[i%numberOfChannels][i/numberOfChannels] = (int) tmp[i];
            }
        } else 
            if(bitsAllocated.getInt(true) == 8) {
                byte[] tmp = waveformData.getBytes();
                for (int i = 0; i < tmp.length; i++ ) {
                    data[i%numberOfChannels][i/numberOfChannels] = (int) tmp[i];
            }
        } else
            throw new Exception("bitsAllocated value: " + bitsAllocated.getInt(true));
            
            DicomElement channelDef = dcm.get(Tag.ChannelDefinitionSequence);
            if(channelDef == null)  
                throw new Exception("ChannelDefinitionSequence");
            
            this.channelDefinitions = new EcgChannel[numberOfChannels];
            for(int i = 0; i < channelDef.countItems(); i++) {
                DicomObject object = channelDef.getDicomObject(i);
                
                DicomElement channelSensitivity = object.get(Tag.ChannelSensitivity);
                if(channelSensitivity == null)
                    throw new Exception("ChannelSensitivity");
                
                String tmp_value = channelSensitivity.getValueAsString(new SpecificCharacterSet("UTF-8"), 50);
                double sensitivity = Double.parseDouble(tmp_value);
                
                DicomElement channelSensitivityCorrection = object.get(Tag.ChannelSensitivityCorrectionFactor);
                if(channelSensitivityCorrection == null)
                    throw new Exception("ChannelSensitivityCorrectionFactor");
                
                tmp_value = channelSensitivityCorrection.getValueAsString(new SpecificCharacterSet("UTF-8"), 50);
                double help = Double.parseDouble(tmp_value);
                int sensitivityCorrection = (int) help;
                
                DicomElement tmpElement =  object.get(Tag.ChannelSourceSequence);
                if(tmpElement == null)
                    throw new Exception("ChannelSourceSequence");
                
                DicomObject channelSS =  tmpElement.getDicomObject();
                if(channelSS == null) 
                    throw new Exception("ChannelSourceSequence DicomObject");
                
                DicomElement meaning = channelSS.get(Tag.CodeMeaning);
                if(meaning == null) 
                    throw new Exception("ChannelSourceSequence Code Meaning");
                
                String name = meaning.getValueAsString(new SpecificCharacterSet("UTF-8"), 50);
                channelDefinitions[i] = new EcgChannel(name, sensitivity, sensitivityCorrection); 
            }
            
        getMinMax(data, channelDefinitions);
        
        // default
        if (displayFormat.equals(TWOPARTS)) {
            displayTwoParts();
        } else {
            displayDefault();
        }
        
        int leftPos = 0;
        int satirLeft = 0;
        double start = 0;
        
        this.ecgH = (canvasH / numberOfChannels) + 1;
        this.ecgW = canvasW;
        
        if (displayFormat.equals(TWOPARTS)) {
            this.ecgH = canvasH / (numberOfChannels / 2);
            this.ecgW = canvasW / 2;
        }
        
        for(int i = 0; i < numberOfChannels; i++) {
            BufferedImage newImage = new BufferedImage(getEcgW(), getEcgH(), BufferedImage.TYPE_INT_RGB);
                Graphics2D drEcg = newImage.createGraphics();
                drEcg.setBackground(Color.WHITE);
                drEcg.clearRect(0, 0, getEcgW(), canvasH);
                
            if (displayFormat.equals(TWOPARTS)) {
                if(i != 0 && (i % 2) != 0) {
                    start = 5.0;
                    leftPos = getEcgW();
                    satirLeft -= getEcgH() ;
                } else {
                    leftPos = 0;
                    start = 0;
                }
            }
            
            EcgDraw ecgD = new EcgDraw(this, data[i], start, channelDefinitions[i], drEcg);
            if (displayFormat.equals(TWOPARTS)) {
                drEcg.setColor(Color.BLUE); 
                drEcg.drawLine(0, 0, 4 , canvasH);
            }
            backImage.drawImage(newImage, leftPos, satirLeft, getEcgW(), getEcgH(), null );
            satirLeft += getEcgH() ;
        }

    }
    
    private short[] toShort(byte[] data, boolean isBigEndian) {
        short[] retdata = new short[data.length / 2];
        int pos = 0;
        ByteBuffer bb = ByteBuffer.allocate(2);
        if(isBigEndian) {
            bb.order(ByteOrder.BIG_ENDIAN);
        } else {
            bb.order(ByteOrder.LITTLE_ENDIAN);
        }
        for (int i = 0; i < data.length; ++i) {
            byte firstByte = data[i];
            byte secondByte = data[++i];
            bb.put(firstByte);
            bb.put(secondByte);
            retdata[pos] = bb.getShort(0);
            pos++;
            bb.clear();
        }
        return retdata;
    }    
    
    private void getMinMax(int data[][], EcgChannel definitions[]) {
        for(int i = 0; i < data.length; i++) {
            double min = 0;
            double max = 0;
            double scalingValue = definitions[i].getSensitity() * definitions[i].getSensitivityCorrection();
            for(int j = 0; j < data[i].length; j++) {
                if (min > data[i][j] * scalingValue) {
                    min = data[i][j] * scalingValue;
                }
                if(max < data[i][j] * scalingValue) {
                    max = data[i][j] * scalingValue;
                }
            }
            definitions[i].setMaximum(max);
            definitions[i].setMinimum(min);
        }
        double max = Double.MIN_VALUE;
        double min = Double.MAX_VALUE;
        for(int i = 0; i < definitions.length; i++) {
            if (min > definitions[i].getMinimum()){
                min = definitions[i].getMinimum();
            }
            if (max < definitions[i].getMaximum()) {
                max = definitions[i].getMaximum();
            }
        }
        
        double minmax = Math.max(Math.abs(max), Math.abs(min));
        int mv_cells = (int) (minmax / 1000);
        if((int) minmax % 1000 != 0) { 
            ++mv_cells;
        }
        mv_cells *= 2;                  
        this.mv_cells = mv_cells;
    }
    
    private void displayDefault() {
        int[][] temp_data = new int[this.data.length][this.data[0].length];
        EcgChannel[] temp_definitions = new EcgChannel[this.channelDefinitions.length];
        for (int i = 0; i < this.data.length; i++) {
            if (this.channelDefinitions[i].getName_I()) {
                    temp_data[0] = this.data[i];
                    temp_definitions[0] = this.channelDefinitions[i]; 
            }
            else if (this.channelDefinitions[i].getName_II()) {
                    temp_data[1] = this.data[i];
                    temp_definitions[1] = this.channelDefinitions[i];
            }
            else if (this.channelDefinitions[i].getName_III()) {
                    temp_data[2] = this.data[i];
                    temp_definitions[2] = this.channelDefinitions[i];
            }
            else if (this.channelDefinitions[i].getName_aVR()) {
                    temp_data[3] = this.data[i];
                    temp_definitions[3] = this.channelDefinitions[i];
            }
            else if (this.channelDefinitions[i].getName_aVL()) {
                    temp_data[4] = this.data[i];
                    temp_definitions[4] = this.channelDefinitions[i];
            }
            else if (this.channelDefinitions[i].getName_aVF()) {
                    temp_data[5] = this.data[i];
                    temp_definitions[5] = this.channelDefinitions[i];
            }
            else if (this.channelDefinitions[i].getName_V1()) {
                    temp_data[6] = this.data[i];
                    temp_definitions[6] = this.channelDefinitions[i];
            }
            else if (this.channelDefinitions[i].getName_V2()) {
                    temp_data[7] = this.data[i];
                    temp_definitions[7] = this.channelDefinitions[i];
            }
            else if (this.channelDefinitions[i].getName_V3()) {
                    temp_data[8] = this.data[i];
                    temp_definitions[8] = this.channelDefinitions[i];
            }
            else if (this.channelDefinitions[i].getName_V4()) {
                    temp_data[9] = this.data[i];
                    temp_definitions[9] = this.channelDefinitions[i];
            }
            else if (this.channelDefinitions[i].getName_V5()) {
                    temp_data[10] = this.data[i];
                    temp_definitions[10] = this.channelDefinitions[i];
            }
            else if (this.channelDefinitions[i].getName_V6()) {
                    temp_data[11] = this.data[i];
                    temp_definitions[11] = this.channelDefinitions[i];
            }
        }
        this.data = temp_data;
        this.channelDefinitions = temp_definitions;
    }

    private void displayTwoParts() {
        int[][] temp_data = new int[this.data.length][this.data[0].length];
        EcgChannel[] temp_definitions = new EcgChannel[this.channelDefinitions.length];
        for (int i = 0; i < this.data.length; i++) {
            if(this.channelDefinitions[i].getName_I()) {
                    temp_data[0] = this.data[i];
                    temp_definitions[0] = this.channelDefinitions[i]; 
            }
            else if (this.channelDefinitions[i].getName_V1()) {
                    temp_data[1] = this.data[i];
                    temp_definitions[1] = this.channelDefinitions[i];
            }
            else if (this.channelDefinitions[i].getName_II()) {
                    temp_data[2] = this.data[i];
                    temp_definitions[2] = this.channelDefinitions[i];
            }
            else if (this.channelDefinitions[i].getName_V2()) {
                    temp_data[3] = this.data[i];
                    temp_definitions[3] = this.channelDefinitions[i];
            }
            else if (this.channelDefinitions[i].getName_III()) {
                    temp_data[4] = this.data[i];
                    temp_definitions[4] = this.channelDefinitions[i];
            }
            else if (this.channelDefinitions[i].getName_V3()) {
                    temp_data[5] = this.data[i];
                    temp_definitions[5] = this.channelDefinitions[i];
            }
            else if (this.channelDefinitions[i].getName_aVR() ) {
                    temp_data[6] = this.data[i];
                    temp_definitions[6] = this.channelDefinitions[i];
            }
            else if (this.channelDefinitions[i].getName_V4()) {
                    temp_data[7] = this.data[i];
                    temp_definitions[7] = this.channelDefinitions[i];
            }
            else if (this.channelDefinitions[i].getName_aVL()) {
                    temp_data[8] = this.data[i];
                    temp_definitions[8] = this.channelDefinitions[i];
            }
            else if (this.channelDefinitions[i].getName_V5() ) {
                    temp_data[9] = this.data[i];
                    temp_definitions[9] = this.channelDefinitions[i];
            }
            else if (this.channelDefinitions[i].getName_aVF()) {
                    temp_data[10] = this.data[i];
                    temp_definitions[10] = this.channelDefinitions[i];
            }
            else if (this.channelDefinitions[i].getName_V6()) {
                    temp_data[11] = this.data[i];
                    temp_definitions[11] = this.channelDefinitions[i];
            }
        }
        this.data = temp_data;
        this.channelDefinitions = temp_definitions;
    }

}
