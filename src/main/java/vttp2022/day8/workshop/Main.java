package vttp2022.day8.workshop;

import java.util.LinkedList;
import java.util.List;

/**
 * Entry point of the HTTP server
 */
public class Main 
{
    public static void main( String[] args )
    {
        try {
            int PORT = 3000;
            List<String> DOCROOT = new LinkedList<>();
            String[] dirs;

            System.out.println( "I am a web server!" );
            System.out.println();
            
            // TASK 3
            switch (args.length) {
                case 0:     // default values
                    PORT = 3000;
                    DOCROOT.add("./target");
                    break;
                case 2:     // only PORT or only DOCROOT is specified
                    if (args[0].toUpperCase().equals("--PORT"))
                        PORT = Integer.parseInt(args[1]);
                    else if (args[0].toUpperCase().equals("--DOCROOT")) {
                        DOCROOT.clear();
                        dirs = args[1].split(":");
                        for (int i = 0; i < dirs.length; i++) {
                            DOCROOT.add(dirs[i]);
                        }
                    }
                    break;
                case 4:     // both PORT and DOCROOT are specified, the order doesn't matter
                    for (int i = 0; i < 4; i++) {
                        if (args[i].toUpperCase().equals("--PORT"))
                            PORT = Integer.parseInt(args[i+1]);
                        else if (args[i].toUpperCase().equals("--DOCROOT")) {
                            DOCROOT.clear();
                            dirs = args[i+1].split(":");
                            for (int j = 0; j < dirs.length; j++)
                                DOCROOT.add(dirs[j]);
                        } else {
                            continue;
                        }
                    }
                    break;
                default:    // otherwise input is invalid
                    System.out.println( "Please use --port [port number] or --docRoot [directory path]");
                    System.exit(1);
                    break;
                }

                // start the server
                HttpServer httpServer = new HttpServer(PORT, DOCROOT);
                httpServer.start();

        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        }
    }
}
