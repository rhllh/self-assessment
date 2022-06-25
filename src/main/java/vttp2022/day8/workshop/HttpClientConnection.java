package vttp2022.day8.workshop;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;

/*
 * This class handles the request and response communication 
 * between the server and the client (browser)
 */
public class HttpClientConnection implements Runnable {
    private Socket socket;
    private InputStream is = null;
    private OutputStream os = null;
    private InputStreamReader isr = null;
    private BufferedReader br = null;

    public HttpClientConnection(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        System.out.println("Starting client thread");

        BufferedReader br = null;
        PrintWriter pw = null;
        BufferedOutputStream bos = null;
        String fileRequested = "";

        String requestedResource = "";
        String incomingLineFromClient;
        String methodName;

        try {
            is = socket.getInputStream();
            os = socket.getOutputStream();

            isr = new InputStreamReader(is);
            br = new BufferedReader(isr);

            while ((incomingLineFromClient = br.readLine()) != null) {
                System.out.println("The request message: " + incomingLineFromClient);

                if (incomingLineFromClient.contains("HTTP/1.1")) {
                    requestedResource = incomingLineFromClient;
                }

                if (incomingLineFromClient.equals(""))
                    break;
            }

            PrintWriter out = new PrintWriter(os);

            String response = "You have requested this resource: " + requestedResource;

            out.print("HTTP/1.1 200 OK\n");
            out.print("Content-Length: " + response.length() + "\n");
            out.print("Content-Type: text/html; charset=utf-8\n");
            out.print("Date: Tue, 25 Oct 2016 08:17:59 GMT\n");
            out.print("\n");
            out.print(response);
            out.flush();

            //socket.close();

            //sendPage(socket);
            
            //String httpResponse = "HTTP/1.1 200 OK\r\n\r\n" + "hello u";
            //os.write(httpResponse.getBytes("UTF-8"));
            
        } catch (IOException e) {
            System.err.println("Error: " + e.getMessage());
        }        
    }

    public void sendPage(Socket socket) throws IOException {
        File index = new File("./static/index.html");

        PrintWriter writer = new PrintWriter(os);

        BufferedReader reader = new BufferedReader(new FileReader(index));

        // HTTP headers
        writer.println("HTTP/1.1 200 OK");
        writer.println("Content-Type: text/html");
        writer.println("Content-Length: " + index.length());
        writer.println("\r\n");
        String line = reader.readLine();
        while (line != null)
        {
            writer.println(line);
            line = reader.readLine();
        }

        reader.close();
        writer.close();
    }
}
