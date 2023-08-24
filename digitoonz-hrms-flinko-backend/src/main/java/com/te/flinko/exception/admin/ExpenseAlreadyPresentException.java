package com.te.flinko.exception.admin;

public class ExpenseAlreadyPresentException extends RuntimeException {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ExpenseAlreadyPresentException(String message) {
		super(message);
	}

}
