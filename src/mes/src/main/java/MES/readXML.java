package MES;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static MES.Main.orderListTransformation;
import static MES.Main.orderListUnload;
import static MES.createXML.*;

// This piece of code reads the XML and adds the orders to the respective list
// The parameters are just mockups

public class readXML {

    public static void ReadXML(String pathname) throws Exception {

        File xmlFile = new File(pathname);

        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
        Document document = documentBuilder.parse(xmlFile);

        // Envia info sobre as peças em armazém quando pedido
        if (document.getElementsByTagName("Request_Stores").getLength() != 0) {
            CreateXML("C:\\XML\\requeststores.xml");
            udpClient client = new udpClient(54321, "C:\\XML\\requeststores.xml");
            ExecutorService executorService = Executors.newFixedThreadPool(2);
            executorService.submit(client);
            executorService.shutdown();
        }

        // Lê as ordens restantes do XML e cria um novo elemento do tipo ordem
        NodeList list = document.getElementsByTagName("Order");

        order order;

        for (int i = 0; i < list.getLength(); i++) {
            Node node = list.item(i);

            System.out.println("\nCurrent Element: " + node.getNodeName());

            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element element = (Element) node;
                System.out.println("Number: " + element.getAttribute("Number"));

                if (((Element) node).getElementsByTagName("Unload").getLength() != 0) {
                    System.out.println("Ordem do tipo descarga!");
                    NodeList list2 = ((Element) node).getElementsByTagName("Unload");

                    Node node2 = list2.item(0);
                    Element element2 = (Element) node2;
                    System.out.println("Type: " + element2.getAttribute("Type"));
                    System.out.println("Destination: " + element2.getAttribute("Destination"));
                    System.out.println("Quantity: " + element2.getAttribute("Quantity"));

                    order = new orderUnload(Integer.parseInt(element.getAttribute("Number")),
                            0,
                            00,
                            00,
                            00,
                            1,
                            1,
                            element2.getAttribute("Type"),
                            element2.getAttribute("Destination"),
                            Integer.parseInt(element2.getAttribute("Quantity")),
                            0,
                            "C1T1");

                    orderListUnload.add((orderUnload) order);
                }

                if (((Element) node).getElementsByTagName("Transform").getLength() != 0) {
                    System.out.println("Ordem do tipo transformação!");
                    NodeList list1 = ((Element) node).getElementsByTagName("Transform");
                    Node node1 = list1.item(0);
                    Element element1 = (Element) node1;
                    System.out.println("From: " + element1.getAttribute("From"));
                    System.out.println("To: " + element1.getAttribute("To"));
                    System.out.println("Quantity: " + element1.getAttribute("Quantity"));
                    System.out.println("MaxDelay: " + element1.getAttribute("MaxDelay"));

                    order = new orderTransform(Integer.parseInt(element.getAttribute("Number")),
                            Integer.parseInt(element1.getAttribute("MaxDelay")),
                            00,
                            00,
                            00,
                            1,
                            1,
                            element1.getAttribute("From"),
                            element1.getAttribute("To"),
                            Integer.parseInt(element1.getAttribute("Quantity")),
                            00,
                            00,
                            Integer.parseInt(element1.getAttribute("MaxDelay")),
                            "C1T1");

                    orderListTransformation.add((orderTransform) order);
                }
            }
            //if (orderListTransformation.size() != 0) System.out.println("The first element of the transformation list is:" + orderListTransformation.get(0).getId());
            //System.out.println("The size of the transformation list is:" + orderListTransformation.size());

            //if (orderListUnload.size() != 0) System.out.println("The first element of the unload list is:" + orderListUnload.get(0).getId());
            //System.out.println("The size of the transformation list is:" + orderListUnload.size());
        }
    }


}
