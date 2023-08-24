package com.te.flinko.exception;

public class CompanyIdNotFoundException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public CompanyIdNotFoundException(String message) {
		super(message);
	}

}
