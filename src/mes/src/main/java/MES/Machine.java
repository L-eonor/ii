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

    // Constructor

    public Machine(int y, int x) {
        super(y,x);
        super.setName("Machine");

    }

    //Methods

    //Setters
    public void setProcessTime(int processTime) {
        this.processTime = processTime;
    }

    public void setTool(int tool) {
        this.tool = tool;
    }

    //Getters
    public int getProcessTime() {
        return processTime;
    }

    public int getTool() {
        return tool;
    }

}
