package MES;

import javax.sound.midi.Soundbank;
import java.sql.SQLOutput;
import java.util.*;

import static MES.Main.*;
import static java.lang.Thread.*;

public class UnloadThread implements Runnable {

    //Attributes
    static boolean bigFlagP1P9Init=true;
    static boolean bigFlagP1P9End=false;
    static boolean bigFlagDoubleInitA=true;
    static boolean bigFlagDoubleEndA=false;
    static boolean bigFlagDoubleInitB=true;
    static boolean bigFlagDoubleEndB=false;
    static boolean bigFlagDoubleInitC=true;
    static boolean bigFlagDoubleEndC=false;
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


            if(!Main.orderListUnload.isEmpty() && checkIfAtLeastOneCanHappen()) {

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

                    //Update order in db
                    dbConnection.updateStatus_OrderUnloadDB(order.getId(), order.getStatus());


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
                    pathString += order.getPx() +"T"+ "2" + order.getId();

                    //System.out.println("[Unload] Esta é a string: " + pathString);

                    for (int a = orderUnitsDone; a < orderUnitsTotal; a++) {
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

                        //Update Unload Order on DataBase
                        dbConnection.updateNDone_OrderUnloadDB(order.getId(), orderUnitsDone);

                    }

                }
                else {
                    Main.orderListUnload.add(order); //adds order to top of Unload List
                }

