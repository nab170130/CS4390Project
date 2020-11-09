package com.github.project.core;

/**
 * This class implements the connection establish response message of the application layer communication 
 * protocol. It is used by the server to acknowledge ConnectionEstablishRequest objects sent from the client.
 * 
 * @author Nathan Beck
 * @version 1.0
 * @since 31 October 2020
 */
public class ConnectionEstablishResponse extends Response
{
	public static final long serialVersionUID = 1L;
	
	/**
	 * Creates a ConnectionEstablishResponse object with the provided response code.
	 * 
	 * @param responseCode_ The response code of this ConnectionEstablishResponse object
	 */
	public ConnectionEstablishResponse(ResponseCode responseCode_)
	{
		super(responseCode_);
	}
}
