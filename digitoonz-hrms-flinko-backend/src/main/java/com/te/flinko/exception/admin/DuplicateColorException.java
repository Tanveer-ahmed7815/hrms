package com.te.flinko.exception.admin;

@SuppressWarnings("serial")
public class DuplicateColorException extends RuntimeException {
	
	public DuplicateColorException (String message) {
		
		super(message);
		
	}

}
