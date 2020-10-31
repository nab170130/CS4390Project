package com.github.project.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

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
		launchProcessingQueueThread();
		acceptRequestConnections(54321);
	}
	
	/**
	 * This method launches the thread that processes math requests.
	 */
	public static void launchProcessingQueueThread()
	{
		// Create a thread and start it with the Runnable singleton instance
		ProcessingQueue instance = ProcessingQueue.getInstance();
		Thread processingQueueThread = new Thread(instance);
		processingQueueThread.start();
	}
	
	/**
	 * This method listens for connection requests and accepts them if possible. 
	 * It does this action in an infinite loop until the server is terminated.
	 * 
	 * @param serverPort The TCP port on which to accept connection requests
	 */
	public static void acceptRequestConnections(int serverPort)
	{
		// Attempt to create a ServerSocket object on which to receive requests.
		ServerSocket serverSocket;
		
		try
		{
			serverSocket = new ServerSocket(serverPort);
		}
		catch(IOException ex)
		{
			return;
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
			}
			catch(IOException ex)
			{
			
			}
		}
	}
}
