package MES;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.*;
import java.nio.file.Files;
import java.nio.file.Paths;

public class udpClient implements Runnable {
    private final int serverPort;
    public final String pathname;
    public final InetAddress addressFromOrder;
    private final DatagramSocket serverSocket;
    private final int clientPort;

    public udpClient(int serverPort, String pathname, InetAddress addressFromOrder, DatagramSocket serverSocket, int clientPort) {
        this.serverPort = serverPort;
        this.pathname = pathname;
        this.addressFromOrder = addressFromOrder;
        this.serverSocket = serverSocket;
        this.clientPort=clientPort;
    }

    @Override
    public void run() {

        try (DatagramSocket clientSocket = new DatagramSocket(50000)) {

            byte[] array = Files.readAllBytes(Paths.get(pathname));
            DatagramPacket datagramPacket = new DatagramPacket(
                    array,
                    array.length,
                    addressFromOrder,
                    clientPort
            );
            serverSocket.send(datagramPacket);
            System.out.println("XML Sent");

        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}

