package MES;

import org.bouncycastle.asn1.eac.UnsignedInteger;
import org.eclipse.milo.opcua.sdk.client.OpcUaClient;
import org.eclipse.milo.opcua.sdk.client.api.UaClient;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UShort;

import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.Timer;


public class Main {

    public static List<orderTransform> orderListTransformationEnded = Collections.synchronizedList(new ArrayList<>());
    public static List<orderUnload> orderListUnloadEnded = Collections.synchronizedList(new ArrayList<>());
    public static List<orderUnload> orderListUnload = Collections.synchronizedList(new ArrayList<>());
    public static PriorityQueue<orderTransform> ordersPriority = new PriorityQueue<>(new OrderComparator());

    public static StopWatch stopWatch = new StopWatch();
    public static int unitCount;


    //OPC-UA related
    //public static String aux = "DESKTOP-LPATDUL";
    public static String Client = "opc.tcp://DESKTOP-RNTM3PU:4840";
    //public static String Client = "opc.tcp://LAPTOP-UGJ82VH1:4840";
    public static OPCUA_Connection MyConnection = new OPCUA_Connection(Client);


    public static void main(String[] args) throws Exception {
        //Start Global Timer
        StopWatch.start();

        //Creates object for connection and makes the connection
        OpcUaClient connection = MyConnection.MakeConnection();
        connection.connect().get();


        int port = 54321;

        udpServer server = new udpServer(port);
        //createXML client = new createXML("C:\\Users\\kicop\\Desktop\\requeststores.xml"); // linha de testes

        ExecutorService executorService = Executors.newFixedThreadPool(6);

        //Starts thread that reads XML files with orders from ERP
        executorService.submit(server);

        //Start thread that updates Floor
        readSystem floorUpdate = new readSystem();
        executorService.submit(floorUpdate);

        //Start thread that handles Unload Orders
        UnloadThread unloadHandler = new UnloadThread();
        executorService.submit(unloadHandler);

        //Start thread that handles Load Orders
        LoadThread loadHandler = new LoadThread();
        executorService.submit(loadHandler);

    }

}

