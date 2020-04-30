package MES;

import org.bouncycastle.asn1.eac.UnsignedInteger;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UShort;

import java.net.URLStreamHandler;

public class Machine extends Cell {

    /*
     * Class MES.Machine, that represents all the action related to the machine cell on the shop floor simulator.
     */

    // Attributes
    private int processTime;
    private int tool;
    private int weight;

    // Constructor

    public Machine(int y, int x) {
        super(y,x);
        super.setName("Machine");

    }

    //Methods

    //Setters
    @Override
    public void setUnitPresence(boolean unitPresence) {
        if(!unitPresence && this.unitPresence) {
            removeWeight();
        }
        this.unitPresence = unitPresence;
    }

    public void setProcessTime(int processTime) {
        this.processTime = processTime;
    }

    public void setTool(int tool) {
        this.tool = tool;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    //Getters
    public int getProcessTime() {
        return processTime;
    }

    public int getTool() {
        return tool;
    }

    public int getWeight() {
        return weight;
    }

    //+Methods

    public void addWeight() {
        this.weight++;
    }

    public void removeWeight() {
        this.weight--;
    }

}
