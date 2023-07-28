
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Line2D;

public class EcgDraw {
    
    private int[] data;
    private float scalingWidth;
    private EcgChannel definition;
    private int mv_cell_count;
    private int secs_cell_count;
    private double cellheight;
    private double cellwidth;
    private Dimension dim;
    private int start;
    private int end;
    private double valueScaling;
    private double offset; 
    private boolean isRhythm;
    private EcgParams param;
    
    public EcgDraw(EcgParams param, int[] values, double start, EcgChannel definition, Graphics2D drEcg) {
        this.param = param;
        this.data = values;
        this.definition = definition;                   
        this.mv_cell_count = param.mv_cells;
        this.secs_cell_count = param.seconds * 10;
        this.dim = new Dimension(param.getEcgW(), param.getEcgH());
        
        this.cellheight = dim.getHeight() / mv_cell_count;
        this.cellwidth = dim.getWidth() / secs_cell_count;
        this.start = (int) (start * param.samples_per_second);
        this.end = data.length;
        this.offset = start;
        this.valueScaling = this.definition.getSensitity() * this.definition.getSensitivityCorrection();
        this.isRhythm = false;
        this.paintComponent(drEcg);
    }
    
    public void setRhythm(boolean mode) {
        this.isRhythm = mode;
    }
    
    public void paintComponent(Graphics2D g2) {
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);    
        g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g2.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_NORMALIZE);
        g2.setRenderingHint(RenderingHints.KEY_DITHERING, RenderingHints.VALUE_DITHER_ENABLE);
        
        if(param.displayFormat.equals(param.DEFAULTFORMAT) || isRhythm) {
                this.secs_cell_count = param.seconds * 10;
                this.end = this.start + this.data.length;
        }
        else if(param.displayFormat.equals(param.FOURPARTS)) {
                this.secs_cell_count = (int) (2.5 * 10);
                this.end = this.start + (int) (2.5 * param.samples_per_second);
        }
        else if(param.displayFormat.equals(param.FOURPARTSPLUS)) {
                this.secs_cell_count = (int) (2.5 * 10);
                this.end = this.start + (int) (2.5 * param.samples_per_second);
        }
        else if(param.displayFormat.equals(param.TWOPARTS)) {
                this.secs_cell_count = 5 * 10;
                this.end = this.start + 5 * param.samples_per_second;
        }
        g2.setBackground(Color.WHITE);
        
        this.cellheight = dim.getHeight() / this.mv_cell_count;
        this.cellwidth = dim.getWidth() / this.secs_cell_count;
        this.scalingWidth = (float) (cellwidth / ((this.end - this.start) / secs_cell_count ));                        
        
        drawGrid(g2);
        drawGraph(g2);
        drawName(g2);
    }    
    
    private void drawGrid(Graphics2D g2) {
        //g2.setColor(new Color(231, 84, 72));
        g2.setColor(new Color(222, 173, 169));
        g2.setStroke(new BasicStroke(1.0f));
        for(int i = 0; i < mv_cell_count; i++) {
            g2.draw(new Line2D.Double(0, i * cellheight, 
            dim.getWidth(), i * cellheight));                       
        }

        for(int i = 0; i < secs_cell_count; i++ ) {
            if(i % 10 == 0) {
                g2.setStroke(new BasicStroke(2.0f));
            } else {
                g2.setStroke(new BasicStroke(1.0f));
            }
            g2.draw(new Line2D.Double(i * cellwidth , 0,  i * cellwidth, dim.getHeight()));
        }
    }
    
    private void drawGraph(Graphics2D g2) {
        g2.setColor(Color.BLACK);
        g2.setStroke(new BasicStroke(1.2f));
        for(int i  = this.start; i < (this.end - 1); i++) {
            int a = i;
            int b = i + 1;
            Line2D line = new Line2D.Double(
                            this.scalingWidth * (a - this.start), 
                            (this.dim.height /2 - this.valueScaling * ( (float)(this.data[a] / (float) 1000) * this.cellheight) ), 
                            this.scalingWidth * (b - this.start), 
                            ( this.dim.height /2 - this.valueScaling * ( (float)(this.data[b] / (float) 1000) * this.cellheight ) ));
            g2.draw(line);
         }      
    }
    
    private void drawName(Graphics2D g2) {
        g2.setColor(Color.blue);
        g2.setFont(new Font("SanSerif", Font.BOLD, 15));
        g2.drawString(definition.getName(), 5, 15);
    }
    
}
