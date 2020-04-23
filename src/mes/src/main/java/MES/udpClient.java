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

    public udpClient(int serverPort, String pathname) {
        this.serverPort = serverPort;
        this.pathname = pathname;
    }

    @Override
    public void run() {

        try (DatagramSocket clientSocket = new DatagramSocket(50000)) {

            byte[] array = Files.readAllBytes(Paths.get(pathname));

            DatagramPacket datagramPacket = new DatagramPacket(
                    array,
                    array.length,
                    InetAddress.getLocalHost(),
                    serverPort
            );
            clientSocket.send(datagramPacket);

        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}

