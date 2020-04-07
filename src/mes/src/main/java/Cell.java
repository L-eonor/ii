public class Cell {

    /*
     * Class Cell, that represents all the action related to the machine cells on the shop floor simulator.
     * It's a generalization of all the existing cells and also represents the Conveyor cell, since it is the
      most simple one and all the others share the same characteristics as the Conveyor.
     */

    //Attributes
    public boolean unitPresence;
    public boolean motorState;


    // Constructor
    public Cell() {
        this.motorState=false;
        this.unitPresence=false;
    }

    //Methods

    public boolean startMotor() {
        if(motorState=true) return true;
        else return false;
    }

    public boolean stopMotor() {
        if(motorState=false) return true;
        else return false;
    }

}


