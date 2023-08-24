package com.te.flinko.exception;

public class EmployeeTimeSheetCannottEditedException extends RuntimeException{

	 String message;
	
	public EmployeeTimeSheetCannottEditedException(String message) {
		
		super(message);
	}
}
