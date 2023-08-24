package com.te.flinko.exception.admin;

/**
 * 
 * 
 * @author Vinayak More *
 *
 *
 **/

public class NoExpensePresentException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public NoExpensePresentException(String message) {
		super(message);
	}

}
