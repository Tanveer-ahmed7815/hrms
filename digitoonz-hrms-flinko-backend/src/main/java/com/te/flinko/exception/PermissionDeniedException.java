package com.te.flinko.exception;

public class PermissionDeniedException extends RuntimeException {
	String message;
	
	public PermissionDeniedException(String message) {

		super(message);
	}

}
