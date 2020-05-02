package MES;

import java.util.List;
import java.util.concurrent.ExecutionException;

import org.bouncycastle.asn1.eac.UnsignedInteger;
import org.eclipse.milo.opcua.sdk.client.OpcUaClient;
import org.eclipse.milo.opcua.sdk.client.api.config.OpcUaClientConfig;
import org.eclipse.milo.opcua.stack.client.DiscoveryClient;
import org.eclipse.milo.opcua.stack.core.types.builtin.DataValue;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UShort;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned;
import org.eclipse.milo.opcua.stack.core.types.enumerated.TimestampsToReturn;
import org.eclipse.milo.opcua.stack.core.types.structured.EndpointDescription;
import org.eclipse.milo.opcua.stack.core.util.annotations.UInt16Primitive;

public class OPCUA_Connection {

    private static OpcUaClient client;
    private static Object ValueL;
    private String Client_Name;
    private static int id_node = 4;
    private static String aux = "|var|CODESYS Control Win V3 x64.Application.";

    public OPCUA_Connection(String client_Name) {
        super();
        Client_Name = client_Name;
    }

    //Connection Function
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


    /*Função para ler o valor de uma variavel em especifico de uma célula em especifico
     * String celulaName -> contém o nome do POU da célula
     * String VarName -> contém o nome da variavál que se pretende ler o valor
     */
    public static void getValue(String cellName, String VarName) {
        String aux1;
        aux1 = aux + cellName + "." + VarName;
        NodeId nodeidstring = new NodeId(id_node, aux1);
        DataValue value;
        try {
            value = client.readValue(0, TimestampsToReturn.Both, nodeidstring).get();
            setValueL(value);
            ValueL = ((DataValue)getValueL()).getValue().getValue();
            //System.out.println("O valor da variável é: " + ValueL);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (ExecutionException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    public static int getValueInt(String cellName, String VarName) {
        String aux1;
        aux1 = aux + cellName + "." + VarName;
        NodeId nodeidstring = new NodeId(id_node, aux1);
        DataValue value;
        try {
            value = client.readValue(0, TimestampsToReturn.Both, nodeidstring).get();
            setValueL(value);
            ValueL = ((DataValue)getValueL()).getValue().getValue();
            //System.out.println("O valor da variável é: " + ValueL);

        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (ExecutionException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        String vString = String.valueOf(ValueL); //Passar do tipo UShort para String
        int vInt =  Integer.parseInt(vString); //Passar de String para int (não encontrámos forma direta)
        return vInt;
    }


    public static boolean getValueBoolean(String cellName, String VarName) {
        String aux1;
        aux1 = aux + cellName + "." + VarName;
        NodeId nodeidstring = new NodeId(id_node, aux1);
        DataValue value;
        try {
            value = client.readValue(0, TimestampsToReturn.Both, nodeidstring).get();
            setValueL(value);
            ValueL = ((DataValue)getValueL()).getValue().getValue();
            //System.out.println("O valor da variável é: " + ValueL);

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
    static void setValueBoolean(String cellName, String VarName, boolean ValueSet) {
        String aux2;
        aux2 = aux + cellName + "." + VarName;
        NodeId nodeidstring = new NodeId(id_node, aux2);

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

    public static void setValueInt(String cellName, String VarName, int ValueSet) {
        String aux2;
        aux2 = aux + cellName + "." + VarName;
        NodeId nodeidstring = new NodeId(id_node,aux2);

        Variant v = new Variant(UShort.valueOf(ValueSet));
        DataValue dv = new DataValue(v);

        try {
            getClient().writeValue(nodeidstring, dv).get();
            //System.out.println("Variável alterada para: " + ((DataValue) client.readValue(0, TimestampsToReturn.Both, nodeidstring).get()).getValue().getValue());
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (ExecutionException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public static void setValueString(String cellName, String VarName, String ValueSet) {
        String aux2;
        aux2 = aux + cellName + "." + VarName;
        NodeId nodeidstring = new NodeId(id_node, aux2);

        Variant v = new Variant(ValueSet);
        DataValue dv = new DataValue(v);

        try {
            getClient().writeValue(nodeidstring, dv).get();
            //System.out.println("Variável alterada para: " + ((DataValue) client.readValue(0, TimestampsToReturn.Both, nodeidstring).get()).getValue().getValue());
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (ExecutionException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public static void setValueInt16(String cellName, String VarName, int ValueSet) {
        String aux2;
        aux2 = aux + cellName + "." + VarName;
        NodeId nodeidstring = new NodeId(id_node,aux2);

        short i = (short) ValueSet;
        Variant v = new Variant(i);
        DataValue dv = new DataValue(v);

        try {
            getClient().writeValue(nodeidstring, dv).get();
            //System.out.println("Variável alterada para: " + ((DataValue) client.readValue(0, TimestampsToReturn.Both, nodeidstring).get()).getValue().getValue());
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (ExecutionException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

}