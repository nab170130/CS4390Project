package com.github.project.server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;

import com.github.project.core.CalculationRequest;

public class ConnectionHandler implements Runnable
{
	private Socket connectionSocket;
	
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
	
	public ConnectionHandler(Socket connectionSocket_)
	{
		connectionSocket = connectionSocket_;
	}
}
