
public class EcgChannel {
    
    private String name;
    private double sensitivity;
    private int sensitivityCorrection;
    private double minimum;
    private double maximum;
    
    public EcgChannel(String name, double sensitity, int sensitivityCorrection) {
        this.name = name;
        this.sensitivity = sensitity;
        this.sensitivityCorrection = sensitivityCorrection;
        this.maximum = 0.0;
        this.minimum = 0.0;
    }
    
    public String getName() {
            return name;
    }

    public double getSensitity() {
        return sensitivity;
    }

    public int getSensitivityCorrection() {
        return sensitivityCorrection;
    }

    public double getMinimum() {
        return minimum;
    }

    public void setMinimum(double minimum) {
            this.minimum = minimum;
    }

    public double getMaximum() {
        return maximum;
    }

    public void setMaximum(double maximum) {
        this.maximum = maximum;
    }
    
    public Boolean getName_I() {
        return (name.equals("I") || name.equalsIgnoreCase("Lead I"));
    }
    
    public Boolean getName_II() {
        return (name.equals("II") || name.equalsIgnoreCase("Lead II"));
    }

    public Boolean getName_V1() {
        return (name.equals("V1") || name.equalsIgnoreCase("Lead V1"));
    }

    public Boolean getName_V2() {
        return (name.equals("V2") || name.equalsIgnoreCase("Lead V2"));
    }

    public Boolean getName_V3() {
        return (name.equals("V3") || name.equalsIgnoreCase("Lead V3"));
    }

    public Boolean getName_V4() {
        return (name.equals("V4") || name.equalsIgnoreCase("Lead V4"));
    }

    public Boolean getName_V5() {
        return (name.equals("V5") || name.equalsIgnoreCase("Lead V5"));
    }

    public Boolean getName_V6() {
        return (name.equals("V6") || name.equalsIgnoreCase("Lead V6"));
    }
    
    public Boolean getName_III() {
        return (name.equals("III") || name.equalsIgnoreCase("Lead III"));
    }

    public Boolean getName_aVR() {
        return (name.equals("aVR") || name.equalsIgnoreCase("Lead aVR"));
    }

    public Boolean getName_aVL() {
        return (name.equals("aVL") || name.equalsIgnoreCase("Lead aVL"));
    }
    
    public Boolean getName_aVF() {
        return (name.equals("aVF") || name.equalsIgnoreCase("Lead aVF"));
    }

}
