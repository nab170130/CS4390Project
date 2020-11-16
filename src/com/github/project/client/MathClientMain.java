package com.github.project.client;

import java.io.IOException;
import java.net.Socket;
import java.io.*;
import java.util.*;

import com.github.project.core.CalculationRequest;
import com.github.project.core.CalculationResponse;
import com.github.project.core.ConnectionEstablishRequest;
import com.github.project.core.ConnectionEstablishResponse;
import com.github.project.core.ConnectionTerminateRequest;
import com.github.project.core.Response;
import com.github.project.core.ResponseCode;

/**
 * This is the main class of the client-side program. 
 * 
 * @author Fiaz, Nathan
 * @version 1.0
 * @since 31 October 2020
 */

public class MathClientMain 
{
	/**
	 * Executes client in procedural fashion. The client parses command-line arguments, 
	 * connects to the server, receives user input, sends calculation requests, receives 
	 * responses, prints them to the screen, and terminates connection with the server.
	 * 
	 * @param args The command line arguments (0: ip address, 1: port, 2: username)
	 */
	public static void main(String[] args)
	{
		// Create the socket that will be used to communicate with the server
		Socket socket = null;
		
		// Set the username equal to the third argument
		String userName = args[2];
	
		// Validate port number and create socket
		try 
		{
			int clientPort = Integer.parseInt(args[1]);
			String IPaddress = args[0];
			
			if(clientPort < 1024 || clientPort > 65535)
			{
				System.out.println("Port number out of range");
				return;
			}
			
			socket = new Socket(IPaddress, clientPort);
		} 
		catch(IOException e) 
		{
			// The socket could not be opened; print error
			System.err.println("Fatal Connection error!");
			e.printStackTrace();
			return;
		}
		catch(NumberFormatException nfe)
		{
			// The port number was not parsable; print error
			System.err.println("Bad Port Number!");
			return;
		}
		
		try
		{
			// Create input and output streams from the socket
			ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
			ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
			
			// Create a ConnectionEstablishRequest with the provided username and send it to the server
			ConnectionEstablishRequest cer = new ConnectionEstablishRequest(userName);
			oos.writeObject(cer);
			
			// Wait for a response
			ConnectionEstablishResponse conEstResponse = (ConnectionEstablishResponse) ois.readObject();
		
			// If the server received it as a bad request, print error and exit
			if(conEstResponse.getResponseCode() == ResponseCode.BAD_REQUEST)
			{
				System.out.println("Server Could Not Process Connection Request");
				return;
			}
			
			// Notify user of successful connection
			System.out.println("Client-Server Connection Established");
			
			// Create a Scanner for receiving user input
			Scanner sc = new Scanner(System.in);
			String input = null;
			
			CalculationRequest cr = null;
			
			System.out.print("Input: ");
			
			// Keep reading input, sending CalculationRequests, and receiving CalculationResponses until user types exit
			while(!(input = sc.nextLine()).equals("exit"))
			{
				// Send request
				cr = new CalculationRequest(input);
				oos.writeObject(cr);
				
				// Receive response
				Response calculationResponse =  (Response)ois.readObject();
				
				// Print error if the server received a bad request and wait for next input
				if(calculationResponse.getResponseCode() == ResponseCode.BAD_REQUEST)
				{
					System.out.println("Server Could Not Process Calculation Request. Try Agin.");
					continue;
				}
				
				// Cast the correct response
				CalculationResponse calResponse = (CalculationResponse) calculationResponse;
				double result = calResponse.getCalculationResult();
				String errorString = calResponse.getCalculationErrorDesc();
				
				// Print result and possible error string
				System.out.printf("Result: %s\n", calResponse.getCalculationResult());
				
				if(Double.isNaN(result))
				{
					System.out.printf("%s", errorString);
				}
				
				System.out.print("Input: ");
			}
			
			// User specified exit, create ConnectionTerminateRequest
			ConnectionTerminateRequest ctr = new ConnectionTerminateRequest();
			
			// Attempt to send this request 5 times or until accepted
			for(int i = 0; i < 5; i++)
			{
				oos.writeObject(ctr);
				
				Response terminateResponse = (Response) ois.readObject();
				
				if(terminateResponse.getResponseCode() == ResponseCode.BAD_REQUEST)
				{
					continue;
				}
				
				break;
			}
			
			// Close this connection
			socket.close();
		}
		catch(IOException e) 
		{
			System.err.println("Fatal Connection error!");
			e.printStackTrace();
			return;
		}
		catch(ClassNotFoundException clne)
		{
			System.err.println("Corrupted Data!");
			return;
		}
	}
}
