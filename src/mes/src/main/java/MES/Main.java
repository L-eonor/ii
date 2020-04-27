package MES;

import MES.*;
import org.eclipse.milo.opcua.sdk.client.OpcUaClient;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class Main {

    public static List<orderTransform> orderListTransformation = Collections.synchronizedList( new ArrayList<orderTransform>());
    public static List<orderUnload> orderListUnload = Collections.synchronizedList( new ArrayList<orderUnload>());

    public static OPCUA_Connection MyConnection;
    public static OpcUaClient client;
    public static String pathString="";
    public static String aux = "DESKTOP-LPATDUL";
    public static String Client = "opc.tcp://DESKTOP-RNTM3PU:4840";
    public static SFS floor = new SFS();



    public static void main(String[] args) throws Exception {

        // Creates object for connection and makes the connection
        MyConnection = new OPCUA_Connection(Client);
        OpcUaClient connection = MyConnection.MakeConnection();
        connection.connect().get();

        int port = 54321;

        udpServer server = new udpServer(port);
        //sendXML client = new sendXML(port, "C:\\XML\\received_data.xml");

        ExecutorService executorService = Executors.newFixedThreadPool(2);
        //executorService.submit(client);
        executorService.submit(server);

        TransformationsGraph transformTable = new TransformationsGraph();

        while(true) {

            //Example for transformation order

            int[] start = {1, 1};
            int[] end = {0, 7};



            if(!orderListUnload.isEmpty()) {

                StringBuilder pathString = new StringBuilder();
                System.out.println("Tamanho "+orderListUnload.size());
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
                Path_Logic path = new Path_Logic(start, goal);
                pathString.append(path.getStringPath());

                System.out.println("[Unload] Esta é a string: " + pathString);
                //OPCUA_Connection.setValue_string("", "", pathString.toString());

            }

            if(!orderListTransformation.isEmpty()){

                orderTransform  order= orderListTransformation.remove(0);
                //Order attributes
                int orderMaxDelay = order.getMaxDelay();
                int orderUnits = order.getNTotal();
                String orderPx = order.getPx();
                String orderPy = order.getPy();

                if(transformTable.searchTransformations(orderPx, orderPy)) {
                    System.out.println("Searched transformations. Found "+ transformTable.solutions.size() +" solutions.");
                    /* prints all transformations
                    Iterator value = transformTable.solutions.iterator();
                    while(value.hasNext()) {
                        System.out.println(transformTable.solutions.poll());
                    }*/

                }
                else  System.out.println(" No need for transformations. ");


                //String with the whole path of the Transformation order
                StringBuilder pathString = new StringBuilder();
                GraphSolution transformationResult = transformTable.solutions.poll();
                for(int j=0; j < transformationResult.transformations.size(); j++) {
                    //Identify machine
                    String machine = transformationResult.machines.get(j);
                    //Relate machine type with respective position
                    int[] goal = floor.getMachinePositions(machine);
                    if(goal == null) System.out.println("Error: order machine input not valid. ");
                    //Calculate path to Machine
                    Path_Logic path = new Path_Logic(start, goal);
                    pathString.append(path.getStringPath());
                    //Add Tool and Time to string respectively
                    pathString.append(transformationResult.tool.get(j)).append(transformationResult.timeCost.get(j));
                    start = goal;
                }
                //Path to Warehouse In cell

                Path_Logic pathEnd = new Path_Logic(start, end);
                pathString.append(pathEnd.getStringPath());

                System.out.println("[Transformation] Esta é a string: " + pathString);
                OPCUA_Connection.setValue_string("", "", pathString.toString());
            }

            boolean cell81=floor.getCell(1,8).getUnitPresence();
            boolean cell87=floor.getCell(7,8).getUnitPresence();
            StringBuilder pathStringLoad = new StringBuilder();
            if(cell81) {
                start = floor.getCell(1,8).position;
                Path_Logic pathLoad = new Path_Logic(start, end);
                pathStringLoad.append(pathLoad.getStringPath());
            }
            else if(cell87) {
                start = floor.getCell(1,8).position;
                Path_Logic pathLoad = new Path_Logic(start, end);
                pathStringLoad.append(pathLoad.getStringPath());
            }
            //System.out.println("[Load] Esta é a string: " + pathStringLoad);
            //OPCUA_Connection.setValue_string("", "", pathStringLoad.toString());

        }



        /*
        //UDP MAIN

        int port = 54321;

        udpServer server = new udpServer(port);
        //sendXML client = new sendXML(port, "C:\\XML\\received_data.xml");

        ExecutorService executorService = Executors.newFixedThreadPool(2);
        //executorService.submit(client);
        executorService.submit(server);
         */

    }
}
