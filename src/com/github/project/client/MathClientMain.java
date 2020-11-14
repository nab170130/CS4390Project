package com.github.project.client;

import java.io.IOException;
import java.net.ServerSocket;
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
 * @author Fiaz
 * @version 1.0
 * @since 31 October 2020
 */

public class MathClientMain 
{
	public static void main(String[] args)
	{
		// comment testing push attempt 2
		Socket socket = null;
		
		String userName = args[2];
	
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
			System.err.println("Fatal Connection error!");
			e.printStackTrace();
			return;
		}
		catch(NumberFormatException nfe)
		{
			System.err.println("Bad Port Number!");
			return;
		}
		try
		{
			ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
		
			ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
			
			
			ConnectionEstablishRequest cer = new ConnectionEstablishRequest(userName);
			
			
			oos.writeObject(cer);
			
			ConnectionEstablishResponse conEstResponse =  (ConnectionEstablishResponse)ois.readObject();
		
			if(conEstResponse.getResponseCode() == ResponseCode.BAD_REQUEST)
			{
				System.out.println("Server Could Not Process Connection Request");
				return;
			}
			System.out.println("Client-Server Connection Established");
			
			Scanner sc = new Scanner(System.in);
			String input = null;
			
			CalculationRequest cr = null;
			
			while(!(input = sc.nextLine()).equals("exit"))
			{
				cr = new CalculationRequest(input);
				oos.writeObject(cr);
				Response calculationResponse =  (Response)ois.readObject();
				if(calculationResponse.getResponseCode() == ResponseCode.BAD_REQUEST)
				{
					System.out.println("Server Could Not Process Calculation Request. Try Agin.");
					continue;
				}
				CalculationResponse calResponse =  (CalculationResponse)calculationResponse;
				
				System.out.printf("Result: %s (%s)\n", calResponse.getCalculationResult(), calResponse.getCalculationErrorDesc());
				
			}
			
			ConnectionTerminateRequest ctr = new ConnectionTerminateRequest();
			for(int i = 0; i < 5; i++)
			{
				oos.writeObject(ctr);
				Response terminateResponse =  (Response)ois.readObject();
				if(terminateResponse.getResponseCode() == ResponseCode.BAD_REQUEST)
				{
					continue;
				}
				break;
			}
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
