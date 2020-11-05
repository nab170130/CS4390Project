package com.github.project.core;

/**
 * This class implements the connection establish request message of the application layer communication 
 * protocol. It encapsulates a String username message, and it is used by the client to initiate 
 * app-layer communication state with the server.
 * 
 * @author Nathan Beck
 * @version 1.0
 * @since 31 October 2020
 */
public class ConnectionEstablishRequest 
{
	private String username; // The requested username to use for the connection
	
	/**
	 * This method returns the username encapsulated in this ConnectionEstablishRequest
	 * 
	 * @return The encapsulated username
	 */
	public String getUsername()
	{
		return username;
	}
	
	/**
	 * Creates a ConenctionEstablishRequest object with the provided username.
	 * 
	 * @param username_ The username to be used in the app-layer connection state
	 */
	public ConnectionEstablishRequest(String username_)
	{
		username = username_;
	}
}
