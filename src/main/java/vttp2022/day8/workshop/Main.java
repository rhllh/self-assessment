package vttp2022.day8.workshop;

import java.util.LinkedList;
import java.util.List;

/**
 * Hello world!
 *
 */
public class Main 
{
    public static void main( String[] args )
    {
        try {
            int PORT = 3000;
            List<String> DOCROOT = new LinkedList<>();
            DOCROOT.add("./target");
            String[] dirs;

            System.out.println( "Hello World!" );
            System.out.println();
            
            switch (args.length) {
                case 0:
                    PORT = 3000;
                    DOCROOT.add("./target");
                    break;
                case 2:
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
                case 4:
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
                default:
                    System.out.println( "Please use --port [option] or --docRoot [option]");
                    System.exit(1);
                    break;
                }

                /*System.out.println(PORT);
                for (String item : DOCROOT)
                    System.out.println(item);*/

                HttpServer httpServer = new HttpServer(PORT, DOCROOT);
                httpServer.start();

        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }
}
