import MES.*;
import org.eclipse.milo.opcua.sdk.client.OpcUaClient;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class Main {

    public static List<order> orderListTransformation = Collections.synchronizedList( new ArrayList<order>());
    public static List<order> orderListUnload = Collections.synchronizedList( new ArrayList<order>());

    public static OPCUA_Connection MyConnection ;
    public static OpcUaClient client;
    public static String pathString="";
    public static String aux = "DESKTOP-LPATDUL";
    public static String Client = "opc.tcp://DESKTOP-RNTM3PU:4840";

    public static void main(String[] args) throws Exception {

        // Creates object for connection and makes the connection
        MyConnection = new MES.OPCUA_Connection(Client);
        OpcUaClient connection = MyConnection.MakeConnection();
        connection.connect().get();


        int[] start = {5, 3};
        int[] goal = {7, 2};

        System.out.println(" - - - - - - - - - - - - - - - - - - - ");
        System.out.println("Starting shortest path finding . . .");
        System.out.println(" - - - - - - - - - - - - - - - - - - - ");
        Path_Logic path = new Path_Logic(start, goal);

        if (path.findPath()) {
            System.out.print("Path to solution is: ");
            int sizePath = path.getPath().size();
            for (int i = 0; i < sizePath; i++) {
                Node nodePopped = path.getPath().pop();
                pathString = pathString + Integer.toString(nodePopped.getPosition()[1]) + Integer.toString(nodePopped.getPosition()[0]);
            }
        }
        System.out.println(pathString);



        //Inicialização das variáveis
        String cellName, variable;
        int value;
        cellName = "";
        variable = "Hello";
        value= 234;

        //Funções: get_Value para saber o valor de uma variavel setValue para escrever o valor numa variavel
        System.out.println("--------------Value Get--------------");
        MES.OPCUA_Connection.get_Value(cellName,variable);
        System.out.println("--------------Value Change--------------");
        MES.OPCUA_Connection.setValue_int(cellName,variable,value); */


        /*
        //UDP MAIN

        int port = 54321;

        udpServer server = new udpServer(port);
        //sendXML client = new sendXML(port, "C:\\XML\\received_data.xml");

        ExecutorService executorService = Executors.newFixedThreadPool(2);
        //executorService.submit(client);
        executorService.submit(server);
         */

        /*
        int port = 54321;

        udpServer server = new udpServer(port);
        //sendXML client = new sendXML(port, "C:\\XML\\received_data.xml");

        ExecutorService executorService = Executors.newFixedThreadPool(2);
        //executorService.submit(client);
        executorService.submit(server); */
    }
}
