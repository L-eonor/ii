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
                int orderMaxDelay = order.getMaxDelay();
                int orderUnits = order.getQuantity();
                String orderPx = order.getPx();
                String orderDy = order.getDy(); //variável está a null

                //Identify and relate Dy with respective position of Slider
                int[] goal = floor.getUnloadPosition(orderDy);
                if(goal == null) System.out.println("Error: order machine input not valid. ");
                //Calculate path to Slider
                Path_Logic path = new Path_Logic(warehouseOut, goal);
                pathString.append(path.getStringPath());
                pathString.append(actionPush);
                System.out.println("[Unload] Esta é a string: " + pathString);
                OPCUA_Connection.setValueString("MAIN_TASK", "AT1_order_path_mes", pathString.toString());

            }
        }

    }
}
