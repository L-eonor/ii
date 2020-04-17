package MES;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.PriorityQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class main {

    public static List<order> orderListTransformation = Collections.synchronizedList( new ArrayList<order>());
    public static List<order> orderListUnload = Collections.synchronizedList( new ArrayList<order>());

    public static void main(String[] args) {
        int port = 54321;

        udpServer server = new udpServer(port);
        //sendXML client = new sendXML(port, "C:\\XML\\received_data.xml");

        ExecutorService executorService = Executors.newFixedThreadPool(2);
        //executorService.submit(client);
        executorService.submit(server);

    }
}

