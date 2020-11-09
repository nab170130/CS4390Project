package com.github.project.server;

import com.github.project.core.CalculationRequest;

/**
 * This class implements an element of the generic queue structure. Here, 
 * the CalculationRequest object and the ConnectionHandler that received it are held 
 * in each element of the queue.
 * 
 * @author Nathan Beck
 * @version 1.0
 * @since 31 October 2020
 */
public class QueueElement
{
	private CalculationRequest request;				// The attached CalculationRequest
	private ConnectionHandler requestingHandler;	// The attached ConnectionHandler to use in response messages
	private QueueElement next;						// The next element in the queue
	
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
	 * This method returns the ConnectionHandler object to which the calculation response should be 
	 * written
	 * 
	 * @return The ConnectionHandler that received the encapsulated CalculationRequest object
	 */
	public ConnectionHandler getRequestingHandler()
	{
		return requestingHandler;
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
	 * be processed and the ConnectionHandler object that received it.
	 * 
	 * @param request_ The CalculationRequest object to encapsulate
	 * @param requestingHandler_ The ConnectionHandler object to encapsulate
	 */
	public QueueElement(CalculationRequest request_, ConnectionHandler requestingHandler_)
	{
		request = request_;
		requestingHandler = requestingHandler_;
	}
}
