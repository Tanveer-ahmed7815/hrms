package com.te.flinko.exception.admindept;

/**
 * 
 * 
 * @author Vinayak More *
 *
 *
 **/

@SuppressWarnings("serial")
public class PurchaseIdPresentException extends RuntimeException {
	
	public PurchaseIdPresentException (String message) {
		
		super(message);
	}

}
