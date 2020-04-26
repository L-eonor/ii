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

    public static OPCUA_Connection MyConnection ;
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

            if(!orderListTransformation.isEmpty()){

                orderTransform  order= orderListTransformation.remove(0);
                String orderPx = order.getPx();
                String orderPy = order.getPy();
                System.out.println(orderPy);
                if(transformTable.searchTransformations(orderPx, orderPy)) {
                    System.out.println("Searched transformations. Found "+ transformTable.solutions.size() +" solutions.");
                    /* prints all transformations
                    Iterator value = transformTable.solutions.iterator();
                    while(value.hasNext()) {
                        System.out.println(transformTable.solutions.poll());
                    }*/

                }
                else  System.out.println(" No need for transformations. ");


                //String do caminho total conforme transformação
                StringBuilder pathString = new StringBuilder();
                GraphSolution transformationResult = transformTable.solutions.poll();
                for(int j=0; j < transformationResult.transformations.size(); j++) {
                    //Identify machine
                    String machine = transformationResult.machines.get(j);
                    //Relate machine type with respective position
                    int[] goal = floor.getMachinePositions(machine);
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

                System.out.println("Esta é a string: " + pathString);
                OPCUA_Connection.setValue_string("", "", pathString.toString());
            }
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
