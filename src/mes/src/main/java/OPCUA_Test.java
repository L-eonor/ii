/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.util.concurrent.ExecutionException;
import org.eclipse.milo.opcua.sdk.client.OpcUaClient;

/**
 *
 * @author Diogo & Marco
 */

public class OPCUA_Test {

    /**
     * @param args the command line arguments
     */
    public static OPCUA_Connection MyConnection ;
    public static OpcUaClient client;
    public static String aux = "DESKTOP-LPATDUL";
    public static String Client = "opc.tcp://DESKTOP-RNTM3PU:4840";

    public static void main(String[] args) throws Exception {

        MyConnection = new OPCUA_Connection(Client);
        OpcUaClient connection = MyConnection.MakeConnection();
        connection.connect().get();


        //Inicialização das variáveis
        String cellName, variable;
        int value;
        cellName = "eu";
        variable = "Hello";
        value= 234;

        /*Funções: get_Value para saber o valor de uma variavel
         *          setValue para escrever o valor numa variavel
         */
        System.out.println("--------------Value Get--------------");
        OPCUA_Connection.get_Value(cellName,variable);
        System.out.println("--------------Value Change--------------");
        OPCUA_Connection.setValue_int(cellName,variable,value);
    }

}
