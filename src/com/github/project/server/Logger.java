package com.github.project.server;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Date;

public class Logger 
{
	private static final Logger instance = new Logger();
	
	private boolean initialized;
	
	private PrintWriter textWriter;
	
	public static Logger getInstance()
	{
		return instance;
	}
	
	public void connectionHandlerLog(int connectionID, String username, String message)
	{
		String formatString = "%s | CONNECTION %d <%s>: %s\n";
		String dateString = new Date().toString();
		commitMessage(formatString, dateString, connectionID, username, message);
	}
	
	public void serverLog(String message)
	{
		String formatString = "%s | SYSTEM: %s\n";
		String dateString = new Date().toString();
		commitMessage(formatString, dateString, message);
	}
	
	private synchronized void commitMessage(String formatString, Object... args)
	{
		System.out.printf(formatString, args);
		
		if(initialized)
		{
			textWriter.printf(formatString, args);
			textWriter.flush();
		}
	}
	
	public synchronized void bindOutput(String path) throws FileNotFoundException
	{
		if(initialized)
		{
			textWriter.close();
		}
		
		textWriter = new PrintWriter(path);
		initialized = true;
		
		StringBuilder strBuilder = new StringBuilder();
		strBuilder.append("Bound to output file ");
		strBuilder.append(path);
		serverLog(strBuilder.toString());
	}
	
	private Logger()
	{
		initialized = false;
	}
}
