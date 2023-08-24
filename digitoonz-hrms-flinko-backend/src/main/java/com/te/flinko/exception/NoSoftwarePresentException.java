package com.te.flinko.exception;

/**
 * @author Manish Kumar
 * 
 * */

public class NoSoftwarePresentException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public NoSoftwarePresentException(String message) {
		super(message);

	}

}
