package MES;

import org.bouncycastle.asn1.eac.UnsignedInteger;
import org.eclipse.milo.opcua.sdk.client.OpcUaClient;
import org.eclipse.milo.opcua.sdk.client.api.UaClient;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UShort;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class Main {

    public static List<orderTransform> orderListTransformation = Collections.synchronizedList(new ArrayList<>());
    public static List<orderUnload> orderListUnload = Collections.synchronizedList(new ArrayList<>());

    public static SFS floor = new SFS();
    //public static String aux = "DESKTOP-LPATDUL";
    public static String Client = "opc.tcp://DESKTOP-RNTM3PU:4840";
    //public static String Client = "opc.tcp://LAPTOP-UGJ82VH1:4840";
    public static OPCUA_Connection MyConnection = new OPCUA_Connection(Client);

    public static void main(String[] args) throws Exception {

        //Creates object for connection and makes the connection
        OpcUaClient connection = MyConnection.MakeConnection();
        connection.connect().get();


        int port = 54321;

        udpServer server = new udpServer(port);
        //sendXML client = new sendXML(port, "C:\\XML\\received_data.xml"); // linha de testes

        ExecutorService executorService = Executors.newFixedThreadPool(3);
        //executorService.submit(client);
        executorService.submit(server);
        //executorService.submit(client); //linha de testes
        //executorService.submit(server);
        passPath passPath = new passPath();
        executorService.submit(passPath);


        TransformationsGraph transformTable = new TransformationsGraph();

        /* TESTE DE FUNÇÕES PARA OPC-UA
        // (funcionar) getValue, getValueBoolean, setValueBoolean, getValueString, setValueString, getValueInt, setValueInt
        // (não Funcionar)
        System.out.println("--------------Value Change--------------");
        OPCUA_Connection.getValueBoolean("MAIN_TASK","AT1.SENSOR");
        System.out.println("--------------Value Change--------------");
        int x = OPCUA_Connection.getValueInt("MAIN_TASK","UNIT_COUNT_MES");
        System.out.println("----" + x);
         */

        Thread threadReadSystem = new Thread(new readSystem());
        threadReadSystem.start();



        while(true) {

            //Example for transformation order

            int[] start = {1, 1};
            int[] end = {0, 7};



            if(!orderListUnload.isEmpty()) {

                StringBuilder pathString = new StringBuilder();
                System.out.println("Size of ListUnload: "+orderListUnload.size());
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
                //MyConnection.setValue_string("", "", pathString.toString());

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
                if (transformationResult == null) throw new AssertionError("Error: transformationResult null pointer. ");

                for(int j = 0; j < transformationResult.transformations.size(); j++) {
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
                OPCUA_Connection.setValueString("MAIN_TASK", "AT1_order_path_mes", pathString.toString());
            }


            StringBuilder pathStringLoad1 = new StringBuilder();
            StringBuilder pathStringLoad2 = new StringBuilder();

            if(floor.getCell(1,8).getUnitPresence()) {
                int[] load1 = {7,1};
                Path_Logic pathLoad = new Path_Logic(load1, end);
                pathStringLoad1.append(pathLoad.getStringPath());
                System.out.println("[Load] Esta é a string: " + pathStringLoad1);
                OPCUA_Connection.setValueString("MAIN_TASK", "C7T1b_order_path_mes", pathStringLoad1.toString());
            }
            if(floor.getCell(7,8).getUnitPresence()) {
                int[] load2 = {7,7};
                Path_Logic pathLoad = new Path_Logic(load2, end);
                pathStringLoad2.append(pathLoad.getStringPath());
                System.out.println("[Load] Esta é a string: " + pathStringLoad2);
                OPCUA_Connection.setValueString("MAIN_TASK", "C7T7b_order_path_mes", pathStringLoad2.toString());
            }

            System.out.println("[Test] Elemento no tapete rotativo C2T1: "+floor.getCell(1,2).getUnitPresence());


        }

        //Funções: get_Value para saber o valor de uma variavel setValue para escrever o valor numa variavel
        //System.out.println("--------------Value Get--------------");
        //MyConnection.get_Value(cellName,pathString);
        //System.out.println("--------------Value Change--------------");
        //MyConnection.setValue_int("MAIN_TASK","UNIT_COUNT_MES", 2);


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
