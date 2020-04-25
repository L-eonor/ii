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



            int[] start = {1, 1};
            int[] stop = {1, 3};
            int[] goal = {8, 1};

            if(!orderListTransformation.isEmpty()){

                orderTransform  order= orderListTransformation.remove(0);
                String orderPx = order.getPx();
                String orderPy = order.getPy();
                System.out.println(orderPy);
                if(transformTable.searchTransformations(orderPx, orderPy)) {
                    System.out.println("Searched transformations. ");
                    System.out.println("Found "+ transformTable.solutions.size() +" solutions");
                    Iterator value = transformTable.solutions.iterator();
                    while(value.hasNext()) {
                        System.out.println(transformTable.solutions.poll());
                    }
                }
                else  System.out.println(" No need for transformations. ");



                if ((orderPy.equals("P2")) && orderPx.equals("P1")) {
                    Path_Logic path1 = new Path_Logic(start, stop);
                    String path1StringPath = path1.getStringPath();
                    path1StringPath = path1StringPath.replaceFirst("11", "");
                    Path_Logic path2 = new Path_Logic(stop, goal);
                    String path2StringPath = path2.getStringPath();
                    path2StringPath = path2StringPath.replaceFirst("13", "");
                    String s=path1StringPath+"115"+path2StringPath;
                    System.out.println("Esta é a string: " + s);
                    OPCUA_Connection.setValue_string("", "", s);
                }
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
