package com.te.flinko.exception.admindept;

/**
 * 
 * 
 * @author Vinayak More *
 *
 *
 **/

@SuppressWarnings("serial")
public class SalesIdNotPresentException extends RuntimeException {
	
	public SalesIdNotPresentException (String  message) {
		
		super(message);
	}

}
