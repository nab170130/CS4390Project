package com.github.project.server;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Date;

/**
 * This class provides a singleton instance by which the server process can 
 * log actions to the output window and a text file.
 * 
 * @author nbeck
 * @version 1.0
 */
public class Logger 
{
	private static final Logger instance = new Logger(); // The singleton instance of this class
	
	private boolean initialized; // Reflects whether or not the Logger is attached to a file
	
	private PrintWriter textWriter; // The PrintWriter used to write to the text file
	
	/**
	 * Returns the singleton instance of the Logger class.
	 * 
	 * @return The singleton instance of the Logger class
	 */
	public static Logger getInstance()
	{
		return instance;
	}
	
	/**
	 * This method logs a message sent from a connection handler.
	 * 
	 * @param connectionID The connection ID specific to the connection
	 * @param username The username attached to the connection
	 * @param message The message to be printed by the connection handler
	 */
	public void connectionHandlerLog(int connectionID, String username, String message)
	{
		// Declare format strings and date strings
		String formatString = "%s | CONNECTION %d <%s>: %s\n";
		String dateString = new Date().toString();
		
		// Commit the message to the output and text file
		commitMessage(formatString, dateString, connectionID, username, message);
	}
	
	/**
	 * This method logs a message sent from the server in a generalized manner
	 * 
	 * @param message The message to be printed by the server
	 */
	public void serverLog(String message)
	{
		// Declare format strings and date strings
		String formatString = "%s | SYSTEM: %s\n";
		String dateString = new Date().toString();
		
		// Commit the message to the output and text file
		commitMessage(formatString, dateString, message);
	}
	
	/**
	 * Writes a message to the output screen and the bound text file.
	 * 
	 * @param formatString The formatted string to print
	 * @param args The arguments to inject into the formatted string
	 */
	private synchronized void commitMessage(String formatString, Object... args)
	{
		// Print the message to the screen
		System.out.printf(formatString, args);
		
		// Print the message to the text file if this Logger is bound to a file
		if(initialized)
		{
			textWriter.printf(formatString, args);
			textWriter.flush();
		}
	}
	
	/**
	 * This method binds the Logger to a text file with the given path
	 * 
	 * @param path The Path of the text file
	 * @throws FileNotFoundException Thrown if the path cannot be opened or created
	 */
	public synchronized void bindOutput(String path) throws FileNotFoundException
	{
		// Close the current binding if this Logger is bound
		if(initialized)
		{
			textWriter.close();
		}
		
		// Create a new binding and reflect this binding with a true state
		textWriter = new PrintWriter(path);
		initialized = true;
		
		// Log the binding
		StringBuilder strBuilder = new StringBuilder();
		strBuilder.append("Bound to output file ");
		strBuilder.append(path);
		serverLog(strBuilder.toString());
	}
	
	/**
	 * Creates the singleton instance of this class with a currently unbound state
	 */
	private Logger()
	{
		initialized = false;
	}
}
