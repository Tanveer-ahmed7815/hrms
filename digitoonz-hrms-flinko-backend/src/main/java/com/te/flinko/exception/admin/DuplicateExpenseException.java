package com.te.flinko.exception.admin;

@SuppressWarnings("serial")
public class DuplicateExpenseException extends RuntimeException {
	
	public DuplicateExpenseException (String message) {
		
		super(message);
	}

}