                //Checks if order is done and updates Order
                if(order.getQuantity() == order.getNDone()) {
                    order.setStatus(3); //Set order status to "All pieces sent".
                    Main.orderListUnloadEnded.add(order);

                    //Update order in db
                    dbConnection.updateStatus_OrderUnloadDB(order.getId(), order.getStatus());

                }

            }
            else if (!Main.ordersPriority.isEmpty()) {
                System.out.println("Tamanho lista: "+ordersPriority.size() );

                int index = getMaxPriority(); //retorna a mais "urgente" desde que existam peças
                orderTransform order= ordersPriority.get(index);

                System.out.println("Tamanho lista: "+ordersPriority.size()+ " + Ordem índice: "+index);

                //Order attributes
                int orderUnitsDone = order.getNDone();
                int orderUnitsTotal = order.getNTotal();
                String orderPx = order.getPx();
                String orderPy = order.getPy();
                int orderUnitsToDo= orderUnitsTotal-orderUnitsDone;
                int orderCountAux=1;

                if(Warehouse.getPiece(orderPx) > 0) {
                    while (orderUnitsDone < orderUnitsTotal) {
                        System.out.println("A fazer "+ orderPx +"->" +orderPy);

                        //Verifies if it's out of units
                        if(Warehouse.getPiece(orderPx) <= 0){
                            order.setStatus(2); //Set order status to "All pieces sent".
                            order.setOutOfUnits(true);
                            break;
                        }

                        //Verifies if orderUnitsDone = orderUnitsTotal and if yes, polls and updates Order
                        if (order.getNDone() == order.getNTotal()) {
                            order.setStatus(3); //Set order status to "All pieces sent".
                            Main.orderListTransformationEnded.add(Main.ordersPriority.remove(index));

                            //Update order in db
                            dbConnection.updateStatus_OrderTransformDB(order.getId(), order.getStatus());

                            break;
                        }


                        //Verifies if an Unload Order came in
                        if (!Main.orderListUnload.isEmpty() && checkIfAtLeastOneCanHappen()) {
                            order.setStatus(2); //Set order status to "in pause".
                            break;
                        }

                        //Verifies if a Transform Order order with higher priority came in
                        int indexAux = getMaxPriority();
                        if (indexAux != index) {
                            order.setStatus(2); //Set order status to "in pause".
                            break;
                        }

                        if (orderUnitsDone == 0){
                            order.setStartTime(StopWatch.getTimeElapsed()); //Set order start Time
                            dbConnection.updateStatus_OrderTransformDB(order.getId(), 2);
                        }
                        if (order.getStatus() != 2){
                            order.setStatus(2); //Set order status to "in progress".
                            //Update order in db
                            dbConnection.updateStatus_OrderTransformDB(order.getId(), order.getStatus());
                        }

                        String pathString;


                        while(bigFlagP1P9End){
                            boolean mA=SFS.getCell(3,1).getUnitPresence();
                            boolean mB=SFS.getCell(4,1).getUnitPresence();
                            boolean mC=SFS.getCell(5,1).getUnitPresence();
                            boolean rA=SFS.getCell(3,2).getUnitPresence();
                            boolean rB=SFS.getCell(4,2).getUnitPresence();
                            boolean rC=SFS.getCell(5,2).getUnitPresence();

                            if(!mA && !rA && !mB && !rB && !mC && !rC){
                                bigFlagP1P9End=false;
                            }
                        }

                        while(bigFlagDoubleEndA){
                            boolean mA=SFS.getCell(3,1).getUnitPresence();
                            boolean rA=SFS.getCell(3,2).getUnitPresence();

                            if(!mA && !rA ){
                                bigFlagDoubleEndA=false;
                            }
                        }
                        while(bigFlagDoubleEndB){
                            boolean mB=SFS.getCell(4,1).getUnitPresence();
                            boolean rB=SFS.getCell(4,2).getUnitPresence();

                            if(!mB && !rB ){
                                bigFlagDoubleEndB=false;
                            }
                        }
                        while(bigFlagDoubleEndC){
                            boolean mC=SFS.getCell(5,1).getUnitPresence();
                            boolean rC=SFS.getCell(5,2).getUnitPresence();

                            if(!mC && !rC ){
                                bigFlagDoubleEndC=false;
                            }
                        }


                        pathString=getPathByTransformation(orderPx,orderPy,orderUnitsToDo,orderCountAux,false);
                        String orderInfo ="1" + order.getId();
                        pathString=pathString+"T"+orderInfo;

                        //Sends information to OPC-UA
                        sendPathToOPC(unitTypeIdentifier(orderPx), pathString);

                        orderCountAux++;
                        //Updates order information
                        orderUnitsDone++;
                        order.setNDone(orderUnitsDone);
                        //System.out.println(order);

                        //Update Unload Order on DataBase
                        dbConnection.updateNSent_OrderTransformationDB(order.getId(), orderUnitsDone);




                        // Displaying the values after iterating through the queue
                        //System.out.println("The iterator values are: ");
                        int i=1;
                        boolean flagA=false, flagB=false,flagC=false, flagDouble=false;
                        String pathDoubleTransf="";
                        orderTransform orderDoubleComp=null;
                        int orderDoubleCompIndex=0;

                        for(int a=0; a < Main.ordersPriority.size(); a++){
                            if(a==index){
                                continue;
                            }

                            orderTransform orderComp = Main.ordersPriority.get(a);

                            // If it's been done, keep looking
                            if(orderComp.getNDone()==orderComp.getNTotal()) {
                                orderComp.setStatus(3);
                                Main.orderListTransformationEnded.add(ordersPriority.remove(a));
                                dbConnection.updateStatus_OrderTransformDB(orderComp.getId(), orderComp.getStatus());
                                continue;
                            }

                            if((Warehouse.getPiece(orderComp.getPx()) <= 0)) {
                                orderComp.setOutOfUnits(true);
                                continue;
                            }


                            String transf = isCompatible(orderPx,orderPy,orderComp.getPx(),orderComp.getPy());
                            if((transf.equals("1|2") || transf.equals("3|2")) && !flagB){
                                flagB = true;

                                pathString = getPathByTransformation(orderComp.getPx(), orderComp.getPy(), 1, 1, true);
                                orderInfo ="1" + orderComp.getId();
                                pathString=pathString+"T"+orderInfo;

                                if (orderComp.getNDone() == 0) orderComp.setStartTime(StopWatch.getTimeElapsed()); //Set order start Time
                                if (orderComp.getStatus() != 2){
                                    orderComp.setStatus(2); //Set order status to "in progress".

                                    //Update order in db
                                    dbConnection.updateStatus_OrderTransformDB(orderComp.getId(), orderComp.getStatus());

                                }

                                //Sends information to OPC-UA
                                sendPathToOPC(unitTypeIdentifier(orderComp.getPx()), pathString);

                                //Updates order information
                                orderComp.setNDone(orderComp.getNDone() + 1);
                                //System.out.println(orderComp);

                                //Update Order on DataBase
                                dbConnection.updateNSent_OrderTransformationDB(orderComp.getId(), orderComp.getNDone());



                            }
                            else if((transf.equals("1|3") || transf.equals("2|3")) && !flagC){
                                flagC = true;

                                pathString = getPathByTransformation(orderComp.getPx(), orderComp.getPy(), 1, 1, true);
                                orderInfo ="1" + orderComp.getId();
                                pathString=pathString+"T"+orderInfo;

                                if (orderComp.getNDone() == 0) orderComp.setStartTime(StopWatch.getTimeElapsed()); //Set order start Time
                                if (orderComp.getStatus() != 2){
                                    orderComp.setStatus(2); //Set order status to "in progress".

                                    dbConnection.updateStatus_OrderTransformDB(orderComp.getId(), orderComp.getStatus());

                                }

                                //Sends information to OPC-UA
                                sendPathToOPC(unitTypeIdentifier(orderComp.getPx()), pathString);

                                //Updates order information
                                orderComp.setNDone(orderComp.getNDone() + 1);
                                //System.out.println(orderComp);

                                //Update Order on DataBase
                                dbConnection.updateNSent_OrderTransformationDB(orderComp.getId(), orderComp.getNDone());



                            }
                            else if((transf.equals("2|1") || transf.equals("3|1")) && !flagA){
                                flagA = true;

                                pathString = getPathByTransformation(orderComp.getPx(), orderComp.getPy(), 1, 1, true);
                                orderInfo ="1" + orderComp.getId();
                                pathString=pathString+"T"+orderInfo;

                                if (orderComp.getNDone() == 0) orderComp.setStartTime(StopWatch.getTimeElapsed()); //Set order start Time
                                if (orderComp.getStatus() != 2){
                                    orderComp.setStatus(2); //Set order status to "in progress".

                                    dbConnection.updateStatus_OrderTransformDB(orderComp.getId(), orderComp.getStatus());

                                }

                                //Sends information to OPC-UA
                                sendPathToOPC(unitTypeIdentifier(orderComp.getPx()), pathString);
                                //Updates order information
                                orderComp.setNDone(orderComp.getNDone() + 1);
                                //System.out.println(orderComp);

                                //Update Order on DataBase
                                dbConnection.updateNSent_OrderTransformationDB(orderComp.getId(), orderComp.getNDone());

                            }

                            //Caso encontre um transformação dupla possível
                            if((transf.equals("1|23") || transf.equals("3|12") || transf.equals("12|3") || transf.equals("23|1"))  && !flagDouble){
                                orderDoubleComp=ordersPriority.get(a);
                                orderDoubleCompIndex=a;
                                flagDouble = true;

                                if (orderDoubleComp.getNDone() == 0) orderDoubleComp.setStartTime(StopWatch.getTimeElapsed()); //Set order start Time
                                if (orderDoubleComp.getStatus() != 2){
                                    orderDoubleComp.setStatus(2); //Set order status to "in progress".

                                    dbConnection.updateStatus_OrderTransformDB(orderDoubleComp.getId(), orderDoubleComp.getStatus());

                                }

                                pathDoubleTransf = getPathByTransformation(orderComp.getPx(), orderComp.getPy(), 1, 1, true);
                                orderInfo ="1" + orderComp.getId();
                                pathDoubleTransf=pathDoubleTransf+"T"+orderInfo;
                            }


                            //Checks if order is done
                            if(orderComp.getNTotal()==orderComp.getNDone()){
                                orderComp.setStatus(3);
                                Main.orderListTransformationEnded.add(ordersPriority.remove(a));
                                dbConnection.updateStatus_OrderTransformDB(orderComp.getId(), orderComp.getStatus());

                            }


                        }

                        if(!flagA && !flagB && !flagC && flagDouble){
                            //Sends information to OPC-UA
                            sendPathToOPC(unitTypeIdentifier(orderDoubleComp.getPx()), pathDoubleTransf);
                            //Updates order information
                            orderDoubleComp.setNDone(orderDoubleComp.getNDone() + 1);
                            //System.out.println(orderDoubleComp);

                            //Update Order on DataBase
                            dbConnection.updateNSent_OrderTransformationDB(orderDoubleComp.getId(), orderDoubleComp.getNDone());

                        }

                        if(orderDoubleComp!=null) {
                            if (orderDoubleComp.getNDone() == orderDoubleComp.getNTotal() && orderDoubleComp != null) {
                                orderDoubleComp.setStatus(3);
                                Main.orderListTransformationEnded.add(ordersPriority.remove(orderDoubleCompIndex));
                                dbConnection.updateStatus_OrderTransformDB(orderDoubleComp.getId(), orderDoubleComp.getStatus());
                            }
                        }



                    }
                    if (order.getNDone() == order.getNTotal()) {
                        order.setStatus(3); //Set order status to "All pieces sent".
                        Main.orderListTransformationEnded.add(Main.ordersPriority.remove(index));

                        //Update order in db
                        dbConnection.updateStatus_OrderTransformDB(order.getId(), order.getStatus());
                    }
                }
                else {
                    System.out.println("Sem peças.");
                    order.setStatus(2); //Set order status to "All pieces sent".
                    order.setOutOfUnits(true);
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

    private void sendPathToOPC(int unitType, String path){
        //Sends information to OPC-UA
        OPCUA_Connection.setValueBoolean("MAIN_TASK", "GO", true);
        OPCUA_Connection.setValueInt("MAIN_TASK", "unit_type", unitType);
        OPCUA_Connection.setValueString("MAIN_TASK", "AT1_order_path_mes", path);
        //System.out.println(path);
        int aux = 1;
        while (true){
            try {
                if (aux == 1 && !SFS.getCell(1, 0).getUnitPresence()) {
                    Main.unitCount++;
                    OPCUA_Connection.setValueInt("MAIN_TASK", "UNIT_COUNT_AT1", Main.unitCount);
                    aux++;
                }
                if (aux == 2 && OPCUA_Connection.getValueInt("MAIN_TASK", "UNIT_COUNT_AT1") == Main.unitCount) {
                    aux++;
                }
                if (aux == 3 && (OPCUA_Connection.getValueInt("MAIN_TASK", "CONFIRMATIONGO") == Main.unitCount)) break;
            }
            catch(Exception e){
                System.out.println("Send Path OPC:  Exception");
            }
        }

        Warehouse.removePiece("P"+unitType);

    }

    private boolean checkIfAtLeastOneCanHappen(){

        for(int i=0; i < Main.orderListUnload.size(); i++){
            String type = Main.orderListUnload.get(i).getPx();
            int nPieces = Warehouse.getPiece(type);
            int[] position = SFS.getUnloadPosition(Main.orderListUnload.get(i).getDy());
            assert position != null;
            Slider slider = (Slider) SFS.getCell(position[1], position[0]);

            //Verificar se existe pelo menos 1 slider livre e com peças disponíveis que significa que existe uma transformação de unload que pode ser feita
            if(!slider.isFull() && (nPieces > 0)) {
                return true;
            }
        }
        return false;
    }

    private String getPathByTransformation(String orderPx,String orderPy, int orderUnitsToDo,int orderCountAux, boolean parallel) {
        String path="";
        int countCollumnsInitial=countCollumns;

        if(orderPx.equals("P1")) {

            switch (orderPy) {

                case "P2":
                    if (countCollumns==1){
                        path = "21314151616263531156364656667574737271707P2Q2"; //Ma3

                        countCollumns++;
                    }
                    else if(countCollumns == 2){
                        path = "213141424333115434445464737271707P2Q2"; //Ma2

                        countCollumns++;
                    }
                    else if(countCollumns == 3){
                        path = "2122231311523242526271707P2Q2"; //Ma1
                        countCollumns=1;

                    }
                    break;

                case "P3":
                    if(parallel){
                        if (countCollumns==1){
                            path = "21314151616263645412064656667574737271707P3Q3"; //Mb3
                            countCollumns++;
                        }
                        else if(countCollumns == 2){
                            path="213141424344341204445464737271707P3Q3";//Mb2
                            countCollumns++;
                        }
                        else if(countCollumns == 3){
                            path="2122232414120242526271707P3Q3"; //Mb1
                            countCollumns=1;
                        }

                    }
                    if (countCollumns==1){
                        if (orderCountAux%3 == 1) {
                            path = "21314151616263645412064656667574737271707P3Q3"; //Mb3
                        }
                        else if (orderCountAux%3 == 2) {
                            path = "2131415161626353115531156364656667574737271707P3Q23"; //Ma3
                        }
                        else if (orderCountAux%3 == 0) {
                            path = "21314151616263645412064656667574737271707P3Q3"; //Mb3
                        }
                        if(orderCountAux==orderUnitsToDo || orderCountAux%3== 0){
                            countCollumns++;
                        }
                    }
                    else if(countCollumns == 2){
                        if (orderCountAux%3 == 1) {
                            path="213141424344341204445464737271707P3Q3";//Mb2
                        }
                        else if (orderCountAux%3 == 2) {
                            path = "21314142433311533115434445464737271707P3Q23";//Ma2
                        }
                        else if (orderCountAux%3 == 0) {
                            path="213141424344341204445464737271707P3Q3";//Mb2
                        }
                        if(orderCountAux==orderUnitsToDo || orderCountAux%3== 0){
                            countCollumns++;
                        }
                    }
                    else if(countCollumns == 3){
                        if (orderCountAux%3 == 1) {
                            path="2122232414120242526271707P3Q3"; //Mb1
                        }
                        else if (orderCountAux%3 == 2) {
                            path="212223131151311523242526271707P3Q23";//Ma1
                        }
                        else if (orderCountAux%3 == 0) {
                            path="2122232414120242526271707P3Q3"; //Mb1
                        }
                        if(orderCountAux==orderUnitsToDo || orderCountAux%3== 0){
                            countCollumns=1;
                        }
                    }
                    break;

                case "P4":
                    if(parallel){
                        if (countCollumns==1){
                            path = "21314151616263646555110656667574737271707P4Q4"; //Mc3
                            countCollumns++;
                        }
                        else if(countCollumns == 2){
                            path = "213141424344453511045464737271707P4Q4"; //Mc2
                            countCollumns++;
                        }
                        else if(countCollumns == 3){
                            path = "2122232425151102526271707P4Q4"; //Mc1
                            countCollumns=1;
                        }

                    }

                    else if(orderUnitsToDo <= 3){
                        if (countCollumns==1) {
                            path = "21314151616263646555110656667574737271707P4Q4"; //Mc3
                            if(orderCountAux==orderUnitsToDo){
                                countCollumns++;
                            }
                        }
                        else if (countCollumns==2) {
                            path = "213141424344453511045464737271707P4Q4"; //Mc2
                            if(orderCountAux==orderUnitsToDo){
                                countCollumns++;
                            }
                        }
                        else if (countCollumns==3) {
                            path = "2122232425151102526271707P4Q4"; //Mc1
                            if(orderCountAux==orderUnitsToDo){
                                countCollumns=1;
                            }
                        }

                    }
                    else {
                        if (countCollumns==1){
                            if (orderCountAux%3 == 1) {
                                path = "21314151616263646555110656667574737271707P4Q4"; //Mc3
                            }
                            else if (orderCountAux%3 == 2) {
                                path = "2131415161626364541205411564656667574737271707P4Q34"; //Mb3
                            }
                            else if (orderCountAux%3 == 0) {
                                path = "21314151616263646555110656667574737271707P4Q4"; //Mc3
                            }

                            if(orderCountAux==orderUnitsToDo || orderCountAux%3== 0){
                                countCollumns++;
                            }
                        }
                        else if(countCollumns == 2){
                            if (orderCountAux%3 == 1) {
                                path = "213141424344453511045464737271707P4Q4"; //Mc2
                            }
                            else if (orderCountAux%3 == 2) {
                                path = "21314142434434120341154445464737271707P4Q34"; //Mb2
                            }
                            else if (orderCountAux%3 == 0) {
                                path = "213141424344453511045464737271707P4Q4"; //Mc2
                            }

                            if(orderCountAux==orderUnitsToDo || orderCountAux%3== 0){
                                countCollumns++;
                            }
                        }
                        else if(countCollumns == 3){
                            if (orderCountAux%3 == 1) {
                                path = "2122232425151102526271707P4Q4"; //Mc1
                            }
                            else if (orderCountAux%3 == 2) {
                                path = "212223241412014115242526271707P4Q34"; //Mb1
                            }
                            else if (orderCountAux%3 == 0) {
                                path = "2122232425151102526271707P4Q4"; //Mc1
                            }

                            if(orderCountAux==orderUnitsToDo || orderCountAux%3== 0){
                                countCollumns=1;
                            }
                        }
                    }
                    break;


                case "P5":
                    if(orderUnitsToDo <=3){
                        if (countCollumns == 1) {
                            path = "2131415161626364655511055130656667574737271707P5Q45"; //Mc3

                            if (orderCountAux == orderUnitsToDo) {
                                countCollumns++;
                            }
                        } else if (countCollumns == 2) {
                            path = "21314142434445351103513045464737271707P5Q45"; //Mc2

                            if (orderCountAux == orderUnitsToDo) {
                                countCollumns++;
                            }
                        } else if (countCollumns == 3) {
                            path = "212223242515110151302526271707P5Q45"; //Mc1

                            if (orderCountAux == orderUnitsToDo) {
                                countCollumns = 1;
                            }
                        }

                    }
                    else {
                        if (countCollumns == 1) {
                            if(orderCountAux%3 == 1) {
                                path = "2131415161626364655511055130656667574737271707P5Q45"; //Mc3
                            }
                            else if(orderCountAux%3 == 2){
                                path = "21314151616263645412054115646555130656667574737271707P5Q345"; //Mb3+Mc3
                            }
                            else if(orderCountAux%3 == 0) {
                                path = "2131415161626364655511055130656667574737271707P5Q45"; //Mc3
                            }

                            if (orderCountAux == orderUnitsToDo || orderCountAux % 3 == 0) {
                                countCollumns++;
                            }
                        }
                        else if (countCollumns == 2) {

                            if(orderCountAux%3 == 1) {
                                path = "21314142434445351103513045464737271707P5Q45"; //Mc2
                            }
                            else if(orderCountAux%3 == 2){
                                path = "213141424344341203411544453513045464737271707P5Q345"; //Mb2+Mc2
                            }
                            else if(orderCountAux%3 == 3) {
                                path = "21314142434445351103513045464737271707P5Q45"; //Mc2
                            }

                            if (orderCountAux == orderUnitsToDo || orderCountAux % 3 == 0) {
                                countCollumns++;
                            }

                        }
                        else if (countCollumns == 3) {

                            if(orderCountAux%3 == 1) {
                                path = "212223242515110151302526271707P5Q45"; //Mc1
                            }
                            else if(orderCountAux%3 == 2){
                                path = "2122232414120141152425151302526271707P5Q345"; //Mb1+Mc1
                            }
                            else if(orderCountAux%3 == 0) {
                                path = "212223242515110151302526271707P5Q45"; //Mc1
                            }


                            if (orderCountAux == orderUnitsToDo || orderCountAux % 3 == 0) {
                                countCollumns=1;
                            }
                        }

                    }
                    break;


                case "P6":
                    path = "212223131152333215434445464737271707P9Q26"; //Ma1+Ma2

                    while(bigFlagDoubleInitA && !parallel){
                        boolean p42=SFS.getCell(2,4).getUnitPresence();
                        boolean p33=SFS.getCell(3,3).getUnitPresence();

                        if(!p33 && !p42 ){
                            bigFlagDoubleInitA=false;
                        }

                    }
                    countCollumns++;
                    if(countCollumns > 3){
                        countCollumns=1;
                    }

                    if (orderCountAux == orderUnitsToDo && !parallel ) {
                        bigFlagDoubleEndA=true;
                        bigFlagDoubleInitA=true;
                    }
                    break;

                case "P7":

                    path = "212223241412024342204445464737271707P7Q37"; //Mb1+Mb2

                    while(bigFlagDoubleInitB && !parallel){
                        boolean p43=SFS.getCell(3,4).getUnitPresence();
                        boolean p34=SFS.getCell(4,3).getUnitPresence();

                        if(!p34 && !p43 ){
                            bigFlagDoubleInitB=false;
                        }

                    }
                    countCollumns++;
                    if(countCollumns > 3){
                        countCollumns=1;
                    }

                    if (orderCountAux == orderUnitsToDo && !parallel) {
                        bigFlagDoubleEndB=true;
                        bigFlagDoubleInitB=true;
                    }

                    break;

                case "P8":
                    path = "212223242515110253521045464737271707P9Q48"; //Mc1+Mc2

                    while(bigFlagDoubleInitC && !parallel){
                        boolean p43=SFS.getCell(3,4).getUnitPresence();
                        boolean p34=SFS.getCell(4,3).getUnitPresence();

                        if(!p34 && !p43 ){
                            bigFlagDoubleInitC=false;
                        }

                    }
                    countCollumns++;
                    if(countCollumns > 3){
                        countCollumns=1;
                    }

                    if (orderCountAux == orderUnitsToDo && !parallel) {
                        bigFlagDoubleEndC=true;
                        bigFlagDoubleInitC=true;
                    }

                    break;

                case "P9":

                    if(orderUnitsToDo <= 4){
                        path = "21222324251511025352104555310656667574737271707P9Q489"; //Mc1+Mc2+Mc3

                        if (orderCountAux == orderUnitsToDo) {
                            countCollumns=3;
                        }
                    }
                    else if (orderUnitsToDo <= 15) {
                        System.out.println("Espera para inciar P1->P9...");
                        while(bigFlagP1P9Init){
                            boolean p41=SFS.getCell(1,4).getUnitPresence();
                            boolean p42=SFS.getCell(2,4).getUnitPresence();
                            boolean p43=SFS.getCell(3,4).getUnitPresence();
                            boolean p61=SFS.getCell(1,6).getUnitPresence();
                            boolean p62=SFS.getCell(2,6).getUnitPresence();
                            boolean p63=SFS.getCell(3,6).getUnitPresence();


                            if(!p41 && !p42 && !p43 && !p61 && !p62 && !p63){
                                bigFlagP1P9Init=false;
                            }

                        }
                        System.out.println("Começou P1->P9.");

                        if (orderCountAux % 3 == 1) {
                            path = "21222324251511025352104555310656667574737271707P9Q489"; //Mc1+Mc2+Mc3
                        } else if (orderCountAux % 3 == 2) {
                            path = "21222324251511025352104555310656667574737271707P9Q489"; //Mc1+Mc2+Mc3
                        } else if (orderCountAux % 3 == 0) {
                            path = "21222313115233321543533156364656667574737271707P9Q269"; //Ma1+Ma2+Ma3
                        }

                        if (orderCountAux == orderUnitsToDo) {
                            bigFlagP1P9End=true;
                            bigFlagP1P9Init=true;
                            countCollumns = 3;
                        }
                    }
                    else{
                        while(bigFlagP1P9Init){
                            boolean p41=SFS.getCell(1,4).getUnitPresence();
                            boolean p42=SFS.getCell(2,4).getUnitPresence();
                            boolean p43=SFS.getCell(3,4).getUnitPresence();
                            boolean p61=SFS.getCell(1,6).getUnitPresence();
                            boolean p62=SFS.getCell(2,6).getUnitPresence();
                            boolean p63=SFS.getCell(3,6).getUnitPresence();

                            if(!p41 && !p42 && !p43 && !p61 && !p62 && !p63){
                                bigFlagP1P9Init=false;
                            }

                        }

                        if (orderCountAux%5 == 1) {
                            path = "21222324251511025352104555310656667574737271707P9Q489"; //Mc1+Mc2+Mc3
                        }
                        else if (orderCountAux%5 == 2) {
                            path = "21222324141202434220445432064656667574737271707P9Q379"; //Mb1+Mb2+Mb3
                        }
                        else if (orderCountAux%5 == 3) {
                            path = "21222313115233321543533156364656667574737271707P9Q269"; //Ma1+Ma2+Ma3
                        }
                        else if (orderCountAux%5 == 4) {
                            path = path = "21222324251511025352104555310656667574737271707P9Q489"; //Mc1+Mc2+Mc3
                        }
                        else if (orderCountAux%5 == 0) {
                            path = "21222313115233321543533156364656667574737271707P9269"; //Ma1+Ma2+Ma3
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
                    if (countCollumns==1){
                        path = "21314151616263531156364656667574737271707P3Q3"; //Ma3

                        countCollumns++;
                    }
                    else if(countCollumns == 2){
                        path = "213141424333115434445464737271707P3Q3"; //Ma2

                        countCollumns++;
                    }
                    else if(countCollumns == 3){
                        path = "2122231311523242526271707P3Q3"; //Ma1
                        countCollumns=1;
                    }
                    break;
                case "P4":
                    if (countCollumns==1){
                        path = "213141516162635311563645411564656667574737271707P4Q34"; //Ma3 + Mb3
                        countCollumns++;
                    }
                    else if(countCollumns == 2){
                        path = "2131414243331154344341154445464737271707P4Q34"; //Ma2 + Mb2
                        countCollumns++;
                    }
                    else if(countCollumns == 3){
                        path = "21222313115232414115242526271707P4Q34"; //Ma1 + Mb1
                        countCollumns=1;
                    }
                    break;

                case "P5":
                    if(orderUnitsToDo <=3){
                        if (countCollumns == 1) {
                            path = "2131415161626353115636454115646555130656667574737271707P5Q345"; //Ma3 + Mb3 + Mc3
                        } else if (countCollumns == 2) {
                            path = "21314142433311543443411544453513045464737271707P5Q345"; //Ma2 + Mb2 + Mc2
                        } else if (countCollumns == 3) {
                            path = "212223131152324141152425151302526271707P5Q345"; //Ma1 + Mb1 + Mc1
                        }

                        if(orderUnitsToDo == orderCountAux){
                            if(countCollumns == 3) countCollumns=1;
                            else countCollumns++;
                        }

                    }
                    else {
                        if (countCollumns == 1) {
                            path = "2131415161626353115636454115646555130656667574737271707P5Q345"; //Ma3 + Mb3 + Mc3
                            countCollumns++;
                        } else if (countCollumns == 2) {
                            path = "21314142433311543443411544453513045464737271707P5Q345"; //Ma2 + Mb2 + Mc2
                            countCollumns++;
                        } else if (countCollumns == 3) {
                            path = "212223131152324141152425151302526271707P5Q345"; //Ma1 + Mb1 + Mc1
                            countCollumns = 1;
                        }
                    }

                    break;
                case "P6":
                    if (countCollumns==1){
                        path = "21314151616263532156364656667574737271707P6Q6"; //Ma3
                        countCollumns++;
                    }
                    else if(countCollumns == 2){
                        path = "213141424333215434445464737271707P6Q6"; //Ma2
                        countCollumns++;
                    }
                    else if(countCollumns == 3){
                        path = "2122231321523242526271707P6Q6"; //Ma1
                        countCollumns=1;
                    }
                    break;
                case "P7":
                    if (countCollumns==1){
                        path = "213141516162635311563645422064656667574737271707P7Q37"; //Ma3 + Mb3
                        countCollumns++;
                    }
                    else if(countCollumns == 2){
                        path = "2131414243331154344342204445464737271707P7Q37"; //Ma2 + Mb2
                        countCollumns++;
                    }
                    else if(countCollumns == 3){
                        path = "21222313115232414220242526271707P7Q37"; //Ma1 + Mb1
                        countCollumns=1;
                    }
                    break;
                case "P8":
                    if(orderUnitsToDo <=3){
                        if (countCollumns == 1) {
                            path = "2131415161626353115636454115646555210656667574737271707P8Q348"; //Ma3 + Mb3 + Mc3
                        } else if (countCollumns == 2) {
                            path = "21314142433311543443411544453521045464737271707P8Q348"; //Ma2 + Mb2 + Mc2
                        } else if (countCollumns == 3) {
                            path = "212223131152324141152425152102526271707P8Q348"; //Ma1 + Mb1 + Mc1
                        }

                        if(orderUnitsToDo == orderCountAux){
                            if(countCollumns == 3) countCollumns=1;
                            else countCollumns++;
                        }

                    }
                    else {
                        if (countCollumns == 1) {
                            path = "2131415161626353115636454115646555210656667574737271707P8Q348"; //Ma3 + Mb3 + Mc3
                            countCollumns++;
                        } else if (countCollumns == 2) {
                            path = "21314142433311543443411544453521045464737271707P8Q348"; //Ma2 + Mb2 + Mc2
                            countCollumns++;
                        } else if (countCollumns == 3) {
                            path = "212223131152324141152425152102526271707P8Q348"; //Ma1 + Mb1 + Mc1
                            countCollumns = 1;
                        }
                    }

                    break;
                case "P9":
                    path = "212223132152333315434445464737271707P9Q69"; //Ma1+Ma2

                    while(bigFlagDoubleInitA && !parallel){
                        boolean p42=SFS.getCell(2,4).getUnitPresence();
                        boolean p33=SFS.getCell(3,3).getUnitPresence();

                        if(!p33 && !p42 ){
                            bigFlagDoubleInitA=false;
                        }

                    }
                    countCollumns++;
                    if(countCollumns > 3){
                        countCollumns=1;
                    }

                    if (orderCountAux == orderUnitsToDo && !parallel) {
                        bigFlagDoubleEndA=true;
                        bigFlagDoubleInitA=true;
                    }
                    break;
            }
        }
        else if (orderPx.equals("P3")) {
            switch (orderPy) {
                case "P4":
                    if (countCollumns==1){
                        path = "21314151616263645411564656667574737271707P4Q4"; //Mb3
                        countCollumns++;
                    }
                    else if(countCollumns == 2){
                        path = "213141424344341154445464737271707P4Q4"; //Mb2
                        countCollumns++;
                    }
                    else if(countCollumns == 3){
                        path = "2122232414115242526271707P4Q4"; //Mb1
                        countCollumns=1;
                    }
                    break;
                case "P5":

                    if (countCollumns == 1) {
                        path="213141516162636454115646555130656667574737271707P5Q45"; //Mb3+Mc3
                        countCollumns++;
                    }
                    else if (countCollumns == 2) {
                        path="2131414243443411544453513045464737271707P5Q45"; //Mb2+Mc2
                        countCollumns++;

                    }
                    else if (countCollumns == 3) {
                        path="21222324141152425151302526271707P5Q45"; //Mb1+Mc1
                        countCollumns=1;
                    }

                    break;

                case "P7":
                    if (countCollumns==1){
                        path = "21314151616263645422064656667574737271707P7Q7"; //Mb3
                        countCollumns++;
                    }
                    else if(countCollumns == 2){
                        path = "213141424344342204445464737271707P7Q7"; //Mb2
                        countCollumns++;
                    }
                    else if(countCollumns == 3){
                        path = "2122232414220242526271707P7Q7"; //Mb1
                        countCollumns=1;
                    }
                    break;
                case "P8":
                    if (countCollumns==1){
                        path = "213141516162636454115646555210656667574737271707P8Q48"; //Mb3 + Mc3
                        countCollumns++;
                    }
                    else if(countCollumns == 2){
                        path = "2131414243443411544453521045464737271707P8Q48"; //Mb2 + Mc2
                        countCollumns++;
                    }
                    else if(countCollumns == 3){
                        path = "21222324141152425152102526271707P8Q48"; //Mb1 + Mc1
                        countCollumns=1;
                    }
                    break;

                case "P9":
                    path = "212223241422024343204445464737271707P9Q79"; //Mb1+Mb2

                    while(bigFlagDoubleInitB && !parallel){
                        boolean p43=SFS.getCell(3,4).getUnitPresence();
                        boolean p34=SFS.getCell(4,3).getUnitPresence();

                        if(!p34 && !p43 ){
                            bigFlagDoubleInitB=false;
                        }

                    }
                    countCollumns++;
                    if(countCollumns > 3){
                        countCollumns=1;
                    }

                    if (orderCountAux == orderUnitsToDo && !parallel) {
                        bigFlagDoubleEndB=true;
                        bigFlagDoubleInitB=true;
                    }

                    break;
            }
        }
        else if (orderPx.equals("P4")) {
            switch (orderPy) {
                case "P5":
                    if (countCollumns == 1) {
                        path = "21314151616263646555130656667574737271707P5Q5"; //Mc3

                        countCollumns++;
                    }
                    else if (countCollumns == 2) {
                        path = "213141424344453513045464737271707P5Q5"; //Mc2
                        countCollumns++;

                    } else if (countCollumns == 3) {
                        path = "2122232425151302526271707P5Q5"; //Mc1
                        countCollumns=1;
                    }

                    break;

                case "P8":
                    if (countCollumns == 1) {
                        path = "21314151616263646555210656667574737271707P8Q8"; //Mc3
                        countCollumns++;
                    }
                    else if (countCollumns == 2) {
                        path = "213141424344453521045464737271707P8Q8"; //Mc2
                        countCollumns++;
                    }
                    else if (countCollumns == 3) {
                        path = "2122232425152102526271707P8Q8"; //Mc1
                        countCollumns=1;
                    }

                    break;
                case "P9":
                    path = "212223242515210253531045464737271707P9Q89"; //Mc1+Mc2

                    while(bigFlagDoubleInitC && !parallel){
                        boolean p43=SFS.getCell(3,4).getUnitPresence();
                        boolean p34=SFS.getCell(4,3).getUnitPresence();

                        if(!p34 && !p43 ){
                            bigFlagDoubleInitC=false;
                        }

                    }
                    countCollumns++;
                    if(countCollumns > 3){
                        countCollumns=1;
                    }

                    if (orderCountAux == orderUnitsToDo && !parallel) {
                        bigFlagDoubleEndC=true;
                        bigFlagDoubleInitC=true;
                    }
            }
        }
        else if (orderPx.equals("P6") && orderPy.equals("P9")) {
            if (countCollumns == 1) {
                path = "21314151616263533156364656667574737271707P9Q9"; //Ma3
                countCollumns++;
            }
            else if (countCollumns == 2) {
                path = "213141424333315434445464737271707P9Q9"; //Ma2
                countCollumns++;
            }
            else if (countCollumns == 3) {
                path = "2122231331523242526271707P9Q9"; //Ma1
                countCollumns=1;
            }
        }
        else if (orderPx.equals("P7") && orderPy.equals("P9")) {
            if (countCollumns == 1) {
                path = "21314151616263645432064656667574737271707P9Q9"; //Mb3
                countCollumns++;

            } else if (countCollumns == 2) {
                path = "213141424344343204445464737271707P9Q9"; //Mb2
                countCollumns++;

            } else if (countCollumns == 3) {
                path = "2122232414320242526271707P9Q9"; //Mb1
                countCollumns=1;

            }

        }
        else if (orderPx.equals("P8") && orderPy.equals("P9")) {
            if (countCollumns == 1) {
                path = "21314151616263646555310656667574737271707P9Q9"; //Mc3
                countCollumns++;
            }
            else if (countCollumns == 2) {
                path = "213141424344453531045464737271707P9Q9"; //Mc2
                countCollumns++;
            }
            else if (countCollumns == 3) {
                path = "2122232425153102526271707P9Q9"; //Mc1
                countCollumns=1;
            }
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

        if((orderPxNow.equals("P1") && orderPyNow.equals("P2")) || (orderPxNow.equals("P2") && orderPyNow.equals("P6")) || (orderPxNow.equals("P6") && orderPyNow.equals("P9")) || (orderPxNow.equals("P2") && orderPyNow.equals("P3"))
                || (orderPxNow.equals("P1") && orderPyNow.equals("P6")) || (orderPxNow.equals("P2") && orderPyNow.equals("P9")) || (orderPxNow.equals("P3") && orderPyNow.equals("P6"))){
            compatible=compatible + "1|";
        }
        else if ((orderPxNow.equals("P3") && orderPyNow.equals("P7")) || (orderPxNow.equals("P7") && orderPyNow.equals("P9")) || (orderPxNow.equals("P3") && orderPyNow.equals("P4"))
                || (orderPxNow.equals("P1") && orderPyNow.equals("P7")) || (orderPxNow.equals("P3") && orderPyNow.equals("P9"))){
            compatible=compatible + "2|";
        }
        else if((orderPxNow.equals("P4") && orderPyNow.equals("P5")) || (orderPxNow.equals("P4") && orderPyNow.equals("P8")) || (orderPxNow.equals("P8") && orderPyNow.equals("P9"))
                || (orderPxNow.equals("P1") && orderPyNow.equals("P8")) || (orderPxNow.equals("P4") && orderPyNow.equals("P9"))){
            compatible=compatible + "3|";
        }
        else if((orderPxNow.equals("P2") && orderPyNow.equals("P4")) || (orderPxNow.equals("P2") && orderPyNow.equals("P7"))) {
            compatible=compatible + "12|";
        }
        else if((orderPxNow.equals("P3") && orderPyNow.equals("P5")) || (orderPxNow.equals("P3") && orderPyNow.equals("P8"))) {
            compatible=compatible + "23|";
        }


        if((orderPxCompatible.equals("P1") && orderPyCompatible.equals("P2")) || (orderPxCompatible.equals("P2") && orderPyCompatible.equals("P6")) || (orderPxCompatible.equals("P6") && orderPyCompatible.equals("P9")) || (orderPxCompatible.equals("P2") && orderPyCompatible.equals("P3"))){
            compatible=compatible + "1";
        }
        else if ((orderPxCompatible.equals("P1") && orderPyCompatible.equals("P3")) || (orderPxCompatible.equals("P7") && orderPyCompatible.equals("P9")) || (orderPxCompatible.equals("P3") && orderPyCompatible.equals("P4"))){
            compatible=compatible + "2";
        }

        else if((orderPxCompatible.equals("P1") && orderPyCompatible.equals("P4")) || (orderPxCompatible.equals("P4") && orderPyCompatible.equals("P5")) || (orderPxCompatible.equals("P4") && orderPyCompatible.equals("P8")) || (orderPxCompatible.equals("P8") && orderPyCompatible.equals("P9"))){
            compatible=compatible + "3";
        }
        else if((orderPxCompatible.equals("P3") && orderPyCompatible.equals("P8")) || (orderPxCompatible.equals("P3") && orderPyCompatible.equals("P5"))){
            compatible=compatible + "23";
        }
        else if((orderPxCompatible.equals("P2") && orderPyCompatible.equals("P4")) || (orderPxCompatible.equals("P2") && orderPyCompatible.equals("P7"))){
            compatible=compatible + "12";
        }

        return compatible;
    }

    /*
    private int getMaxPriority(){

        orderTransform maxPriority = ordersPriority.get(0);
        orderTransform maxPriorityNew;
        int maxPriorityIndex=0;

        for(int i=1; i < ordersPriority.size(); i++){
            maxPriorityNew=ordersPriority.get(i);

            if(Warehouse.getPiece(maxPriorityNew.getPx()) > 0) {
                if ((maxPriorityNew.getIdealEndTime() < maxPriority.getIdealEndTime())) {
                    maxPriority = maxPriorityNew;
                    maxPriorityIndex = i;
                }
            }

        }

        return maxPriorityIndex;
    }
*/

    private int getMaxPriority(){

        orderTransform maxPriority = ordersPriority.get(0);
        int maxPriorityIndex=0;
        orderTransform maxPriorityNew;

        if(maxPriority.getOutOfUnits() && Warehouse.getPiece(maxPriority.getPx()) > 0){
            maxPriority.setOutOfUnits(false);
        }

        for(int i=1; i < ordersPriority.size(); i++){
            maxPriorityNew=ordersPriority.get(i);

            if(maxPriority.getOutOfUnits() && !maxPriorityNew.getOutOfUnits()){
                maxPriority=maxPriorityNew;
                maxPriorityIndex=i;
            }

            if(Warehouse.getPiece(maxPriorityNew.getPx()) > 0) {
                if ((maxPriorityNew.getIdealEndTime() < maxPriority.getIdealEndTime())) {
                    maxPriority = maxPriorityNew;
                    maxPriorityIndex = i;
                }
            }

        }

        return maxPriorityIndex;
    }

}


