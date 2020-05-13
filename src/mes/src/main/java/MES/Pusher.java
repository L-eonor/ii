package MES;

public class Pusher extends Cell {

    /*
     * Class MES.Pusher, that represents all the action related to the MES.Pusher cell on the shop floor simulator.
     */

    // Attributes
    private int orderPushing;
    private int unitTypePushing;
    private boolean pushing;

    // Constructor
    public Pusher(int y, int x) {
        super(y,x);
        super.setName("Pusher");
        this.unitTypePushing=0;
        this.pushing = false;
    }

    //Methods

    public synchronized void setPushing(boolean value){
        //XOR - only change if they're different
        boolean different = this.pushing ^ value;
        if(different) this.pushing=value;
    }

    public synchronized boolean isPushing(){return this.pushing;}

    public synchronized void setUnitTypePushing(int x) {if (this.unitTypePushing != x && x!=0) this.unitTypePushing=x;}

    public synchronized void setOrderPushing(int x) {if (this.orderPushing != x) this.orderPushing=x;}

    public synchronized int getOrderPushing() {return this.orderPushing;}

    public synchronized int getUnitTypePushing() {return this.unitTypePushing;}

}
