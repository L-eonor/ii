package MES;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static MES.Main.*;
import static java.lang.Thread.*;

public class UnloadThread implements Runnable {

    //Attributes
    static int[] warehouseOut = {1, 1};
    static int[] warehouseIn = {0, 7};
    public static List<orderUnload> orderListUnloadInterrupted = Collections.synchronizedList(new ArrayList<>());
    String actionPush = "99";

    TransformationsGraph transformTable = new TransformationsGraph();
    /*
    List<orderTransform> orderListTransformationEnded = Main.orderListTransformationEnded;
    List<orderUnload> orderListUnload = Main.orderListUnload;
     */

    SFS floor = Main.floor;


    public UnloadThread() {

    }

    public void run() {
        System.out.println("--------------[Executing] UnloadThread is Running [Executing]--------------");
        //Tells TransformationThread to immediately wait

        while (true) {

            if (!orderListUnload.isEmpty()) {

                orderUnload order = orderListUnload.remove(0);

                //Order attributes
                int orderUnitsDone = order.getNDone();
                int orderUnitsTotal = order.getQuantity();
                String orderPx = order.getPx();

                //Identify and relate Dy with respective position of Slider
                String orderDy = order.getDy();
                int[] goal = floor.getUnloadPosition(orderDy);
                if (goal == null) System.out.println("Error: order machine input not valid. ");
                Slider slider = (Slider) floor.getCell(goal[1],goal[0]);

                if(!slider.isFull()) {

                    //Calculate path to Slider (every Unload order unit has the same path)
                    StringBuilder pathStringBuilder = new StringBuilder();
                    Path_Logic path = new Path_Logic(warehouseOut, goal, (orderUnitsTotal-orderUnitsDone));
                    pathStringBuilder.append(path.getStringPath());
                    String pathString = pathStringBuilder.toString().replaceFirst(".{2}$", actionPush);
                    System.out.println("[Unload] Esta é a string: " + pathString);

                    for (int a = 0; a < orderUnitsTotal; a++) {
                        System.out.println(" # # # # # # # # # # # # ");

                        //Sends information to OPC-UA
                        sendPathToOPC(unitTypeIdentifier(orderPx), pathString);

                        //Updates Unload order information
                        orderUnitsDone++;
                        order.setNDone(orderUnitsDone);

                        //Condition to verify when Slider is full
                        if (slider.isFull()) {
                            orderListUnload.add(order);
                            break;
                        }

                        System.out.println(" # # # # # # # # # # # # ");

                    }
                }
                else orderListUnload.add(order); //adds order to top of Unload List

                //Checks if order is done
                if(order.getQuantity() == order.getNDone()) orderListUnloadEnded.add(order);

            }
            else if (!ordersPriority.isEmpty()) {

                orderTransform order = ordersPriority.peek();
                //Order attributes
                int orderUnitsDone = order.getNDone();
                int orderUnitsTotal = order.getNTotal();
                String orderPx = order.getPx();
                String orderPy = order.getPy();

                System.out.println("[ORDER TRANSFORM] END TIME: "+order.getEndTime());

                while(orderUnitsDone <= orderUnitsTotal) {
                    //Verifies if an Unload Order came in
                    if (!orderListUnload.isEmpty()) break;
                    //Verifies if orderUnitsDone = orderUnitsTotal and if yes, polls
                    if(order.getNDone() == order.getNTotal()) {
                        orderListTransformationEnded.add(ordersPriority.poll());
                        break;
                    }
                    //Verifies if a Transform Order order with higher priority came in
                    if(ordersPriority.peek() != order) break;

                    System.out.println(" # # # # # # # # # # # # ");

                    if (transformTable.searchTransformations(orderPx, orderPy)) {
                        System.out.println("Searched transformations. Found " + transformTable.solutions.size() + " solutions.");
                        /* prints all transformations
                        Iterator value = transformTable.solutions.iterator();
                        while(value.hasNext()) {
                            System.out.println(transformTable.solutions.poll());
                        }*/

                    } else System.out.println(" No need for transformations. ");

                    //String with the whole path of the Transformation order
                    StringBuilder pathString = new StringBuilder();

                    //Finds transformations to do
                    GraphSolution transformationResult = transformTable.solutions.poll();
                    if (transformationResult == null)
                        throw new AssertionError("Error: transformationResult null pointer. ");


                    //Starts finding path according to transformations
                    int[] startTransformation = warehouseOut;
                    for (int j = 0; j < transformationResult.transformations.size(); j++) {
                        //Identify machine
                        String machine = transformationResult.machines.get(j);
                        //Relate machine type with respective position
                        Machine machineToGo = floor.getMachineToSendPiece(machine);
                        int[] goal = reverseArray(machineToGo.getPosition());
                        if (goal == null) System.out.println("Error: order machine input not valid. ");
                        else {
                            machineToGo.addWeight(1);
                        }
                        //Calculate path to Machine
                        Path_Logic path = new Path_Logic(startTransformation, goal);
                        pathString.append(path.getStringPath());
                        //Add Tool and Time to string respectively
                        pathString.append(transformationResult.tool.get(j)).append(transformationResult.timeCost.get(j));
                        startTransformation = goal;
                    }

                    //Path to Warehouse In cell
                    Path_Logic pathEnd = new Path_Logic(startTransformation, warehouseIn);
                    pathString.append(pathEnd.getStringPath());
                    System.out.println("[Transformation] Esta é a string: " + pathString);


                    //Sends information to OPC-UA
                    sendPathToOPC(unitTypeIdentifier(orderPx), pathString.toString());

                    //Updates order information
                    orderUnitsDone++;
                    order.setNDone(orderUnitsDone);

                    System.out.println(" # # # # # # # # # # # # ");
                    System.out.println();

                }

            }

        }

    }

    public int unitTypeIdentifier(String Px) {
        switch (Px) {
            case "P1":
                return 1;
            case "P2":
                return 2;
            case "P3":
                return 3;
            case "P4":
                return 4;
            case "P5":
                return 5;
            case "P6":
                return 6;
            case "P7":
                return 7;
            case "P8":
                return 8;
            case "P9":
                return 9;
            default:
                return 0;
        }
    }

    private int[] reverseArray(int[] a) {
        int n = a.length;
        int[] b = new int[n];
        int j = n;
        for (int i = 0; i < n; i++) {
            b[j - 1] = a[i];
            j = j - 1;
        }
        return b;
    }

    private void sendPathToOPC(int unitType, String path){
        //Sends information to OPC-UA
        OPCUA_Connection.setValueInt("MAIN_TASK", "unit_type", unitType);
        OPCUA_Connection.setValueString("MAIN_TASK", "AT1_order_path_mes", path);

        int aux = 1;
        while (true){
            if(aux == 1 && !floor.getCell(1,0).unitPresence) {
                OPCUA_Connection.setValueInt("MAIN_TASK", "UNIT_COUNT_AT1", ++Main.unitCount);
                aux++;
            }
            if(aux == 2 && OPCUA_Connection.getValueInt("MAIN_TASK", "UNIT_COUNT_AT1") == Main.unitCount){
                aux++;
            }
            if(aux == 3 && floor.getCell(1,0).unitPresence) break;

        }

    }
}
