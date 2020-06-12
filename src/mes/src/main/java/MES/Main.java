package MES;

import io.netty.handler.codec.http.CombinedHttpHeaders;
import org.eclipse.milo.opcua.sdk.client.OpcUaClient;

import javax.swing.*;
import java.net.InetAddress;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.PriorityBlockingQueue;

import static javax.swing.UIManager.get;


public class Main {
    public static List<orderTransform> ordersPriority = Collections.synchronizedList(new ArrayList<>());// to keep Incoming Transformation Orders
    public static List<orderTransform> orderListTransformationOutOfUnits = Collections.synchronizedList(new ArrayList<>());
    public static List<orderTransform> orderListTransformationEnded = Collections.synchronizedList(new ArrayList<>()); //to keep Transformation Orders after being sent
    public static List<orderUnload> orderListUnloadEnded = Collections.synchronizedList(new ArrayList<>()); //to keep Unload orders after being sent
    public static List<orderUnload> orderListUnload = Collections.synchronizedList(new ArrayList<>()); //to keep unload orders in waiting state
    public static ConcurrentHashMap<Integer, Integer> receivedOrderPieces = new ConcurrentHashMap<>(); //
    public static List<orderLoad> orderListLoad = Collections.synchronizedList(new ArrayList<>());



    public static Warehouse warehouse = new Warehouse();


    public static int unitCount=0;


    //OPC-UA related
    //public static String aux = "DESKTOP-LPATDUL";
    //public static String Client = "opc.tcp://DESKTOP-RNTM3PU:4840";
    //public static String Client = "opc.tcp://LAPTOP-UGJ82VH1:4840";
    //public static OPCUA_Connection MyConnection = new OPCUA_Connection(Client);

    public static void main(String[] args) throws Exception {
        //Start Global Timer
        StopWatch.start();



        String hostname = "Unknown";
        String restartFlag = "";

        /*

        try{
            InetAddress addr;
            addr = InetAddress.getLocalHost();
            hostname = addr.getHostName();
        }catch(Exception e){
            System.out.println("ERROR: Hostname can not be resolved");
            System.exit(0);
        }

*/


        if (args.length!=2){
            try{
                InetAddress addr;
                addr = InetAddress.getLocalHost();
                hostname = addr.getHostName();

            }catch(Exception e){
                System.out.println("ERROR: Hostname can not be resolved");
                System.exit(0);
            }
        }
        else{
            try{
                //hostname = "DESKTOP-G3Q32TE";
                hostname = args[0];
                System.out.println(hostname);
                System.out.println("Connected to remote host");

                restartFlag = args[1];
                if (restartFlag.equals("y")){
                    cleanStartDB();
                    System.out.println("Database is clean");
                }
                if (restartFlag.equals("n")){
                    retrieveDataDB();
                    System.out.println("Database orders were retrieved to MES");
                }

            }catch(Exception e){
                System.out.println("ERROR: Remote hostname can not be reached");
                System.exit(0);
            }
        }



        String Client = String.format("opc.tcp://%s:4840", hostname);
        OPCUA_Connection MyConnection = new OPCUA_Connection(Client);
        System.out.println("hostname: "+hostname);


        //Creates object for connection and makes the connection
        try {
            OpcUaClient connection = MyConnection.MakeConnection();
            connection.connect().get();
        }catch(Exception e){
            System.out.println("ERROR: OPC-UA connection failed");
            System.exit(0);
        }

        WarehouseIn.missedPieces();

        int port = 54321;

        udpServer server = new udpServer(port);
        //createXML client = new createXML("C:\\Users\\kicop\\Desktop\\requeststores.xml"); // linha de testes


        // Init number of pieces in Warehouse
        initWarehouse();

        //initRoutine();

        //Start thread Server
        new Thread(server).start();

        //Start thread that updates Floor
        new Thread(new readSystem()).start();

        //Start thread that handles Unload Orders
        new Thread(new UnloadThread()).start();

        //Start thread that handles Load Orders
        new Thread(new LoadThread()).start();

        //Start thread that handles Unload Orders arriving
        new Thread(new PusherThread1()).start();

        //Start thread that handles Transformations and Load Orders arriving
        new Thread(new WarehouseIn()).start();


        JFrame mainWindow = new mainWindow();
        mainWindow.setVisible(true);



    }

