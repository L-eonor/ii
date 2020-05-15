package MES;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static MES.Main.orderListTransformationOutOfUnits;
import static java.lang.Thread.*;

public class UnloadThread implements Runnable {

    //Attributes
    static int[] warehouseOut = {1, 1};
    static int[] warehouseIn = {0, 7};
    String actionPush = "99";

    TransformationsGraph transformTable = new TransformationsGraph();
    /*
    List<orderTransform> orderListTransformationEnded = Main.orderListTransformationEnded;
    List<orderUnload> orderListUnload = Main.orderListUnload;
     */

    public UnloadThread() {

    }

    public void run() {
        System.out.println("--------------[Executing] UnloadThread is Running [Executing]--------------");
        //Tells TransformationThread to immediately wait

        while (true) {

            //System.out.println(" - 1 : "+ !Main.orderListUnload.isEmpty() +" "+!checkIfWaiting()+" "+ piecesAvailableForOneUnload());
            //System.out.println(" - 2 : "+!Main.ordersPriority.isEmpty()+ " "+ !piecesAvailableForOutofUnits());
            //System.out.println(" - 3 : "+ !orderListTransformationOutOfUnits.isEmpty() +""+ piecesAvailableForOutofUnits());

            if(!Main.orderListUnload.isEmpty() && !checkIfWaiting() && piecesAvailableForOneUnload()) {

                orderUnload order = Main.orderListUnload.remove(0);

                //Order attributes
                int orderUnitsDone = order.getNDone();
                int orderUnitsTotal = order.getQuantity();
                String orderPx = order.getPx();

                //Identify and relate Dy with respective position of Slider
                String orderDy = order.getDy();
                int[] goal = SFS.getUnloadPosition(orderDy);
                if (goal == null) System.out.println("Error: order machine input not valid. ");
                Slider slider = (Slider) SFS.getCell(goal[1],goal[0]);


                if(!slider.isFull() && Warehouse.getPiece(order.getPx()) > 0) {
                    //Mark down start timing
                    if(orderUnitsDone == 0) order.setStartTime(StopWatch.getTimeElapsed());

                    //Update order status to "in progress".
                    if(order.getStatus() != 2) order.setStatus(2);

                    //Calculate path to Slider (every Unload order unit has the same path)
                    StringBuilder pathStringBuilder = new StringBuilder();


                    if(order.getDy().equals("D1")){
                        //System.out.println("Calculating path.......");
                        int[] a={7,1};
                        Path_Logic path = new Path_Logic(warehouseOut, a, "Unload", (orderUnitsTotal - orderUnitsDone));
                        pathStringBuilder.append(path.getStringPath());
                        path = new Path_Logic(a, goal, "Unload", (orderUnitsTotal - orderUnitsDone));
                        pathStringBuilder.append(path.getStringPath());
                    }
                    else if(order.getDy().equals("D2")) {
                        //System.out.println("Calculating path.......");
                        Path_Logic path = new Path_Logic(warehouseOut, goal, "Unload", (orderUnitsTotal - orderUnitsDone));
                        pathStringBuilder.append(path.getStringPath());
                    }
                    else if(order.getDy().equals("D3")){
                        //System.out.println("Calculating path.......");
                        int[] a={6,6};
                        Path_Logic path = new Path_Logic(warehouseOut, a, "Unload", (orderUnitsTotal - orderUnitsDone));
                        pathStringBuilder.append(path.getStringPath());
                        path = new Path_Logic(a, goal, "Unload", (orderUnitsTotal - orderUnitsDone));
                        pathStringBuilder.append(path.getStringPath());
                    }

                    //Adds "99" action to the pusher
                    String pathString = pathStringBuilder.toString().replaceFirst(".{2}$", actionPush);

                    // Adds order info to the end
                    pathString += order.getPx() + "2" + order.getId();

                    System.out.println("[Unload] Esta é a string: " + pathString);

                    for (int a = orderUnitsDone; a < orderUnitsTotal; a++) {
                        //System.out.println(" # # # # # # # # # # # # ");

                        //Condition to verify when Slider is full or no more units are available to send
                        if (slider.isFull() || (Warehouse.getPiece(order.getPx()) <= 0)) {
                            order.setStatus(2);
                            Main.orderListUnload.add(order);
                            break;
                        }

                        //Sends information to OPC-UA
                        sendPathToOPC(unitTypeIdentifier(orderPx), pathString);

                        //Updates Unload order information
                        orderUnitsDone++;
                        order.setNDone(orderUnitsDone);

                        //System.out.println(" # # # # # # # # # # # # ");

                    }

                }
                else {
                    Main.orderListUnload.add(order); //adds order to top of Unload List
                }

                //Checks if order is done and updates Order
                if(order.getQuantity() == order.getNDone()) {
                    order.setStatus(3); //Set order status to "All pieces sent".
                    Main.orderListUnloadEnded.add(order);
                }

            }
            else if (!orderListTransformationOutOfUnits.isEmpty() && piecesAvailableForOutofUnits()) {

                orderTransform order = orderListTransformationOutOfUnits.remove(0);

                //Order attributes
                int orderUnitsDone = order.getNDone();
                int orderUnitsTotal = order.getNTotal();
                String orderPx = order.getPx();
                String orderPy = order.getPy();

                if(Warehouse.getPiece(orderPx) > 0) {
                    while (orderUnitsDone <= orderUnitsTotal) {

                        //Verifies if orderUnitsDone = orderUnitsTotal and if yes, polls and updates Order
                        if (order.getNDone() == order.getNTotal()) {
                            order.setStatus(3); //Set order status to "All pieces sent".
                            //System.out.println(order);
                            Main.orderListTransformationEnded.add(order);
                            break;
                        }

                        //Verifies if it's out of units
                        if(Warehouse.getPiece(orderPx) <= 0){
                            order.setStatus(2); //Set order status to "All pieces sent".
                            orderListTransformationOutOfUnits.add(order);
                            break;
                        }

                        //Verifies if an Unload Order came in
                        if (!Main.orderListUnload.isEmpty() && !checkIfWaiting() && piecesAvailableForOneUnload()) {
                            order.setStatus(2); //Set order status to "in pause".
                            orderListTransformationOutOfUnits.add(order);
                            break;
                        }

                        //System.out.println(" # # # # # # # # # # # # ");

                        if (orderUnitsDone == 0) order.setStartTime(StopWatch.getTimeElapsed()); //Set order start Time
                        if (order.getStatus() != 2) order.setStatus(2); //Set order status to "in progress".

                        //Finds transformations to do
                        if (transformTable.searchTransformations(orderPx, orderPy)) {
                            //System.out.println("Searched transformations. Found " + transformTable.solutions.size() + " solutions.");
                        } else System.out.println(" No need for transformations. ");

                        //String with the whole path of the Transformation order
                        StringBuilder pathString = new StringBuilder();

                        //Retrieves transformations with lowest time cost
                        GraphSolution transformationResult = transformTable.solutions.poll();
                        if (transformationResult == null)
                            throw new AssertionError("Error: transformationResult null pointer.");


                        //Starts finding path according to transformations
                        Machine machineToGo;
                        int[] startTransformation = warehouseOut;
                        int[] goalTransformation = {0, 0};
                        String previousMachine = "";

                        for (int j = 0; j < transformationResult.transformations.size(); j++) {

                            //Identify machine
                            String machineNow = transformationResult.machines.get(j);

                            //Relate machine type with respective position
                            if (!previousMachine.equals(machineNow)) {
                                machineToGo = SFS.getMachineToSendPiece(machineNow);
                                goalTransformation = reverseArray(machineToGo.getPosition());
                                if (goalTransformation == null)
                                    System.out.println("Error: order machine input not valid. ");
                                else {
                                    machineToGo.addWeight(1);
                                }

                                //Calculate path to Machine
                                Path_Logic path = new Path_Logic(startTransformation, goalTransformation, "Transformation");
                                pathString.append(path.getStringPath());
                                previousMachine = machineNow;
                            } else {
                                String machinePositionString = String.valueOf(goalTransformation[0]) + String.valueOf(goalTransformation[1]);
                                pathString.append(machinePositionString);
                            }

                            //Add Tool and Time to string respectively
                            pathString.append(transformationResult.tool.get(j)).append(transformationResult.timeCost.get(j));
                            startTransformation = goalTransformation;
                        }

                        //Path to Warehouse In cell
                        Path_Logic pathEnd = new Path_Logic(startTransformation, warehouseIn, "Transformation");
                        pathString.append(pathEnd.getStringPath());
                        // Adds order info to the end
                        String orderInfo = order.getPy() + "1" + order.getId();
                        pathString.append(orderInfo);

                        System.out.println("[Transformation] Esta é a string: " + pathString);


                        //Sends information to OPC-UA
                        sendPathToOPC(unitTypeIdentifier(orderPx), pathString.toString());

                        //Updates order information
                        orderUnitsDone++;
                        order.setNDone(orderUnitsDone);

                        //System.out.println(" # # # # # # # # # # # # ");
                        //System.out.println();

                    }
                }
                else {
                    order.setStatus(3); //Set order status to "All pieces sent".
                    orderListTransformationOutOfUnits.add(order);
                }

            }
            else if (!Main.ordersPriority.isEmpty()) {

                orderTransform order = Main.ordersPriority.peek();
                int count=1;

                //Order attributes
                int orderUnitsDone = order.getNDone();
                int orderUnitsTotal = order.getNTotal();
                String orderPx = order.getPx();
                String orderPy = order.getPy();

                if(Warehouse.getPiece(orderPx) > 0) {
                    while (orderUnitsDone <= orderUnitsTotal) {

                        //Verifies if orderUnitsDone = orderUnitsTotal and if yes, polls and updates Order
                        if (order.getNDone() == order.getNTotal()) {
                            order.setStatus(3); //Set order status to "All pieces sent".
                            //System.out.println(order);
                            Main.orderListTransformationEnded.add(Main.ordersPriority.poll());
                            break;
                        }

                        //Verifies if it's out of units
                        if(Warehouse.getPiece(orderPx) <= 0){
                            order.setStatus(3); //Set order status to "All pieces sent".
                            orderListTransformationOutOfUnits.add(Main.ordersPriority.poll());
                            break;
                        }

                        //Verifies if an Unload Order came in
                        if (!Main.orderListUnload.isEmpty() && !checkIfWaiting() && piecesAvailableForOneUnload()) {
                            order.setStatus(2); //Set order status to "in pause".
                            break;
                        }

                        //Verifies if a Transform Order order with higher priority came in
                        if (Main.ordersPriority.peek() != order) {
                            order.setStatus(2); //Set order status to "in pause".
                            break;
                        }

                        if (orderUnitsDone == 0) order.setStartTime(StopWatch.getTimeElapsed()); //Set order start Time
                        if (order.getStatus() != 2) order.setStatus(2); //Set order status to "in progress".

                        //Finds transformations to do
                        if (transformTable.searchTransformations(orderPx, orderPy)) {
                        /* prints all transformations
                        Iterator value = transformTable.solutions.iterator();
                        while(value.hasNext()) {
                            System.out.println(transformTable.solutions.poll());
                        }*/
                        } else System.out.println(" No need for transformations. ");

                        //String with the whole path of the Transformation order
                        StringBuilder pathString = new StringBuilder();

                        //Retrieves transformations with lowest time cost
                        GraphSolution transformationResult = transformTable.solutions.poll();
                        if (transformationResult == null)
                            throw new AssertionError("Error: transformationResult null pointer.");


                        //Starts finding path according to transformations
                        Machine machineToGo;
                        int[] startTransformation = warehouseOut;
                        int[] goalTransformation = {0, 0};
                        String previousMachine = "";

                        String aux="";
                        int sameMachine=1;
                        int lastSameMachine=0;

                        for(int a=0; a < transformationResult.transformations.size(); a++) {

                            String machineNow = transformationResult.machines.get(a);
                            if (aux.equals(machineNow)) {
                                sameMachine++;
                                lastSameMachine=a;
                            }
                            aux=machineNow;
                        }

                        for (int j = 0; j < transformationResult.transformations.size(); j++) {

                            if(sameMachine == 1) {
                                if(count==1){
                                    if(transformationResult.machines.get(j).equals("Ma")) {
                                        int[] p={1,3};
                                        Path_Logic count1 = new Path_Logic(startTransformation, p, "Transformation");
                                        pathString.append(count1.getStringPath());
                                        goalTransformation=cloneArray(p);
                                    }
                                    else if(transformationResult.machines.get(j).equals("Mb")){
                                        int[] p={1,4};
                                        Path_Logic count2 = new Path_Logic(startTransformation, p, "Transformation");
                                        pathString.append(count2.getStringPath());
                                        goalTransformation=cloneArray(p);
                                    }
                                    else if(transformationResult.machines.get(j).equals("Mc")){
                                        int[] p={1,5};
                                        Path_Logic count3 = new Path_Logic(startTransformation, p, "Transformation");
                                        pathString.append(count3.getStringPath());
                                        goalTransformation=cloneArray(p);
                                    }
                                }
                                else if(count == 2){
                                    if(transformationResult.machines.get(j).equals("Ma")) {
                                        int[] p={3,3};
                                        Path_Logic count1 = new Path_Logic(startTransformation, p, "Transformation");
                                        pathString.append(count1.getStringPath());
                                        goalTransformation=cloneArray(p);
                                    }
                                    else if(transformationResult.machines.get(j).equals("Mb")){
                                        int[] p={3,4};
                                        Path_Logic count2 = new Path_Logic(startTransformation, p, "Transformation");
                                        pathString.append(count2.getStringPath());
                                        goalTransformation=cloneArray(p);

                                    }
                                    else if(transformationResult.machines.get(j).equals("Mc")){
                                        int[] p={3,5};
                                        Path_Logic count3 = new Path_Logic(startTransformation, p, "Transformation");
                                        pathString.append(count3.getStringPath());
                                        goalTransformation=cloneArray(p);
                                    }
                                }
                                else if (count == 3) {
                                    if(transformationResult.machines.get(j).equals("Ma")) {
                                        int[] p={5,3};
                                        Path_Logic count1 = new Path_Logic(startTransformation, p, "Transformation");
                                        pathString.append(count1.getStringPath());
                                        goalTransformation=cloneArray(p);
                                    }
                                    else if(transformationResult.machines.get(j).equals("Mb")){
                                        int[] p={5,4};
                                        Path_Logic count2 = new Path_Logic(startTransformation, p, "Transformation");
                                        pathString.append(count2.getStringPath());
                                        goalTransformation=cloneArray(p);

                                    }
                                    else if(transformationResult.machines.get(j).equals("Mc")){
                                        int[] p={5,5};
                                        Path_Logic count3 = new Path_Logic(startTransformation, p, "Transformation");
                                        pathString.append(count3.getStringPath());
                                        goalTransformation=cloneArray(p);
                                    }

                                }

                            }
                            else if(sameMachine == 2){
                                if(count==1 || count == 3){
                                    if(transformationResult.machines.get(j).equals("Ma")) {
                                        int[] p={1,3};
                                        if(lastSameMachine == j) {p[0]=3;}
                                        Path_Logic count1 = new Path_Logic(startTransformation, p, "Transformation");
                                        pathString.append(count1.getStringPath());
                                        goalTransformation=cloneArray(p);
                                    }
                                    else if(transformationResult.machines.get(j).equals("Mb")){
                                        int[] p={1,4};
                                        if(lastSameMachine == j) {p[0]=3;}
                                        Path_Logic count2 = new Path_Logic(startTransformation, p, "Transformation");
                                        pathString.append(count2.getStringPath());
                                        goalTransformation=cloneArray(p);
                                    }
                                    else if(transformationResult.machines.get(j).equals("Mc")){
                                        int[] p={1,5};
                                        if(lastSameMachine == j) {p[0]=3;}
                                        Path_Logic count3 = new Path_Logic(startTransformation, p, "Transformation");
                                        pathString.append(count3.getStringPath());
                                        goalTransformation=cloneArray(p);
                                    }
                                }
                                else if (count == 2) {
                                    if(transformationResult.machines.get(j).equals("Ma")) {
                                        int[] p={5,3};
                                        if(lastSameMachine == j) {p[0]=3;}
                                        Path_Logic count1 = new Path_Logic(startTransformation, p, "Transformation");
                                        pathString.append(count1.getStringPath());
                                        goalTransformation=cloneArray(p);
                                    }
                                    else if(transformationResult.machines.get(j).equals("Mb")){
                                        int[] p={5,4};
                                        if(lastSameMachine == j) {p[0]=3;}
                                        Path_Logic count2 = new Path_Logic(startTransformation, p, "Transformation");
                                        pathString.append(count2.getStringPath());
                                        goalTransformation=cloneArray(p);

                                    }
                                    else if(transformationResult.machines.get(j).equals("Mc")){
                                        int[] p={5,5};
                                        if(lastSameMachine == j) {p[0]=3;p[1]=3;}
                                        Path_Logic count3 = new Path_Logic(startTransformation, p, "Transformation");
                                        pathString.append(count3.getStringPath());
                                        goalTransformation=cloneArray(p);
                                    }
                                }

                            }
                            else if(sameMachine == 3){
                                if(transformationResult.machines.get(j).equals("Ma")) {
                                    int[] p={1,3};
                                    if(j==1) p[0]=3;
                                    if(j==2) p[0]=5;
                                    Path_Logic count1 = new Path_Logic(startTransformation, p, "Transformation");
                                    pathString.append(count1.getStringPath());
                                    goalTransformation=cloneArray(p);
                                }
                                else if(transformationResult.machines.get(j).equals("Mb")){
                                    int[] p={1,4};
                                    if(j==1) p[0]=3;
                                    if(j==2) p[0]=5;
                                    Path_Logic count2 = new Path_Logic(startTransformation, p, "Transformation");
                                    pathString.append(count2.getStringPath());
                                    goalTransformation=cloneArray(p);
                                }
                                else if(transformationResult.machines.get(j).equals("Mc")){
                                    int[] p={1,5};
                                    if(j==1) p[0]=3;
                                    if(j==2) p[0]=5;
                                    Path_Logic count3 = new Path_Logic(startTransformation, p, "Transformation");
                                    pathString.append(count3.getStringPath());
                                    goalTransformation=cloneArray(p);
                                }

                            }

                            //Identify machine
                            //String machineNow = transformationResult.machines.get(j);
                            /*
                            //Relate machine type with respective position
                            if (!previousMachine.equals(machineNow)) {
                                machineToGo = SFS.getMachineToSendPiece(machineNow);
                                goalTransformation = reverseArray(machineToGo.getPosition());
                                if (goalTransformation == null)
                                    System.out.println("Error: order machine input not valid. ");
                                else {
                                    machineToGo.addWeight(1);
                                }

                                //Calculate path to Machine
                                Path_Logic path = new Path_Logic(startTransformation, goalTransformation, "Transformation");
                                pathString.append(path.getStringPath());
                                previousMachine = machineNow;
                            }
                            else {
                                String machinePositionString = String.valueOf(goalTransformation[0]) + String.valueOf(goalTransformation[1]);
                                pathString.append(machinePositionString);
                            }

                             */


                            //Add Tool and Time to string respectively
                            pathString.append(transformationResult.tool.get(j)).append(transformationResult.timeCost.get(j));
                            startTransformation = cloneArray(goalTransformation);
                        }

                        //Path to Warehouse In cell
                        Path_Logic pathEnd = new Path_Logic(startTransformation, warehouseIn, "Transformation");
                        pathString.append(pathEnd.getStringPath());
                        // Adds order info to the end
                        String orderInfo = order.getPy() + "1" + order.getId();
                        pathString.append(orderInfo);

                        System.out.println("[Transformation] Esta é a string: " + pathString);


                        //Sends information to OPC-UA
                        sendPathToOPC(unitTypeIdentifier(orderPx), pathString.toString());

                        //Updates order information
                        orderUnitsDone++;
                        order.setNDone(orderUnitsDone);

                        count++;
                        if(count == 4) count = 1;

                    }
                }
                else {
                    order.setStatus(3); //Set order status to "All pieces sent".
                    orderListTransformationOutOfUnits.add(Main.ordersPriority.poll());
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

            if(aux == 1 && !SFS.getCell(1,0).getUnitPresence()) {
                OPCUA_Connection.setValueInt("MAIN_TASK", "UNIT_COUNT_AT1", ++Main.unitCount);
                aux++;
            }
            if(aux == 2 && OPCUA_Connection.getValueInt("MAIN_TASK", "UNIT_COUNT_AT1") == Main.unitCount){
                aux++;
            }
            if(aux == 3 && SFS.getCell(1,0).getUnitPresence()) break;

        }

    }

    private boolean checkIfWaiting(){
        boolean waiting=true;
        for(int i=0; i < Main.orderListUnload.size(); i++){
            int[] position = SFS.getUnloadPosition(Main.orderListUnload.get(i).getDy());
            Slider slider = (Slider) SFS.getCell(position[1], position[0]);
            if(!slider.isFull()) {
                waiting=false;
                break;
            }
        }
        return waiting;
    }

    private boolean piecesAvailableForOneUnload(){
        boolean available=false;
        for(int i=0; i < Main.orderListUnload.size(); i++){
            String type = Main.orderListUnload.get(i).getPx();
            int nPieces =Warehouse.getPiece(type);
            if(nPieces >= (Main.orderListUnload.get(i).getQuantity() - Main.orderListUnload.get(i).getNDone())) {
                available=true;
                break;
            }
        }
        return available;
    }

    private boolean piecesAvailableForOutofUnits(){
        boolean available=false;
        for(int i=0; i < orderListTransformationOutOfUnits.size(); i++){
            String type = orderListTransformationOutOfUnits.get(i).getPx();
            int nPieces=Warehouse.getPiece(type);
            if(nPieces >= (orderListTransformationOutOfUnits.get(i).getNTotal()-orderListTransformationOutOfUnits.get(i).getNDone())){
                available=true;
                break;
            }
        }
        return available;
    }

    private int[] cloneArray(int[] a) {
        int[] b = new int[a.length];
        for (int i = 0; i < a.length; i++) {
            b[i] = a[i];
        }
        return b;
    }
}
