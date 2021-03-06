package MES;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static MES.Main.*;
import static MES.createXML.*;

// This piece of code reads the XML and adds the orders to the respective list
// The parameters are just mockups

public class readXML {

    public static void ReadXML(String pathname, InetAddress addressFromOrder, DatagramSocket serverSocket, int clientPort) throws Exception {

        File xmlFile = new File(pathname);

        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
        Document document = documentBuilder.parse(xmlFile);

        // Envia info sobre as peças em armazém quando pedido
        if (document.getElementsByTagName("Request_Stores").getLength() != 0) {
            createXML xml = new createXML("requeststores.xml");

            udpClient client = new udpClient(54321, "requeststores.xml", addressFromOrder, serverSocket, clientPort);
            client.run();
            System.out.println("[REQUEST STORES] from " + addressFromOrder);
            //createXML xml = new createXML("C:\\Users\\cjoao\\Desktop\\ii-Teste\\src\\mes\\requeststores.xml");
            //udpClient client = new udpClient(54321, "C:\\Users\\cjoao\\Desktop\\ii-Teste\\src\\mes\\requeststores.xml");
        }

        // Lê as ordens restantes do XML e cria um novo elemento do tipo ordem
        NodeList list = document.getElementsByTagName("Order");

        order order;

        for (int i = 0; i < list.getLength(); i++) {
            Node node = list.item(i);


            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element element = (Element) node;

                float submitTime = StopWatch.getTimeElapsed();

                if (((Element) node).getElementsByTagName("Unload").getLength() != 0) {
                    NodeList list2 = ((Element) node).getElementsByTagName("Unload");

                    Node node2 = list2.item(0);
                    Element element2 = (Element) node2;
                    System.out.println("[Unload Order] Type: " + element2.getAttribute("Type") + " | Destination: " + element2.getAttribute("Destination") +" | Quantity: " + element2.getAttribute("Quantity"));

                    order = new orderUnload(Integer.parseInt(element.getAttribute("Number")),
                            submitTime,
                            2,
                            1,
                            element2.getAttribute("Type"),
                            element2.getAttribute("Destination"),
                            Integer.parseInt(element2.getAttribute("Quantity")),
                            0);


                    orderListUnload.add((orderUnload) order);

                    //add on Database
                    orderUnload order_aux = (orderUnload) order;
                    dbConnection.insertNew_OrderUnloadDB(order_aux.getId(), order_aux.getPx(), order_aux.getDy(), order_aux.getQuantity());
                }

                if (((Element) node).getElementsByTagName("Transform").getLength() != 0) {

                    NodeList list1 = ((Element) node).getElementsByTagName("Transform");

                    Node node1 = list1.item(0);
                    Element element1 = (Element) node1;

                    System.out.println("[Transformation Order] From: " + element1.getAttribute("From") + " | To: " + element1.getAttribute("To")+ " | Quantity: " + element1.getAttribute("Quantity") + " | MaxDelay: " + element1.getAttribute("MaxDelay"));

                    order = new orderTransform(Integer.parseInt(element.getAttribute("Number")),
                            submitTime,
                            1,
                            1,
                            element1.getAttribute("From"),
                            element1.getAttribute("To"),
                            Integer.parseInt(element1.getAttribute("Quantity")),
                            0,
                            Integer.parseInt(element1.getAttribute("MaxDelay")));

                    ordersPriority.add((orderTransform) order);

                    //add on Database
                    orderTransform order_aux = (orderTransform) order;
                    dbConnection.insertNew_OrderTransformationDB(order_aux.getId(), order_aux.getPx(), order_aux.getPy(), order_aux.getNTotal(), order_aux.getMaxDelay());

                }

            }

        }
    }


}
