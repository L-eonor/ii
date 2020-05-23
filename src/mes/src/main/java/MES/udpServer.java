package MES;

import java.io.FileWriter;
import java.io.PrintWriter;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

import static MES.readXML.*;
import static MES.createXML.*;

public class udpServer implements Runnable{

    private int buffer_size = 65507; //Tamanho máximo -> Alterar para o tamanho máximo real

    private final int port;

    public udpServer(int port) {
        this.port = port;
    }

    public void run() {
        try(DatagramSocket serverSocket = new DatagramSocket(port)){
            byte[] buffer = new byte[buffer_size];
            //serverSocket.setSoTimeout(3000); //não sei se é necessário


            while (true){
                DatagramPacket datagramPacket = new DatagramPacket(buffer, 0, buffer.length);
                serverSocket.receive(datagramPacket);

                if(datagramPacket.getData()!=null) {
                    //FileWriter file = new FileWriter("C:\\Users\\kicop\\Git_Repo\\InformaticaIndustrial\\ii\\src\\mes\\received_data.xml");
                    FileWriter file = new FileWriter("C:\\Users\\cjoao\\Desktop\\ii-Teste\\src\\mes\\received_data.xml");

                    PrintWriter out = new PrintWriter(file,false);
                    byte[] data = new byte[datagramPacket.getLength()];
                    System.arraycopy(datagramPacket.getData(), datagramPacket.getOffset(), data, 0, datagramPacket.getLength());
                    String line = new String(data);
                    out.println(line);
                    out.flush();
                    //ReadXML("C:\\Users\\kicop\\Git_Repo\\InformaticaIndustrial\\ii\\src\\mes\\received_data.xml");
                    ReadXML("C:\\Users\\cjoao\\Desktop\\ii-Teste\\src\\mes\\received_data.xml");
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


