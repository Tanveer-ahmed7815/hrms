package com.te.flinko.exception;

public class TaskIdNotFoundException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public TaskIdNotFoundException(String message) {
		super(message);
	}

}
