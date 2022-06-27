package vttp2022.day8.workshop;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.Socket;
import java.util.LinkedList;
import java.util.List;

/*
 * This class handles the request and response communication 
 * between the server and the client (browser)
 */
public class HttpClientConnection implements Runnable {
    private Socket socket;
    private List<String> DOCROOT = new LinkedList<>();
    private InputStream is = null;
    private OutputStream os = null;
    private InputStreamReader isr = null;
    private File resourceFile;

    public HttpClientConnection(Socket socket, List<String> DOCROOT) {
        this.socket = socket;
        this.DOCROOT = DOCROOT;
    }

    @Override
    public void run() {
        System.out.println("Starting client thread");

        BufferedReader br = null;
        DataInputStream dis = null;
        PrintStream ps = null;

        String incomingLineFromClient;

        try {
            is = socket.getInputStream();
            os = socket.getOutputStream();

            isr = new InputStreamReader(is);
            br = new BufferedReader(isr);

            ps = new PrintStream(os);

            while ((incomingLineFromClient = br.readLine()) != null) {
                if (incomingLineFromClient.isEmpty()) break;

                System.out.println("\nThe request message: " + incomingLineFromClient);

                String[] request = incomingLineFromClient.split(" ");
                
                if (request[0].equals("GET")) {

                    String resourceName = request[1];
                    if (resourceName.contains(".ico")) continue;
                    if (resourceName.equals("/")) resourceName = "/index.html";
                    System.out.println("Resource requested: " + resourceName);

                    // file not found
                    boolean doesFileExist = checkFileInDOCROOT(resourceName);
                    if (!doesFileExist) {
                        System.out.println("File not found");
                        // response message header, no body
                        fileNotFoundHeader(ps, request[1]);
                        break;
                    }

                    dis = new DataInputStream(new FileInputStream(this.resourceFile));

                    String fileExt = resourceName.split("\\.")[1];

                    // response message header
                    fileFoundHeader(ps, (int) this.resourceFile.length(), fileExt);

                    // response message body
                    responseMsgBody(ps, dis, (int) this.resourceFile.length());

                } else if (request[0].equals("POST") ||
                           request[0].equals("PUT")  ||
                           request[0].equals("PATCH") ||
                           request[0].equals("DELETE") ||
                           request[0].equals("HEAD")) 
                {
                    // non-GET methods
                    System.out.println("Method not found");

                    // response message header, no body
                    methodNotAllowedHeader(ps, request[0]);

                } else {
                    // other input lines
                    continue;
                }
            }

            System.out.println("Returned thread");
   
        } catch (IOException e) {
            System.err.println("Error: " + e.getMessage());
        } finally {
            // close connection and exit thread
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {}
            }
            if (os != null) {
                try {
                    os.close();
                } catch (IOException e) {}
            }
            if (socket != null) {
                try {
                    socket.close();
                } catch (IOException e) {}
            }
        }        
    }

    public void methodNotAllowedHeader(PrintStream ps, String methodName) {
        ps.print("HTTP/1.1 405 Method Not Allowed\r\n");
        ps.print("\r\n");
        ps.print(methodName + " not supported\r\n");
    }

    public boolean checkFileInDOCROOT(String fileName) {
        for (String item : DOCROOT) {
            File file = new File(item + fileName);
            if (file.exists()) {
                this.resourceFile = file;
                return true;
            }
        }
        return false;
    }

    public void fileFoundHeader(PrintStream ps, int fileLength, String fileExt) {
        ps.print("HTTP/1.1 200 OK\r\n");
        ps.print("Content-Length: " + fileLength + "\r\n");
        
        if (fileExt.equals("html")) {
            ps.print("Content-Type: text/html; charset=utf-8\r\n");
        } else if (fileExt.equals("css")) {
            ps.print("Content-Type: text/css; charset=utf-8\r\n");
        } else if (fileExt.equals("png")) {
            ps.print("Content-Type: image/png; charset=utf-8\r\n");
        }

        ps.print("\r\n");
    }

    public void fileNotFoundHeader(PrintStream ps, String resourceName) {
        ps.print("HTTP/1.1 404 Not Found\r\n");
        ps.print("\r\n");
        ps.print(resourceName + " not found\r\n");
    }

    public void responseMsgBody(PrintStream ps, DataInputStream dis, int fileLength) {
        try {
            byte buffer[] = new byte[fileLength];
            dis.readFully(buffer);
            ps.write(buffer, 0, fileLength);
            dis.close();
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        }
    }
}
