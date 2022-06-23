package vttp2022.day8.workshop;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;
import java.util.List;

/*
 * This is the server file
 */
public class HttpServer {
    private ServerSocket server = null;
    private int PORT;
    private List<String> DOCROOT = new LinkedList<>();
    //private int numOfThreads = 3;

    public HttpServer(int port, List<String> docroot){
        this.PORT = port;
        this.DOCROOT = docroot;
    }

    public void start() throws IOException {
        // start the server
        server = new ServerSocket(PORT);
        System.out.printf("Waiting for client to connect on port %s..\n", PORT);
        int count = 0;

        while (true) {
            try (Socket socket = server.accept()) {
                count++;
                System.out.printf("\nClient has connected #%d\n", count);
                
                //PrintWriter writer = new PrintWriter(socket.getOutputStream());
                //writer.close();

                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
 
                String requestedResource = "";
                String incomingLineFromClient;
                while ((incomingLineFromClient = in.readLine()) != null) {
                    System.out.println(incomingLineFromClient);
                
                    if(incomingLineFromClient.contains("HTTP/1.1")) {
                        requestedResource = incomingLineFromClient;
                    }
                
                    if (incomingLineFromClient.equals(""))
                        break;
                }

                PrintWriter out = new PrintWriter(socket.getOutputStream());
 
                String response = "You have requested this resource: " + requestedResource;
                
                out.print("HTTP/1.1 200 OK\n");
                out.print("Content-Length: " + response.length() + "\n");
                out.print("Content-Type: text/html; charset=utf-8\n");
                out.print("Date: Tue, 25 Oct 2016 08:17:59 GMT\n");
                out.print("\n");
                out.print(response);
                out.flush();
                //String httpResponse = "HTTP/1.1 200 OK\r\n\r\n" + "hello u"; 
                //socket.getOutputStream().write(httpResponse.getBytes("UTF-8"));
                
            } catch (Exception e) {
                server.close();
                e.printStackTrace();
                System.exit(1);
            }
        }

    }
}
