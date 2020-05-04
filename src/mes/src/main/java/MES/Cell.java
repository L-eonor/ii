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
    private float weight;


    // Constructor
    public Cell(int y, int x) {
        this.weight=0;
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
        //If cell gets free, removes 1 from weight
        if(!unitPresence && this.unitPresence) {
            removeWeight();
        }
        //XOR - only change if they're different
        boolean different = this.unitPresence ^ unitPresence;
        if(different) this.unitPresence=unitPresence;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    //Getters

    public String getName() {
        return this.name;
    }

    public boolean getUnitPresence() {
        return this.unitPresence;
    }

    public int[] getPosition() {
        return position;
    }


    public float getWeight() {
        return weight;
    }

    //+Methods

    public void addWeight(int nTimes) {
        this.weight= (float) ((this.weight + 0.5)*nTimes);
    }

    public void removeWeight() {
        this.weight= (float) (this.weight - 0.5);
    }

}



