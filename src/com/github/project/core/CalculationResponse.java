package com.github.project.core;

import java.io.Serializable;

/**
 * This class implements the response message of the application layer communication 
 * protocol. It is used by the server and client to send and receive calculation results.
 * 
 * @author Nathan Beck
 * @version 1.0
 * @since 31 October 2020
 */
public class CalculationResponse implements Serializable
{
	public static final long serialVersionUID = 1L;
	
	private double calculationResult;
	private String calculationErrorDesc;
	
	public double getCalculationResult()
	{
		return calculationResult;
	}
	
	public String getCalculationErrorDesc()
	{
		return calculationErrorDesc;
	}
	
	public CalculationResponse(double calculationResult_, String calculationErrorDesc_)
	{
		calculationResult = calculationResult_;
		calculationErrorDesc = calculationErrorDesc_;
	}
}
