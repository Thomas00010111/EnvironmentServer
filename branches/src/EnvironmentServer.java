import java.io.BufferedReader;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
/**
 * 
 * @author Thomas Fischle
 *
 */

public class EnvironmentServer {
	
	private final int MYSERVERSOCKET = 4444;
	
	public double orientation;
	String inputLine, outputLine;
	PrintWriter out;
	BufferedReader in;
	Socket clientSocket;
	//static ServerSocket serverSocket;
	static ServerSocket serverSocket;
	static boolean run = true;
	static Environment environment;
	 
    public static void main(String[] args)  {
    	serverSocket = null;    	
    	EnvironmentServer environmentServer = new EnvironmentServer();
    	
    	/** check passed argument **/
    	if(args.length>0 && args[0].equalsIgnoreCase("--nographic")) 
    	{
    		System.out.println("The server is started in No Graphics mode!");
    		environment = new Environment(false);
    	}
    	else
    		environment = new Environment(true);
             

    	/** start server ***/
    	try{
        	//start server
    		environmentServer.startServer();
    	}
    	catch(IOException e){
    		System.err.println(e.toString());
    	}
 
    	/** wait for connection and handle connection **/
    	while(run)
    	{
    		//wait for connection
	    	try{
	        	//wait for a client to connect 
	    		environmentServer.waitForClient();
	    		System.out.println("Connected");
	    		
	    		//handle the communication, with or without timeout
	    		
	    		// **********************
	    		// **** set timeout ***** //		
	    		environmentServer.handleConnection(10);
	    		//environmentServer.handleConnection();
	    	}
	    	catch(IOException e){
	    		System.err.println(e.toString());
	    	}
    	}
    	
    	/** close server **/
    	try{
    		//close server
    		serverSocket.close();
    	}
    	catch(IOException e){
    		System.err.println(e.toString());
    	}
    	
    	System.exit(0);
    }
  
    /***
     * start a server socket on port MYSERVERSOCKET
     * @throws IOException
     */
    public void startServer() throws IOException
    {    		
    	serverSocket = null;
        try {
            serverSocket = new ServerSocket(MYSERVERSOCKET);
        } catch (IOException e) {
            System.err.println("Could not listen on port: " + MYSERVERSOCKET);
            System.exit(1);
        }
    }
    /***
     * wait for a client to connect (method is not left before a client has connected)
     * 
     * @throws IOException
     */
    public void waitForClient() throws IOException
    {
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
    
    
    /***
     * Continuously send current position to client
     * while client does not send "Bye." (without quotes) the received string is passed
     * to the method processInput(receivedString) of class Environment. Thus, a new command can simply be
     * added in method processInput().
     * 
     * When "Restart" (without quotes) is sent the method is left. (Yes, the command is misleading)
     * 
     * @throws IOException
     */
    private void handleConnection(int timeout) throws IOException
    {        
    	clientSocket.setSoTimeout(timeout);
        out.println(environment.processInput("CURPOS"));
    	while(ConnectionWithTimeout()){}
    	closeConnection();
    }
    
//    private void handleConnection() throws IOException
//    {        
//    	out.println(environment.processInput("CURPOS"));
//    	ConnectionWithTimeout();
//    	closeConnection();
//    }
    
    private void closeConnection() throws IOException
    {
        out.close();        
        in.close();
        clientSocket.close();
        //serverSocket.close();
    }
    
    /***
     * Continuously send current position to client
     * while client does not send "Bye." (without quotes) the received string is passed
     * to the method processInput(receivedString) of class Environment. Thus, a new command can simply be
     * added in method processInput().
     * 
     * When "Restart" (without quotes) is sent the method is left. (Yes, the command is misleading)
     * 
     * @throws IOException
     */
    public boolean ConnectionWithTimeout() throws IOException
    {            	
    	try
        {
    		while ((inputLine = in.readLine()) != null) 
	    	{
	             outputLine = environment.processInput(inputLine);
	             System.out.println(System.currentTimeMillis() +"   :"  + outputLine);
	             out.println(outputLine);
	             if (outputLine.equals("Bye."))
	             {
	            	 //reset server and wait for new connection request? how?
	            	 run = false;	//stop server
	            	 break;
	             }
	             if (outputLine.equals("Restart"))
	             {
	            	 break;			//restart client
	             }
	        }
        }
        catch(java.net.SocketTimeoutException e)
        {
        	//in case of a timeout send current car position
        	System.out.println("Timeout!");
        	//outputLine = environment.processInput("CURPOS");
            //out.println(outputLine);
        	return true;
        }
    	catch(IOException e)
        {
        	//IOException
        	System.out.println(e.getMessage());
        }
        return false;
    }
    
    
//    /***
//     * while client does not send "Bye." (without quotes) the received string is passed
//     * to the method processInput(receivedString) of class Environment. Thus, a new command can simply be
//     * added in method processInput().
//     * 
//     * When "Restart" (without quotes) is sent the method is left. (Yes, the command is misleading)
//     * 
//     * @throws IOException
//     */
//    public void handleConnection() throws IOException
//    {        
//        //outputLine = environment.processInput(null);
//        //out.println(outputLine);
//        while ((inputLine = in.readLine()) != null) {
//             outputLine = environment.processInput(inputLine);
//             System.out.println(System.currentTimeMillis() +"   :"  + outputLine);
//             out.println(outputLine);
//             if (outputLine.equals("Bye."))
//             {
//            	 //reset server and wait for new connection request? how?
//            	 run = false;	//stop server
//            	 break;
//             }
//             if (outputLine.equals("Restart"))
//             {
//            	 break;			//restart client
//             }
//        }
//        out.close();        
//        in.close();
//        clientSocket.close();
//        //serverSocket.close();
//    }
}
