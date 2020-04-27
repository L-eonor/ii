package MES;

import java.util.List;
import java.util.concurrent.ExecutionException;

import org.eclipse.milo.opcua.sdk.client.OpcUaClient;
import org.eclipse.milo.opcua.sdk.client.api.config.OpcUaClientConfig;
import org.eclipse.milo.opcua.stack.client.DiscoveryClient;
import org.eclipse.milo.opcua.stack.core.types.builtin.DataValue;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.eclipse.milo.opcua.stack.core.types.enumerated.TimestampsToReturn;
import org.eclipse.milo.opcua.stack.core.types.structured.EndpointDescription;

public class OPCUA_Connection {

    private static OpcUaClient client;
    private static Object ValueL;
    private String Client_Name;
    private static int id_node = 4;
    private static String aux = "|var|CODESYS Control Win V3 x64.Application.";

    public static OpcUaClient getClient() {
        return client;
    }

    public static void setClient(OpcUaClient client) {
        OPCUA_Connection.client = client;
    }

    public static Object getValueL() {
        return ValueL;
    }

    public static void setValueL(Object valueL) {
        ValueL = valueL;
    }

    public OPCUA_Connection(String client_Name) {
        super();
        Client_Name = client_Name;
    }

    //Função de Conexão
    public OpcUaClient MakeConnection() throws Exception {

        List<EndpointDescription> endpoints = null;
        try {
            endpoints = DiscoveryClient.getEndpoints(Client_Name).get();

        } catch (Throwable ex) {

        }
        OpcUaClientConfig config = OpcUaClientConfig.builder()
                .setEndpoint(endpoints.get(0))
                .build();

        return client = OpcUaClient.create(config);


    }


    /*Função para ler o valor de uma variavel em especifico de uma célula em especifico
     * String celulaName -> contém o nome do POU da célula
     * String VarName -> contém o nome da variavál que se pretende ler o valor
     */
    public static void get_Value(String cellName, String VarName) {
        String aux1;
        aux1 = aux + cellName + "." + VarName;
        NodeId nodeidstring = new NodeId(id_node, aux);
        DataValue value;
        try {
            value = client.readValue(0, TimestampsToReturn.Both, nodeidstring).get();
            setValueL(value);
            ValueL = ((DataValue)getValueL()).getValue().getValue();
            System.out.println("O valor da variável é: " + ValueL);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (ExecutionException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    public static boolean get_Value_bool(String cellName, String VarName) {
        String aux1;
        aux1 = aux + cellName + "." + VarName;
        NodeId nodeidstring = new NodeId(id_node, aux);
        DataValue value;
        try {
            value = client.readValue(0, TimestampsToReturn.Both, nodeidstring).get();
            setValueL(value);
            ValueL = ((DataValue)getValueL()).getValue().getValue();
            System.out.println("O valor da variável é: " + ValueL);

        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (ExecutionException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return (boolean) ValueL;

    }

    /*Função para inserir valores
     * String celulaName -> contém o nome do POU da célula
     * String VarName -> contém o nome da variavál que se pretende mudar o valor
     * boolean ValueSet -> contém o valor "true" ou "false" que se pretende atribuir à variável
     */
    static void setValue(String cellName, String VarName, boolean ValueSet) {
        //String aux2;
        //aux2 = aux + cellName + "." + VarName;
        NodeId nodeidstring = new NodeId(id_node, aux);

        boolean i = ValueSet;
        Variant v = new Variant(i);
        DataValue dv = new DataValue(v);

        try {
            getClient().writeValue(nodeidstring, dv).get();
            //System.out.println("Variavel alterada para: " + ((DataValue) client.readValue(0, TimestampsToReturn.Both, nodeidstring).get()).getValue().getValue());
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (ExecutionException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public static void setValue_int(String cellName, String VarName, Integer ValueSet) {
        String aux2;
        aux2 = aux + cellName + "." + VarName;
        NodeId nodeidstring = new NodeId(id_node, aux);

        Integer i = ValueSet;
        Variant v = new Variant( i);
        DataValue dv = new DataValue(v);

        try {
            getClient().writeValue(nodeidstring, dv).get();
            //System.out.println("Variavel alterada para: " + ((DataValue) client.readValue(0, TimestampsToReturn.Both, nodeidstring).get()).getValue().getValue());
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (ExecutionException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public static void setValue_string(String cellName, String VarName, String ValueSet) {
        //String aux2;
        //aux2 = aux + cellName + "." + VarName;
        NodeId nodeidstring = new NodeId(id_node, aux);

        String i = ValueSet;
        Variant v = new Variant( i);
        DataValue dv = new DataValue(v);

        try {
            getClient().writeValue(nodeidstring, dv).get();
            System.out.println("Variavel alterada para: " + ((DataValue) client.readValue(0, TimestampsToReturn.Both, nodeidstring).get()).getValue().getValue());
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (ExecutionException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

}