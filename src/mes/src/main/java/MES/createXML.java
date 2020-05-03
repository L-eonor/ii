package MES;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;

public class createXML {
    String pathname;
    public createXML(String pathname) {
        this.pathname = pathname;
        send();
    }

    public void send() {
        //public static void main(String[] args) throws Exception {

        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder documentBuilder = null;
        try {
            documentBuilder = documentBuilderFactory.newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }
        Document doc = documentBuilder.newDocument();

        //<Current_Stores> <WorkPiece type=”Px” quantity=”XX”/> <WorkPiece type=”Px” quantity=”XX”/> ... </Current_Stores>

        // root element
        Element rootElement = doc.createElement("Current_Stores");
        doc.appendChild(rootElement);

        for(int i=1; i <= 9;i++) {
            // Transform element
            Element workPiece = doc.createElement("WorkPiece");

            Attr attrType1 = doc.createAttribute("type");
            attrType1.setValue("P"+i);
            workPiece.setAttributeNode(attrType1);

            Attr attrType2 = doc.createAttribute("quantity");
            attrType2.setValue(Integer.toString(i));
            workPiece.setAttributeNode(attrType2);

            rootElement.appendChild(workPiece);
        }

        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = null;
        try {
            transformer = transformerFactory.newTransformer();
        } catch (TransformerConfigurationException e) {
            e.printStackTrace();
        }
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");

        DOMSource source = new DOMSource(doc);

        StreamResult streamResult = new StreamResult(new File(pathname));
        //StreamResult streamResult = new StreamResult(new File("C:\\Users\\kicop\\Desktop\\requeststores.xml"));

        try {
            transformer.transform(source, streamResult);
        } catch (TransformerException e) {
            e.printStackTrace();
        }
    }
}
