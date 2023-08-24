package com.te.flinko.exception.admindept;

/**
 * 
 * 
 * @author Vinayak More *
 *
 *
 **/

@SuppressWarnings("serial")
public class IdentificationNumberPresentException extends RuntimeException {
	
	public IdentificationNumberPresentException (String message) {
		
		super(message);
	}

}
