package MES;

public class Cell {


    /*
     * Class MES.Cell, that represents all the action related to the machine cells on the shop floor simulator.
     * It's a generalization of all the existing cells and also represents the Conveyor cell, since it is the
      most simple one and all the others share the same characteristics as the Conveyor.
     */

    //Attributes
    public String name;
    private int[] position = new int[2];
    public boolean unitPresence;


    // Constructor
    public Cell(int y, int x) {
        this.unitPresence=false;
        this.position[0]=y;
        this.position[1]=x;
        this.setName("Conveyor");
    }

    public Cell(int y, int x, String name) {
        this.unitPresence=false;
        this.position[0]=y;
        this.position[1]=x;
        this.setName(name);
    }


    //Methods

    //Setters

    public void setName(String a){
        this.name=a;
    }

    public void setUnitPresence(boolean unitPresence) {
        this.unitPresence = unitPresence;
    }

    //Getters

    public String getName() {
        return this.name;
    }

    public boolean getUnitPresence() {
        return unitPresence;
    }

    public int[] getPosition() {
        return position;
    }


    public void updateOPCUA() {

    }
}



