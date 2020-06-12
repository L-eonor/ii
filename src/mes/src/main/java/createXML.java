package MES;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;

public class createXML {

    public static void CreateXML(String pathname) throws Exception {
    //public static void main(String[] args) throws Exception {

        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
        Document doc = documentBuilder.newDocument();

        //<Current_Stores> <WorkPiece type=”Px” quantity=”XX”/> <WorkPiece type=”Px” quantity=”XX”/> ... </Current_Stores>

        // root element
        Element rootElement = doc.createElement("Current_Stores");
        doc.appendChild(rootElement);

        // Transform element
        Element workPiece = doc.createElement("WorkPiece");
        Attr attrType2 = doc.createAttribute("type");
        attrType2.setValue("Py");
        workPiece.setAttributeNode(attrType2);
        Attr attrType = doc.createAttribute("quantity");
        attrType.setValue("XX");
        workPiece.setAttributeNode(attrType);
        rootElement.appendChild(workPiece);

        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        DOMSource source = new DOMSource(doc);

        StreamResult streamResult = new StreamResult(new File(pathname));
        //StreamResult streamResult = new StreamResult(new File("C:\\XML\\requeststores.xml"));

        transformer.transform(source, streamResult);
    }
}
