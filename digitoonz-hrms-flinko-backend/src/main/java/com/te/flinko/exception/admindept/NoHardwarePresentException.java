package com.te.flinko.exception.admindept;

/**
 * 
 * 
 * @author Vinayak More *
 *
 *
 **/

@SuppressWarnings("serial")
public class NoHardwarePresentException extends RuntimeException {
	
	public NoHardwarePresentException (String message) {
		super(message);
	}

}
