package com.github.project.core;

/**
 * This class implements the calculation response message of the application layer communication 
 * protocol. It is used by the server and client to send and receive calculation results.
 * 
 * @author Nathan Beck
 * @version 1.0
 * @since 31 October 2020
 */
public class CalculationResponse extends Response
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
	
	public CalculationResponse(ResponseCode responseCode_, double calculationResult_, String calculationErrorDesc_)
	{
		super(responseCode_);
		calculationResult = calculationResult_;
		calculationErrorDesc = calculationErrorDesc_;
	}
}
