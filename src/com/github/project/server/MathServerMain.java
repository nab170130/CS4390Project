package com.github.project.server;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * This is the main class of the server-side program. It launches the 
 * processing thread and listens/accepts connection requests from the 
 * clients.
 * 
 * @author Nathan Beck
 * @version 1.0
 * @since 31 October 2020
 */
public class MathServerMain 
{
	/**
	 * This is the entry method of the program. It launches the 
	 * processing thread and listens/accepts connection requests.
	 * 
	 * @param args Arguments passed by the loader/command line
	 */
	public static void main(String[] args)
	{
		// Assert arguments and return if unsuccessful
		if(!assertArguments(args))
		{
			return;
		}
		
		// Bind logger output and return if unsuccessful
		if(!bindLoggerOutput(args[1]))
		{
			return;
		}
		
		// Launch the processing queue thread and begin accepting connections on provided port
		launchProcessingQueueThread();
		acceptRequestConnections(new Integer(args[0]));
	}
	
	/**
	 * Binds the logger output to the provided text file path
	 * 
	 * @param path The provided text file path to bind the Logger
	 * @return Whether or not the bind was successful
	 */
	private static boolean bindLoggerOutput(String path)
	{
		// Get the singleton instance
		Logger logger = Logger.getInstance();
		
		// Bind the output; log error if unsuccessful
		try
		{
			logger.bindOutput(path);
		}
		catch(FileNotFoundException ex)
		{
			logger.serverLog("Failed to bind log output");
			return false;
		}
		
		return true;
	}
	
	/**
	 * Validates the command-line arguments
	 * 
	 * @param args The command-line arguments (0: port, 1: text file path)
	 * @return Whether or not the command-line arguments are valid
	 */
	private static boolean assertArguments(String[] args)
	{
		// Get the singleton instance and specify lower and upper port number bounds
		Logger logger = Logger.getInstance();
		int lowerPortRange = 1024;
		int upperPortRange = 65536;
		
		// Log error if there is not enough command-line args
		if(args.length < 2)
		{
			logger.serverLog("Insufficient arguments");
			return false;
		}
		
		// Attempt to parse port number; log error if unsuccessful
		Integer portNumber = null;
		
		try
		{
			portNumber = new Integer(args[0]);
		}
		catch(NumberFormatException ex)
		{
			logger.serverLog("Bad server port number format");
			return false;
		}
		
		// Log error if port number is out of valid range
		if(portNumber < lowerPortRange || portNumber >= upperPortRange)
		{
			logger.serverLog("Server port in bad range");
			return false;
		}
		
		return true;
	}
	
	/**
	 * This method launches the thread that processes math requests.
	 */
	private static void launchProcessingQueueThread()
	{
		// Create a thread and start it with the Runnable singleton instance
		ProcessingQueue instance = ProcessingQueue.getInstance();
		Thread processingQueueThread = new Thread(instance);
		processingQueueThread.start();
		
		// Log the start of the thread
		Logger logger = Logger.getInstance();
		logger.serverLog("Processing queue thread launched");
	}
	
	/**
	 * This method listens for connection requests and accepts them if possible. 
	 * It does this action in an infinite loop until the server is terminated.
	 * 
	 * @param serverPort The TCP port on which to accept connection requests
	 */
	private static void acceptRequestConnections(int serverPort)
	{
		// Get singleton instance of Logger
		Logger logger = Logger.getInstance();
		
		// Attempt to create a ServerSocket object on which to receive requests.
		ServerSocket serverSocket;
		
		try
		{
			serverSocket = new ServerSocket(serverPort);
		}
		catch(IOException ex)
		{
			logger.serverLog("Failed to open server socket on provided port");
			return;
		}
		
		// Log the opening of the ServerSocket
		try
		{
			StringBuilder builder = new StringBuilder();
			builder.append("Opened TCP connection on port ");
			builder.append(serverSocket.getLocalPort());
			builder.append(" at IP address ");
			builder.append(InetAddress.getLocalHost().getHostAddress());
			logger.serverLog(builder.toString());
		}
		catch(UnknownHostException ex)
		{
			
		}
			
		// Accept connections until the server is terminated
		while(true)
		{
			try
			{
				// Accept a connection
				Socket newConnection = serverSocket.accept();
				
				// Launch a thread to handle incoming requests from the connection
				ConnectionHandler newConnectionHandler = new ConnectionHandler(newConnection);
				Thread newConnectionHandlerThread = new Thread(newConnectionHandler);
				newConnectionHandlerThread.start();
				
				logger.serverLog("Accepted new TCP connection...");
			}
			catch(IOException ex)
			{
			
			}
		}
	}
}
