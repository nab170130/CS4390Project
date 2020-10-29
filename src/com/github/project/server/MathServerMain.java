package com.github.project.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class MathServerMain 
{
	public static void main(String[] args)
	{
		launchProcessingQueueThread();
		acceptRequestConnections(54321);
	}
	
	public static void launchProcessingQueueThread()
	{
		ProcessingQueue instance = ProcessingQueue.getInstance();
		Thread processingQueueThread = new Thread(instance);
		processingQueueThread.start();
	}
	
	public static void acceptRequestConnections(int serverPort)
	{
		ServerSocket serverSocket;
		
		try
		{
			serverSocket = new ServerSocket(serverPort);
		}
		catch(IOException ex)
		{
			return;
		}
		
		while(true)
		{
			try
			{
				Socket newConnection = serverSocket.accept();
				
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
