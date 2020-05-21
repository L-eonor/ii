package MES;

import javax.sound.midi.Soundbank;
import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import static MES.Main.orderListTransformationOutOfUnits;
import static MES.Main.ordersPriority;
import static java.lang.Thread.*;

public class UnloadThread implements Runnable {

    //Attributes
    static boolean bigFlagP1P9Init=true;
    static boolean bigFlagP1P9End=false;
    static int  countCollumns=1;
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
                                    machineToGo.addWeight();
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

                //Order attributes
                int orderUnitsDone = order.getNDone();
                int orderUnitsTotal = order.getNTotal();
                String orderPx = order.getPx();
                String orderPy = order.getPy();
                int orderUnitsToDo= orderUnitsTotal-orderUnitsDone;
                int orderCountAux=1;

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

                        String pathString;
    /*


                        //Finds transformations to do
                        if (transformTable.searchTransformations(orderPx, orderPy)) {
                        /* prints all transformations
                        Iterator value = transformTable.solutions.iterator();
                        while(value.hasNext()) {
                            System.out.println(transformTable.solutions.poll());
                        }
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
    */
                        while(bigFlagP1P9End){
                            boolean mA=SFS.getCell(3,1).getUnitPresence();
                            boolean mB=SFS.getCell(4,1).getUnitPresence();
                            boolean mC=SFS.getCell(5,1).getUnitPresence();
                            boolean rA=SFS.getCell(3,2).getUnitPresence();
                            boolean rB=SFS.getCell(4,2).getUnitPresence();
                            boolean rC=SFS.getCell(5,2).getUnitPresence();

                            if(!mA && !rA && !mB && !rB && !mC && !rC){
                                System.out.println("Não resttaaaaaaaa nadaaaa.");
                                bigFlagP1P9End=false;
                            }
                        }


                        pathString=getPathByTransformation(orderPx,orderPy,orderUnitsToDo,orderCountAux,false);
                        //Sends information to OPC-UA
                        sendPathToOPC(unitTypeIdentifier(orderPx), pathString);
                        orderCountAux++;
                        //Updates order information
                        orderUnitsDone++;
                        order.setNDone(orderUnitsDone);
                        System.out.println(order);


                        //Iterate to check if the transformations can be done at the same time
                        Iterator valueQueue = ordersPriority.iterator();

