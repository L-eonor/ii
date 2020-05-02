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
    public static SFS floor = new SFS();

    //OPC-UA related
    //public static String aux = "DESKTOP-LPATDUL";
    public static String Client = "opc.tcp://DESKTOP-RNTM3PU:4840";
    //public static String Client = "opc.tcp://LAPTOP-UGJ82VH1:4840";
    public static OPCUA_Connection MyConnection = new OPCUA_Connection(Client);


    public static void main(String[] args) throws Exception {
        //Start Global Timer
        stopWatch.start();

        //Creates object for connection and makes the connection
        OpcUaClient connection = MyConnection.MakeConnection();
        connection.connect().get();


        int port = 54321;

        udpServer server = new udpServer(port);
        //sendXML client = new sendXML(port, "C:\\XML\\received_data.xml"); // linha de testes

        ExecutorService executorService = Executors.newFixedThreadPool(6);
        //executorService.submit(client);
        executorService.submit(server);
        //executorService.submit(client); //linha de testes
        //executorService.submit(server);


        /* TESTE DE FUNÇÕES PARA OPC-UA
        // (funcionar) getValue, getValueBoolean, setValueBoolean, getValueString, setValueString, getValueInt, setValueInt
        // (não Funcionar)
        System.out.println("--------------Value Change--------------");
        OPCUA_Connection.getValueBoolean("MAIN_TASK","AT1.SENSOR");
        System.out.println("--------------Value Change--------------");
        int x = OPCUA_Connection.getValueInt("MAIN_TASK","UNIT_COUNT_MES");
        System.out.println("----" + x);
         */

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

