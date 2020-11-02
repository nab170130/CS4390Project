package com.github.project.client;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.io.*;
import java.util.*;

import com.github.project.core.CalculationRequest;

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
		
		int clientPort = 54321;
	
		try 
		{
			socket = new Socket("localhost", clientPort);
		} 
		catch(IOException e) 
		{
			System.err.println("Fatal Connection error!");
			e.printStackTrace();
		}
		try
		{
			ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
		
			CalculationRequest cr = new CalculationRequest("working");
			oos.writeObject(cr);
			
//			InputStreamReader in = new InputStreamReader(socket.getInputStream());
//			BufferedReader bf = new BufferedReader(in);
//			
//			String str = bf.readLine();
//			System.out.println("server:" + str);
			
		}
		catch(Exception e)
		{
			
		}
		
		
		
		
	}
}
