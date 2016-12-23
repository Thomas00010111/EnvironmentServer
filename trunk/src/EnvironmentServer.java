import java.net.*;
import java.io.*;

/**
 * 
 * @author Thomas Fischle
 *
 */

public class EnvironmentServer {
	
	//Environment environment = new Environment();
	
	private final int MYSERVERSOCKET = 4444;
	
	public double orientation;
	String inputLine, outputLine;
	PrintWriter out;
	BufferedReader in;
	Socket clientSocket;
	ServerSocket serverSocket;
	static boolean run = true;
	static Environment environment;
	 
    public static void main(String[] args)  {
    	environment = new Environment();
    	EnvironmentServer environmentServer = new EnvironmentServer();
    	
    	while(run)
    	{
	    	//Run server
	    	try{
	    		environmentServer.startServer();
	    		environmentServer.handleConnection();
	    	}
	    	catch(IOException e){
	    		 System.err.println(e.toString());
	    	}
    	}
    	System.exit(0);
    }   

    public void startServer() throws IOException
    {    		
    	serverSocket = null;
        try {
            serverSocket = new ServerSocket(MYSERVERSOCKET);
        } catch (IOException e) {
            System.err.println("Could not listen on port: " + MYSERVERSOCKET);
            System.exit(1);
        }

        clientSocket = null;
        try {
            clientSocket = serverSocket.accept();
        } catch (IOException e) {
            System.err.println("Accept failed.");
            System.exit(1);
        }

        out = new PrintWriter(clientSocket.getOutputStream(), true);
        in = new BufferedReader(
				new InputStreamReader(
				clientSocket.getInputStream()));        
    }
    
    public void handleConnection() throws IOException
    {        
        //outputLine = environment.processInput(null);
        //out.println(outputLine);

        while ((inputLine = in.readLine()) != null) 
        {
             outputLine = environment.processInput(inputLine);
             out.println(outputLine);
             if (outputLine.equals("Bye."))
             {
            	 //reset server and wait for new connection request? how?
            	 run = false;	//stop server
            	 break;
             }
             if (outputLine.equals("Restart"))
             {
            	 break;			//restart server
             }
        }
        out.close();        
        in.close();
        clientSocket.close();
        serverSocket.close();
    }
}
