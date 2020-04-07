public class Rotator extends Cell {

    /*
     * Class Rotator, that represents all the action related to the Rotator Cell on the shop floor simulator.
     */

    //Attributes
    public boolean rotorState;
    public boolean isDirectionVertical;
    public boolean isDirectionHorizontal;

    //Constructor
    public Rotator() {

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
