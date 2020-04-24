package MES;

public class Rotator extends Cell {

    /*
     * Class MES.Rotator, that represents all the action related to the MES.Rotator MES.Cell on the shop floor simulator.
     */

    //Attributes
    public String name="Rotator";
    public boolean rotorState;
    public boolean isDirectionVertical;
    public boolean isDirectionHorizontal;

    //Constructor
    public Rotator(int y, int x) {
        super(y,x);
        super.setName("Rotator");
        this.rotorState=false;
        this.isDirectionHorizontal=true;
        this.isDirectionVertical=false;

    }

    //Methods

    public boolean startRotor() {
        if(rotorState=true) return true;
        else return false;
    }

    public boolean stopRotor() {
        if(rotorState=false) return true;
        else return false;
    }
}
