package com.te.flinko.exception;

/**
 * 
 * @author Brunda
 *
 */

public class ComapnyNameAlreadyExistsException extends RuntimeException{

	private static final long serialVersionUID = 1L;

	public ComapnyNameAlreadyExistsException(String message) {
		super(message);
	}
}
