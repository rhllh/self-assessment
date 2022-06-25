package vttp2022.day8.workshop;

import java.io.File;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/*
 * This is the server file
 */
public class HttpServer {
    private ExecutorService threadPool = null;
    private ServerSocket server = null;
    private int PORT;
    private List<String> DOCROOT = new LinkedList<>();
    private int numOfThreads = 3;

    public HttpServer(int port, List<String> docroot){
        this.PORT = port;
        this.DOCROOT = docroot;
    }

    public void start() throws IOException {
        // check paths of docRoot
        for (String path : DOCROOT) {
            File f = new File(path);
            if (f.exists() && f.isDirectory())
                continue;
            else {
                if (!f.exists())
                    System.out.printf("The path '%s' is not valid\n", path);
                else if (!f.isDirectory())
                    System.out.printf("The path '%s' is not a directory\n", path);
                
                System.exit(1);
            }
        }

        // start the server
        threadPool = Executors.newFixedThreadPool(numOfThreads);
        server = new ServerSocket(PORT);
        System.out.printf("Waiting for client to connect on port %s..\n", PORT);
        int count = 0;

        while (true) {
            try {
                Socket socket = server.accept();
                count++;
                System.out.printf("\nClient has connected #%d\n", count);

                // instantiate a clientconnection object
                HttpClientConnection t = new HttpClientConnection(socket);
                threadPool.submit(t);

                System.out.println("Submitted to thread");
               
            } catch (Exception e) {
                server.close();
                e.printStackTrace();
                
                System.exit(1);
            }
        }

    }

}
