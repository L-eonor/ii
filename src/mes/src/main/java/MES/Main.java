package MES;

import MES.*;
import org.eclipse.milo.opcua.sdk.client.OpcUaClient;

import java.util.ArrayList;
import java.util.Collections;
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

       // ExecutorService executorService = Executors.newFixedThreadPool(2);
        //executorService.submit(client);
        //executorService.submit(server);

        int[] start = {1, 1};
        int[] stop = {1, 3};
        int[] goal = {8,1};



        System.out.println(" - - - - - - - - - - - - - - - - - - - ");
        System.out.println("Starting shortest path finding . . .");
        System.out.println(" - - - - - - - - - - - - - - - - - - - ");

        //while(orderListTransformation.get(i) != null) {
            //int orderType = orderListTransformation.get(i).getType();
            //String orderPx = orderListTransformation.get(i).getPx();
            //String orderPy = orderListTransformation.get(i).getPy();
            //if (orderPx == "P1" && orderPy == "P2") {
                Path_Logic path1 = new Path_Logic(start, stop);
                Path_Logic path2 = new Path_Logic(stop, goal);
                String totalPath=path1.getStringPath() + "115" +path2.getStringPath();

            //}
       // }
        //pathString.replaceFirst("11", "");
        String s = totalPath.replaceFirst("11", "");

        System.out.println("Esta é a string: "+ s);



        //Inicialização das variáveis
        String cellName, variable;
        int value;
        cellName = "x";
        variable = "Hello";
        value= 234;

        //Funções: get_Value para saber o valor de uma variavel setValue para escrever o valor numa variavel
        //System.out.println("--------------Value Get--------------");
        //MES.OPCUA_Connection.get_Value(cellName,pathString);
        System.out.println("--------------Value Change--------------");
        OPCUA_Connection.setValue_string(cellName,variable, s);


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
