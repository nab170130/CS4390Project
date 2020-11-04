package com.github.project.server;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
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
		ObjectInputStream requestReader = null;
		ObjectOutputStream responseWriter = null;
		
		try
		{
			InputStream socketInputStream = connectionSocket.getInputStream();
			requestReader = new ObjectInputStream(socketInputStream);
			OutputStream socketOutputStream = connectionSocket.getOutputStream();
			responseWriter = new ObjectOutputStream(socketOutputStream);
		}
		catch(IOException ex)
		{
			ex.printStackTrace();
			return;
		}
		
		while(connectionSocket.isConnected())
		{
			try
			{
				CalculationRequest receivedRequest = (CalculationRequest) requestReader.readObject();
				processingQueue.addToQueue(receivedRequest, responseWriter);
			}
			catch(ClassNotFoundException ex)
			{
				break;
			}
			catch(IOException ex)
			{
				break;
			}
		}
		
		try
		{
			requestReader.close();
			responseWriter.close();
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
