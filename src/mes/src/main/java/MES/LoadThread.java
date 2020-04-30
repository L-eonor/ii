package MES;

public class LoadThread implements Runnable {

    //Attributes
    static int[] warehouseIn = {0, 7};
    static int[] loadP1 = {7,1};
    static int[] loadP2 = {7,7};

    SFS floor = Main.floor;

    public LoadThread() {

    }

    public void run() {
        System.out.println("--------------[Executing] LoadThread is Running [Executing]--------------");

        while(true) {
            StringBuilder pathStringLoad1 = new StringBuilder();
            StringBuilder pathStringLoad2 = new StringBuilder();

            if(floor.getCell(1,8).getUnitPresence()) {
                Path_Logic pathLoad = new Path_Logic(loadP1, warehouseIn);
                pathStringLoad1.append(pathLoad.getStringPath());
                System.out.println("[Load1] Esta é a string: " + pathStringLoad1);
                OPCUA_Connection.setValueString("MAIN_TASK", "C7T1b_order_path_mes", pathStringLoad1.toString());
            }
            if(floor.getCell(7,8).getUnitPresence()) {
                Path_Logic pathLoad = new Path_Logic(loadP2, warehouseIn);
                pathStringLoad2.append(pathLoad.getStringPath());
                System.out.println("[Load2] Esta é a string: " + pathStringLoad2);
                OPCUA_Connection.setValueString("MAIN_TASK", "C7T7b_order_path_mes", pathStringLoad2.toString());
            }
        }
    }
}
