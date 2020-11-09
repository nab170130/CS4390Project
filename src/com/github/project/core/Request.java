package com.github.project.core;

import java.io.Serializable;

/**
 * This class represents app-layer request messages sent from the client to the server. 
 * It is the superclass of all request-like messages.
 * 
 * @author nbeck
 * @version 1.0
 * @since 31 October 2020
 */
public abstract class Request implements Serializable
{
	public static final long serialVersionUID = 1L;
}
