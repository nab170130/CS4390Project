package com.github.project.core;

import java.io.Serializable;

/**
 * This class implements the calculation request message of the application layer communication 
 * protocol. It encapsulates a String request message, and it is used by the server 
 * and client to send and receive math processing requests.
 * 
 * @author Nathan Beck
 * @version 1.0
 * @since 31 October 2020
 */
public class CalculationRequest extends Request implements Serializable
{
	public static final long serialVersionUID = 1L;
	
	private String rawRequest; // The encapsulated math processing request
	
	/**
	 * This method returns the encapsulated math processing request
	 * 
	 * @return The encapsulated math processing request
	 */
	public String getRawRequest()
	{
		return rawRequest;
	}
	
	/**
	 * Creates a CalculationRequest object that encapsulates a math processing request
	 * 
	 * @param rawRequest_ The math processing request to encapsulate
	 */
	public CalculationRequest(String rawRequest_)
	{
		rawRequest = rawRequest_;
	}
}
