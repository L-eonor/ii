package MES;

public class Cell {


    /*
     * Class MES.Cell, that represents all the action related to the machine cells on the shop floor simulator.
     * It's a generalization of all the existing cells and also represents the Conveyor cell, since it is the
      most simple one and all the others share the same characteristics as the Conveyor.
     */

    //Attributes
    private String name;
    private final int[] position = new int[2];
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
        this.weight=0;
        this.unitPresence=false;
        this.position[0]=y;
        this.position[1]=x;
        this.setName(name);
    }


    //Methods

    //Setters

    public synchronized void setName(String a){
        this.name=a;
    }

    public synchronized void setUnitPresence(boolean unitPresence) {
        //If cell gets free, removes 1 from weight
        if(!unitPresence && this.unitPresence) {
            removeWeight();
        }
        //XOR - only change if they're different
        boolean different = this.unitPresence ^ unitPresence;
        if(different) this.unitPresence=unitPresence;
    }

    public synchronized void setWeight(int weight) {
        this.weight = weight;
    }

    //Getters

    public synchronized String getName() {
        return this.name;
    }

    public synchronized boolean getUnitPresence() {
        return this.unitPresence;
    }

    public synchronized int[] getPosition() {
        return position;
    }


    public synchronized float getWeight() {
        return weight;
    }

    //+Methods

    public synchronized void addWeight() {
        this.weight= (float) ((this.weight + 1));
    }

    public synchronized void removeWeight() {
        this.weight= (float) (this.weight - 1);
        if(this.weight < 0) this.weight=0;
    }

}



