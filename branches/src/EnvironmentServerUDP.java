import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.DatagramSocket;
import java.net.DatagramPacket;
/**
 * 
 * @author Thomas Fischle
 *
 */

public class EnvironmentServerUDP {
	
	private final int MYSERVERSOCKET = 4444;
	
	public double orientation;
	String inputLine, outputLine;
	PrintWriter out;
	BufferedReader in;
	Socket clientSocket;
	static DatagramSocket serverSocket;
	static boolean run = true;
	static Environment environment;
	byte[] sendData = new byte[1024];
	InetAddress IPAddressClient;
    int portClient;
    int msgId = 0;
	 
    public static void main(String[] args)  {
    	serverSocket = null;    	
    	EnvironmentServerUDP environmentServer = new EnvironmentServerUDP();
    	try {
			System.setOut(new PrintStream(new FileOutputStream("output.txt", false)));
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
    	
    	/** check passed argument **/
    	if(args.length>0 && args[0].equalsIgnoreCase("--nographic")) 
    	{
    		System.out.println("The server is started in No Graphics mode!");
    		environment = new Environment(false);
    	}
    	else
    		environment = new Environment(true);
             

    	/** wait for connection and handle connection **/
    	while(run)
    	{
    		/** start server ***/
        	try{
            	//start server
        		environmentServer.startServer();
        	}
        	catch(IOException e){
        		System.err.println(e.toString());
        	}
    		
    		try{
	    		//***********
	    		// The server needs one packet from the client to set port and
	    		// Ip adress.
	    		// ***********
	    		environmentServer.waitForFirstPacket();

	    		// **** set timeout ***** //	
	    		// DO NOT UNDERSTAND WHY, BUT THIS TIMEOUT INFLUENCES THE
	    		// MOVEMENT SPEED OF THE AGEN !!!???
	    		environmentServer.handleConnection(2000);
	    		//environmentServer.handleConnection();
	    		environmentServer.closeConnection();
	    	}
	    	catch(IOException e){
	    		System.out.println(e.toString());
	    		System.err.println(e.toString());
	    	}
    	}

    	//close server
    	serverSocket.close();
    	
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
            serverSocket = new DatagramSocket(MYSERVERSOCKET);
        } catch (IOException e) {
            System.err.println("Could not listen on port: " + MYSERVERSOCKET);
            System.exit(1);
        }
    }
    
    private void waitForFirstPacket() throws IOException
    {
    	byte[] receiveData = new byte[1024];
    	DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
		serverSocket.receive(receivePacket);
		String receivedData = new String(receivePacket.getData());
		System.out.println("First Packet: " + receivedData);
		IPAddressClient = receivePacket.getAddress();
        portClient = receivePacket.getPort();
        
        System.out.println("IPAddressClient: " + IPAddressClient +"   portClient: " + portClient);
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
    	serverSocket.setSoTimeout(timeout);
    	while(ConnectionWithTimeout()){}
    }
    
    private void sendData(String sendString) throws IOException
    {
    	msgId++;
    	String message = new String(msgId + ";" + sendString);
    	System.out.println("sending: " + message);
    	sendData = message.getBytes();
    	DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddressClient , portClient);
        serverSocket.send(sendPacket);
    }
    
//    private void handleConnection() throws IOException
//    {        
//    	sendData(environment.processInput("CURPOS"));
//    	ConnectionWithTimeout();
//    	closeConnection();
//    }
    
    private void closeConnection() throws IOException
    {
        serverSocket.close();
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
    		//send current position of car
    		if (outputLine == null)
    		{
    			sendData(environment.processInput("CURPOS"));
    		}
    		else
    		{
    			sendData(outputLine);
    		}
    		
//    		//give client some processing time, then wait for data
//    		try {
//    		    Thread.sleep(2);                 //1000 milliseconds is one second.
//    		} catch(InterruptedException ex) {
//    		    Thread.currentThread().interrupt();
//    		}
    		byte[] receiveData = new byte[128];
    		DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
			serverSocket.receive(receivePacket);
			int recMsgId = Integer.parseInt(new String(receivePacket.getData()).split(";")[0]);
			inputLine = new String(receivePacket.getData()).split(";")[1];
			System.out.println("inputLine: " + inputLine);
			outputLine = environment.processInput(inputLine);
			System.out.println("["+ System.currentTimeMillis() +"] outputLine:"  + outputLine);

			//sendData(outputLine);
			if (outputLine.equals("Bye."))
			{
				//reset server and wait for new connection request? how?
				run = false;	//stop server
			}
			if (outputLine.equals("Restart"))
			{
				outputLine = null;
				return false;			//restart client
			}
			if (recMsgId !=  msgId && !( outputLine == "CarRan" || outputLine == "CarRam" || outputLine == "Restart" || outputLine == "CarSta") )
			{
				System.out.println("System Exit!! recMsgId: " + recMsgId + "  msgId: " + msgId + "    outputLine: " + outputLine);
				System.exit(-1);
			}
			if ( outputLine == "CarRan" || outputLine == "CarRam" || outputLine == "Restart" || outputLine == "CarSta")
			{
				msgId = 0;
				outputLine = null;
			}
	        
        }
        catch(java.net.SocketTimeoutException e)
        {
        	System.out.println("Timeout!");
        	
        	//was return true
        	return false;
        }
    	catch(java.net.SocketException e)
    	{
    		System.out.println("ConnectionWithTimeout: " + e.getMessage());
    		return false;
    	}
    	catch(IOException e)
        {
        	//IOException
        	System.out.println(e.getMessage());
        	return false;
        }
        return true;
    }
}
