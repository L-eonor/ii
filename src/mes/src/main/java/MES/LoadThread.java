package MES;

public class LoadThread implements Runnable {

    //Attributes
    static int loadOrderCount;
    static int[] warehouseIn = {0, 7};
    static int[] loadP1 = {7,1};
    static int[] loadP2 = {7,7};
    static orderLoad orderLoad1;
    static orderLoad orderLoad2;


    public LoadThread() {

    }

    public void run() {
        System.out.println("--------------[Executing] LoadThread is Running [Executing]--------------");

        //Get loadOrderCount from dataBase if possible otherwise set as 0
        loadOrderCount=0;
        int ID;
        int aux1=1;
        int aux2=1;

        while(true) {

            StringBuilder pathStringLoad1 = new StringBuilder();
            StringBuilder pathStringLoad2 = new StringBuilder();
            boolean C7T1b = SFS.getCell(1,8).getUnitPresence();
            boolean C7T7b = SFS.getCell(7,8).getUnitPresence();
            //System.out.println("Peça: " + SFS.getCell(1,8).getUnitPresence() + " Aux: " + aux1 );

            if(C7T1b && aux1 == 1) {

                loadOrderCount++;
                ID=loadOrderCount;
                //createOrder
                orderLoad1 = new orderLoad(ID,StopWatch.getTimeElapsed(),3, 1,0, "P1");
                //Calculates path
                Path_Logic pathLoad = new Path_Logic(loadP1, warehouseIn, "Load");
                pathStringLoad1.append(pathLoad.getStringPath());

                // Adds order info to the end
                String orderInfo = "P13" + (loadOrderCount);
                pathStringLoad1.append(orderInfo);

                System.out.println("[Load1] Esta é a string: " + pathStringLoad1);

                //Sends via OPC-UA
                sendPathToOPCLoad(pathStringLoad1.toString(),1);
                aux1++;
            }
            if(!SFS.getCell(1,8).getUnitPresence() && aux1 == 2) {
                orderLoad1.setStartTime(StopWatch.getTimeElapsed());
                orderLoad1.setStatus(3);
                Main.orderListLoad.add(orderLoad1);
                aux1=1;
            }

            if(C7T7b && aux2 == 1) {

                loadOrderCount++;
                ID=loadOrderCount;

                //createOrder
                orderLoad2 = new orderLoad(ID,StopWatch.getTimeElapsed(),3, 1,0, "P2");


                //Calculates path
                Path_Logic pathLoad = new Path_Logic(loadP2, warehouseIn, "Load");
                pathStringLoad2.append(pathLoad.getStringPath());

                // Adds order info to the end
                String orderInfo = "P23" + (loadOrderCount);
                pathStringLoad2.append(orderInfo);

                System.out.println("[Load2] Esta é a string: " + pathStringLoad2);

                //Sends via OPC-UA
                sendPathToOPCLoad(pathStringLoad2.toString(),2);
                aux2++;
            }
            if(!SFS.getCell(7,8).getUnitPresence() && aux2 == 2) {
                orderLoad2.setStartTime(StopWatch.getTimeElapsed());
                orderLoad2.setStatus(3);
                Main.orderListLoad.add(orderLoad2);
                aux2=1;
            }
        }
    }

    private void sendPathToOPCLoad(String path,int n){
        String VarCellName="";
        String VarUCountName="";
        int [] cell = new int[2];

        if(n == 1) {
            VarCellName = "C7T1b_order_path_mes" ;
            VarUCountName = "UNIT_COUNT_C7T1B";
            cell[0]=1;
            cell[1]=8;

        }
        if(n == 2) {
            VarCellName = "C7T7b_order_path_mes";
            VarUCountName = "UNIT_COUNT_C7T7b";
            cell[0]=7;
            cell[1]=8;
        }

        //Sends information to OPC-UA
        OPCUA_Connection.setValueString("MAIN_TASK", VarCellName, path);

        int aux = 1;
        while (true){
            if(aux == 1 && SFS.getCell(cell[0],cell[1]).getUnitPresence()) {
                OPCUA_Connection.setValueInt("MAIN_TASK", VarUCountName, ++Main.unitCount);
                aux++;
            }
            if(aux == 2 && OPCUA_Connection.getValueInt("MAIN_TASK", VarUCountName) == Main.unitCount){
                break;
            }
        }
    }

}
