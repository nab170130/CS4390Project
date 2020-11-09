package com.github.project.server;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;

import com.github.project.core.CalculationRequest;
import com.github.project.core.CalculationResponse;
import com.github.project.core.ConnectionEstablishRequest;
import com.github.project.core.ConnectionEstablishResponse;
import com.github.project.core.ConnectionTerminateRequest;
import com.github.project.core.ConnectionTerminateResponse;
import com.github.project.core.Response;
import com.github.project.core.ResponseCode;

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
	private Socket connectionSocket; 	// The Socket receiving requests
	
	private ObjectInputStream ois;		// Input stream from Socket 
	private ObjectOutputStream oos;		// Output stream from Socket
	
	private String username;									// The passed username from the client (not unique to each connection)
	private final int connectionID;								// The connection ID for this ConnectionHandler (unique to each connection)
	private ApplicationLayerConnectionState connectionState;	// The app-layer connection state of this ConnectionHandler
	
	private static int nextConnectionID = 0; // Used for ID generation in constructor
	
	/**
	 * This method serves as the entry point for the threads handling 
	 * requests from their clients. It receives requests and issues 
	 * them to the processing queue until the connection is terminated.
	 */
	public void run()
	{	
		Logger logger = Logger.getInstance();
		
		// Wait for and accept username information
		initializeApplicationLayerConnection();
		
		logger.connectionHandlerLog(connectionID, username, "Established application-layer connection");
		
		// Get the processing queue for queueing of requests
		ProcessingQueue processingQueue = ProcessingQueue.getInstance();
		
		// Keep doing the following actions until TCP socket disconnects, client requests termination, or app-layer 
		// state is changed to KILLED
		while(connectionSocket.isConnected())
		{
			// Terminate the thread if the app-layer connection state directs the connection to be killed
			if(connectionState == ApplicationLayerConnectionState.KILLED)
			{
				return;
			}
			
			// Attempt to receive client message
			try
			{
				// Receive client message
				Object receivedMessage = ois.readObject();
				
				// Determine which message was received
				if(receivedMessage instanceof CalculationRequest)
				{
					CalculationRequest calcRequest = (CalculationRequest) receivedMessage;
					
					StringBuilder builder = new StringBuilder();
					builder.append("Received calculation request: ");
					builder.append(calcRequest.getRawRequest());
					logger.connectionHandlerLog(connectionID, username, builder.toString());
					
					// Add calculation requests to the processing queue
					processingQueue.addToQueue((CalculationRequest) receivedMessage, this);
				}
				else if(receivedMessage instanceof ConnectionTerminateRequest)
				{
					logger.connectionHandlerLog(connectionID, username, "Received request to terminate. Sending acknowledgement...");
					
					// Acknowledge the terminate request and prepare for connection finalization
					ConnectionTerminateResponse okResponse = new ConnectionTerminateResponse(ResponseCode.OK);
					sendResponse(okResponse);
					break;
				}
			}
			catch(Exception ex)
			{
				logger.connectionHandlerLog(connectionID, username, "Received bad request message");
				
				// Send a generic response indicating poor reception
				Response poorReceptionResponse = new Response(ResponseCode.BAD_REQUEST);
				sendResponse(poorReceptionResponse);
			}
		}
		
		// Terminate the thread if the app-layer connection state directs the connection to be killed instead of finalized
		if(connectionState == ApplicationLayerConnectionState.KILLED)
		{
			return;
		}
		
		// Neatly finalize the app-layer connection
		finalizeConnection();
	}
	
	/**
	 * This method sends an OK CalculationResponse to the connected client with the provided 
	 * result and (possible) error message.
	 * 
	 * @param result The result of the calculation
	 * @param calculationErrorMessage The error message of the calculation, if it exists
	 */
	public void sendCalculationResponse(double result, String calculationErrorMessage)
	{
		// Do not bother sending the message if the TCP connection is severed
		if(!connectionSocket.isConnected())
		{
			return;
		}
		
		// Do not bother sending the message if the connection is finalized
		if(connectionState == ApplicationLayerConnectionState.FINALIZED)
		{
			return;
		}
		
		// Create the CalculationResponse message and send it to the client
		CalculationResponse response = new CalculationResponse(ResponseCode.OK, result, calculationErrorMessage);
		sendResponse(response);
	}
	
	/**
	 * This method initializes the app-layer connection state by receiving 
	 * a ConnectionEstablishRequest from the client and sending a 
	 * ConnectionEstablishResponse to the client indicating success or failure.
	 */
	private void initializeApplicationLayerConnection()
	{
		try
		{
			// Receive the request from the client
			ConnectionEstablishRequest receivedRequest = (ConnectionEstablishRequest) ois.readObject();
			username = receivedRequest.getUsername();
			
			// Switch app-layer state to USERNAME as the username has been set
			connectionState = ApplicationLayerConnectionState.USERNAME;
			
			// Send an OK response back to the client
			ConnectionEstablishResponse okResponse = new ConnectionEstablishResponse(ResponseCode.OK);
			sendResponse(okResponse);
		}
		catch(Exception ex)
		{
			// Send a BAD_REQUEST response back to the client indicating failure
			ConnectionEstablishResponse failedResponse = new ConnectionEstablishResponse(ResponseCode.BAD_REQUEST);
			sendResponse(failedResponse);
		}
	}
	
	/**
	 * This method sends the passed Response message to the client. If at any point the response fails to send, 
	 * then the server cannot communicate as there are failings with the underlying connection. In such a case, 
	 * the app-layer protocol cannot function correctly, so the app-layer connection state is set to KILLED to 
	 * direct the severing of the TCP connection (if it is not already severed).
	 * 
	 * @param response The Response object to send to the client
	 */
	private void sendResponse(Response response)
	{
		Logger logger = Logger.getInstance();
		
		try
		{
			oos.writeObject(response);
		}
		catch(IOException ex)
		{
			logger.connectionHandlerLog(connectionID, username, "Underlying TCP connection failed. Killing handler...");
			connectionState = ApplicationLayerConnectionState.KILLED;
		}
	}
	
	/**
	 * This method neatly finalizes the app-layer connection state. It is called after successful reception of 
	 * a terminate request. This method sets the connection state to FINALIZED and closes its IO streams. It 
	 * then busy-waits until the client finishes its activities and closes the TCP connection.
	 */
	private void finalizeConnection()
	{
		Logger logger = Logger.getInstance();
		logger.connectionHandlerLog(connectionID, username, "Finalizing connection...");
		
		connectionState = ApplicationLayerConnectionState.FINALIZED;
		
		try
		{
			ois.close();
			oos.close();
		}
		catch(IOException ex)
		{
		}
		
		// Keep the connection open until the client ends TCP. Busy waits 
		// as the connection should terminate quickly.
		while(connectionSocket.isConnected());
		
		logger.connectionHandlerLog(connectionID, username, "Client terminated connection succesfully");
	}
	
	/**
	 * Creates a ConnectionHandler object that receives requests on 
	 * the passed Socket object.
	 * 
	 * @param connectionSocket_ The Socket to receive requests
	 */
	public ConnectionHandler(Socket connectionSocket_)
	{
		connectionID = nextConnectionID++;
		connectionState = ApplicationLayerConnectionState.NO_USERNAME;
		connectionSocket = connectionSocket_;
		
		try
		{
			InputStream socketInputStream = connectionSocket.getInputStream();
			ois = new ObjectInputStream(socketInputStream);
			OutputStream socketOutputStream = connectionSocket.getOutputStream();
			oos = new ObjectOutputStream(socketOutputStream);
		}
		catch(IOException ex)
		{
			ex.printStackTrace();
			return;
		}
	}
	
	/**
	 * An enumeration defining the app-layer connection states for the enclosing ConnectionHandler.
	 * 
	 * @author nbeck
	 *
	 */
	private enum ApplicationLayerConnectionState
	{
		NO_USERNAME,
		USERNAME,
		FINALIZED,
		KILLED
	}
}
