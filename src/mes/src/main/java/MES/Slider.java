package MES;

public class Slider extends Cell {
    /*
     * Class MES.Slider, that represents all the action related to the MES.Slider cell on the shop floor simulator.
     */

    // Attributes
    private boolean sensorLevel4;

    private boolean sensorLevel3;

    private boolean sensorLevel2;


    // Constructor

    public Slider(int y, int x) {
        super(y,x);
        super.setName("Slider");
    }

    //Methods

    public synchronized boolean getSensorLevel4() {
        return this.sensorLevel4;
    }

    public synchronized void setSensorLevel4(boolean sensorLevel4) {
        this.sensorLevel4 = sensorLevel4;
    }

    public synchronized boolean getSensorLevel3() {
        return sensorLevel3;
    }

    public synchronized void setSensorLevel3(boolean sensorLevel3) {
        this.sensorLevel3 = sensorLevel3;
    }

    public synchronized boolean getSensorLevel2() {
        return sensorLevel2;
    }

    public synchronized void setSensorLevel2(boolean sensorLevel2) {
        this.sensorLevel2 = sensorLevel2;
    }

    public synchronized boolean isFull() {
     if(this.sensorLevel4)  return true;
     else return false;
    }

    public synchronized int getNumberOfUnits() {
        if(this.sensorLevel4) return 4;
        else if(this.sensorLevel3) return 3;
        else if(this.sensorLevel2) return 2;
        else return 1;
    }

}