    private static void cleanStartDB(){
        dbConnection.deleteLoadTableDB();
        dbConnection.deleteTransformationTableDB();
        dbConnection.deleteUnloadTableDB();
    }

    private static void retrieveDataDB(){
        parseDbUnloadOrderTableToMes();
        parseDbTransformationOrderTableToMes();
        parseDbLoadOrderTableToMes();
        parseUnloadStatsTableToMes();
    }

    private static void parseDbUnloadOrderTableToMes(){
        Vector<Vector<Object>> tableElements = dbConnection.readOrderListUnloadDB();

        for (int i = 0; i < tableElements.size(); i++){
            int id = Integer.parseInt(tableElements.get(i).get(0).toString());
            float submitTime = 0;
            float startTime = 0;
            int type = 2;
            String status = String.valueOf(tableElements.get(i).get(3));
            String PX = tableElements.get(i).get(4).toString();
            String DY = tableElements.get(i).get(5).toString();
            Integer qTotal = Integer.parseInt(tableElements.get(i).get(6).toString());
            Integer qDone = Integer.parseInt(tableElements.get(i).get(7).toString());

            int statusInt = 1;
            if(status.equals("por_iniciar"))  statusInt = 1;
            else if (status.equals("em_processamento"))  statusInt = 2;
            else if (status.equals("concluida"))  statusInt = 3;

            orderUnload order = new orderUnload(id, submitTime, type, statusInt, PX, DY, qTotal, 0);
            order.setNDone(qDone);

            orderListUnload.add(order);
        }
    }

    private static void parseDbTransformationOrderTableToMes(){
        Vector<Vector<Object>> tableElements = dbConnection.readOrderListTransformationDB();

        for (int i = 0; i < tableElements.size(); i++){
            int id = Integer.parseInt(tableElements.get(i).get(0).toString());
            float submitTime = 0;
            float startTime = 0;
            int type = 1;
            String status = String.valueOf(tableElements.get(i).get(3));
            String PX = tableElements.get(i).get(4).toString();
            String PY = tableElements.get(i).get(5).toString();
            Integer qTotal = Integer.parseInt(tableElements.get(i).get(6).toString());
            Integer qDone = Integer.parseInt(tableElements.get(i).get(7).toString());
            Integer maxDelay = Integer.parseInt(tableElements.get(i).get(8).toString());

            int statusInt = 1;
            if(status.equals("por_iniciar"))  statusInt = 1;
            else if (status.equals("em_processamento"))  statusInt = 2;
            else if (status.equals("concluida"))  statusInt = 3;

            if (statusInt != 3) {
                orderTransform order = new orderTransform(id, submitTime, type, statusInt, PX, PY, qTotal, 0, maxDelay);
                order.setNDone(qDone);

                ordersPriority.add(order);
            }
        }
    }