                        // Displaying the values after iterating through the queue
                        //System.out.println("The iterator values are: ");
                        int i=1;
                        boolean flagA=false, flagB=false,flagC=false;
                        while(valueQueue.hasNext()) {
                            if(i==1){
                                valueQueue.next();
                                i++;
                                continue;
                            }

                            orderTransform orderComp;
                            orderComp = (orderTransform) valueQueue.next();
                            // If it's been done, keep looking
                            if(orderComp.getNDone()==orderComp.getNTotal()) continue;

                            String transf = isCompatible(orderPx,orderPy,orderComp.getPx(),orderComp.getPy());
                            if(transf.equals("12") && !flagB){
                                flagB = true;
                                System.out.println("Px: " + orderComp.getPx() + " Py: " + orderComp.getPy());
                                pathString = getPathByTransformation(orderComp.getPx(), orderComp.getPy(), 1, 1, true);
                                //Sends information to OPC-UA
                                sendPathToOPC(unitTypeIdentifier(orderComp.getPx()), pathString);
                                System.out.println("[Transformation Máq B] Esta é a string: " + pathString);
                                //Updates order information
                                orderComp.setNDone(orderComp.getNDone() + 1);
                                System.out.println(orderComp);
                            }
                            if(transf.equals("13") && !flagC){
                                flagC = true;
                                System.out.println("Px: " + orderComp.getPx() + " Py: " + orderComp.getPy());
                                pathString = getPathByTransformation(orderComp.getPx(), orderComp.getPy(), 1, 1, true);
                                //Sends information to OPC-UA
                                sendPathToOPC(unitTypeIdentifier(orderComp.getPx()), pathString);
                                //Updates order information
                                orderComp.setNDone(orderComp.getNDone() + 1);
                                System.out.println(orderComp);
                            }

                            //Checks if order is done
                            if(orderComp.getNTotal()==orderComp.getNDone()){
                                orderComp.setStatus(3);
                                Main.orderListTransformationEnded.add(orderComp);
                            }

                        }

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
        //System.out.println(path);
        int aux = 1;
        while (true){
            if(aux == 1 && !SFS.getCell(1,0).getUnitPresence()) {
                Main.unitCount++;
                OPCUA_Connection.setValueInt("MAIN_TASK", "UNIT_COUNT_AT1", Main.unitCount);
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

    private String getPathByTransformation(String orderPx,String orderPy, int orderUnitsToDo,int orderCountAux, boolean parallel) {
        String path="";
        int countCollumnsInitial=countCollumns;

        if(orderPx.equals("P1")) {

            switch (orderPy) {

                case "P2":
                    if (countCollumns==1){
                        path = "21314151616263531156364656667574737271707P2"; //Ma3

                        if(orderCountAux==orderUnitsToDo || orderCountAux%2 == 0){
                            countCollumns++;
                        }
                    }
                    else if(countCollumns == 2){
                        path = "213141424333115434445464737271707P2"; //Ma2

                        if(orderCountAux==orderUnitsToDo || orderCountAux%2== 0){
                            countCollumns++;
                        }
                    }
                    else if(countCollumns == 3){
                        path = "2122231311523242526271707P2"; //Ma1

                        if(orderCountAux==orderUnitsToDo || orderCountAux%2== 0){
                            countCollumns=1;
                        }
                    }
                    break;

                case "P3":
                    if (countCollumns==1){
                        if (orderCountAux%3 == 1) {
                            path = "21314151616263645412064656667574737271707P3"; //Mb3
                        }
                        else if (orderCountAux%3 == 2) {
                            path = "2131415161626353115531156364656667574737271707P3"; //Ma3
                        }
                        else if (orderCountAux%3 == 0) {
                            path = "21314151616263645412064656667574737271707P3"; //Mb3
                        }
                        if(orderCountAux==orderUnitsToDo || orderCountAux%3== 0){
                            countCollumns++;
                        }
                    }
                    else if(countCollumns == 2){
                        if (orderCountAux%3 == 1) {
                            path="213141424344341204445464737271707P3";//Mb2
                        }
                        else if (orderCountAux%3 == 2) {
                            path = "21314142433311533115434445464737271707P3";//Ma2
                        }
                        else if (orderCountAux%3 == 0) {
                            path="213141424344341204445464737271707P3";//Mb2
                        }
                        if(orderCountAux==orderUnitsToDo || orderCountAux%3== 0){
                            countCollumns++;
                        }
                    }
                    else if(countCollumns == 3){
                        if (orderCountAux%3 == 1) {
                            path="2122232414120242526271707P3";
                        }
                        else if (orderCountAux%3 == 2) {
                            path="212223131151311523242526271707P3";//Ma1
                        }
                        else if (orderCountAux%3 == 0) {
                            path="2122232414120242526271707P3";
                        }
                        if(orderCountAux==orderUnitsToDo || orderCountAux%3== 0){
                            countCollumns=1;
                        }
                    }
                    break;

                case "P4":
                    if(orderUnitsToDo <= 3){
                        if (countCollumns==1) {
                            path = "21314151616263646555110656667574737271707P4"; //Mc3
                            if(orderCountAux==orderUnitsToDo){
                                countCollumns++;
                            }
                        }
                        else if (countCollumns==2) {
                            path = "213141424344453511045464737271707P4"; //Mc2
                            if(orderCountAux==orderUnitsToDo){
                                countCollumns++;
                            }
                        }
                        else if (countCollumns==3) {
                            path = "2122232425151102526271707P4"; //Mc1
                            if(orderCountAux==orderUnitsToDo){
                                countCollumns=1;
                            }
                        }

                    }
                    else {
                        if (countCollumns==1){
                            if (orderCountAux%5 == 1) {
                                path = "21314151616263646555110656667574737271707P4"; //Mc3
                            }
                            else if (orderCountAux%5 == 2) {
                                path = "2131415161626364541205411564656667574737271707P4"; //Mb3
                            }
                            else if (orderCountAux%5 == 3) {
                                path = "21314151616263646555110656667574737271707P4"; //Mc3
                            }
                            else if (orderCountAux%5 == 4) {
                                path = "21314151616263646555110656667574737271707P4"; //Mc3
                            }
                            else if (orderCountAux%5 == 0) {
                                path = "21314151616263646555110656667574737271707P4"; //Mc3
                            }
                            if(orderCountAux==orderUnitsToDo || orderCountAux%5== 0){
                                countCollumns++;
                            }
                        }
                        else if(countCollumns == 2){
                            if (orderCountAux%5 == 1) {
                                path = "213141424344453511045464737271707P4"; //Mc2
                            }
                            else if (orderCountAux%5 == 2) {
                                path = "21314142434434120341154445464737271707P4"; //Mb2
                            }
                            else if (orderCountAux%5 == 3) {
                                path = "213141424344453511045464737271707P4"; //Mc2
                            }
                            else if (orderCountAux%5 == 4) {
                                path = "213141424344453511045464737271707P4"; //Mc2
                            }
                            else if (orderCountAux%5 == 0) {
                                path = "213141424344453511045464737271707P4"; //Mc2
                            }
                            if(orderCountAux==orderUnitsToDo || orderCountAux%5== 0){
                                countCollumns++;
                            }
                        }
                        else if(countCollumns == 3){
                            if (orderCountAux%5 == 1) {
                                path = "2122232425151102526271707P4"; //Mc1
                            }
                            else if (orderCountAux%5 == 2) {
                                path = "212223241412014115242526271707P4"; //Mb1
                            }
                            else if (orderCountAux%5 == 3) {
                                path = "2122232425151102526271707P4"; //Mc1
                            }
                            else if (orderCountAux%5 == 4) {
                                path = "2122232425151102526271707P4"; //Mc1
                            }
                            else if (orderCountAux%5 == 0) {
                                path = "2122232425151102526271707P4"; //Mc1
                            }
                            if(orderCountAux==orderUnitsToDo || orderCountAux%5== 0){
                                countCollumns=1;
                            }
                        }
                    }
                    break;


                case "P5":
                    if(orderUnitsToDo <=3){
                        if (countCollumns == 1) {
                            path = "2131415161626364655511055130656667574737271707P5"; //Mc3

                            if (orderCountAux == orderUnitsToDo) {
                                countCollumns++;
                            }
                        } else if (countCollumns == 2) {
                            path = "21314142434445351103513045464737271707P5"; //Mc2

                            if (orderCountAux == orderUnitsToDo) {
                                countCollumns++;
                            }
                        } else if (countCollumns == 3) {
                            path = "212223242515110151302526271707P5"; //Mc1

                            if (orderCountAux == orderUnitsToDo) {
                                countCollumns = 1;
                            }
                        }

                    }
                    else {
                        if (countCollumns == 1) {
                            if(orderCountAux%4 == 1) {
                                path = "2131415161626364655511055130656667574737271707P5"; //Mc3
                            }
                            else if(orderCountAux%4 == 2){
                                path = "21314151616263645412054115646555130656667574737271707P5"; //Mb3+Mc3
                            }
                            else if(orderCountAux%4 == 3) {
                                path = "2131415161626364655511055130656667574737271707P5"; //Mc3
                            }
                            else if(orderCountAux%4 == 0) {
                                path = "2131415161626364655511055130656667574737271707P5"; //Mc3
                            }

                            if (orderCountAux == orderUnitsToDo || orderCountAux % 4 == 0) {
                                countCollumns++;
                            }
                        }
                        else if (countCollumns == 2) {

                            if(orderCountAux%4 == 1) {
                                path = "21314142434445351103513045464737271707P5"; //Mc2
                            }
                            else if(orderCountAux%4 == 2){
                                path = "213141424344341203411544453513045464737271707P5"; //Mb2+Mc2
                            }
                            else if(orderCountAux%4 == 3) {
                                path = "21314142434445351103513045464737271707P5"; //Mc2
                            }
                            else if(orderCountAux%4 == 0) {
                                path = "21314142434445351103513045464737271707P5"; //Mc2
                            }

                            if (orderCountAux == orderUnitsToDo || orderCountAux % 4 == 0) {
                                countCollumns++;
                            }

                        }
                        else if (countCollumns == 3) {

                            if(orderCountAux%4 == 1) {
                                path = "212223242515110151302526271707P5"; //Mc1
                            }
                            else if(orderCountAux%4 == 2){
                                path = "2122232414120141152425151302526271707P5"; //Mb1+Mc1
                            }
                            else if(orderCountAux%4 == 3) {
                                path = "212223242515110151302526271707P5"; //Mc1
                            }
                            else if(orderCountAux%4 == 0) {
                                path = "212223242515110151302526271707P5"; //Mc1
                            }

                            if (orderCountAux == orderUnitsToDo || orderCountAux % 4 == 0) {
                                countCollumns=1;
                            }
                        }

                    }
                    break;

/*
                case "P6":
                    "21314151616263531154333215434445464737271707P6"
                    "21222313115233321523242526271707P6"
                case "P7":
                    "21314151616263645412044342204445464737271707P7"
                    "21222324141202434220242526271707P7"

                    "21314151616263531155311563645422064656667574737271707P7"
                    "213141424333115331154344342204445464737271707P7"
                    "2122231311513115232414220242526271707P7"

                case "P8":
                    "21314151616263645412054115646555210656667574737271707P8"
                    "213141424344341203411544453521045464737271707P8"
                    "2122232414120141152425152102526271707P8"

                    "21314151616263646555110453521045464737271707P8"
                    "21222324251511025352102526271707P8"
*/
                case "P9":

                    if(orderUnitsToDo <= 4){
                        path = "21222324251511025352104555310656667574737271707P9"; //Mc1+Mc2+Mc3

                        if (orderCountAux == orderUnitsToDo) {
                            countCollumns=3;
                        }
                    }
                    else if (orderUnitsToDo <= 15) {
                        while(bigFlagP1P9Init){
                            boolean mA2=SFS.getCell(3,3).getUnitPresence();
                            boolean mB2=SFS.getCell(4,3).getUnitPresence();
                            boolean mC2=SFS.getCell(5,3).getUnitPresence();
                            boolean rA2=SFS.getCell(3,4).getUnitPresence();
                            boolean rB2=SFS.getCell(4,4).getUnitPresence();
                            boolean rC2=SFS.getCell(5,4).getUnitPresence();


                            if(!mA2 && !rA2 && !mB2 && !rB2 && !mC2 && !rC2 ){
                                System.out.println("Posso Mandar");
                                bigFlagP1P9Init=false;
                            }

                        }

                        if (orderCountAux % 3 == 1) {
                            path = "21222324251511025352104555310656667574737271707P9"; //Mc1+Mc2+Mc3
                        } else if (orderCountAux % 3 == 2) {
                            path = "21222324251511025352104555310656667574737271707P9"; //Mc1+Mc2+Mc3
                        } else if (orderCountAux % 3 == 0) {
                            path = "21222313115233321543533156364656667574737271707P9"; //Ma1+Ma2+Ma3
                        }

                        if (orderCountAux == orderUnitsToDo) {
                            bigFlagP1P9End=true;
                            bigFlagP1P9Init=true;
                            countCollumns = 3;
                        }
                    }
                    else{
                        while(bigFlagP1P9Init){
                            boolean mA2=SFS.getCell(3,3).getUnitPresence();
                            boolean mB2=SFS.getCell(4,3).getUnitPresence();
                            boolean mC2=SFS.getCell(5,3).getUnitPresence();
                            boolean rA2=SFS.getCell(3,4).getUnitPresence();
                            boolean rB2=SFS.getCell(4,4).getUnitPresence();
                            boolean rC2=SFS.getCell(5,4).getUnitPresence();


                            if(!mA2 && !rA2 && !mB2 && !rB2 && !mC2 && !rC2 ){
                                System.out.println("Posso Mandar");
                                bigFlagP1P9Init=false;
                            }
                        }

                        if (orderCountAux%6 == 1) {
                            path = "21222324251511025352104555310656667574737271707P9"; //Mc1+Mc2+Mc3
                        }
                        else if (orderCountAux%6 == 2) {
                            path = "21222324141202434220445432064656667574737271707P9"; //Mb1+Mb2+Mb3
                        }
                        else if (orderCountAux%6 == 3) {
                            path = "21222313115233321543533156364656667574737271707P9"; //Ma1+Ma2+Ma3
                        }
                        else if (orderCountAux%6 == 4) {
                            path = path = "21222324251511025352104555310656667574737271707P9"; //Mc1+Mc2+Mc3
                        }
                        else if (orderCountAux%6 == 5) {
                            path = "21222324251511025352104555310656667574737271707P9"; //Mc1+Mc2+Mc3
                        }
                        else if (orderCountAux%6 == 0) {
                            path = "21222313115233321543533156364656667574737271707P9"; //Ma1+Ma2+Ma3
                        }

                        if(orderCountAux==orderUnitsToDo){
                            bigFlagP1P9End=true;
                            bigFlagP1P9Init=true;
                            countCollumns=3;
                        }

                    }
                    break;

            }
        }
        else if (orderPx.equals("P2")) {
            switch (orderPy) {
                case "P3":
                    break;
                case "P4":
                    break;
                case "P5":
                    break;
                case "P6":
                    if (countCollumns==1){
                        path = "21314151616263532156364656667574737271707P6"; //Ma3
                        countCollumns++;
                    }
                    else if(countCollumns == 2){
                        path = "213141424333215434445464737271707P6"; //Ma2
                        countCollumns++;
                    }
                    else if(countCollumns == 3){
                        path = "2122231321523242526271707P6"; //Ma1
                        countCollumns=1;
                    }
                    break;
                case "P7":
                    break;
                case "P8":
                    break;
                case "P9":
                    break;
            }
        }
        else if (orderPx.equals("P3")) {
            switch (orderPy) {
                case "P4":
                case "P5":

                    if (countCollumns == 1) {
                        path="213141516162636454115646555130656667574737271707P5"; //Mb3+Mc3

                        if (orderCountAux == orderUnitsToDo || orderCountAux % 3 == 0) {
                            countCollumns++;
                        }
                    }
                    else if (countCollumns == 2) {

                        path="2131414243443411544453513045464737271707P5"; //Mb2+Mc2

                        if (orderCountAux == orderUnitsToDo || orderCountAux % 3 == 0) {
                            countCollumns++;
                        }

                    }
                    else if (countCollumns == 3) {
                        path="21222324141152425151302526271707P5"; //Mb1+Mc1

                        if (orderCountAux == orderUnitsToDo || orderCountAux % 3 == 0) {
                            countCollumns=1;
                        }
                    }

                    break;

                case "P7":
                    break;
                case "P8":
                    break;
                case "P9":
                    break;
            }
        }
        else if (orderPx.equals("P4")) {
            switch (orderPy) {
                case "P5":
                    if(orderUnitsToDo <=3){
                        if (countCollumns == 1) {
                            path = "21314151616263646555130656667574737271707P5"; //Mc3

                            if (orderCountAux == orderUnitsToDo) {
                                countCollumns++;
                            }
                        }
                        else if (countCollumns == 2) {

                            path = "213141424344453513045464737271707P5"; //Mc2

                            if (orderCountAux == orderUnitsToDo) {
                                countCollumns++;
                            }

                        }
                        else if (countCollumns == 3) {

                            path = "2122232425151302526271707P5"; //Mc1

                            if (orderCountAux == orderUnitsToDo) {
                                countCollumns = 1;
                            }
                        }
                    }
                    else {
                        if (countCollumns == 1) {
                            path = "21314151616263646555130656667574737271707P5"; //Mc3

                            countCollumns++;
                        } else if (countCollumns == 2) {

                            path = "213141424344453513045464737271707P5"; //Mc2

                            countCollumns++;

                        } else if (countCollumns == 3) {

                            path = "2122232425151302526271707P5"; //Mc1

                            countCollumns=1;
                        }
                    }

                    break;

                case "P8":
                    if (orderUnitsToDo <= 4) {
                        if (countCollumns == 1) {
                            path = "21314151616263646555210656667574737271707P8"; //Mc3

                            if (orderCountAux == orderUnitsToDo) {
                                countCollumns++;
                            }
                        }
                        else if (countCollumns == 2) {
                            path = "213141424344453521045464737271707P8"; //Mc2

                            if (orderCountAux == orderUnitsToDo) {
                                countCollumns++;
                            }
                        } else if (countCollumns == 3) {
                            path = "2122232425152102526271707P8"; //Mc1
                            if (orderCountAux == orderUnitsToDo) {
                                countCollumns=1;
                            }
                        }
                    }
                    else {
                        if (countCollumns == 1) {
                            path = "21314151616263646555210656667574737271707P8"; //Mc3
                            countCollumns++;
                        } else if (countCollumns == 2) {
                            path = "213141424344453521045464737271707P8"; //Mc2
                            countCollumns++;
                        } else if (countCollumns == 3) {
                            path = "2122232425152102526271707P8"; //Mc1
                            countCollumns=1;
                        }

                    }
                    break;
                case "P9":
                    break;
            }
        }
        else if (orderPx.equals("P6") && orderPy.equals("P9")) {
        }
        else if (orderPx.equals("P7") && orderPy.equals("P9")) {
            if (orderUnitsToDo <= 3) {
                if (countCollumns == 1) {
                    path = "21314151616263645432064656667574737271707P9"; //Mb3

                    if (orderCountAux == orderUnitsToDo) {
                        countCollumns++;
                    }
                }
                else if (countCollumns == 2) {
                    path = "213141424344343204445464737271707P9"; //Mb2

                    if (orderCountAux == orderUnitsToDo) {
                        countCollumns++;
                    }
                } else if (countCollumns == 3) {
                    path = "2122232414320242526271707P9"; //Mb1
                    if (orderCountAux == orderUnitsToDo) {
                        countCollumns=1;
                    }
                }
            }
            else {
                if (countCollumns == 1) {
                    path = "21314151616263645432064656667574737271707P9"; //Mb3
                    countCollumns++;

                } else if (countCollumns == 2) {
                    path = "213141424344343204445464737271707P9"; //Mb2
                    countCollumns++;

                } else if (countCollumns == 3) {
                    path = "2122232414320242526271707P9"; //Mb1
                    countCollumns=1;

                }

            }
        }
        else if (orderPx.equals("P8") && orderPy.equals("P9")) {
        }


        if(parallel) countCollumns=countCollumnsInitial;
        return path;
    }


    private String isCompatible(String orderPxNow,String orderPyNow,String orderPxCompatible,String orderPyCompatible){
        String compatible="";
        boolean maNow = false;
        boolean mbNow = false;
        boolean mcNow=false;
        boolean maCompatible =false, mbCompatible=false, mcCompatible = false;

        if((orderPxNow.equals("P1") && orderPyNow.equals("P2")) || (orderPxNow.equals("P2") && orderPyNow.equals("P6")) || (orderPxNow.equals("P6") && orderPyNow.equals("P9")) || (orderPxNow.equals("P2") && orderPyNow.equals("P3"))){
            compatible=compatible + "1";
        }
        else if ((orderPxNow.equals("P1") && orderPyNow.equals("P3")) || (orderPxNow.equals("P3") && orderPyNow.equals("P7")) || (orderPxNow.equals("P7") && orderPyNow.equals("P9")) || (orderPxNow.equals("P3") && orderPyNow.equals("P4"))){
            compatible=compatible + "2";
        }
        else if((orderPxNow.equals("P1") && orderPyNow.equals("P4")) || (orderPxNow.equals("P4") && orderPyNow.equals("P5")) || (orderPxNow.equals("P4") && orderPyNow.equals("P8")) || (orderPxNow.equals("P8") && orderPyNow.equals("P9"))){
            compatible=compatible + "3";
        }

        if((orderPxCompatible.equals("P1") && orderPyCompatible.equals("P2")) || (orderPxCompatible.equals("P2") && orderPyCompatible.equals("P6")) || (orderPxCompatible.equals("P6") && orderPyCompatible.equals("P9")) || (orderPxCompatible.equals("P2") && orderPyCompatible.equals("P3"))){
            compatible=compatible + "1";
        }
        else if ((orderPxCompatible.equals("P1") && orderPyCompatible.equals("P3")) || (orderPxCompatible.equals("P3") && orderPyCompatible.equals("P7")) || (orderPxCompatible.equals("P7") && orderPyCompatible.equals("P9")) || (orderPxCompatible.equals("P3") && orderPyCompatible.equals("P4"))){
            compatible=compatible + "2";
        }

        else if((orderPxCompatible.equals("P1") && orderPyCompatible.equals("P4")) || (orderPxCompatible.equals("P4") && orderPyCompatible.equals("P5")) || (orderPxCompatible.equals("P4") && orderPyCompatible.equals("P8")) || (orderPxCompatible.equals("P8") && orderPyCompatible.equals("P9"))){
            compatible=compatible + "3";
        }

        return compatible;
    }

}
