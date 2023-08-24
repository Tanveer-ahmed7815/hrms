package com.te.flinko.exception;

public class DataNotFoundException extends RuntimeException{
	
	private static final long serialVersionUID = 1L;
	
	String message;
	
	public DataNotFoundException(String message) {
		super(message);
		
	}

}

