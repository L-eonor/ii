import org.eclipse.milo.opcua.sdk.client.OpcUaClient;

public class Main {

    public static OPCUA_Connection MyConnection ;
    public static OpcUaClient client;
    public static String pathString="";
    public static String aux = "DESKTOP-LPATDUL";
    public static String Client = "opc.tcp://DESKTOP-RNTM3PU:4840";

    public static void main(String[] args) throws Exception {

        int[] start = {1, 1};
        int[] goal = {7, 2};

        System.out.println(" - - - - - - - - - - - - - - - - - - - ");
        System.out.println("Starting shortest path finding . . .");
        System.out.println(" - - - - - - - - - - - - - - - - - - - ");
        Path_Logic path= new Path_Logic(start,goal);

        if(path.findPath()) {
            System.out.print("Path to solution is: ");
            int sizePath = path.getPath().size();
            for (int i = 0; i < sizePath; i++) {
                Node nodePopped = path.getPath().pop();
                pathString = pathString + Integer.toString(nodePopped.getPosition()[1]) + Integer.toString(nodePopped.getPosition()[0]);
            }
        }
        System.out.println(pathString);
/*
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
/*        System.out.println("--------------Value Get--------------");
        OPCUA_Connection.get_Value(cellName,variable);
        System.out.println("--------------Value Change--------------");
        OPCUA_Connection.setValue_int(cellName,variable,value);
*/
    }
}
