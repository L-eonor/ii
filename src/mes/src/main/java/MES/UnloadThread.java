package MES;

import java.util.List;

public class UnloadThread implements Runnable {

    //Attributes
    static int[] warehouseOut = {1, 1};
    String actionPush="99";
    List<orderUnload> orderListUnload=Main.orderListUnload;
    SFS floor = Main.floor;


    public UnloadThread() {

    }
    public void run() {
        System.out.println("--------------[Executing] UnloadThread is Running [Executing]--------------");

        while(true) {

            if(!orderListUnload.isEmpty()) {

                StringBuilder pathString = new StringBuilder();
                orderUnload  order= orderListUnload.remove(0);


                //Order attributes
                int orderUnits = order.getQuantity();
                String orderPx = order.getPx();
                String orderDy = order.getDy(); //variável está a null

                for (int a=0; a < orderUnits ; a++) {

                    //Identify and relate Dy with respective position of Slider
                    int[] goal = floor.getUnloadPosition(orderDy);
                    if (goal == null) System.out.println("Error: order machine input not valid. ");

                    //Calculate path to Slider and send via OPC-UA
                    Path_Logic path = new Path_Logic(warehouseOut, goal);
                    pathString.append(path.getStringPath());
                    pathString.append(actionPush);
                    System.out.println("[Unload] Esta é a string: " + pathString);
                    OPCUA_Connection.setValueString("MAIN_TASK", "AT1_order_path_mes", pathString.toString());
                    OPCUA_Connection.setValueInt("MAIN_TASK", "unit_type", unitTypeIdentifier(orderPx));
                    OPCUA_Connection.setValueInt("MAIN_TASK", "UNIT_COUNT_MES", ++Main.unitCount);
                }
            }
        }

    }

    public int unitTypeIdentifier(String Px){
        switch (Px){
            case "P1": return 1;
            case "P2": return 2;
            case "P3": return 3;
            case "P4": return 4;
            case "P5": return 5;
            case "P6": return 6;
            case "P7": return 7;
            case "P8": return 8;
            case "P9": return 9;
            default: return 0;
        }
    }
}
