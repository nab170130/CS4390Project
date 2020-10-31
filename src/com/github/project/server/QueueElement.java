package com.github.project.server;

import java.net.Socket;

import com.github.project.core.CalculationRequest;

/**
 * This class implements an element of the generic queue structure. Here, 
 * the CalculationRequest object and the Socket that received it are held 
 * in each element of the queue.
 * 
 * @author Nathan Beck
 * @version 1.0
 * @since 31 October 2020
 */
public class QueueElement
{
	private CalculationRequest request;	// The attached CalculationRequest
	private Socket requestingSocket;	// The attached Socket that received the CalculationRequest above
	private QueueElement next;			// The next element in the queue
	
	/**
	 * This method returns the CalculationRequest object encapsulated in this QueueElement object
	 * 
	 * @return The attached CalculationRequest held by this QueueElement object
	 */
	public CalculationRequest getItem()
	{
		return request;
	}
	
	/**
	 * This method returns the Socket object that received the encapsulated CalculationRequest object
	 * 
	 * @return The Socket that received the encapsulated CalculationRequest object
	 */
	public Socket getRequestingSocket()
	{
		return requestingSocket;
	}
	
	/**
	 * This method returns the next QueueElement object in the queue
	 * 
	 * @return The next QueueElement object in the queue
	 */
	public QueueElement getNextQueueElement()
	{
		return next;
	}
	
	/**
	 * This method sets the next QueueElement object to be presented in the queue
	 * 
	 * @param next_ The next QueueElement object to be presented in the queue
	 */
	public void setNext(QueueElement next_)
	{
		next = next_;
	}
	
	/**
	 * Creates a QueueElement object that contains the CalculationRequest object to 
	 * be processed and the Socket object that received it.
	 * 
	 * @param request_ The CalculationRequest object to encapsulate
	 * @param requestingSocket_ The Socket object to encapsulate
	 */
	public QueueElement(CalculationRequest request_, Socket requestingSocket_)
	{
		request = request_;
		requestingSocket = requestingSocket_;
	}
}
