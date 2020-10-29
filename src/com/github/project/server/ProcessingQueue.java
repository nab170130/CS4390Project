package com.github.project.server;

import java.net.Socket;

import com.github.project.core.CalculationRequest;

public class ProcessingQueue implements Runnable
{	
	private static ProcessingQueue instance = new ProcessingQueue();
	
	private QueueElement queueHead;
	private QueueElement queueTail;
	
	public static ProcessingQueue getInstance()
	{
		return instance;
	}
	
	public synchronized void addToQueue(CalculationRequest request, Socket requestingSocket)
	{
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
	
	private boolean processRequest()
	{
		// Return false if there is no element to process
		if(queueHead == null)
		{
			return false;
		}
		
		System.out.print(queueHead.getItem().getRawRequest());
		
		QueueElement nextQueueHead = queueHead.getNextQueueElement();
		queueHead = nextQueueHead;
		
		return true;
	}
	
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
	
	private ProcessingQueue()
	{
		// Add a dummy QueueElement to the tail, which will be removed via automatic garbage collection as 
		// soon as a request is added to the queue.
		queueTail = new QueueElement(new CalculationRequest(""), null);
	}
}
