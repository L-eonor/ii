package MES;

import static MES.SFS.getCell;

public class readSystem implements Runnable {

    public readSystem() {
    }

    public void run(){

        SFS map = new SFS();
        System.out.println("--------------readSystem is running--------------");
        if(OPCUA_Connection.get_Value_bool("MAIN_TASK", "AT1_S")) map.getCell(0,1).unitPresence = true;
        else map.getCell(0,1).unitPresence = true;
        System.out.println("--------------CELL AT1--------------");
        System.out.println(map.getCell(0,1).unitPresence);

    }
}