package MES;

import io.netty.handler.codec.http.CombinedHttpHeaders;
import org.eclipse.milo.opcua.sdk.client.OpcUaClient;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.PriorityBlockingQueue;


public class Main {
    public static List<orderTransform> ordersPriority = Collections.synchronizedList(new ArrayList<>());// to keep Incoming Transformation Orders
    public static List<orderTransform> orderListTransformationOutOfUnits = Collections.synchronizedList(new ArrayList<>());
    public static List<orderTransform> orderListTransformationEnded = Collections.synchronizedList(new ArrayList<>()); //to keep Transformation Orders after being sent
    public static List<orderUnload> orderListUnloadEnded = Collections.synchronizedList(new ArrayList<>()); //to keep Unload orders after being sent
    public static List<orderUnload> orderListUnload = Collections.synchronizedList(new ArrayList<>()); //to keep unload orders in waiting state
    public static ConcurrentHashMap<Integer, Integer> receivedOrderPieces = new ConcurrentHashMap<>(); //
    public static List<orderLoad> orderListLoad = Collections.synchronizedList(new ArrayList<>());

    public static int unitCount=0;


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

        //ExecutorService executorService = Executors.newFixedThreadPool(10);

        //Starts thread that reads XML files with orders from ERP
        //executorService.submit(server);
        new Thread(server).start();

        //Start thread that updates Floor
        //readSystem floorUpdate = new readSystem();
        //executorService.submit(floorUpdate);
        new Thread(new readSystem()).start();

        //Start thread that handles Unload Orders
        //UnloadThread unloadHandler = new UnloadThread();
        //executorService.submit(unloadHandler);
        new Thread(new UnloadThread()).start();

        //Start thread that handles Load Orders
        //LoadThread loadHandler = new LoadThread();
        //executorService.submit(loadHandler);
        new Thread(new LoadThread()).start();

        //PusherThread1 pusherHandler1 = new PusherThread1();
        //executorService.submit(pusherHandler1);

        //PusherThread2 pusherHandler2 = new PusherThread2();
        //executorService.submit(pusherHandler2);

        //PusherThread3 pusherHandler3 = new PusherThread3();
        //executorService.submit(pusherHandler3);

        //WarehouseIn warehouseInHandler = new WarehouseIn();
        //executorService.submit(warehouseInHandler);
        new Thread(new WarehouseIn()).start();


    }

}

