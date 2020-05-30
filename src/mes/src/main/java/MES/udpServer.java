package MES;

import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

import static MES.readXML.*;
import static MES.createXML.*;

public class udpServer implements Runnable{

    private int buffer_size = 65507; //Tamanho máximo -> Alterar para o tamanho máximo real

    private final int port;

    private static String FILE_NAME = "received_data.xml";

    public InetAddress addressFromOrder;

    public udpServer(int port) {
        this.port = port;
    }

    public void run() {
        try(DatagramSocket serverSocket = new DatagramSocket(port)){
            serverSocket.setReuseAddress(true);
            byte[] buffer = new byte[buffer_size];
            //serverSocket.setSoTimeout(3000); //não sei se é necessário

            //InetAddress address2 = InetAddress.getByName("8.8.8.8")

            //serverSocket.bind();

            while (true){
                DatagramPacket datagramPacket = new DatagramPacket(buffer, 0, buffer.length);
                serverSocket.receive(datagramPacket);


                addressFromOrder = datagramPacket.getAddress();
                int clientPort = datagramPacket.getPort();

                System.out.println("IP do ERP = " + addressFromOrder);

                if(datagramPacket.getData()!=null) {
                    //FileWriter file = new FileWriter("C:\\Users\\kicop\\Git_Repo\\InformaticaIndustrial\\ii\\src\\mes\\received_data.xml");
                    //FileWriter file = new FileWriter("C:\\Users\\cjoao\\Desktop\\II_last\\ii-Teste\\src\\mes\\received_data.xml");
                    FileWriter file = new FileWriter(FILE_NAME);
                    File f = new File(FILE_NAME);
                    PrintWriter out = new PrintWriter(file,false);
                    byte[] data = new byte[datagramPacket.getLength()];
                    System.arraycopy(datagramPacket.getData(), datagramPacket.getOffset(), data, 0, datagramPacket.getLength());
                    String line = new String(data);
                    out.println(line);
                    out.flush();
                    //ReadXML("C:\\Users\\kicop\\Git_Repo\\InformaticaIndustrial\\ii\\src\\mes\\received_data.xml");
                    //ReadXML("C:\\Users\\cjoao\\Desktop\\II_last\\ii-Teste\\src\\mes\\received_data.xml");

                    System.out.println("Port do ERP = " +clientPort);
                    ReadXML(f.getAbsolutePath(), addressFromOrder, serverSocket, clientPort);
                    out.close();
                    file.close();
                }
                //Imprime o ficheiro inteiro
                //String receivedMessage = new String(datagramPacket.getData());
                //System.out.println(receivedMessage);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}


