package com.github.project.core;

import java.io.Serializable;

/**
 * This class represents app-layer response messages sent by the server to the clients.
 * It is the superclass of all response-like messages and includes response code information.
 * 
 * @author nbeck
 * @version 1.0
 * @since 31 October 2020
 */
public class Response implements Serializable
{
	public static final long serialVersionUID = 1L;
	
	private ResponseCode responseCode;
	
	public ResponseCode getResponseCode()
	{
		return responseCode;
	}
	
	public Response(ResponseCode responseCode_)
	{
		responseCode = responseCode_;
	}
}
