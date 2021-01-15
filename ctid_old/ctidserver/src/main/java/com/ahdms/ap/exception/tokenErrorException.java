package com.ahdms.ap.exception;

public class tokenErrorException extends RuntimeException {
	
	private static final long serialVersionUID = -3959770528139611592L;

	public tokenErrorException(String msg)  
    {  
		   super(msg); 
    }  
	 public tokenErrorException() { 
		 super();
	}
}