    private static void parseUnloadStatsTableToMes(){
        Vector<Vector<Object>> tableElements = dbConnection.readUnloadStatsDB();


        Pusher C7T3 = (Pusher) SFS.getCell(3, 7);
        C7T3.setUnloadUnitCount(Integer.parseInt(tableElements.get(0).get(1).toString()));
        C7T3.setUnloadStats(Integer.parseInt(tableElements.get(0).get(2).toString()), 1);
        C7T3.setUnloadStats(Integer.parseInt(tableElements.get(0).get(3).toString()), 2);
        C7T3.setUnloadStats(Integer.parseInt(tableElements.get(0).get(4).toString()), 3);
        C7T3.setUnloadStats(Integer.parseInt(tableElements.get(0).get(5).toString()), 4);
        C7T3.setUnloadStats(Integer.parseInt(tableElements.get(0).get(6).toString()), 5);
        C7T3.setUnloadStats(Integer.parseInt(tableElements.get(0).get(7).toString()), 6);
        C7T3.setUnloadStats(Integer.parseInt(tableElements.get(0).get(8).toString()), 7);
        C7T3.setUnloadStats(Integer.parseInt(tableElements.get(0).get(9).toString()), 8);
        C7T3.setUnloadStats(Integer.parseInt(tableElements.get(0).get(10).toString()), 9);

        Pusher C7T4 = (Pusher) SFS.getCell(4, 7);
        C7T3.setUnloadUnitCount(Integer.parseInt(tableElements.get(1).get(1).toString()));
        C7T3.setUnloadStats(Integer.parseInt(tableElements.get(1).get(2).toString()), 1);
        C7T3.setUnloadStats(Integer.parseInt(tableElements.get(1).get(3).toString()), 2);
        C7T3.setUnloadStats(Integer.parseInt(tableElements.get(1).get(4).toString()), 3);
        C7T3.setUnloadStats(Integer.parseInt(tableElements.get(1).get(5).toString()), 4);
        C7T3.setUnloadStats(Integer.parseInt(tableElements.get(1).get(6).toString()), 5);
        C7T3.setUnloadStats(Integer.parseInt(tableElements.get(1).get(7).toString()), 6);
        C7T3.setUnloadStats(Integer.parseInt(tableElements.get(1).get(8).toString()), 7);
        C7T3.setUnloadStats(Integer.parseInt(tableElements.get(1).get(9).toString()), 8);
        C7T3.setUnloadStats(Integer.parseInt(tableElements.get(1).get(10).toString()), 9);


        Pusher C7T5 = (Pusher) SFS.getCell(5, 7);
        C7T3.setUnloadUnitCount(Integer.parseInt(tableElements.get(2).get(1).toString()));
        C7T3.setUnloadStats(Integer.parseInt(tableElements.get(2).get(2).toString()), 1);
        C7T3.setUnloadStats(Integer.parseInt(tableElements.get(2).get(3).toString()), 2);
        C7T3.setUnloadStats(Integer.parseInt(tableElements.get(2).get(4).toString()), 3);
        C7T3.setUnloadStats(Integer.parseInt(tableElements.get(2).get(5).toString()), 4);
        C7T3.setUnloadStats(Integer.parseInt(tableElements.get(2).get(6).toString()), 5);
        C7T3.setUnloadStats(Integer.parseInt(tableElements.get(2).get(7).toString()), 6);
        C7T3.setUnloadStats(Integer.parseInt(tableElements.get(2).get(8).toString()), 7);
        C7T3.setUnloadStats(Integer.parseInt(tableElements.get(2).get(9).toString()), 8);
        C7T3.setUnloadStats(Integer.parseInt(tableElements.get(2).get(10).toString()), 9);
    }

    private static void parseDbLoadOrderTableToMes(){
        Vector<Vector<Object>> tableElements = dbConnection.readOrderListLoadDB();

        for (int i = 0; i < tableElements.size(); i++){
            int id = Integer.parseInt(tableElements.get(i).get(0).toString());
            float submitTime = 0;
            float startTime = 0;
            int type = 3;
            String status = String.valueOf(tableElements.get(i).get(3));
            String unitType = tableElements.get(i).get(4).toString();

            int statusInt = 1;
            if(status.equals("por_iniciar"))  statusInt = 1;
            else if (status.equals("em_processamento"))  statusInt = 2;
            else if (status.equals("concluida"))  statusInt = 3;

            if (statusInt != 3) {
                orderLoad order = new orderLoad(id, submitTime, type, statusInt, 0, unitType);
                orderListLoad.add(order);
            }
        }
    }



    private static void initWarehouse(){

        for(int i=1; i < 10; i++){
            String wareHouseUnits = "ARMAZEM.P"+i;
            String type = "P"+i;
            try {
                int nPieces = OPCUA_Connection.getValueInt("MAIN_TASK", wareHouseUnits);
                Warehouse.setPiece(nPieces, type);
            }
            catch(Exception e){

            }

        }

    }



}

