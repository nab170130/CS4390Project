package com.github.project.core;

public class CalculationRequest 
{
	private String rawRequest;
	
	public String getRawRequest()
	{
		return rawRequest;
	}
	
	public CalculationRequest(String rawRequest_)
	{
		rawRequest = rawRequest_;
	}
}
