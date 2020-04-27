package MES;

public class Rotator extends Cell {

    /*
     * Class MES.Rotator, that represents all the action related to the MES.Rotator MES.Cell on the shop floor simulator.
     */

    //Attributes
    public String name="Rotator";

    public boolean isDirectionVertical;

    public boolean isDirectionHorizontal;
    //Constructor

    public Rotator(int y, int x) {
        super(y,x);
        super.setName("Rotator");
        this.isDirectionHorizontal=true;
        this.isDirectionVertical=false;

    }

    //Methods
    //Setters
    public void setDirectionVertical(boolean directionVertical) {
        isDirectionVertical = directionVertical;
    }

    public void setDirectionHorizontal(boolean directionHorizontal) {
        isDirectionHorizontal = directionHorizontal;
    }

    //Getters
    public boolean isDirectionHorizontal() {
        return isDirectionHorizontal;
    }

    public boolean isDirectionVertical() {
        return isDirectionVertical;
    }

}
