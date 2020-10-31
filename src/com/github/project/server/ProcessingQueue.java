package com.github.project.server;

import java.net.Socket;

import com.github.project.core.CalculationRequest;

/**
 * This class implements the FIFO processing queue of the server. It is 
 * a Runnable singleton class, and a single thread will process requests 
 * in an infinite loop until the server is stopped.
 * 
 * @author Nathan Beck
 * @version 1.0
 * @since 31 October 2020
 */
public class ProcessingQueue implements Runnable
{	
	private static ProcessingQueue instance = new ProcessingQueue(); // The singleton instance of this class
	
	private QueueElement queueHead; // The head pointer of the queue
	private QueueElement queueTail; // The tail pointer of the queue
	
	/**
	 * This method returns the singleton instance of this class
	 * 
	 * @return The singleton instance of this class
	 */
	public static ProcessingQueue getInstance()
	{
		return instance;
	}
	
	/**
	 * This method adds a CalculationRequest object to the queue with additional receiving 
	 * Socket information. This method is synchronized as multiple threads may access this 
	 * method at the same time.
	 * 
	 * @param request The CalculationRequest object to add and process in the queue
	 * @param requestingSocket The Socket object that received the CalculationRequest object
	 */
	public synchronized void addToQueue(CalculationRequest request, Socket requestingSocket)
	{
		// Create the QueueElement object, put it as the next element of the tail, and update the tail pointer
		QueueElement addRequestQueueElement = new QueueElement(request, requestingSocket);
		queueTail.setNext(addRequestQueueElement);
		queueTail = addRequestQueueElement;
		
		// Make the head point to new element if it is null, wake up processing thread if so
		if(queueHead == null)
		{
			queueHead = addRequestQueueElement;
			notifyAll();
		}
	}
	
	/**
	 * This method processes a request from the queue.
	 * 
	 * @return A boolean signaling if a request was processed.
	 */
	private boolean processRequest()
	{
		// Return false if there is no element to process
		if(queueHead == null)
		{
			return false;
		}
		
		// Process the request
		System.out.print(queueHead.getItem().getRawRequest());
		
		// Remove the processed request from the queue
		QueueElement nextQueueHead = queueHead.getNextQueueElement();
		queueHead = nextQueueHead;
		
		// Return true as a request was processed
		return true;
	}
	
	/**
	 * This method is the entry point for the processing thread. It processes 
	 * requests in an infinite loop. This method blocks when there are no requests 
	 * in the queue. It resumes when requests are added to the queue.
	 */
	public void run()
	{
		// Perform these actions until server is terminated
		while(true)
		{
			// Attempt to process a request. If unsuccessful, block this thread until signaled by addToQueue
			if(!processRequest())
			{
				try
				{
					wait();
				}
				catch(InterruptedException ex)
				{
				}
			}
		}
	}
	
	/**
	 * Creates the singleton instance of this class.
	 */
	private ProcessingQueue()
	{
		// Add a dummy QueueElement to the tail, which will be removed via automatic garbage collection as 
		// soon as a request is added to the queue.
		queueTail = new QueueElement(new CalculationRequest(""), null);
	}
}
