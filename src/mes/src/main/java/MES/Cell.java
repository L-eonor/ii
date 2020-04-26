package MES;

public class Cell {

    /*
     * Class MES.Cell, that represents all the action related to the machine cells on the shop floor simulator.
     * It's a generalization of all the existing cells and also represents the Conveyor cell, since it is the
      most simple one and all the others share the same characteristics as the Conveyor.
     */

    //Attributes
    public String name;
    public int[] position = new int[2];
    public boolean unitPresence;
    public boolean motorState;


    // Constructor
    public Cell(int y, int x) {
        this.motorState=false;
        this.unitPresence=false;
        this.position[0]=y;
        this.position[1]=x;
        this.setName("Conveyor");
    }

    public Cell(int y, int x, String name) {
        this.motorState=false;
        this.unitPresence=false;
        this.position[0]=y;
        this.position[1]=x;
        this.setName(name);
    }


    //Methods
    public String getName(){
        return this.name;
    }

    public int[] getPosition() {
        return this.position;
    }

    public void setName(String a){
        this.name=a;
    }

    public boolean startMotor() {
        if(motorState=true) return true;
        else return false;
    }

    public boolean stopMotor() {
        if(motorState=false) return true;
        else return false;
    }

}


