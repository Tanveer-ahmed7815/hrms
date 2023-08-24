package com.te.flinko.exception;

public class LeaveIdNotFoundException extends RuntimeException {

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public LeaveIdNotFoundException(String message) {
		super(message);
	}
}
