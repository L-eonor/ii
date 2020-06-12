package MES;

import org.bouncycastle.asn1.eac.UnsignedInteger;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UShort;

import java.net.URLStreamHandler;

public class Machine extends Cell {

    /*
     * Class MES.Machine, that represents all the action related to the machine cell on the shop floor simulator.
     */

    // Attributes
    private int totalTime;
    private int totalUnits;
    private int unitsDoneByType[] = { 0, 0, 0, 0, 0, 0, 0, 0, 0};

    // Constructor

    public Machine(int y, int x) {
        super(y,x);
        super.setName("Machine");

    }

    //Methods

    public synchronized int getTotalTime(){
        String totalT = "C"+this.getPosition()[1]+"T"+this.getPosition()[0]+".TOTAL_TIME";
        try {
            this.totalTime = OPCUA_Connection.getValueInt("MAIN_TASK", totalT);
        }
        catch (Exception e){
            System.out.println(e);
        }

        //System.out.println("Tempo total na Máquina "+ "C"+this.getPosition()[1]+"T"+this.getPosition()[0]+ ": "+ this.totalTime);
        return this.totalTime;
    }

    public synchronized int getTotalUnits() {
        String totalU = "C"+this.getPosition()[1]+"T"+this.getPosition()[0]+".TOTAL_UNITS_PROCESSED";
        try {
            this.totalUnits = OPCUA_Connection.getValueInt("MAIN_TASK", totalU);
        }
        catch (Exception e){
            System.out.println(e);
        }

        //System.out.println("Unidades processadas na Máquina "+ "C"+this.getPosition()[1]+"T"+this.getPosition()[0]+ ": "+ this.totalUnits);
        return this.totalUnits;
    }

    public synchronized int[] getUnitsDoneByType(){
        //System.out.println("Unidades por tipo na Máquina "+ "C"+this.getPosition()[1]+"T"+this.getPosition()[0]+ ": ");
        for(int i=0; i < 9; i++ ){
            String ubt = "C"+this.getPosition()[1]+"T"+this.getPosition()[0]+".UNITS_DONE_BY_TYPE["+i+"]";
            try {
                this.unitsDoneByType[i] = OPCUA_Connection.getValueInt("MAIN_TASK", ubt);
            }
            catch (Exception e){
                System.out.println(e);
            }
            int a=i;
            //System.out.println("  - P"+(a+1)+": "+ this.unitsDoneByType[i]);
        }

        return this.unitsDoneByType;
    }






}
