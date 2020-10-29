package com.github.project.server;

import java.net.Socket;

import com.github.project.core.CalculationRequest;

public class QueueElement
{
	private CalculationRequest request;
	private Socket requestingSocket;
	private QueueElement next;
	
	public CalculationRequest getItem()
	{
		return request;
	}
	
	public Socket getRequestingSocket()
	{
		return requestingSocket;
	}
	
	public QueueElement getNextQueueElement()
	{
		return next;
	}
	
	public void setNext(QueueElement next_)
	{
		next = next_;
	}
	
	public QueueElement(CalculationRequest request_, Socket requestingSocket_)
	{
		request = request_;
		requestingSocket = requestingSocket_;
	}
}
