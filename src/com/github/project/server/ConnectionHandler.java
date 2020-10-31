package com.github.project.server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;

import com.github.project.core.CalculationRequest;

/**
 * This class implements the connection handlers of the server. It is a 
 * Runnable class that serves as an entry point for the threads that respond 
 * to the connection requests.
 * 
 * @author Nathan Beck
 * @version 1.0
 * @since 31 October 2020
 */
public class ConnectionHandler implements Runnable
{
	private Socket connectionSocket; // The Socket receiving requests
	
	/**
	 * This method serves as the entry point for the threads handling 
	 * requests from their clients. It receives requests and issues 
	 * them to the processing queue until the connection is terminated.
	 */
	public void run()
	{
		ProcessingQueue processingQueue = ProcessingQueue.getInstance();
		ObjectInputStream requestReader;
		
		try
		{
			requestReader = new ObjectInputStream(connectionSocket.getInputStream());
		}
		catch(IOException ex)
		{
			return;
		}
		
		while(connectionSocket.isConnected())
		{
			try
			{
				CalculationRequest receivedRequest = (CalculationRequest) requestReader.readObject();
				processingQueue.addToQueue(receivedRequest, connectionSocket);
			}
			catch(ClassNotFoundException ex)
			{
				
			}
			catch(IOException ex)
			{
				
			}
		}
		
		try
		{
			requestReader.close();
		}
		catch(IOException ex)
		{
			
		}
	}
	
	/**
	 * Creates a ConnectionHandler object that receives requests on 
	 * the passed Socket object.
	 * 
	 * @param connectionSocket_ The Socket to receive requests
	 */
	public ConnectionHandler(Socket connectionSocket_)
	{
		connectionSocket = connectionSocket_;
	}
}
