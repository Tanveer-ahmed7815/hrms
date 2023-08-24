package com.te.flinko.exception.admindept;

/**
 * 
 * 
 * @author Vinayak More *
 *
 *
 **/

@SuppressWarnings("serial")
public class NoProductPresentException extends RuntimeException {
	
	public NoProductPresentException(String message) {
		
		super(message);
	}

}
